package com.turkcell.crm.identity_service.entities.concretes;


import com.turkcell.crm.identity_service.core.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
@SQLRestriction(value = "deleted_date is null")
public class User extends BaseEntity<Integer> implements UserDetails {
    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<UserRole> authorities;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<RefreshToken> refreshTokens;

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.authorities == null)
            return new HashSet<>();
        return authorities.stream()
                .map(UserRole::getRole)
                .toList();
    }

    public User(int id) {
        setId(id);
    }
}
