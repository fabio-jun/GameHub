import { api } from './api.js';
import { setCliente } from './auth.js';

export async function renderLogin(root) {
  root.innerHTML = `
    <div class="login-wrapper">
      <div class="login-card">
        <h2>Entrar</h2>
        <form id="formLogin">
          <input id="email" placeholder="Email" type="email" required />
          <input id="senha" placeholder="Senha" type="password" required />
          <button class="btn" type="submit">Entrar</button>
        </form>
        <hr />
        <details>
          <summary>Criar conta</summary>
          <form id="formCriar">
            <input id="pnome" placeholder="Primeiro nome" required />
            <input id="snome" placeholder="Sobrenome" required />
            <input id="endereco" placeholder="Endereço" />
            <input id="telefone" placeholder="Telefone" />
            <input id="emailCadastro" placeholder="Email" type="email" required />
            <input id="senhaCadastro" placeholder="Senha" type="password" required />
            <button class="btn" type="submit">Criar e entrar</button>
          </form>
        </details>
        <p id="loginMsg" class="error" style="margin-top:.75rem"></p>
      </div>
    </div>
  `;

  const $msg = root.querySelector('#loginMsg');

  root.querySelector('#formLogin').addEventListener('submit', async (e) => {
    e.preventDefault();
    $msg.textContent = '';
    const email = root.querySelector('#email').value.trim();
    const senha = root.querySelector('#senha').value;
    try {
      const cliente = await api.login(email, senha);
      if (!cliente) throw new Error('Credenciais inválidas.');
      setCliente(cliente);
      window.location.hash = '#';
    } catch (err) {
      if (err.status === 401) {
        $msg.textContent = 'Email ou senha incorretos.';
      } else {
        $msg.textContent = err.message || 'Erro ao autenticar.';
      }
    }
  });

  root.querySelector('#formCriar').addEventListener('submit', async (e) => {
    e.preventDefault();
    $msg.textContent = '';
    const body = {
      pnome: root.querySelector('#pnome').value.trim(),
      snome: root.querySelector('#snome').value.trim(),
      endereco: root.querySelector('#endereco').value.trim(),
      telefone: root.querySelector('#telefone').value.trim(),
      email: root.querySelector('#emailCadastro').value.trim(),
      senha: root.querySelector('#senhaCadastro').value,
    };
    if (!body.pnome || !body.snome || !body.email || !body.senha) {
      $msg.textContent = 'Preencha os campos obrigatórios (nome, sobrenome, email, senha).';
      return;
    }
    try {
      const cliente = await api.criarCliente(body);
      setCliente(cliente);
      window.location.hash = '#';
    } catch (err) {
      $msg.textContent = err.message || 'Erro ao criar cliente.';
    }
  });
}
