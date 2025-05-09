package com.techshare.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Table(name = "user")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @Column(unique = true)
    private String username;
    private String password;

    private String firstName;
    private String lastName;

    @Column(name = "is_enabled", columnDefinition = "boolean default false")
    private boolean isEnabled = false;

    @Column(name = "account_no_expired", columnDefinition = "boolean default false")
    private boolean accountNoExpired = false; 

    @Column(name = "credential_no_expired", columnDefinition = "boolean default false")
    private boolean credentialNoExpired = false;

    @Column(name = "is_account_no_locked", columnDefinition = "boolean default false")
    private boolean isAccountNoLocked = false; 

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles = new HashSet<>();

    // Un usuario puede tener solo una membresía activa a la vez
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserMembership> membershipHistory = new HashSet<>();

    // Método para obtener la membresía activa actual
    public UserMembership getActiveMembership() {
        return membershipHistory.stream()
                .filter(UserMembership::isActive)
                .findFirst()
                .orElse(null);
    }

    public Set<UserMembership> getMembershipHistory() {
        return membershipHistory;
    }

    public void setMembershipHistory(Set<UserMembership> membershipHistory) {
        this.membershipHistory = membershipHistory;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public boolean isAccountNoExpired() {
        return accountNoExpired;
    }

    public void setAccountNoExpired(boolean accountNoExpired) {
        this.accountNoExpired = accountNoExpired;
    }

    public boolean isCredentialNoExpired() {
        return credentialNoExpired;
    }

    public void setCredentialNoExpired(boolean credentialNoExpired) {
        this.credentialNoExpired = credentialNoExpired;
    }

    public boolean isAccountNoLocked() {
        return isAccountNoLocked;
    }

    public void setAccountNoLocked(boolean accountNoLocked) {
        isAccountNoLocked = accountNoLocked;
    }

    public Set<RoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleEntity> roles) {
        this.roles = roles;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
