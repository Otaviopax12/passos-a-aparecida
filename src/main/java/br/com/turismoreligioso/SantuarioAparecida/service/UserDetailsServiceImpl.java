package br.com.turismoreligioso.SantuarioAparecida.service;

import br.com.turismoreligioso.SantuarioAparecida.model.Pessoa;
import br.com.turismoreligioso.SantuarioAparecida.model.Perfil;
import br.com.turismoreligioso.SantuarioAparecida.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Pessoa pessoa = pessoaRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + email));


        Set<GrantedAuthority> authorities = new HashSet<>();


        Perfil perfil = pessoa.getPerfil();


        if (perfil != null) {

            authorities.add(new SimpleGrantedAuthority("ROLE_" + perfil.name()));
        }


        return new User(pessoa.getEmail(), pessoa.getSenha(), authorities);
    }
}