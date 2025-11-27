# Arquitectura - TechShare

## Gestión de Usuarios OAuth2

### Descripción General

El sistema implementa un mecanismo automático de aprovisionamiento de usuarios (User Provisioning) que permite a usuarios autenticados mediante OAuth2 (Google) ser creados automáticamente en la base de datos cuando acceden por primera vez al sistema.

### Componentes Principales

#### 1. **UserProvisioningService** 
📁 `src/main/java/com/techshare/services/user/UserProvisioningService.java`

**Responsabilidad**: Servicio centralizado para gestionar el aprovisionamiento automático de usuarios.

**Métodos principales**:
- `findOrCreateUser(String username)`: Busca un usuario por email, si no existe lo crea con datos básicos
- `findOrCreateUser(String username, String firstName, String lastName)`: Igual que el anterior pero con datos personalizados
- `userExists(String username)`: Verifica si un usuario existe
- `findUser(String username)`: Busca un usuario sin crearlo

**Características**:
- ✅ Separa la lógica de negocio del código de infraestructura
- ✅ Evita duplicación de código
- ✅ Facilita el testing (se puede mockear fácilmente)
- ✅ Usa `@Transactional` para garantizar consistencia de datos
- ✅ Usa `@Lazy` en `PasswordEncoder` para evitar referencias circulares

**Flujo de creación de usuario**:
```
1. Buscar usuario por email en la base de datos
2. Si existe → Retornar usuario
3. Si NO existe:
   a. Obtener rol USER de la base de datos
   b. Crear nuevo UserEntity con:
      - Username: email del OAuth2
      - Password: UUID aleatorio (seguro)
      - Nombre: proporcionado o derivado del email
      - Apellido: proporcionado o vacío
      - Todos los permisos de cuenta habilitados
      - Rol: USER
   c. Guardar en la base de datos
   d. Retornar usuario creado
```

---

#### 2. **OAuth2SuccessHandler**
📁 `src/main/java/com/techshare/security/OAuth2SuccessHandler.java`

**Responsabilidad**: Manejar la autenticación exitosa de OAuth2 con Google.

**Flujo**:
```
1. Usuario se autentica con Google
2. Google devuelve información del usuario (email, nombre, apellido)
3. Se invoca UserProvisioningService.findOrCreateUser(email, firstName, lastName)
4. Se genera un JWT token con la autenticación completa
5. Se redirige al frontend con el token: 
   http://localhost:4200/oauth2/success?token=XXX
```

**Mejoras implementadas**:
- ✅ Inyección de dependencias por constructor (más testeable)
- ✅ Eliminación de código duplicado (usa UserProvisioningService)
- ✅ Mejor manejo de errores con método `redirectToError()`
- ✅ Logging más descriptivo

---

#### 3. **SaleController**
📁 `src/main/java/com/techshare/controllers/user/SaleController.java`

**Responsabilidad**: Gestionar las operaciones CRUD de ventas.

**Mejoras implementadas**:
- ✅ Inyección de dependencias por constructor
- ✅ Método auxiliar `getCurrentUser()` que centraliza la obtención del usuario autenticado
- ✅ Usa `UserProvisioningService` para auto-crear usuarios de OAuth2
- ✅ Logging mejorado con información de usuario

**Flujo de creación de venta**:
```
1. Request POST /api/sale con token JWT
2. Se extrae el username (email) del token
3. Se llama a getCurrentUser() que:
   a. Obtiene el username del SecurityContext
   b. Llama a UserProvisioningService.findOrCreateUser(username)
4. Si el usuario NO existe → Se crea automáticamente
5. Se usa el user_id para crear la venta
```

---

### Ventajas de la Arquitectura Actual

1. **Separación de Responsabilidades (SRP)**:
   - `UserProvisioningService`: Solo maneja aprovisionamiento de usuarios
   - `OAuth2SuccessHandler`: Solo maneja el flujo de OAuth2
   - `SaleController`: Solo maneja ventas

2. **DRY (Don't Repeat Yourself)**:
   - La lógica de creación de usuarios está en UN solo lugar
   - Fácil de mantener y modificar

3. **Testeable**:
   - Los servicios se pueden mockear fácilmente
   - Inyección de dependencias por constructor facilita el testing

4. **Mantenible**:
   - Código limpio y bien documentado
   - Nombres descriptivos de métodos y variables
   - Comentarios JavaDoc en métodos públicos

5. **Escalable**:
   - Si necesitas agregar más proveedores OAuth2 (Facebook, GitHub), solo usas el mismo servicio
   - Si necesitas cambiar la lógica de creación de usuarios, solo modificas UserProvisioningService

---

### Diagrama de Flujo

```
┌─────────────────────────────────────────────────────────────┐
│                    Usuario accede al sistema                │
└────────────────┬────────────────────────────────────────────┘
                 │
                 ├─────────────────┬──────────────────────────┐
                 │                 │                          │
         ┌───────▼────────┐  ┌─────▼──────┐       ┌──────────▼─────────┐
         │  Login OAuth2  │  │Login Normal│       │   API con JWT      │
         │    (Google)    │  │  (email/pw)│       │   (crear venta)    │
         └───────┬────────┘  └─────┬──────┘       └──────────┬─────────┘
                 │                 │                          │
                 │                 │                          │
         ┌───────▼────────────────▼──────────────────────────▼─────────┐
         │                UserProvisioningService                      │
         │  findOrCreateUser(username, firstName?, lastName?)          │
         └───────┬─────────────────────────────────────────────────────┘
                 │
         ┌───────▼────────┐
         │ Usuario existe?│
         └───────┬────────┘
                 │
        ┌────────┴─────────┐
        │                  │
   ┌────▼─────┐     ┌──────▼────────┐
   │   SÍ     │     │      NO       │
   │Retornar  │     │  Crear nuevo  │
   │ usuario  │     │    usuario    │
   └────┬─────┘     └──────┬────────┘
        │                  │
        └────────┬─────────┘
                 │
         ┌───────▼─────────┐
         │  UserEntity     │
         │  (con user_id)  │
         └─────────────────┘
```

---

### Configuración de Seguridad

**Problema resuelto: Referencia Circular**

Antes teníamos un problema de dependencias circulares:
```
SecurityConfig → OAuth2SuccessHandler → PasswordEncoder → SecurityConfig
```

**Solución**: Usar `@Lazy` en `PasswordEncoder`
```java
@Autowired
public UserProvisioningService(
    UserRepository userRepository,
    RoleRepository roleRepository,
    @Lazy PasswordEncoder passwordEncoder) {
    // ...
}
```

El `@Lazy` hace que Spring no inyecte el `PasswordEncoder` inmediatamente, sino cuando se necesite por primera vez, rompiendo el ciclo.

---

### Testing

Para testear `UserProvisioningService`:

```java
@Test
void testFindOrCreateUser_UserExists() {
    // Arrange
    UserEntity existingUser = new UserEntity();
    when(userRepository.findUserEntityByUsername("test@example.com"))
        .thenReturn(Optional.of(existingUser));
    
    // Act
    UserEntity result = userProvisioningService.findOrCreateUser("test@example.com");
    
    // Assert
    assertEquals(existingUser, result);
    verify(userRepository, times(1)).findUserEntityByUsername("test@example.com");
    verify(userRepository, never()).save(any());
}
```

---

### Próximos Pasos (Mejoras Futuras)

1. **Events**: Emitir un evento `UserCreatedEvent` cuando se crea un usuario nuevo
2. **Audit**: Agregar auditoría de cuándo se creó el usuario y desde dónde
3. **Roles dinámicos**: Permitir asignar diferentes roles según el dominio del email
4. **Email de bienvenida**: Enviar email cuando se crea un usuario nuevo
5. **Rate limiting**: Limitar la cantidad de usuarios creados desde la misma IP

---

### Conclusión

La arquitectura actual es **limpia, mantenible y escalable**. Sigue principios SOLID y permite agregar nuevas funcionalidades fácilmente sin romper el código existente.
