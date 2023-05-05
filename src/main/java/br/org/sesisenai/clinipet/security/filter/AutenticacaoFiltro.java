package br.org.sesisenai.clinipet.security.filter;

import br.org.sesisenai.clinipet.security.Utils.JwtUtils;
import br.org.sesisenai.clinipet.security.exception.CookieNaoEncontrado;
import br.org.sesisenai.clinipet.security.exception.TokenInvalido;
import br.org.sesisenai.clinipet.security.exception.UrlNaoEncontrada;
import br.org.sesisenai.clinipet.security.model.entity.UserJpa;
import br.org.sesisenai.clinipet.security.utils.CookieUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AutenticacaoFiltro extends OncePerRequestFilter {
    private final CookieUtils cookieUtils = new CookieUtils();
    private final JwtUtils jwtUtils = new JwtUtils();

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        try {
            // Busca o cookie e realiza a validção
            String token = cookieUtils.getTokenCookie(request);
            jwtUtils.validarToken(token);

            // Extrai o usuário do cookie
            UserJpa userJpa = cookieUtils.getUsuarioCookie(request);

            // Objeto utilizado para validar a autenticação
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(userJpa.getUsuario(), userJpa.getSenha(), userJpa.getAutorizacoes());
            System.out.println(usernamePasswordAuthenticationToken);
            // Se fosse utilizado um parâmetro que mantesse a autenticação
            // Não seria necessário o cookie, mas sim o Security Context Holder, porém, ele não manteria o usuário
            SecurityContextHolder.getContext().setAuthentication(
                    usernamePasswordAuthenticationToken);
        } catch (TokenInvalido | CookieNaoEncontrado e) {
            e.printStackTrace();
            try {
                // Mesmo sem o token/cookie, ele validará se a URL é permitida ou não
                validarUrl(request.getRequestURI());
            } catch (UrlNaoEncontrada UrlNaoEncontrada) {
                UrlNaoEncontrada.printStackTrace();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            filterChain.doFilter(request, response);
        }
    }

    private void validarUrl(String url) throws UrlNaoEncontrada {
        if (!(url.equals("/api/login/auth")
                || url.equals("/api/logout")
                || url.equals("http://localhost:8085/api/login")
                || url.equals("https://localhost:8085/login")
        )) {
            throw new UrlNaoEncontrada();
        }
    }
}
