const EMAIL_REGEX = /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/;

const formCadastro = document.getElementById('form-cadastro');
const formLogin = document.getElementById('form-login');

const campos = {
  nome: formCadastro.nome,
  email: formCadastro.email,
  telefone: formCadastro.telefone,
  senha: formCadastro.senha,
};

mascararTelefone(campos.telefone);

function validarNome() {
  const nome = campos.nome.value.trim();
  const invalido = nome.length < 5 || nome.split(/\s+/).length < 2;
  return erroCampo(campos.nome, invalido ? 'Informe nome e sobrenome.' : '');
}

function validarEmail() {
  const email = campos.email.value.trim().toLowerCase();
  return erroCampo(campos.email, EMAIL_REGEX.test(email) ? '' : 'E-mail inválido.');
}

function validarTelefone() {
  const digitos = campos.telefone.value.replace(/\D/g, '');
  const mensagem = digitos.length < 10 ? 'Telefone com DDD (10 ou 11 dígitos).' : '';
  return erroCampo(campos.telefone, mensagem);
}

function validarSenha() {
  const mensagem = campos.senha.value.length < 6 ? 'A senha deve ter ao menos 6 caracteres.' : '';
  return erroCampo(campos.senha, mensagem);
}

function validarCadastro() {
  return [validarNome(), validarEmail(), validarTelefone(), validarSenha()].every(Boolean);
}

Object.values(campos).forEach((input) => {
  input.addEventListener('input', () => erroCampo(input, ''));
});

formCadastro.addEventListener('submit', async (evento) => {
  evento.preventDefault();

  if (!validarCadastro()) {
    return;
  }

  try {
    await DB.cadastrarUsuario({
      nome: campos.nome.value.trim(),
      email: campos.email.value.trim().toLowerCase(),
      telefone: campos.telefone.value,
      senha: campos.senha.value,
    });

    location.href = 'index.html';
  } catch (erro) {
    erroCampo(campos.email, erro.message);
  }
});

function alternarFormulario(mostrarLogin) {
  formCadastro.classList.toggle('oculto', mostrarLogin);
  formLogin.classList.toggle('oculto', !mostrarLogin);
  document.getElementById('titulo').textContent = mostrarLogin ? 'Entrar' : 'Tela de cadastro';
  document.getElementById('troca-cadastro').classList.toggle('oculto', mostrarLogin);
  document.getElementById('troca-login').classList.toggle('oculto', !mostrarLogin);
}

document.getElementById('ir-login').addEventListener('click', () => alternarFormulario(true));
document.getElementById('ir-cadastro').addEventListener('click', () => alternarFormulario(false));

formLogin.addEventListener('submit', async (evento) => {
  evento.preventDefault();

  const email = formLogin.email.value.trim().toLowerCase();
  const senha = formLogin.senha.value;

  try {
    await DB.login(email, senha);
    location.href = 'index.html';
  } catch (erro) {
    erroCampo(formLogin.senha, erro.message);
  }
});

formLogin.senha.addEventListener('input', () => erroCampo(formLogin.senha, ''));
