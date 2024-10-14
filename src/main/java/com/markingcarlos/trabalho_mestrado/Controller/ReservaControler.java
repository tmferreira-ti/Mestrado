package com.markingcarlos.trabalho_mestrado.Controller;

import com.markingcarlos.trabalho_mestrado.Model.AreaModel;
import com.markingcarlos.trabalho_mestrado.Model.MoradorModel;
import com.markingcarlos.trabalho_mestrado.Model.ReservaModel;
import com.markingcarlos.trabalho_mestrado.Repository.AreaRepository;
import com.markingcarlos.trabalho_mestrado.Repository.MoradorRepository;
import com.markingcarlos.trabalho_mestrado.Repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/reserva")
public class ReservaControler {
    private final EmailController emailController;
    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private AreaRepository areaRepository;
    @Autowired
    private MoradorRepository moradorRepository;

    public ReservaControler(EmailController emailController) {
        this.emailController = emailController;
    }


    @ResponseBody
    @PostMapping("/cadastra")
    public String CadastraReserva(@RequestParam("inico") String dataInicio,
                                  @RequestParam("fim")String dataFim,
                                  @RequestParam("descricao")String descricao,
                                  @RequestParam("area")String idArea,
                                  OAuth2AuthenticationToken authentication
                                  ) {

        ReservaModel reserva = new ReservaModel();
        String email = authentication.getPrincipal().getAttribute("email");
        MoradorModel morador = moradorRepository.findByEmail(email);

        reserva.setStatus("Pendente");
        reserva.setAtiva(true);
        if(dataInicio != null || dataFim != null || descricao != null || idArea != null ) {
            reserva.setDescricao(descricao);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate inico = LocalDate.parse(dataInicio, formatter);
            reserva.setDataInicio(inico);
            LocalDate fim = LocalDate.parse(dataFim, formatter);
            reserva.setDataFim(fim);

            int id = Integer.parseInt(idArea);
            Optional<AreaModel> area;
            area = areaRepository.findById(id);
            if (area.isPresent()) {
                if (!area.get().isAlugavel() || !area.get().isUtilizavel()){
                    return "Área não pode ser alugada ou utilizavel no momento";
                }
                reserva.setAreaReservada(area.get());
            } else {
                return "Area não encontrada";
            }


            reserva.setTitularReserva(morador);

            emailController.sendEmail(morador.getEmail(),"Reserva realizada com sucesso","Sua solicitação de reserva foi solicitada com sucesso");
            reservaRepository.save(reserva);
            return "Reserva Cadastrada com sucesso!";
        }
        return "Dados em falta";


    }

    @ResponseBody
    @PostMapping("/status")
    public String MudarStattusReserva(@RequestParam("reserva") String reserva,
                                      @RequestParam("Status")String Status)
    {
        int Id = Integer.parseInt(reserva);
        Optional<ReservaModel> reservaModel = reservaRepository.findById(Id);
        if (reservaModel.isPresent()) {
            reservaModel.get().setStatus(Status);
            reservaRepository.save(reservaModel.get());
        }else{
            return "Reserva não encontrada";
        }
        emailController.sendEmail(reservaModel.get().getTitularReserva().getEmail(),"Sua reserva foi atualizada",
                "O Status atual de sua" + "reserva é"+Status);

        return "Status Atualizado com sucesso! \n seu novo Status é:"+ Status;
    }

    public List<ReservaModel> buscarReservasDeHoje() {
        LocalDate hoje = LocalDate.now();
        return reservaRepository.findBydataInicio(hoje);
    }

    public List<ReservaModel> buscarReservasDeHojefim() {
        LocalDate hoje = LocalDate.now();
        return reservaRepository.findBydataFim(hoje);
    }

    @Scheduled(cron = "0 0 7 * * *") // Executa diariamente às 7h
    public String verificarInicioReserva() {
        List<ReservaModel> reservas = buscarReservasDeHoje();
        StringBuilder resultado = new StringBuilder();
        if (!reservas.isEmpty()) {
            for (ReservaModel reserva : reservas) {
                resultado.append("Sua reserva começa hoje, aproveite:\n");
                resultado.append("Titular: ").append(reserva.getTitularReserva().getNomeMorador()).append("\n");
                resultado.append("Local: ").append(reserva.getAreaReservada().getNome()).append("\n");
                emailController.sendEmail(reserva.getTitularReserva().getEmail(),"Notificação Reserva",resultado.toString());
            }
        } else {
            resultado.append("Nenhuma reserva encontrada para hoje.");
        }
        return resultado.toString();
    }

    @Scheduled(cron = "0 0 15 * * *") // Executa diariamente às 15h
    public String verificaFinalReserva() {
        List<ReservaModel> reservas = buscarReservasDeHojefim();
        StringBuilder resultado = new StringBuilder();
        if (!reservas.isEmpty()) {
            for (ReservaModel reserva : reservas) {
                resultado.append("Sua reserva está chegando ao fim, não esqueça de liberar o local até as 19h\n");
                resultado.append("Titular: ").append(reserva.getTitularReserva().getNomeMorador()).append("\n");
                resultado.append("Local: ").append(reserva.getAreaReservada().getNome()).append("\n");
                emailController.sendEmail(reserva.getTitularReserva().getEmail(),"Notificação Reserva",resultado.toString());
            }
        }else {
            resultado.append("Nenhuma reserva encontrada para hoje.");
        }
        return resultado.toString();
    }

    @Scheduled(cron = "0 0 22 * * *") // Executa diariamente às 22h
    public void FinalizarReserva() {
        List<ReservaModel> reservas = buscarReservasDeHojefim();
        if (!reservas.isEmpty()) {
            for (ReservaModel reserva : reservas) {
                reserva.setStatus("Finalizado");
                reservaRepository.save(reserva);
            }
        }
    }


    @GetMapping("/lista")
    @ResponseBody
    public List<Object[]> ListReservas(){
        return reservaRepository.findByInfo("Pendente");
    }
    @GetMapping("/listaporEmail")
    @ResponseBody
    public List<Object[]> ListReservasporEmail(OAuth2AuthenticationToken authentication){
        String email = authentication.getPrincipal().getAttribute("email");
        return reservaRepository.findByReservaPorEmail(email);
    }




}


