#!/bin/bash

# Script para insertar roles iniciales en la base de datos TechShare
# Ejecutar después de que el contenedor esté corriendo y las tablas creadas

echo "🔄 Insertando roles en la base de datos..."

docker exec -i techshare-mysql mysql -uroot -pBlacky20. techshare << EOF
-- Insertar roles iniciales (ignora si ya existen)
INSERT INTO role (role_name) VALUES ('ADMIN') ON DUPLICATE KEY UPDATE role_name=role_name;
INSERT INTO role (role_name) VALUES ('USER') ON DUPLICATE KEY UPDATE role_name=role_name;
INSERT INTO role (role_name) VALUES ('INVITED') ON DUPLICATE KEY UPDATE role_name=role_name;
INSERT INTO role (role_name) VALUES ('DEVELOPER') ON DUPLICATE KEY UPDATE role_name=role_name;

-- Mostrar roles insertados
SELECT '✅ Roles insertados exitosamente:' AS mensaje;
SELECT * FROM role;
EOF

echo "✅ Roles inicializados correctamente"
