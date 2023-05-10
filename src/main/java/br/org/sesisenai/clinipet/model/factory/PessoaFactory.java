package br.org.sesisenai.clinipet.model.factory;

import br.org.sesisenai.clinipet.model.entity.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class PessoaFactory {
    public Pessoa getPessoa(SimpleGrantedAuthority sga, Pessoa pessoa) {
        switch (sga.getAuthority()) {
            case "Atendente" -> {
                Atendente atendente = new Atendente();
                atendente.setId(pessoa.getId());
                atendente.setNome(pessoa.getNome());
                atendente.setEmail(pessoa.getEmail());
                atendente.setSenha(pessoa.getSenha());
                atendente.setTelefone(pessoa.getTelefone());
                return atendente;
            }
            case "Veterinário" -> {
                Veterinario veterinario = new Veterinario();
                veterinario.setId(pessoa.getId());
                veterinario.setNome(pessoa.getNome());
                veterinario.setEmail(pessoa.getEmail());
                veterinario.setSenha(pessoa.getSenha());
                veterinario.setTelefone(pessoa.getTelefone());
//                veterinario.setEspecialidade(veterinario.getEspecialidade());
                return veterinario;
            }
            case "Cliente" -> {
                Cliente cliente = new Cliente();
                cliente.setId(pessoa.getId());
                cliente.setNome(pessoa.getNome());
                cliente.setEmail(pessoa.getEmail());
                cliente.setSenha(pessoa.getSenha());
                cliente.setTelefone(pessoa.getTelefone());
//                cliente.setAnimais(cliente.getAnimais());
                return cliente;
            }
        }
        throw new IllegalArgumentException("PESSOA NÃO ENCONTRADA.");
    }
}
