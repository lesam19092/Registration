package org.example.registration.model.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    Role_ADMIN,
    Role_USER;

    @Override
    public String getAuthority() {
        return name();
    }
}
