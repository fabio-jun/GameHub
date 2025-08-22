import { api } from './api.js';
import { getClienteId } from './auth.js';

function moedaBR(v) {
  try { return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(Number(v||0)); }
  catch { return `R$ ${(Number(v||0)).toFixed(2)}`; }
}

export async function renderCart(root) {
  const idCliente = getClienteId();
  if (!idCliente) { window.location.hash = '#login'; return; }
  root.innerHTML = `
    <section>
      <div class="row space-between center">
        <h2>Seu carrinho</h2>
        <button id="finalizar" class="btn">Finalizar compra</button>
      </div>
      <div id="itens">Carregando...</div>
    </section>
  `;

  const box = root.querySelector('#itens');

  async function load() {
    try {
      const itens = await api.obterCarrinho(idCliente);
      if (!Array.isArray(itens) || itens.length === 0) {
        box.innerHTML = '<p>Seu carrinho está vazio.</p>';
        return;
      }

      box.innerHTML = itens.map(it => `
        <div class="cart-item">
          <div class="grow">
            <b>Jogo #${it.idJogo}</b>
            <div class="muted">Qtd:</div>
          </div>
          <div class="row gap center">
            <button class="btn ghost" data-act="dec" data-jogo="${it.idJogo}">-</button>
            <input class="qtd" type="number" min="1" value="${it.qtd}" data-jogo="${it.idJogo}" />
            <button class="btn ghost" data-act="inc" data-jogo="${it.idJogo}">+</button>
            <button class="btn danger" data-act="rm" data-jogo="${it.idJogo}">Remover</button>
          </div>
        </div>
      `).join('');
    } catch (e) {
      box.innerHTML = '<p class="error">Erro ao carregar carrinho.</p>';
    }
  }

  await load();

  box.addEventListener('click', async (e) => {
    const btn = e.target.closest('button[data-act]');
    if (!btn) return;
    const idJogo = Number(btn.dataset.jogo);
    const act = btn.dataset.act;
    const input = box.querySelector(`input.qtd[data-jogo="${idJogo}"]`);
    let qtd = Number(input.value) || 1;
    if (act === 'inc') qtd++;
    if (act === 'dec') qtd = Math.max(1, qtd - 1);
    try {
      await api.atualizarQtd(idCliente, idJogo, qtd);
      await load();
    } catch (err) {
      alert('Erro ao atualizar: ' + err.message);
    }
  });

  box.addEventListener('change', async (e) => {
    const input = e.target.closest('input.qtd');
    if (!input) return;
    const idJogo = Number(input.dataset.jogo);
    const qtd = Math.max(1, Number(input.value) || 1);
    try {
      await api.atualizarQtd(idCliente, idJogo, qtd);
    } catch (err) {
      alert('Erro ao atualizar: ' + err.message);
    }
  });

  root.querySelector('#finalizar').addEventListener('click', async () => {
    try {
      await api.realizarCompra(idCliente);
      alert('Compra realizada!');
      window.location.hash = '#';
    } catch (err) {
      alert('Erro ao finalizar: ' + err.message);
    }
  });
}
