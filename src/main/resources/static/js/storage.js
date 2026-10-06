const DB = {
  async cadastrarUsuario(usuario) {
    const resposta = await fetch('/api/auth/register', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(usuario),
    });
    return tratarResposta(resposta);
  },

  async login(email, senha) {
    const resposta = await fetch('/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, senha }),
    });
    return tratarResposta(resposta);
  },

  async usuarioAtual() {
    const resposta = await fetch('/api/auth/me');
    if (resposta.status === 401) {
      return null;
    }
    return tratarResposta(resposta);
  },

  async logout() {
    await fetch('/api/auth/logout', { method: 'POST' });
  },

  async animais() {
    const resposta = await fetch('/api/animais');
    return tratarResposta(resposta);
  },

  async salvarAnimal(animal) {
    const resposta = await fetch('/api/animais', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(animal),
    });
    return tratarResposta(resposta);
  },

  async adotarAnimal(id) {
    const resposta = await fetch(`/api/animais/${id}/adotar`, {
      method: 'PUT',
    });
    return tratarResposta(resposta);
  },

  async excluirAnimal(id) {
    const resposta = await fetch(`/api/animais/${id}`, {
      method: 'DELETE',
    });
    if (!resposta.ok) {
      return tratarResposta(resposta);
    }
    return null;
  },
};

async function tratarResposta(resposta) {
  const texto = await resposta.text();
  let dados = {};
  try {
    dados = texto ? JSON.parse(texto) : {};
  } catch {
    dados = {};
  }

  if (!resposta.ok) {
    throw new Error(dados.detail || dados.message || 'Não foi possível concluir a operação.');
  }

  return dados;
}
