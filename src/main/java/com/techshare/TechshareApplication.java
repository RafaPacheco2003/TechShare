package com.techshare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
