package com.rade.protect.config;

import com.rade.protect.data.FPVPilotRepository;
import com.rade.protect.model.Role;
import com.rade.protect.model.entity.FPVPilot;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration {

    private final FPVPilotRepository fpvPilotRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> fpvPilotRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public CommandLineRunner initUsers(BCryptPasswordEncoder passwordEncoder) {
        return args -> {
            FPVPilot fpvPilotAdmin = FPVPilot.builder()
                    .firstname("Thomas")
                    .lastname("Anderson")
                    .username("neo")
                    .password(passwordEncoder().encode("matrix"))
                    .authorities(Role.USER.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toSet()))
                    .build();
            fpvPilotRepository.save(fpvPilotAdmin);

            FPVPilot fpvPilotUser = FPVPilot.builder()
                    .firstname("Morpheus")
                    .lastname("Lorens")
                    .username("morpheus")
                    .password(passwordEncoder().encode("matrix-reload"))
                    .authorities(Role.ADMIN.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toSet()))
                    .build();
            fpvPilotRepository.save(fpvPilotUser);
        };
    }

}
