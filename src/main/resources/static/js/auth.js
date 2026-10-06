async function usuarioLogado() {
  return DB.usuarioAtual();
}

async function exigirLogin() {
  const usuario = await usuarioLogado();

  if (!usuario) {
    location.replace('cadastro.html');
    return null;
  }

  return usuario;
}

async function deslogar() {
  await DB.logout();
  location.href = 'cadastro.html';
}

document.addEventListener('DOMContentLoaded', () => {
  const botaoInicio = document.getElementById('btn-inicio');
  const botaoDeslogar = document.getElementById('btn-deslogar');

  if (botaoInicio) {
    botaoInicio.addEventListener('click', () => {
      location.href = 'index.html';
    });
  }

  if (botaoDeslogar) {
    botaoDeslogar.addEventListener('click', deslogar);
  }
});
