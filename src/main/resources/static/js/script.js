$(document).ready(function() {
    ListaReservas();
});

function cadastrarMorador(event) {
    event.preventDefault(); // Impede o envio padrão do formulário

    // Obtém os valores dos campos do formulário
    const nome = document.getElementById('nomemorador').value;
    const email = document.getElementById('email').value;
    const telefone = document.getElementById('Telefone').value;
    const endereco = document.getElementById('Endereco').value;
    const cpf = document.getElementById('cpf').value;
    const tipo = document.getElementById('Tipo').value;

    // Cria o objeto com os dados do morador
    const moradorData = new URLSearchParams();
    moradorData.append("nomemorador", nome);
    moradorData.append("email", email);
    moradorData.append("Telefone", telefone);
    moradorData.append("Endereco", endereco);
    moradorData.append("cpf", cpf);
    moradorData.append("Tipo", tipo);

    // Envia a requisição POST para o servidor
    fetch('/morador', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: moradorData.toString()
    })
        .then(response => response.text())
        .then(data => {
            // Mostra a resposta do servidor
            alert(data);
        })
        .catch(error => {
            console.error('Erro:', error);
            alert('Erro ao cadastrar morador.');
        });
}

function cadastrarArea(event) {
    event.preventDefault(); // Impede o envio padrão do formulário

    // Obtém os valores dos campos do formulário
    const nome = document.getElementById('nomeArea').value;
    const desArea = document.getElementById('desArea').value;
    const metrosquadrados = document.getElementById('Tamanho').value;
    const localiza = document.getElementById('localiza').value;
    const utilizavel = document.getElementById('utilizavel').checked;
    const alugavel = document.getElementById('alugavel').checked;
    console.log(nome)

    // Cria o objeto com os dados do morador
    const moradorData = new URLSearchParams();
    moradorData.append("nome", nome);
    moradorData.append("descricao", desArea);
    moradorData.append("tamanhoMetroQuadrado", metrosquadrados);
    moradorData.append("utilizavel", utilizavel);
    moradorData.append("localizacao", localiza);
    moradorData.append("isAlugavel", alugavel);

    // Envia a requisição POST para o servidor
    fetch('/area', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: moradorData.toString()
    })
        .then(response => response.text())
        .then(data => {
            // Mostra a resposta do servidor
            alert(data);
        })
        .catch(error => {
            console.error('Erro:', error);
            alert('Erro ao cadastrar área.');
        });
}

function ListaReservas() {
    $.ajax({
        async: true,
        type: 'GET',
        url: 'reserva/lista',
        success: function (reservas) {
            console.log(reservas); // Verifique se agora está recebendo um array
            $('#ListaMetas').empty();
            reservas.forEach(reserva => {
                $('#select').append(
                    `
                        <option value="${reserva[1]}">Titular: ${reserva[0]} Local:${reserva[4]} Status:${reserva[5]}</option>
                    `
                );
            });
        },
        error: function (xhr, status, error) {
            console.error("Erro na requisição:", status, error);
        }
    });
}

function AlterarStatus(event) {
    event.preventDefault(); // Impede o envio padrão do formulário

    // Obtém os valores dos campos do formulário
    const idreserva = document.getElementById('select').value;
    const TipoStatus = document.getElementById('TipoStatus').value;


    // Cria o objeto com os dados do morador
    const Status = new URLSearchParams();
    Status.append("reserva", idreserva);
    Status.append("Status", TipoStatus);


    // Envia a requisição POST para o servidor
    fetch('reserva/status', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: Status.toString()
    })
        .then(response => response.text())
        .then(data => {
            // Mostra a resposta do servidor
            alert(data);
        })
        .catch(error => {
            console.error('Erro:', error);
            alert('Erro ao cadastrar morador.');
        });
}