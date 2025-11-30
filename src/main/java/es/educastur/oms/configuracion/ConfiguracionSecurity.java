package es.educastur.oms.configuracion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
public class ConfiguracionSecurity {
    
    @Autowired
    @Lazy
    private ServicioDetallesUsuario S_detallesUsuario;
    
    @Autowired
    private RedireccionUsuarioAutenticacionExitosa redireccionautenticaionExitosa;
    
    @Autowired
    private ServicioSession S_session;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(S_detallesUsuario);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authenticationProvider(authenticationProvider())
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/", "/inicio", "/login", "/iniciosesion-registrarse", "/registro", "/css/**", "/js/**", "/img/**").permitAll()
                .requestMatchers("/inicio-admin","/productos-admin", "/productos-adminAñadir", 
			              "/productos-adminModificar", "/productos-adminEliminar","/anadirpersonal")
                .hasRole("ADMIN")
                .requestMatchers("/inicio-personal")
                .hasRole("PERSONAL")
                // permitir que tanto PERSONAL como ADMIN gestionen pedidos
                .requestMatchers("/gestion-pedidos").hasAnyRole("PERSONAL","ADMIN")
                .requestMatchers("/inicio-cliente", "/mispedidos", "/carrito-compra", "/factura").hasRole("CLIENTE")
                .requestMatchers("/gestion-ejemplares", "/gestion-mensajes").hasAnyRole("ADMIN", "PERSONAL")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("usuario")
                .passwordParameter("contrasena")
                .successHandler((request, response, authentication) -> {
                    //S_session.eliminarSesionesPrevias(authentication.getName());
                    redireccionautenticaionExitosa.onAuthenticationSuccess(request, response, authentication);
                })
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/inicio?logout=true")
            )
            .exceptionHandling(ex -> ex.accessDeniedPage("/error-403"))
            .sessionManagement(session -> session
                .maximumSessions(1)
                .expiredSessionStrategy(event -> {
                    System.out.println("Sesión anterior eliminada: " + event.getSessionInformation().getSessionId());
                })
            );

        return http.build();
    }

    // Maneja eventos de sesión HTTP
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}
