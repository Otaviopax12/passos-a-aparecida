package br.com.turismoreligioso.SantuarioAparecida.config;


import br.com.turismoreligioso.SantuarioAparecida.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(authorize -> authorize

                        .requestMatchers("/", "/home", "/login", "/pessoas/cadastrar", "/error", "/destinos").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/roteiros/{id}").permitAll()
                        .requestMatchers("/hospedagens", "/hospedagens/{id}").permitAll() // Lista e detalhes públicos


                        .requestMatchers("/roteiros/disponiveis").hasRole("TURISTA")
                        .requestMatchers("/roteiros/meus-roteiros", "/roteiros/novo", "/roteiros/{id}/editar").hasRole("GUIA")

                        .requestMatchers("/hospedagens/minha-hospedaria", "/hospedagens/cadastrar", "/hospedagens/{id}/editar").hasRole("GERENTE_HOSPEDAGEM") // Gerente gere as suas
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}

