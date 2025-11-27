-- Script de inicialización de la base de datos TechShare
-- Este script se ejecuta automáticamente cuando se crea el contenedor de MySQL

-- Configurar el conjunto de caracteres
ALTER DATABASE techshare CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- NOTA: Las tablas son creadas automáticamente por Hibernate (spring.jpa.hibernate.ddl-auto=update)
-- Por lo tanto, este script NO puede insertar datos en tablas que aún no existen.
-- Los roles deben insertarse DESPUÉS de que la aplicación Spring Boot cree las tablas.

-- Para insertar roles después de que las tablas se creen, ejecutar:
-- docker exec -it techshare-mysql mysql -uroot -pBlacky20. techshare -e "
-- INSERT INTO role (role_name) VALUES ('ADMIN') ON DUPLICATE KEY UPDATE role_name=role_name;
-- INSERT INTO role (role_name) VALUES ('USER') ON DUPLICATE KEY UPDATE role_name=role_name;
-- INSERT INTO role (role_name) VALUES ('INVITED') ON DUPLICATE KEY UPDATE role_name=role_name;
-- INSERT INTO role (role_name) VALUES ('DEVELOPER') ON DUPLICATE KEY UPDATE role_name=role_name;
-- "

-- Mensaje de confirmación
SELECT 'Base de datos TechShare inicializada correctamente' AS mensaje;
