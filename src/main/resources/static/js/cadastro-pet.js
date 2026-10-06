document.addEventListener('DOMContentLoaded', async () => {
  const usuario = await exigirLogin();
  if (!usuario) return;

  const formPet = document.getElementById('form-pet');
  const campos = [formPet.nome, formPet.raca, formPet.abrigo];

  const rotulos = {
    nome: 'o nome do animal',
    raca: 'a raça',
    abrigo: 'o abrigo',
  };

  function validarCampo(input) {
    const valor = input.value.trim();
    let mensagem = '';

    if (valor.length < 2) {
      mensagem = `Informe ${rotulos[input.name]}.`;
    } else if (/^\d+$/.test(valor)) {
      mensagem = 'Não pode conter só números.';
    }

    return erroCampo(input, mensagem);
  }

  campos.forEach((input) => {
    input.addEventListener('input', () => erroCampo(input, ''));
  });

  formPet.addEventListener('submit', async (evento) => {
    evento.preventDefault();

    const todosValidos = campos.map(validarCampo).every(Boolean);
    if (!todosValidos) return;

    try {
      await DB.salvarAnimal({
        nome: formPet.nome.value.trim(),
        raca: formPet.raca.value.trim(),
        abrigo: formPet.abrigo.value.trim(),
      });

      formPet.reset();
      formPet.nome.focus();
    } catch (erro) {
      erroCampo(formPet.abrigo, erro.message);
    }
  });
});
