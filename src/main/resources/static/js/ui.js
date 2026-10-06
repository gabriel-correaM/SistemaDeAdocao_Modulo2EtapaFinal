function confirmar({ titulo, mensagem, textoConfirmar = 'Sim', classe = 'btn-primario' }) {
  return new Promise((resolve) => {
    const dialogo = document.createElement('dialog');
    dialogo.className = 'confirmacao';
    dialogo.innerHTML = `
      <h2></h2>
      <p></p>
      <div class="acoes">
        <button class="btn btn-neutro" data-resposta="nao">Não</button>
        <button class="btn ${classe}" data-resposta="sim"></button>
      </div>
    `;

    dialogo.querySelector('h2').textContent = titulo;
    dialogo.querySelector('p').textContent = mensagem;
    dialogo.querySelector('[data-resposta="sim"]').textContent = textoConfirmar;

    let confirmado = false;

    dialogo.addEventListener('click', (evento) => {
      const resposta = evento.target.dataset && evento.target.dataset.resposta;

      if (resposta) {
        confirmado = resposta === 'sim';
        dialogo.close();
      }
    });

    dialogo.addEventListener('close', () => {
      dialogo.remove();
      resolve(confirmado);
    });

    document.body.appendChild(dialogo);
    dialogo.showModal();
    dialogo.querySelector('[data-resposta="nao"]').focus();
  });
}

function mascararTelefone(input) {
  input.addEventListener('input', () => {
    let numeros = input.value.replace(/\D/g, '').slice(0, 11);

    if (numeros.length > 10) {
      numeros = numeros.replace(/^(\d{2})(\d{5})(\d{4}).*/, '($1) $2-$3');
    } else if (numeros.length > 6) {
      numeros = numeros.replace(/^(\d{2})(\d{4})(\d{0,4}).*/, '($1) $2-$3');
    } else if (numeros.length > 2) {
      numeros = numeros.replace(/^(\d{2})(\d*)/, '($1) $2');
    } else if (numeros.length > 0) {
      numeros = `(${numeros}`;
    }

    input.value = numeros;
  });
}

function erroCampo(input, mensagem) {
  const campo = input.closest('.campo');
  campo.classList.toggle('invalido', Boolean(mensagem));
  campo.querySelector('.msg-erro').textContent = mensagem || '';
  return !mensagem;
}
