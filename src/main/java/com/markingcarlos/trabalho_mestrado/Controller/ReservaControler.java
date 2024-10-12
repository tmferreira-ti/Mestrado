package com.markingcarlos.trabalho_mestrado.Controller;

import com.markingcarlos.trabalho_mestrado.Model.AreaModel;
import com.markingcarlos.trabalho_mestrado.Model.MoradorModel;
import com.markingcarlos.trabalho_mestrado.Model.ReservaModel;
import com.markingcarlos.trabalho_mestrado.Repository.AreaRepository;
import com.markingcarlos.trabalho_mestrado.Repository.MoradorRepository;
import com.markingcarlos.trabalho_mestrado.Repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
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
    @PostMapping
    public String CadastraReserva(@RequestParam("inico") String dataInicio,
                                  @RequestParam("fim")String dataFim,
                                  @RequestParam("descricao")String descricao,
                                  @RequestParam("area")String idArea,
                                  @RequestParam("cpf")String cpfMorador) {

        ReservaModel reserva = new ReservaModel();

        reserva.setStatus("Pendente");
        reserva.setAtiva(true);
        if(dataInicio != null || dataFim != null || descricao != null || idArea != null || cpfMorador != null) {
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

            BigInteger cpf = new BigInteger(cpfMorador);
            MoradorModel morador = moradorRepository.findByCPFMorador(cpf);
            reserva.setTitularReserva(morador);

            emailController.sendEmail(morador.getEmail(),"Reserva realizada com sucesso","Sua solicitação de reserva foi solicitada com sucesso");

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

    @ResponseBody
    @GetMapping("/lista")
    public String ListReservas(){
        StringBuilder resultado = new StringBuilder();
        List<ReservaModel> reservas = reservaRepository.findAll();
        if (!reservas.isEmpty()) {
            for (ReservaModel reserva : reservas) {
                resultado.append("Titular: ").append(reserva.getTitularReserva().getNomeMorador()).append("\n");
                resultado.append("Local: ").append(reserva.getAreaReservada().getNome()).append("\n");
                resultado.append("Data Início: ").append(reserva.getDataInicio()).append("\n");
                resultado.append("----------------------------------------\n");
            }
        } else {
            resultado.append("Nenhuma reserva encontrada.");
        }

        return resultado.toString();
    }




}


