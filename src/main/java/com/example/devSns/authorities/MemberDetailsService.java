package com.example.devSns.authorities;

import com.example.devSns.entities.Users;
import com.example.devSns.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MemberDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByLoginID(username).orElseThrow();
        MemberDetails memberDetails = new MemberDetails(user);
        return memberDetails;
    }
}
