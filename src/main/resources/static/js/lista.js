document.addEventListener('DOMContentLoaded', async () => {
  const usuario = await exigirLogin();
  if (!usuario) return;

  const corpoTabela = document.getElementById('corpo-tabela');
  const tabela = document.getElementById('tabela');
  const mensagemVazia = document.getElementById('vazio');
  const botaoAdotar = document.getElementById('btn-adotar');
  const botaoExcluir = document.getElementById('btn-excluir');
  const dica = document.getElementById('dica');

  let selecionadoId = null;
  let animais = [];

  function criarLinha(animal) {
    const selecionado = animal.id === selecionadoId;
    const linha = document.createElement('tr');

    linha.className = selecionado ? 'linha selecionada' : 'linha';
    linha.tabIndex = 0;
    linha.setAttribute('aria-selected', selecionado);

    const colunaNome = document.createElement('td');
    colunaNome.textContent = animal.nome;

    const colunaStatus = document.createElement('td');
    const selo = document.createElement('span');
    selo.className = animal.adotado ? 'selo sim' : 'selo nao';
    selo.textContent = animal.adotado ? 'Adotado' : 'Não adotado';
    colunaStatus.appendChild(selo);

    const colunaAdotante = document.createElement('td');
    colunaAdotante.textContent = animal.adotado ? animal.adotanteId : 'nenhum';

    linha.append(colunaNome, colunaStatus, colunaAdotante);

    linha.addEventListener('click', () => selecionar(animal.id));

    linha.addEventListener('keydown', (evento) => {
      if (evento.key === 'Enter' || evento.key === ' ') {
        evento.preventDefault();
        selecionar(animal.id);
        corpoTabela.querySelector('.selecionada').focus();
      }
    });

    return linha;
  }

  function selecionar(id) {
    selecionadoId = id;
    desenhar();
  }

  async function carregar() {
    try {
      animais = await DB.animais();
      desenhar();
    } catch (erro) {
      dica.textContent = erro.message;
    }
  }

  function desenhar() {
    if (!animais.some((animal) => animal.id === selecionadoId)) {
      selecionadoId = null;
    }

    corpoTabela.innerHTML = '';
    mensagemVazia.classList.toggle('oculto', animais.length > 0);
    tabela.classList.toggle('oculto', animais.length === 0);

    animais.forEach((animal) => corpoTabela.appendChild(criarLinha(animal)));

    const animalSelecionado = animais.find((animal) => animal.id === selecionadoId);
    botaoAdotar.disabled = !animalSelecionado || animalSelecionado.adotado;
    botaoExcluir.disabled = !animalSelecionado;

    const mostrarDica = animais.length > 0 && !animalSelecionado;
    dica.textContent = mostrarDica ? 'Selecione um animal da lista para adotar ou excluir.' : '';
  }

  botaoAdotar.addEventListener('click', async () => {
    const animal = animais.find((item) => item.id === selecionadoId);

    if (!animal || animal.adotado) return;

    const confirmado = await confirmar({
      titulo: 'Adotar animal',
      mensagem: `Você quer adotar ${animal.nome}?`,
      textoConfirmar: 'Sim, adotar',
      classe: 'btn-dourado',
    });

    if (!confirmado) return;

    try {
      await DB.adotarAnimal(animal.id);
      await carregar();
    } catch (erro) {
      dica.textContent = erro.message;
    }
  });

  botaoExcluir.addEventListener('click', async () => {
    const animal = animais.find((item) => item.id === selecionadoId);
    if (!animal) return;

    const confirmado = await confirmar({
      titulo: 'Excluir animal',
      mensagem: `Você quer excluir ${animal.nome} da lista? Essa ação não pode ser desfeita.`,
      textoConfirmar: 'Sim, excluir',
      classe: 'btn-perigo',
    });

    if (!confirmado) return;

    try {
      await DB.excluirAnimal(animal.id);
      selecionadoId = null;
      await carregar();
    } catch (erro) {
      dica.textContent = erro.message;
    }
  });

  await carregar();
});
