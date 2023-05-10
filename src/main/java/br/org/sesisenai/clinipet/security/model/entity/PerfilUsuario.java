package br.org.sesisenai.clinipet.security.model.entity;

import org.springframework.security.core.GrantedAuthority;

public enum PerfilUsuario implements GrantedAuthority {
    ATENDENTE("ATENDENTE"),
    VETERINARIO("VETERINARIO"),
    CLIENTE("CLIENTE");

    private String descricao;

    PerfilUsuario(String descricao) {
        this.descricao = descricao;
    }

    public static PerfilUsuario perfilUsuarioOf(String nome) {
        return switch (nome) {
            case "Atendente" -> ATENDENTE;
            case "Veterinario" -> VETERINARIO;
            case "Cliente" -> CLIENTE;
            default -> null;
        };
    }

    ;

    @Override
    public String getAuthority() {
        return this.name();
    }

}