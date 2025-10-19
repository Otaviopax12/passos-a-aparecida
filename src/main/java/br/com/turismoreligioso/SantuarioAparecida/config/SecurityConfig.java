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

    // --- INÍCIO DAS ADIÇÕES ---
    // Injeta o nosso serviço de detalhes do usuário que criamos
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    // Bean para criptografar as senhas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean que informa ao Spring Security para usar nosso serviço e nosso encoder de senha
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    // --- FIM DAS ADIÇÕES ---

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // .csrf(csrf -> csrf.disable()) // Reative o CSRF se desejar (removendo ou comentando esta linha)
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(authorize -> authorize
                        // Regras públicas
                        .requestMatchers("/", "/home", "/login", "/pessoas/cadastrar", "/error", "/destinos").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/roteiros/{id}").permitAll()

                        // --- NOVAS REGRAS DE HOSPEDAGEM ADICIONADAS ---
                        .requestMatchers("/hospedagens", "/hospedagens/{id}").permitAll()
                        .requestMatchers("/hospedagens/minha-hospedaria").hasRole("GERENTE_HOSPEDAGEM")
                        // --- FIM DAS NOVAS REGRAS ---

                        // Regras de perfis
                        .requestMatchers("/roteiros/disponiveis").hasRole("TURISTA")
                        .requestMatchers("/roteiros/meus-roteiros", "/roteiros/novo", "/roteiros/{id}/editar").hasRole("GUIA")

                        // Qualquer outra requisição que não foi mencionada acima precisa de autenticação
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

