package br.org.sesisenai.clinipet.security.exception;

public class TokenInvalido extends Exception{
    public TokenInvalido() {
        super("TOKEN INVÁLIDO!");
    }
}
