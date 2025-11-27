# 🔐 Gestión de Roles en TechShare

## Roles Disponibles

La aplicación TechShare utiliza los siguientes roles definidos en `RoleEnum`:

- **ADMIN**: Administrador del sistema con todos los permisos
- **USER**: Usuario regular de la plataforma
- **INVITED**: Usuario invitado con permisos limitados
- **DEVELOPER**: Desarrollador con permisos especiales

## Inicialización de Roles

### Opción 1: Script Automático (Recomendado)

Ejecuta el script de inicialización después de levantar los contenedores:

```bash
./init-roles.sh
```

### Opción 2: Inserción Manual

Conecta a MySQL y ejecuta:

```bash
docker exec -it techshare-mysql mysql -uroot -pBlacky20. techshare
```

Luego ejecuta:

```sql
INSERT INTO role (role_name) VALUES ('ADMIN') ON DUPLICATE KEY UPDATE role_name=role_name;
INSERT INTO role (role_name) VALUES ('USER') ON DUPLICATE KEY UPDATE role_name=role_name;
INSERT INTO role (role_name) VALUES ('INVITED') ON DUPLICATE KEY UPDATE role_name=role_name;
INSERT INTO role (role_name) VALUES ('DEVELOPER') ON DUPLICATE KEY UPDATE role_name=role_name;
```

### Opción 3: Comando Directo

```bash
docker exec -it techshare-mysql mysql -uroot -pBlacky20. techshare -e "
INSERT INTO role (role_name) VALUES ('ADMIN') ON DUPLICATE KEY UPDATE role_name=role_name;
INSERT INTO role (role_name) VALUES ('USER') ON DUPLICATE KEY UPDATE role_name=role_name;
INSERT INTO role (role_name) VALUES ('INVITED') ON DUPLICATE KEY UPDATE role_name=role_name;
INSERT INTO role (role_name) VALUES ('DEVELOPER') ON DUPLICATE KEY UPDATE role_name=role_name;
"
```

## Verificar Roles

Para verificar que los roles se insertaron correctamente:

```bash
docker exec -it techshare-mysql mysql -uroot -pBlacky20. techshare -e "SELECT * FROM role;"
```

Deberías ver:

```
+---------+-----------+
| role_id | role_name |
+---------+-----------+
|       1 | ADMIN     |
|       2 | USER      |
|       3 | INVITED   |
|       4 | DEVELOPER |
+---------+-----------+
```

## Registro de Usuarios

Al registrar un usuario mediante el endpoint `/auth/register`, debes especificar los roles en el request:

```json
{
  "username": "usuario@ejemplo.com",
  "password": "contraseña123",
  "firstName": "Juan",
  "lastName": "Pérez",
  "roles": ["USER"]
}
```

Para un administrador:

```json
{
  "username": "admin@ejemplo.com",
  "password": "admin123",
  "firstName": "Admin",
  "lastName": "Sistema",
  "roles": ["ADMIN"]
}
```

## Troubleshooting

### Error: "Rol no encontrado en base de datos: USER"

Este error ocurre cuando los roles no están insertados en la base de datos. Solución:

1. Verifica que los contenedores estén corriendo: `docker-compose ps`
2. Ejecuta el script de inicialización: `./init-roles.sh`
3. Verifica que los roles se insertaron: `docker exec -it techshare-mysql mysql -uroot -pBlacky20. techshare -e "SELECT * FROM role;"`

### Los roles se pierden al reiniciar el contenedor

Los datos están persistidos en el volumen `mysql_data`. Si eliminas el volumen con `docker-compose down -v`, perderás todos los datos incluyendo los roles. Para mantener los datos:

```bash
# Detener sin eliminar volúmenes
docker-compose down

# Iniciar de nuevo
docker-compose up -d
```

## Agregar Nuevos Roles

1. Agrega el nuevo rol en `RoleEnum.java`:
```java
public enum RoleEnum {
    ADMIN,
    USER,
    INVITED,
    DEVELOPER,
    NUEVO_ROL  // <-- Agregar aquí
}
```

2. Inserta el nuevo rol en la base de datos:
```bash
docker exec -it techshare-mysql mysql -uroot -pBlacky20. techshare -e "INSERT INTO role (role_name) VALUES ('NUEVO_ROL');"
```

3. Reinicia la aplicación si es necesario:
```bash
docker-compose restart app
```

## Estado Actual

✅ Roles insertados correctamente en la base de datos  
✅ La aplicación puede registrar usuarios con rol USER  
✅ Los datos persisten gracias al volumen `mysql_data`  
