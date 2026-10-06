document.addEventListener('DOMContentLoaded', async () => {
  const usuario = await exigirLogin();
  if (!usuario) return;

  const campoSenha = document.getElementById('p-senha');
  const botaoVerSenha = document.getElementById('ver-senha');
  let senhaVisivel = false;

  function atualizarSenha() {
    campoSenha.textContent = senhaVisivel ? usuario.senha : '•'.repeat(usuario.senha.length);
    botaoVerSenha.textContent = senhaVisivel ? 'ocultar' : 'mostrar';
  }

  document.getElementById('p-nome').textContent = usuario.nome;
  document.getElementById('p-email').textContent = usuario.email;
  document.getElementById('p-telefone').textContent = usuario.telefone;

  botaoVerSenha.addEventListener('click', () => {
    senhaVisivel = !senhaVisivel;
    atualizarSenha();
  });

  atualizarSenha();
});
