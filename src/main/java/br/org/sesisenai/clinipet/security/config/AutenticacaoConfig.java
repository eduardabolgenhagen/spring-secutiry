package br.org.sesisenai.clinipet.security.config;

import br.org.sesisenai.clinipet.security.filter.AutenticacaoFiltro;
import br.org.sesisenai.clinipet.security.service.JpaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class AutenticacaoConfig {
    // Para utilizar os nossos usuários
    @Autowired
    private JpaService jpaService;

    @Autowired
    public void configure(AuthenticationManagerBuilder authmb) throws Exception {
        authmb.userDetailsService(jpaService).passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Quais caminhos podem acessar
        configuration.setAllowedOrigins(List.of("/**"));
        // Métodos que podem ser feitos
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        // Permite acesso aos COOKIES
        configuration.setAllowCredentials(true);
        // Acesso aos cabeçalhos de requisição
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Registra as configurações feitas para tudo ("*")
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // Define quais são os caminhos permitidos
        httpSecurity.authorizeHttpRequests()
                // Define quais usuários tem permissão para fazer login
                .requestMatchers("/login").permitAll()


                // VETERINARIO
                .requestMatchers(HttpMethod.GET, "/veterinario").permitAll()
                .requestMatchers(HttpMethod.GET, "/veterinario/*").permitAll()
                .requestMatchers(HttpMethod.POST, "/veterinario").hasAuthority("VETERINARIO")
                .requestMatchers(HttpMethod.PUT, "/veterinario/*").hasAuthority("VETERINARIO")
                .requestMatchers(HttpMethod.DELETE, "/veterinario/*").hasAuthority("VETERINARIO")

                // ATENDENTE
                .requestMatchers(HttpMethod.GET, "/atendente").hasAnyAuthority("ATENDENTE", "VETERINARIO")
                .requestMatchers(HttpMethod.GET, "/atendente/*").hasAnyAuthority("ATENDENTE", "VETERINARIO")
                .requestMatchers(HttpMethod.POST, "/atendente").hasAuthority("VETERINARIO")
                .requestMatchers(HttpMethod.PUT, "/atendente/*").hasAuthority("VETERINARIO")
                .requestMatchers(HttpMethod.DELETE, "/atendente/*").hasAuthority("VETERINARIO")

                // CLIENTE
                .requestMatchers(HttpMethod.GET, "/cliente").hasAnyAuthority("ATENDENTE", "CLIENTE", "VETERINARIO")
                .requestMatchers(HttpMethod.GET, "/cliente/*").hasAnyAuthority("ATENDENTE", "CLIENTE", "VETERINARIO")
                .requestMatchers(HttpMethod.POST, "/cliente").hasAuthority("ATENDENTE")
                .requestMatchers(HttpMethod.PUT, "/cliente/*").hasAnyAuthority("ATENDENTE", "VETERINARIO")
                .requestMatchers(HttpMethod.DELETE, "/cliente/*").hasAnyAuthority("ATENDENTE", "VETERINARIO")

                // SERVICO
                .requestMatchers(HttpMethod.GET, "/servico").permitAll()
                .requestMatchers(HttpMethod.GET, "/servico/*").permitAll()
                .requestMatchers(HttpMethod.POST, "/servico").hasAuthority("VETERINARIO")
                .requestMatchers(HttpMethod.PUT, "/servico/*").hasAuthority("VETERINARIO")
                .requestMatchers(HttpMethod.DELETE, "/servico/*").hasAuthority("VETERINARIO")

                // ANIMAl
                .requestMatchers(HttpMethod.GET, "/animal").hasAnyAuthority("ATENDENTE", "VETERINARIO")
                .requestMatchers(HttpMethod.GET, "/animal/*").hasAnyAuthority("ATENDENTE", "VETERINARIO")
                .requestMatchers(HttpMethod.POST, "/animal").hasAuthority("ATENDENTE")
                .requestMatchers(HttpMethod.PUT, "/animal/*").hasAnyAuthority("ATENDENTE", "VETERINARIO")
                .requestMatchers(HttpMethod.DELETE, "/animal/*").hasAnyAuthority("ATENDENTE", "VETERINARIO")

                // AGENDA
                .requestMatchers(HttpMethod.GET, "/agenda").hasAnyAuthority("ATENDENTE", "CLIENTE", "VETERINARIO")
                .requestMatchers(HttpMethod.GET, "/agenda/*").hasAnyAuthority("ATENDENTE", "CLIENTE", "VETERINARIO")
                .requestMatchers(HttpMethod.POST, "/agenda").hasAuthority("ATENDENTE")
                .requestMatchers(HttpMethod.PUT, "/agenda/*").hasAnyAuthority("ATENDENTE", "VETERINARIO")
                .requestMatchers(HttpMethod.DELETE, "/agenda/*").hasAnyAuthority("ATENDENTE", "VETERINARIO")

                // PRONTUARIO
                .requestMatchers(HttpMethod.GET, "/prontuario").hasAnyAuthority("ATENDENTE", "CLIENTE", "VETERINARIO")
                .requestMatchers(HttpMethod.GET, "/prontuario/*").hasAnyAuthority("ATENDENTE", "CLIENTE", "VETERINARIO")
                .requestMatchers(HttpMethod.POST, "/prontuario").hasAuthority("VETERINARIO")
                .requestMatchers(HttpMethod.PUT, "/prontuario/*").hasAuthority("VETERINARIO")
                .requestMatchers(HttpMethod.DELETE, "/prontuario/*").hasAuthority("VETERINARIO")

                .anyRequest().authenticated();
        httpSecurity.csrf().disable();
        // Libera o acesso para que outra aplicação consuma a minha
        httpSecurity.cors().configurationSource(corsConfigurationSource());
//        // Habilita a função de logout
//        httpSecurity.logout()
//                // Apaga o COOKIE assim que realiza o logout
//                .deleteCookies("token", "user")
//                // Permite que todos os usuário façam logout
//                .permitAll();
        httpSecurity.sessionManagement().sessionCreationPolicy(
                        // STATELESS: não mantém o usuário autenticado, ele verifica o usuário e senha constantemente e
                        // mantém a sessão ativa até que o próprio realize logout
                        SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new AutenticacaoFiltro(jpaService), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
