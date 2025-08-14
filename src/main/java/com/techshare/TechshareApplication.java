package com.techshare;

import com.techshare.entities.PermissionEntity;
import com.techshare.entities.RoleEntity;
import com.techshare.entities.RoleEnum;
import com.techshare.entities.UserEntity;
import com.techshare.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Set;

@SpringBootApplication
public class TechshareApplication {

	public static void main(String[] args) {
		SpringApplication.run(TechshareApplication.class, args);
	}

/*
	@Bean
	CommandLineRunner init(UserRepository userRepository){
		return args -> {

			//Create permissions
			PermissionEntity createPermission = PermissionEntity.builder()
					.name("CREATED")
					.build();

			PermissionEntity readPermission = PermissionEntity.builder()
					.name("READ")
					.build();

			PermissionEntity updatePermission = PermissionEntity.builder()
					.name("UPDATE")
					.build();

			PermissionEntity deletePermission = PermissionEntity.builder()
					.name("DELETE")
					.build();


			//CREATE ROLE
			RoleEntity roleAdmin= RoleEntity.builder()
					.roleEnum(RoleEnum.ADMIN)
					.permissionList(Set.of(createPermission, readPermission, updatePermission, deletePermission))
					.build();

			//CREATE ROLE
			RoleEntity roleUser= RoleEntity.builder()
					.roleEnum(RoleEnum.USER)
					.permissionList(Set.of(createPermission, readPermission))
					.build();

			//CREATE ROLE
			RoleEntity roleInvited= RoleEntity.builder()
					.roleEnum(RoleEnum.INVITED)
					.permissionList(Set.of(createPermission, readPermission))
					.build();

			UserEntity userRafaul = UserEntity.builder()
					.username("rafa")
					.password("123")
					.isEnabled(true)
					.accountNoExpired(true)
					.credentialNoExpired(true)
					.roles(Set.of(roleAdmin))
					.build();
			UserEntity userRafaUser = UserEntity.builder()
					.username("rafaUser")
					.password("123")
					.isEnabled(true)
					.accountNoExpired(true)
					.credentialNoExpired(true)
					.roles(Set.of(roleUser))
					.build();
			UserEntity userRafaVisited = UserEntity.builder()
					.username("rafaVisited")
					.password("123")
					.isEnabled(true)
					.accountNoExpired(true)
					.credentialNoExpired(true)
					.roles(Set.of(roleInvited))
					.build();

			userRepository.saveAll(List.of(userRafaul, userRafaVisited, userRafaUser));
		};


	}*/
}
