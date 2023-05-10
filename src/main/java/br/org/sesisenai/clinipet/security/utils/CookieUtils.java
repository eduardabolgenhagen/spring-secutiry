package br.org.sesisenai.clinipet.security.utils;

import br.org.sesisenai.clinipet.security.exception.CookieNaoEncontrado;
import br.org.sesisenai.clinipet.security.model.entity.UserJpa;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.util.WebUtils;

public class CookieUtils {
    private final JwtUtils jwtUtils = new JwtUtils();

    public Cookie gerarTokenCookie(UserJpa userJpa) {
        String token = jwtUtils.gerarToken(userJpa);
        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        cookie.setMaxAge(2700);
        return cookie;
    }

    public String getTokenCookie(HttpServletRequest request) throws CookieNaoEncontrado {
        try {
            Cookie cookie = WebUtils.getCookie(request, "token");
            return cookie.getValue();
        } catch (Exception e) {
            throw new CookieNaoEncontrado();
        }
    }

}
