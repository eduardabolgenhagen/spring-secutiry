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
        configuration.setAllowedOrigins(List.of("http//localhost:8085"));
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
                .requestMatchers("/api/login/auth", "/api/login").permitAll()
                // Define quais usuários tem permissão para fazer login
                .requestMatchers("/login").permitAll()
                // Métodos POST exclusivos
                .requestMatchers(HttpMethod.POST, "/api/animal", "/api/cliente", "/api/agenda")
                .hasAuthority("Atendente")
                .requestMatchers(HttpMethod.POST, "/api/atendente", "/api/veterinario", "/api/prontuario", "/api/servico")
                .hasAuthority("Veterinario")

                // Métodos PUT exclusivos
                .requestMatchers(HttpMethod.PUT, "/api/animal", "/api/cliente", "/api/agenda")
                .hasAnyAuthority("Atendente", "Veterinario")
                .requestMatchers(HttpMethod.PUT, "/api/prontuario", "/api/servico", "/api/veterinario", "/api/atendente")
                .hasAuthority("Veterinario")

                // Métodos DELETE exclusivos
                .requestMatchers(HttpMethod.DELETE, "/api/animal", "/api/cliente", "/api/agenda")
                .hasAnyAuthority("Atendente", "Veterinario")
                .requestMatchers(HttpMethod.DELETE, "/api/prontuario", "/api/veterinario", "/api/servico", "/api/atendente")
                .hasAuthority("Veterinario")

                // Métodos GET exclusivos
                .requestMatchers(HttpMethod.GET, "/api/animal", "/api/atendente")
                .hasAnyAuthority("Atendente", "Veterinario")
                .requestMatchers(HttpMethod.GET, "/api/prontuario", "/api/agenda", "/api/cliente")
                .hasAnyAuthority("Atendente", "Cliente", "Veterinario");
        httpSecurity.csrf().disable();
        // Libera o acesso para que outra aplicação consuma a minha
        httpSecurity.cors().configurationSource(corsConfigurationSource());
        // Habilita a função de logout
        httpSecurity.logout()
                // Apaga o COOKIE assim que realiza o logout
                .deleteCookies("jwt", "user")
                // Permite que todos os usuário façam logout
                .permitAll();
        httpSecurity.sessionManagement().sessionCreationPolicy(
                // STATELESS: não mantém o usuário autenticado, ele verifica o usuário e senha constantemente e
                // mantém a sessão ativa até que o próprio realize logout
                SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new AutenticacaoFiltro(), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
