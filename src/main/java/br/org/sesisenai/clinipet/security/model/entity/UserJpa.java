package br.org.sesisenai.clinipet.security.model.entity;

import br.org.sesisenai.clinipet.model.entity.Pessoa;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class UserJpa implements UserDetails {
//    private Pessoa pessoa;
//    private List<PerfilUsuario> autorizacoes;
//    private boolean isAccountNonExpired = true;
//    private boolean isAccountNonLocked = true;
//    private boolean isCredentialsNonExpired = true;
//    private boolean isEnabled = true;
//
//    public UserJpa(Pessoa pessoa) {
//        this.pessoa = pessoa;
//    }
//
//    @Override
//    public String getPassword() {
//        return pessoa.getSenha();
//    }
//
//    @Override
//    public String getUsername() {
//        return pessoa.getEmail();
//    }
//
//    @Override
//    public Collection<GrantedAuthority> getAuthorities() {
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(new SimpleGrantedAuthority(this.getPessoa().getClass().getSimpleName()));
//        return authorities;
//    }

    private Pessoa pessoa;
    private List<PerfilUsuario> authorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private String username;
    private String password;

    public UserJpa(Pessoa pessoa) {
        this.pessoa = pessoa;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
        this.password = pessoa.getSenha();
        this.username = pessoa.getEmail();
        this.authorities = new ArrayList<>();
        this.authorities.add(PerfilUsuario.perfilUsuarioOf(pessoa.getClass().getSimpleName()));
    }
}
