package com.example.devSns.authorities;

import com.example.devSns.entities.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@RequiredArgsConstructor
public class MemberDetails implements UserDetails {
    private final Users user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

    }

    @Override
    public String getUsername() {

    }

    @Override
    public String getPassword() {
        return null;
    }
}

