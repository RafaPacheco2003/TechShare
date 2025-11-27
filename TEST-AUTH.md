# 🔐 Guía de Pruebas - Sistema de Autenticación TechShare

## 📋 Análisis del Sistema de Autenticación

### ✅ Configuración Actual

**Endpoints Públicos (NO requieren autenticación):**
- `/auth/**` - Registro, login, verificación
- `/oauth2/**` - Autenticación con Google
- `/images/**` - Imágenes públicas
- `/api/**` - APIs públicas

**Endpoints Protegidos (requieren JWT):**
- `/admin/**` - Solo usuarios autenticados

**Sesiones:** STATELESS (sin sesiones, solo JWT)

### 🔍 Problemas Detectados

#### ⚠️ PROBLEMA 1: Configuración de Seguridad Permisiva
```java
.authorizeHttpRequests(http -> {
    http
        .requestMatchers("/auth/**", "/oauth2/**", "/images/**", "/api/**").permitAll()
        .requestMatchers("/admin/**").authenticated()
        .anyRequest().permitAll(); // ❌ TODO lo demás es público
})
```

**Problema:** `.anyRequest().permitAll()` hace que cualquier ruta que NO sea `/admin/**` sea pública, incluso sin token.

**Solución Recomendada:**
```java
.anyRequest().authenticated() // ✅ Todo lo demás requiere autenticación
```

#### ⚠️ PROBLEMA 2: El filtro JWT no valida rutas protegidas correctamente

El `JwtTokenValidator` solo ignora `/auth/register` pero debería ignorar TODAS las rutas públicas.

---

## 🧪 Cómo Probar el Sistema de Autenticación

### 1️⃣ Verificar que el servidor está corriendo

```bash
docker-compose ps
```

Deberías ver:
```
techshare-mysql   Up (healthy)
techshare-app     Up
```

### 2️⃣ Probar el Registro de Usuario

**Request:**
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "test@example.com",
    "password": "Test123!",
    "firstName": "Test",
    "lastName": "User",
    "roles": ["USER"]
  }'
```

**Respuesta Esperada:**
```
Usuario registrado exitosamente. Por favor verifica tu correo electrónico para activar tu cuenta.
```

**Verificar en BD:**
```bash
docker exec -it techshare-mysql mysql -uroot -pBlacky20. techshare -e "SELECT user_id, username, first_name, is_enabled FROM user;"
```

El usuario debe estar con `is_enabled = 0` (no activado).

### 3️⃣ Activar la Cuenta (Simular Click en Email)

**Primero obtén el token de verificación:**
```bash
docker exec -it techshare-mysql mysql -uroot -pBlacky20. techshare -e "SELECT token, user_id, expiry_date FROM verification_tokens ORDER BY id DESC LIMIT 1;"
```

**Luego verifica la cuenta:**
```bash
curl -X GET "http://localhost:8080/auth/verify-account?token=EL_TOKEN_AQUI"
```

**Respuesta Esperada:**
```json
{
  "status": "success",
  "message": "¡Cuenta verificada exitosamente! Ahora puedes iniciar sesión.",
  "username": "test@example.com",
  "redirectUrl": "/login"
}
```

### 4️⃣ Probar el Login (Autenticación)

**Request:**
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "rodrigorafaelchipacheco@gmail.com",
    "password": "TU_PASSWORD_AQUI"
  }'
```

**Respuesta Esperada:**
```json
{
  "username": "rodrigorafaelchipacheco@gmail.com",
  "firstName": "rodrigo",
  "user_id": 1,
  "role": "ADMIN",
  "message": "User logged successfully",
  "jwt": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "status": true
}
```

**IMPORTANTE:** Guarda el `jwt` para las siguientes pruebas.

### 5️⃣ Probar Endpoint Protegido SIN Token (Debe FALLAR)

```bash
curl -X GET http://localhost:8080/admin/product/all
```

**Resultado Actual:** ⚠️ Probablemente retorna los datos (INCORRECTO)
**Resultado Esperado:** ❌ Error 401 Unauthorized o redirección a login

### 6️⃣ Probar Endpoint Protegido CON Token (Debe FUNCIONAR)

```bash
curl -X GET http://localhost:8080/admin/product/all \
  -H "Authorization: Bearer TU_JWT_AQUI"
```

**Respuesta Esperada:** ✅ Lista de productos

### 7️⃣ Probar Creación de Movimiento (Requiere JWT y Usuario en BD)

```bash
curl -X POST http://localhost:8080/admin/movement \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TU_JWT_AQUI" \
  -d '{
    "product_id": 1,
    "quantity": 5,
    "moveType": "ENTRADA",
    "comment": "Prueba de movimiento"
  }'
```

**Respuesta Esperada:** ✅ Movimiento creado exitosamente

---

## 🔧 Correcciones Recomendadas

### Corrección 1: Hacer los endpoints más seguros

**Archivo:** `SecurityConfig.java`

**Cambiar:**
```java
.authorizeHttpRequests(http -> {
    http
        .requestMatchers("/auth/**", "/oauth2/**", "/images/**").permitAll()
        .requestMatchers("/admin/**").hasRole("ADMIN") // Solo ADMIN
        .requestMatchers("/api/**").authenticated() // APIs requieren autenticación
        .anyRequest().authenticated(); // Todo lo demás también
})
```

### Corrección 2: Mejorar el JwtTokenValidator

**Archivo:** `JwtTokenValidator.java`

**Agregar más rutas públicas:**
```java
@Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String path = request.getServletPath();

    // Lista de rutas públicas que no necesitan JWT
    String[] publicPaths = {
        "/auth/register",
        "/auth/login", 
        "/auth/verify-account",
        "/oauth2/",
        "/images/",
        "/error"
    };

    // Si es una ruta pública, saltar validación
    for (String publicPath : publicPaths) {
        if (path.startsWith(publicPath)) {
            filterChain.doFilter(request, response);
            return;
        }
    }

    String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);
    
    // Si no hay token en rutas protegidas, denegar acceso
    if (jwtToken == null) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("{\"error\": \"No se proporcionó token de autenticación\"}");
        return;
    }

    try {
        jwtToken = jwtToken.substring(7);
        DecodedJWT decodedJWT = jwtUtils.validateToken(jwtToken);
        String username = jwtUtils.extractUsername(decodedJWT);
        String stringAuthorities = jwtUtils.getSpecificClaim(decodedJWT, "authorities").asString();

        Collection<? extends GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(stringAuthorities);

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    } catch (Exception e) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("{\"error\": \"Token inválido o expirado\"}");
        return;
    }
    
    filterChain.doFilter(request, response);
}
```

---

## 📊 Checklist de Pruebas

### Autenticación Básica
- [ ] ✅ Registro de usuario nuevo
- [ ] ✅ Verificación de cuenta por email
- [ ] ✅ Login con credenciales correctas
- [ ] ❌ Login con credenciales incorrectas (debe fallar)
- [ ] ✅ Recepción de JWT válido

### Autorización
- [ ] ⚠️ Acceso a `/admin/**` sin token (debe fallar)
- [ ] ✅ Acceso a `/admin/**` con token válido
- [ ] ❌ Acceso a `/admin/**` con token expirado (debe fallar)
- [ ] ❌ Acceso a `/admin/**` con token inválido (debe fallar)

### OAuth2 (Google)
- [ ] ⚠️ Login con Google (requiere configuración)
- [ ] ⚠️ Callback exitoso de Google
- [ ] ⚠️ Generación de JWT después de OAuth2

### Sesiones
- [ ] ✅ No se crean sesiones (stateless)
- [ ] ✅ JWT se envía en header Authorization

---

## 🐛 Problemas Conocidos

1. **Rutas demasiado permisivas:** `.anyRequest().permitAll()` hace que todo sea público excepto `/admin/**`
2. **JwtTokenValidator no rechaza requests sin token:** Solo valida si existe, pero no bloquea si no existe
3. **OAuth2 genera token con rol USER hardcodeado:** En `JwtUtils.generateToken()` siempre asigna `ROLE_USER`

---

## ✅ Estado Actual del Sistema

### Funcionando Correctamente ✅
- ✅ Registro de usuarios
- ✅ Encriptación de contraseñas (BCrypt)
- ✅ Generación de JWT
- ✅ Validación de JWT
- ✅ Extracción de roles del token
- ✅ Sistema de verificación por email
- ✅ CORS configurado

### Problemas/Mejoras Necesarias ⚠️
- ⚠️ Seguridad demasiado permisiva (`.anyRequest().permitAll()`)
- ⚠️ No rechaza requests sin token en rutas protegidas
- ⚠️ OAuth2 no asigna roles dinámicamente
- ⚠️ Falta manejo de errores en JwtTokenValidator

---

## 🎯 Recomendaciones Finales

1. **Aplicar las correcciones sugeridas** en `SecurityConfig.java` y `JwtTokenValidator.java`
2. **Probar cada endpoint** con y sin token
3. **Verificar logs** de Spring Security (ya están en DEBUG)
4. **Usar Postman o Insomnia** para pruebas más cómodas que curl
5. **Crear tests unitarios** para los servicios de autenticación

---

¿Quieres que aplique alguna de estas correcciones ahora?
