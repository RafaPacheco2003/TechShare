package com.techshare.services;

import com.techshare.entities.RoleEntity;
import com.techshare.entities.enums.RoleEnum;
import com.techshare.exceptions.RoleNotFoundException;
import com.techshare.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;
    
    /**
     * Verifica si todos los roles especificados existen en el sistema
     * @param roleNames nombres de roles a verificar
     * @throws RoleNotFoundException si algún rol no existe
     * @return Set de entidades de roles existentes
     */
    public Set<RoleEntity> validateAndGetRoles(Set<String> roleNames) {
        if (roleNames == null || roleNames.isEmpty()) {
            throw new RoleNotFoundException("No se han especificado roles");
        }
        
        // Verificar si cada nombre de rol es válido (existe en el enum RoleEnum)
        Set<String> validRoleNames = Arrays.stream(RoleEnum.values())
                .map(Enum::name)
                .collect(Collectors.toSet());
        
        // Encontrar roles inválidos
        Set<String> invalidRoles = roleNames.stream()
                .filter(roleName -> !validRoleNames.contains(roleName))
                .collect(Collectors.toSet());
        
        if (!invalidRoles.isEmpty()) {
            throw new RoleNotFoundException("Los siguientes roles no son válidos: " + String.join(", ", invalidRoles) + 
                    ". Roles disponibles: " + String.join(", ", validRoleNames));
        }
        
        // Obtener las entidades de roles
        Set<RoleEntity> roles = new HashSet<>();
        for (String roleName : roleNames) {
            RoleEntity role = roleRepository.findByRoleEnum(RoleEnum.valueOf(roleName))
                    .orElseThrow(() -> new RoleNotFoundException("Rol no encontrado en base de datos: " + roleName));
            roles.add(role);
        }
        
        return roles;
    }
} 