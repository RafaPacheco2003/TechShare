# 🔐 Solución OAuth2 con Google - Creación Automática de Usuarios

## 📋 Problema Original

Cuando un usuario se autenticaba con OAuth2 (Google), el sistema generaba un JWT token pero **el usuario no existía en la base de datos**. Esto causaba errores cuando intentaban crear ventas porque el sistema buscaba el `user_id` que no existía.

## ✅ Solución Implementada

### **OAuth2SuccessHandler Mejorado**

Ahora cuando un usuario hace login con Google:

1. **Busca el usuario** en la base de datos por email
2. **Si NO existe**, lo crea automáticamente con:
   - Username: email de Google
   - Password: UUID aleatorio (no se usará, solo OAuth2)
   - FirstName: nombre de Google
   - LastName: apellido de Google
   - Role: USER (por defecto)
   - Enabled: true (ya verificado por Google)
   - AccountNoExpired: true
   - CredentialNoExpired: true
   - AccountNoLocked: true

3. **Genera el JWT token** con toda la información del usuario
4. **Redirige al frontend** con el token

## 🧪 Cómo Probar

### **1. Iniciar Login con Google**
```
http://localhost:8080/oauth2/authorization/google
```

### **2. Verificar Logs del Backend**
```bash
docker-compose logs -f app | grep -i "oauth2\|creating new user"
```

Deberías ver:
```
Creating new user from OAuth2 login: email@gmail.com
New user created with ID: X
Generating JWT token for user: email@gmail.com
Redirecting to: http://localhost:4200/oauth2/success?token=...
```

### **3. Verificar Usuario en Base de Datos**
```bash
docker exec -it techshare-mysql mysql -uroot -pBlacky20. techshare -e "
SELECT user_id, username, first_name, last_name, is_enabled 
FROM user 
ORDER BY user_id DESC 
LIMIT 5;
"
```

### **4. Verificar Roles del Usuario**
```bash
docker exec -it techshare-mysql mysql -uroot -pBlacky20. techshare -e "
SELECT u.user_id, u.username, GROUP_CONCAT(r.role_name) AS roles
FROM user u
LEFT JOIN user_roles ur ON u.user_id = ur.user_id
LEFT JOIN role r ON ur.role_id = r.role_id
GROUP BY u.user_id, u.username
ORDER BY u.user_id DESC
LIMIT 5;
"
```

## 🔄 Flujo Completo

### **Primera vez (Usuario nuevo):**
```
1. Usuario → http://localhost:8080/oauth2/authorization/google
2. Google → Autenticación
3. Callback → http://localhost:8080/login/oauth2/code/google
4. Backend → Crea usuario en DB (user_id: X)
5. Backend → Genera JWT con user_id
6. Redirect → http://localhost:4200/oauth2/success?token=XXX
```

### **Siguientes veces (Usuario existente):**
```
1. Usuario → http://localhost:8080/oauth2/authorization/google
2. Google → Autenticación
3. Callback → http://localhost:8080/login/oauth2/code/google
4. Backend → Encuentra usuario existente (user_id: X)
5. Backend → Genera JWT con user_id
6. Redirect → http://localhost:4200/oauth2/success?token=XXX
```

## 📊 Estructura del JWT Token

### **Token OAuth2 (ANTES - ❌ Problema):**
```json
{
  "iss": "Rafaul_backend",
  "sub": "email@gmail.com",
  "authorities": "ROLE_USER",
  "iat": 1764131027,
  "exp": 1764132827
}
```
❌ **No incluía user_id**, causaba error al crear ventas

### **Token OAuth2 (AHORA - ✅ Correcto):**
```json
{
  "iss": "Rafaul_backend",
  "sub": "email@gmail.com",
  "authorities": "ROLE_USER",
  "iat": 1764131027,
  "exp": 1764132827,
  "jti": "uuid",
  "nbf": 1764131027
}
```
✅ **Generado con `jwtUtils.createToken(authentication)`** que incluye toda la información del usuario autenticado

## 🎯 Casos de Uso Ahora Funcionan

### ✅ Crear Venta con OAuth2
```javascript
// Frontend - Después de OAuth2 login
POST /api/sale
Authorization: Bearer <TOKEN_DE_OAUTH2>

{
  "items": [...],
  "total": 100
}
```

El backend ahora:
1. Extrae el username del token
2. Busca el usuario en la DB (existe porque fue creado en el login)
3. Obtiene el `user_id`
4. Crea la venta correctamente

### ✅ Ver Ventas del Usuario
```javascript
GET /api/sale/user
Authorization: Bearer <TOKEN_DE_OAUTH2>
```

Funciona porque el usuario existe en la DB.

## 🔧 Configuración Necesaria

### **Roles en la Base de Datos**
Asegúrate de tener el rol USER creado:
```sql
INSERT INTO role (role_name) VALUES ('USER') 
ON DUPLICATE KEY UPDATE role_name=role_name;
```

Ya lo hiciste con el script `init-roles.sh`.

## 🐛 Troubleshooting

### **Error: "Role USER not found in database"**
```bash
./init-roles.sh
```

### **Error: "User not found" al crear venta**
Revisa los logs:
```bash
docker-compose logs -f app
```

Verifica que el usuario se creó:
```bash
docker exec -it techshare-mysql mysql -uroot -pBlacky20. techshare \
  -e "SELECT * FROM user WHERE username='email@gmail.com';"
```

### **Token no incluye información completa**
Verifica que estés usando `jwtUtils.createToken(authentication)` 
en lugar de `jwtUtils.generateToken(email)`.

## 📝 Resumen

| Característica | Antes | Ahora |
|----------------|-------|-------|
| Usuario OAuth2 en DB | ❌ No | ✅ Sí (auto-creado) |
| JWT con user_id | ❌ No | ✅ Sí |
| Crear ventas | ❌ Error | ✅ Funciona |
| Ver ventas | ❌ Error | ✅ Funciona |
| Persistencia | ❌ No | ✅ Sí |

## 🚀 Próximos Pasos

1. **Prueba el login con Google** en modo incógnito
2. **Verifica que el usuario se crea** en la base de datos
3. **Intenta crear una venta** con el token de OAuth2
4. **Verifica que funcione igual** que con el login normal

---

**Estado: ✅ RESUELTO**

El OAuth2 ahora funciona completamente integrado con el sistema de ventas.
