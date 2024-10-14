package com.markingcarlos.trabalho_mestrado.Controller;

import com.markingcarlos.trabalho_mestrado.Model.MoradorModel;
import com.markingcarlos.trabalho_mestrado.Repository.MoradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigInteger;

@Controller
@RequestMapping("/morador")
public class MoradorController {

    @Autowired
    private MoradorRepository moradorRepository;

    @PostMapping
    @ResponseBody
    public String CadastrarMorador(@RequestParam("nomemorador") String nome,
                                   @RequestParam("email")String email,
                                   @RequestParam("Telefone")String Telefone,
                                   @RequestParam("Endereco")String Endereco,
                                   @RequestParam("cpf")String cpf,
                                   @RequestParam("Tipo")String tipo)
    {
        MoradorModel morador = new MoradorModel();
        morador.setNomeMorador(nome);
        morador.setEmail(email);
        morador.setTelefone(Telefone);
        morador.setTipoMorador(tipo);
        morador.setEndereco(Endereco);
        BigInteger numerocpf = new BigInteger(cpf);
        morador.setCPFMorador(numerocpf);
        System.out.println("Teste");
        if (nome.isEmpty() || email.isEmpty() || Telefone.isEmpty() || Endereco.isEmpty()){
            return "Erro ao cadastrar morador";
        }
        // Salva o morador no banco de dados
        MoradorModel moradorSalvo = moradorRepository.save(morador);

        // Verifica se o morador foi realmente salvo
        if (moradorSalvo != null && moradorSalvo.getCPFMorador() != null) {
            System.out.println("Salvo com sucesso!");
            return "Morador salvo com sucesso!";

        } else {
            System.out.println("Erro ao cadastrar morador");
            return "Erro ao salvar o morador.";
        }

    }


}
