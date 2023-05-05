package br.org.sesisenai.clinipet.security.model.entity;

import br.org.sesisenai.clinipet.model.entity.Pessoa;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserJpa implements UserDetails {
    private Pessoa pessoa;
    private List<PerfilUsuario> autorizacoes;
    private boolean contaNaoExpirada;
    private boolean contaNaoBloqueada;
    private boolean credenciaisNaoExpiradas;
    private boolean habilitado;
    private String senha;
    private String usuario;

    public UserJpa(Pessoa pessoa) {
        this.pessoa = pessoa;
        this.autorizacoes = new ArrayList<>();
        this.autorizacoes.add(PerfilUsuario.perfilUsuarioOf(pessoa.getClass().getName()));
        this.contaNaoExpirada = true;
        this.contaNaoBloqueada = true;
        this.credenciaisNaoExpiradas = true;
        this.habilitado = true;
        this.senha = senha;
        this.usuario = usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
