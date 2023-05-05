package br.org.sesisenai.clinipet.security.controller;

import br.org.sesisenai.clinipet.security.model.dto.UsuarioDTO;
import br.org.sesisenai.clinipet.security.model.entity.UserJpa;
import br.org.sesisenai.clinipet.security.utils.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login")
@CrossOrigin
@AllArgsConstructor
public class AutenticacaoController {
    private AuthenticationManager authenticationManager;

    private final CookieUtils cookieUtils = new CookieUtils();

    // Requisição de autenticação
    @PostMapping("/auth")
    public ResponseEntity<Object> autenticacao(@RequestBody UsuarioDTO usuarioDTO, HttpServletResponse response) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    usuarioDTO.getEmail(), usuarioDTO.getSenha());
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            if (authentication.isAuthenticated()) {
                UserJpa usuario = (UserJpa) authentication.getPrincipal();
                Cookie finalCookie = cookieUtils.gerarTokenCookie(usuario);
                Cookie jwtCookie = finalCookie;
                response.addCookie(jwtCookie);
                Cookie userCookie = finalCookie;
                response.addCookie(userCookie);
                return ResponseEntity.ok().build();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
