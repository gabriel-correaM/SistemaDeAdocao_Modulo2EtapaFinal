document.addEventListener('DOMContentLoaded', async () => {
  const usuario = await exigirLogin();
  if (!usuario) return;

  const primeiroNome = usuario.nome.split(' ')[0];
  document.getElementById('saudacao').textContent = `Olá, ${primeiroNome}!`;
});
