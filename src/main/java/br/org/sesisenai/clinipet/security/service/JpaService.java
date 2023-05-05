package br.org.sesisenai.clinipet.security.service;

import br.org.sesisenai.clinipet.model.entity.Pessoa;
import br.org.sesisenai.clinipet.repository.PessoaRepository;
import br.org.sesisenai.clinipet.security.model.entity.UserJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JpaService implements UserDetailsService {
    @Autowired
    private PessoaRepository pessoaRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Pessoa> pessoaOptional = pessoaRepository.findByEmail(username);

        if (pessoaOptional.isPresent()) {
            return new UserJpa(pessoaOptional.get());
        }

        throw new UsernameNotFoundException("USUÁRIO NÃO ENCONTRADO.");
    }

}
