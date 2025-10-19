package br.com.turismoreligioso.SantuarioAparecida;

import br.com.turismoreligioso.SantuarioAparecida.model.*;
import br.com.turismoreligioso.SantuarioAparecida.repository.GerenteHospedariaRepository;
import br.com.turismoreligioso.SantuarioAparecida.repository.HospedagemRepository;
import br.com.turismoreligioso.SantuarioAparecida.repository.PessoaRepository;
import br.com.turismoreligioso.SantuarioAparecida.service.PessoaService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import br.com.turismoreligioso.SantuarioAparecida.repository.LocalRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;

@SpringBootApplication
@ComponentScan(basePackages = "br.com.turismoreligioso.SantuarioAparecida")
public class SantuarioAparecidaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SantuarioAparecidaApplication.class, args);
    }

    @Bean
    @Order(1)
    public CommandLineRunner initDatabase(LocalRepository localRepository) {
        return args -> {
            if (localRepository.count() == 0) {
                System.out.println(">>> Criando locais de exemplo com imagens...");

                Local local1 = new Local();
                local1.setNome("Basílica Histórica");
                local1.setDescricao("Construída em estilo barroco a partir de 1745, foi o primeiro grande templo a abrigar a Imagem de Nossa Senhora Aparecida. Consagrada em 1888, hoje é um marco da fé e da história da cidade.");
                local1.setUrlImagem("/images/basilicaAntiga.jpg");
                localRepository.save(local1);

                Local local2 = new Local();
                local2.setNome("Passarela da Fé");
                local2.setDescricao("Inaugurada em 1972, a passarela de 392 metros de comprimento foi projetada para conectar as duas basílicas, simbolizando a jornada dos peregrinos. Milhares de fiéis a atravessam, muitos de joelhos, como forma de penitência e gratidão.");
                local2.setUrlImagem("/images/passarela.jpg");
                localRepository.save(local2);

                Local local3 = new Local();
                local3.setNome("Basílica Nova (Santuário Nacional)");
                local3.setDescricao("Projetado pelo arquiteto Benedito Calixto, o Santuário Nacional é o maior santuário mariano do mundo. Sua construção iniciou-se em 1955 e sua sagração ocorreu em 1980 pelo Papa João Paulo II. Abriga a imagem original da Padroeira do Brasil.");
                local3.setUrlImagem("/images/basilica.jpg");
                localRepository.save(local3);

                System.out.println(">>> Locais de exemplo criados com sucesso!");
            }
        };
    }

    @Bean
    @Order(2)
    CommandLineRunner criarUsuariosPadrao(PessoaService pessoaService, PessoaRepository pessoaRepository) {
        return args -> {
            if (pessoaRepository.count() == 0) {
                System.out.println(">>> Criando usuários padrão...");

                Pessoa guia = new Pessoa();
                guia.setNome("Guia Padrão");
                guia.setEmail("guia@email.com");
                guia.setSenha("123456");
                guia.setPerfil(Perfil.GUIA);
                pessoaService.cadastrarNovaPessoa(guia, "123456");

                Pessoa turista = new Pessoa();
                turista.setNome("Turista Padrão");
                turista.setEmail("turista@email.com");
                turista.setSenha("123456");
                turista.setPerfil(Perfil.TURISTA);
                pessoaService.cadastrarNovaPessoa(turista, "123456");

                Pessoa gerente = new Pessoa();
                gerente.setNome("Gerente Padrão");
                gerente.setEmail("gerente@email.com");
                gerente.setSenha("123456");
                gerente.setPerfil(Perfil.GERENTE_HOSPEDAGEM);
                pessoaService.cadastrarNovaPessoa(gerente, "123456");

                System.out.println(">>> Usuários padrão criados com sucesso!");
            }
        };
    }
    @Bean
    @Order(3)
    CommandLineRunner criarHospedagensPadrao(HospedagemRepository hospedagemRepository, PessoaRepository pessoaRepository, GerenteHospedariaRepository gerenteHospedariaRepository) {
        return args -> {
            if (hospedagemRepository.count() == 0) {
                System.out.println(">>> Criando hospedagens de exemplo com galeria...");

                Pessoa pessoaGerente = pessoaRepository.findByEmail("gerente@email.com")
                        .orElseThrow(() -> new RuntimeException("Usuário gerente@email.com não encontrado para criar hospedagem."));

                GerenteHospedaria gerente = gerenteHospedariaRepository.findByPessoa(pessoaGerente)
                        .orElseThrow(() -> new RuntimeException("Perfil de Gerente de Hospedaria não encontrado."));


                Hospedagem hotel = new Hospedagem();
                hotel.setNome("Hotel Santo Graal");
                hotel.setDescricao("Localizado a poucos passos do Santuário Nacional, o Hotel Santo Graal oferece uma experiência tranquila e completa para quem vem em busca de fé.");
                hotel.setEndereco("Av. Dr. Julio Prestes, 110 - Centro, Aparecida - SP");
                hotel.setTelefone("(12) 3104-1200");
                hotel.setEmailContato("contato@santograal.com.br");
                hotel.setInstagram("https://instagram.com/hotelsantograal");
                hotel.setUrlImagem("/images/basilica.jpg");
                hotel.setTipo(TipoHospedagem.HOTEL);
                hotel.setGerente(gerente);


                ImagemHospedagem imgHotel1 = new ImagemHospedagem();
                imgHotel1.setUrl("/images/passarela.jpg");
                imgHotel1.setHospedagem(hotel);
                hotel.getGaleria().add(imgHotel1);

                ImagemHospedagem imgHotel2 = new ImagemHospedagem();
                imgHotel2.setUrl("/images/basilicaAntiga.jpg");
                imgHotel2.setHospedagem(hotel);
                hotel.getGaleria().add(imgHotel2);
                hospedagemRepository.save(hotel);



                Hospedagem pousada = new Hospedagem();
                pousada.setNome("Pousada Jovimar");
                pousada.setDescricao("A Pousada Jovimar oferece um ambiente familiar e acolhedor, com uma área verde ampla, piscina e restaurante.");
                pousada.setEndereco("R. Isaac Ferreira da Encarnação, 553 - Jardim Paraiba, Aparecida - SP");
                pousada.setTelefone("(12) 3105-2 Jovimar");
                pousada.setEmailContato("reservas@jovimar.com.br");
                pousada.setInstagram("https://instagram.com/pousadajovimar");
                pousada.setUrlImagem("/images/passarela.jpg");
                pousada.setTipo(TipoHospedagem.POUSADA);
                pousada.setGerente(gerente);

                ImagemHospedagem imgPousada1 = new ImagemHospedagem();
                imgPousada1.setUrl("/images/basilica.jpg");
                imgPousada1.setHospedagem(pousada);
                pousada.getGaleria().add(imgPousada1);
                hospedagemRepository.save(pousada);

                System.out.println(">>> Hospedagens de exemplo criadas com sucesso!");
            }
        };
    }
}

