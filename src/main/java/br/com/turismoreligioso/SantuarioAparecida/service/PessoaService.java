package br.com.turismoreligioso.SantuarioAparecida.service;

import br.com.turismoreligioso.SantuarioAparecida.model.GerenteHospedaria;
import br.com.turismoreligioso.SantuarioAparecida.model.Guia;
import br.com.turismoreligioso.SantuarioAparecida.model.Pessoa;
import br.com.turismoreligioso.SantuarioAparecida.model.Perfil; // Verifique se esta importação está correta
import br.com.turismoreligioso.SantuarioAparecida.repository.GerenteHospedariaRepository;
import br.com.turismoreligioso.SantuarioAparecida.repository.GuiaRepository;
import br.com.turismoreligioso.SantuarioAparecida.repository.PessoaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority; // Importação nova
import org.springframework.security.core.authority.SimpleGrantedAuthority; // Importação nova
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet; // Importação nova
import java.util.Set; // Importação nova
import java.util.Optional;

@Service
public class PessoaService implements UserDetailsService {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private GuiaRepository guiaRepository;
    @Autowired
    private GerenteHospedariaRepository gerenteHospedariaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Pessoa cadastrarNovaPessoa(Pessoa pessoa, String confirmarSenha) {
        if (!pessoa.getSenha().equals(confirmarSenha)) {
            throw new IllegalStateException("As senhas não conferem.");
        }
        Optional<Pessoa> pessoaExistente = pessoaRepository.findByEmail(pessoa.getEmail());
        if (pessoaExistente.isPresent()) {
            throw new IllegalStateException("Email já cadastrado.");
        }

        String senhaCriptografada = passwordEncoder.encode(pessoa.getSenha());
        pessoa.setSenha(senhaCriptografada);

        Pessoa novaPessoa = pessoaRepository.save(pessoa);

        if (novaPessoa.getPerfil() == Perfil.GUIA) {
            Guia novoGuia = new Guia();
            novoGuia.setPessoa(novaPessoa);
            guiaRepository.save(novoGuia);
        } else if (novaPessoa.getPerfil() == Perfil.GERENTE_HOSPEDAGEM) {
            GerenteHospedaria novoGerente = new GerenteHospedaria();
            novoGerente.setPessoa(novaPessoa);
            gerenteHospedariaRepository.save(novoGerente);
        }

        return novaPessoa;
    }


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