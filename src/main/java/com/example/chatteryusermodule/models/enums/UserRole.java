package com.example.chatteryusermodule.models.enums;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    PRO,
    SIMPLE;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
