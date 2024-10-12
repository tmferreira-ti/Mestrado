package com.markingcarlos.trabalho_mestrado.Controller;

import com.markingcarlos.trabalho_mestrado.Model.AreaModel;
import com.markingcarlos.trabalho_mestrado.Repository.AreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.awt.geom.Area;

@Controller
@RequestMapping("/area")
public class AreaController {

    @Autowired
    private AreaRepository areaRepository;

    @PostMapping
    @ResponseBody
    public String CadastraArea(
            @RequestParam("nome") String nome,
            @RequestParam("descricao") String descricao,
            @RequestParam("tamanhoMetroQuadrado") int tamanhoMetroQuadrado,
            @RequestParam("utilizavel") boolean utilizavel,
            @RequestParam("localizacao") String localizacao,
    @       RequestParam("isAlugavel") boolean isAlugavel) {

        // Cria um novo objeto AreaModel com os parâmetros recebidos
        AreaModel area = new AreaModel();
        area.setNome(nome);
        area.setDescricao(descricao);
        area.setTamanhoMetroQuadrado(tamanhoMetroQuadrado);
        area.setUtilizavel(utilizavel);
        area.setLocalizacao(localizacao);
        area.setAlugavel(isAlugavel);

        // Salva a área no banco de dados
        areaRepository.save(area);

        return "Área cadastrada com sucesso!";
    }
}

