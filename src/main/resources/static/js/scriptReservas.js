$(document).ready(function() {
    ListaArea();
    ListaArea();
});

function cadastrarReserva(event) {
    event.preventDefault(); // Impede o envio padrão do formulário

    // Obtém os valores dos campos do formulário
    const inicio = document.getElementById('data').value;
    const fim = document.getElementById('datafim').value;
    const descricao = document.getElementById('desc').value;
    const area = document.getElementById('select').value;


    // Cria o objeto com os dados do morador
    const ReservaData = new URLSearchParams();
    ReservaData.append("inico", inicio);
    ReservaData.append("fim", fim);
    ReservaData.append("descricao", descricao);
    ReservaData.append("area", area);


    // Envia a requisição POST para o servidor
    fetch('/reserva/cadastra', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: ReservaData.toString()
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

function ListaArea() {
    $.ajax({
        async: true,
        type: 'GET',
        url: 'area/listaAreaAlugavel',
        success: function (areas) {

            $('#select').empty();
            areas.forEach(area => {
                $('#select').append(
                    `
                        <option value="${area[0]}">Nome área: ${area[1]} </option>
                    `
                );
            });
        },
        error: function (xhr, status, error) {
            console.error("Erro na requisição:", status, error);
        }
    });
}