package br.org.sesisenai.clinipet.security.Utils;

import br.org.sesisenai.clinipet.security.exception.TokenInvalido;
import br.org.sesisenai.clinipet.security.model.entity.UserJpa;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtils {

    private final String senha = "c127a7b6adb013a5ff879ae71afa62afa4b4ceb72afaa54711dbcde67b6dc325";

    // Método para gerar o token
    public String gerarToken(UserJpa userJpa) {
        return Jwts.builder()
                .setIssuer("CliniPet")
                // Atributo identificador do cookie
                .setSubject(userJpa.getUsername())
                // Data de criação do TOKEN
                .setIssuedAt(new Date())
                // Tempo para 45 minutos
                .setExpiration(new Date(new Date().getTime() + 2700))
                // Criptografia utilizada
                .signWith(SignatureAlgorithm.HS256, senha)
                .compact();
    }

    // Método de validação do token
    public void validarToken(String token) {
        try {
            Jwts.parser().setSigningKey(senha).parseClaimsJws(token);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public String getUsuario(String token) {
        return Jwts.parser().setSigningKey(senha).parseClaimsJws(token).getBody().getSubject();
    }

}
