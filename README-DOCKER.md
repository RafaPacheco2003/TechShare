# 🐳 Guía de Docker para TechShare

Esta guía te ayudará a ejecutar el proyecto TechShare utilizando Docker y Docker Compose.

## 📋 Prerequisitos

Antes de comenzar, asegúrate de tener instalado:

- [Docker](https://docs.docker.com/get-docker/) (versión 20.10 o superior)
- [Docker Compose](https://docs.docker.com/compose/install/) (versión 2.0 o superior)

Para verificar que tienes Docker instalado correctamente:

```bash
docker --version
docker-compose --version
```

## 🏗️ Arquitectura del Proyecto

El proyecto utiliza Docker Compose para orquestar dos servicios:

1. **MySQL Database** (`mysql`):
   - Base de datos MySQL 8.0
   - Puerto expuesto: 3307 (host) → 3306 (container)
   - Volumen persistente para datos: `mysql_data`
   - Scripts de inicialización en `init-db/`

2. **Spring Boot Application** (`app`):
   - Aplicación Java con Spring Boot
   - Puerto expuesto: 8080
   - Volumen persistente para imágenes subidas: `uploaded_images`
   - Compilación multi-stage para optimizar el tamaño de la imagen

## 📁 Volúmenes Persistentes

### Volúmenes Named (Gestionados por Docker)

El proyecto utiliza volúmenes named que persisten los datos:

1. **mysql_data**: Almacena los datos de la base de datos MySQL
2. **uploaded_images**: Almacena las imágenes subidas por los usuarios

Estos volúmenes se mantienen incluso si eliminas los contenedores.

### Ver volúmenes

```bash
# Listar todos los volúmenes
docker volume ls

# Inspeccionar un volumen específico
docker volume inspect techshare_mysql_data
docker volume inspect techshare_uploaded_images
```

### Backup de volúmenes

```bash
# Backup de la base de datos
docker run --rm \
  --volumes-from techshare-mysql \
  -v $(pwd)/backups:/backup \
  ubuntu tar cvf /backup/mysql-backup-$(date +%Y%m%d).tar /var/lib/mysql

# Backup de imágenes subidas
docker run --rm \
  --volumes-from techshare-app \
  -v $(pwd)/backups:/backup \
  ubuntu tar cvf /backup/images-backup-$(date +%Y%m%d).tar /app/uploaded-images
```

## 🚀 Cómo Ejecutar el Proyecto

### 1. Construir y Levantar los Servicios

```bash
# Desde el directorio raíz del proyecto
docker-compose up --build
```

Este comando:
- Construye la imagen de la aplicación Spring Boot
- Descarga la imagen de MySQL 8.0
- Crea los volúmenes necesarios
- Inicia ambos contenedores
- Muestra los logs en tiempo real

### 2. Ejecutar en Segundo Plano (Detached Mode)

```bash
docker-compose up -d --build
```

### 3. Ver los Logs

```bash
# Ver logs de todos los servicios
docker-compose logs -f

# Ver logs solo de la aplicación
docker-compose logs -f app

# Ver logs solo de MySQL
docker-compose logs -f mysql
```

### 4. Verificar el Estado de los Contenedores

```bash
docker-compose ps
```

## 🔍 Acceder a la Aplicación

Una vez que los contenedores estén corriendo:

- **API Backend**: http://localhost:8080
- **Base de datos MySQL**: 
  - Host: localhost
  - Puerto: 3307
  - Usuario: root
  - Contraseña: Blacky20.
  - Base de datos: techshare

## 🛠️ Comandos Útiles

### Detener los Servicios

```bash
# Detener los contenedores (mantiene los volúmenes)
docker-compose stop

# Detener y eliminar los contenedores (mantiene los volúmenes)
docker-compose down
```

### Reiniciar los Servicios

```bash
# Reiniciar todos los servicios
docker-compose restart

# Reiniciar solo la aplicación
docker-compose restart app

# Reiniciar solo MySQL
docker-compose restart mysql
```

### Eliminar Todo (Incluidos Volúmenes)

```bash
# ⚠️ CUIDADO: Esto eliminará todos los datos
docker-compose down -v
```

### Reconstruir la Aplicación

```bash
# Si haces cambios en el código
docker-compose up --build app
```

### Ejecutar Comandos en los Contenedores

```bash
# Acceder a la shell de la aplicación
docker exec -it techshare-app sh

# Acceder a MySQL
docker exec -it techshare-mysql mysql -uroot -pBlacky20. techshare

# Ver archivos en el volumen de imágenes
docker exec -it techshare-app ls -la /app/uploaded-images
```

### Limpiar el Sistema Docker

```bash
# Eliminar imágenes no utilizadas
docker image prune

# Eliminar volúmenes no utilizados
docker volume prune

# Limpiar todo el sistema Docker (cuidado)
docker system prune -a --volumes
```

## 🔧 Configuración

### Variables de Entorno

Puedes personalizar las variables de entorno editando el archivo `docker-compose.yml` o creando un archivo `.env`:

```env
# .env
MYSQL_ROOT_PASSWORD=tu_password
MYSQL_DATABASE=techshare
APP_PORT=8080
DB_PORT=3307
```

### Modificar Puertos

Si necesitas cambiar los puertos, edita `docker-compose.yml`:

```yaml
services:
  mysql:
    ports:
      - "3308:3306"  # Cambiar 3307 a 3308
  
  app:
    ports:
      - "9090:8080"  # Cambiar 8080 a 9090
```

## 🐛 Solución de Problemas

### La aplicación no puede conectarse a MySQL

```bash
# Verificar que MySQL esté saludable
docker-compose ps

# Ver logs de MySQL
docker-compose logs mysql

# Esperar a que MySQL esté listo
docker-compose up --wait
```

### El contenedor se reinicia constantemente

```bash
# Ver los logs para identificar el error
docker-compose logs app

# Verificar el healthcheck
docker inspect techshare-app
```

### Puerto ya en uso

```bash
# Encontrar el proceso usando el puerto
lsof -i :8080
lsof -i :3307

# Matar el proceso o cambiar el puerto en docker-compose.yml
```

### Problemas con volúmenes

```bash
# Eliminar volúmenes y recrear
docker-compose down -v
docker-compose up --build

# Verificar permisos
docker exec -it techshare-app ls -la /app/uploaded-images
```

### La base de datos no persiste los datos

```bash
# Verificar que el volumen existe
docker volume ls | grep mysql_data

# Inspeccionar el volumen
docker volume inspect techshare_mysql_data
```

## 📊 Monitoreo y Salud

### Healthchecks

Ambos servicios tienen healthchecks configurados:

```bash
# Ver el estado de salud
docker inspect techshare-mysql | grep -A 10 Health
docker inspect techshare-app | grep -A 10 Health
```

### Estadísticas de Recursos

```bash
# Ver uso de recursos en tiempo real
docker stats

# Ver solo los contenedores de TechShare
docker stats techshare-app techshare-mysql
```

## 🔐 Seguridad

### Cambiar Contraseñas

Antes de desplegar en producción:

1. Cambia `MYSQL_ROOT_PASSWORD` en `docker-compose.yml`
2. Actualiza `SPRING_DATASOURCE_PASSWORD` para que coincida
3. Cambia las claves de JWT, Stripe, OAuth2, etc.

### Mejores Prácticas

- No subas `docker-compose.yml` con credenciales reales a Git
- Usa variables de entorno o Docker Secrets en producción
- Mantén las imágenes actualizadas
- Limita los recursos de los contenedores en producción

## 📦 Despliegue en Producción

Para producción, considera:

1. Usar un archivo `docker-compose.prod.yml` separado
2. Configurar certificados SSL/TLS
3. Usar un reverse proxy (nginx/traefik)
4. Implementar monitoreo (Prometheus, Grafana)
5. Configurar backups automáticos
6. Usar orquestadores como Kubernetes para escalabilidad

## 🆘 Soporte

Si encuentras problemas:

1. Revisa los logs: `docker-compose logs -f`
2. Verifica el estado: `docker-compose ps`
3. Revisa la documentación oficial de Docker
4. Contacta al equipo de desarrollo

## 📝 Notas Adicionales

- El primer arranque puede tardar varios minutos mientras Maven descarga las dependencias
- Los datos de MySQL y las imágenes subidas persisten entre reinicios gracias a los volúmenes
- El healthcheck de la aplicación espera 60 segundos antes de comenzar a verificar
- MySQL tiene un healthcheck que garantiza que esté listo antes de que la app intente conectarse

---

**¡Feliz Dockerización! 🐳**
