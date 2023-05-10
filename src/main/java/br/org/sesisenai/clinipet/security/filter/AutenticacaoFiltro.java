package br.org.sesisenai.clinipet.security.filter;

import br.org.sesisenai.clinipet.security.Utils.JwtUtils;
import br.org.sesisenai.clinipet.security.exception.UrlNaoEncontrada;
import br.org.sesisenai.clinipet.security.service.JpaService;
import br.org.sesisenai.clinipet.security.utils.CookieUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
public class AutenticacaoFiltro extends OncePerRequestFilter {
    private final CookieUtils cookieUtils = new CookieUtils();
    private final JwtUtils jwtUtils = new JwtUtils();
    private final JpaService jpaService;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws IOException, ServletException {
        try {

            if (validarUrl(request.getRequestURI())) {
                filterChain.doFilter(request, response);
                return;
            }

            // Busca o cookie e realiza a validção
            String token = cookieUtils.getTokenCookie(request);
            jwtUtils.validarToken(token);

            UserDetails usuario = jpaService.loadUserByUsername(jwtUtils.getUsuario(token));

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(usuario.getUsername(), usuario.getPassword(), usuario.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    try {
//        // Mesmo sem o token/cookie, ele validará se a URL é permitida ou não
//        validarUrl(request.getRequestURI());
//    } catch (UrlNaoEncontrada UrlNaoEncontrada) {
//        UrlNaoEncontrada.printStackTrace();
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//    }

    private boolean validarUrl(String url) {
        if ((url.equals("/api/logout")
                || url.equals("http://localhost:8085/api/login")
                || url.equals("https://localhost:8085/login")
                || url.equals("/api/login"))) {
            return true;
        }
        return false;
    }
}
