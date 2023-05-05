package br.org.sesisenai.clinipet.security.utils;

import br.org.sesisenai.clinipet.security.Utils.JwtUtils;
import br.org.sesisenai.clinipet.security.exception.CookieNaoEncontrado;
import br.org.sesisenai.clinipet.security.model.entity.UserJpa;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.util.WebUtils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class CookieUtils {
    private final JwtUtils jwtUtils = new JwtUtils();

    public Cookie gerarTokenCookie(UserJpa userJpa) {
        String token = jwtUtils.gerarToken(userJpa);
        Cookie cookie = new Cookie("jwt", token);
        cookie.setPath("/");
        cookie.setMaxAge(2700);
        return cookie;
    }

    public String getTokenCookie(HttpServletRequest httpServletRequest) throws CookieNaoEncontrado {
        try {
            Cookie cookie = WebUtils.getCookie(httpServletRequest, "jwt");
            return cookie.getValue();
        } catch (Exception e) {
            throw new CookieNaoEncontrado();
        }
    }

    public Cookie renovarCookie(HttpServletRequest httpServletRequest, String nome) {
        Cookie cookie = WebUtils.getCookie(httpServletRequest, nome);
        cookie.setPath("/");
        cookie.setMaxAge(2700);
        return cookie;
    }

    public UserJpa getUsuarioCookie(HttpServletRequest httpServletRequest) throws CookieNaoEncontrado {
        try {
            Cookie cookie = WebUtils.getCookie(httpServletRequest, "usuario");
            String jsonUsuario = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonUsuario, UserJpa.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CookieNaoEncontrado();
        }
    }
}
