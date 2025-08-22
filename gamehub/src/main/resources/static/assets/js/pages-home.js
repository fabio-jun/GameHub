import { api } from './api.js';
import { getClienteId } from './auth.js';

let searchQuery = '';
export function setHomeSearchQuery(q) {
  searchQuery = q || '';
}

function moedaBR(v) {
  try { return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(Number(v||0)); }
  catch { return `R$ ${(Number(v||0)).toFixed(2)}`; }
}

function cardTemplate(j) {
  const nome = j.nome || j.titulo || 'Jogo';
  const preco = j.preco ?? j.valor ?? 0;
  const id = j.idJogo ?? j.id ?? j.codigo ?? 0;
  const img = j.imagemUrl || `/imagens/${(j.capa || 'default.jpg')}`;
  return `
    <article class="card">
      <img src="${img}" alt="${nome}" loading="lazy" />
      <div class="card-body">
        <h3>${nome}</h3>
        <p class="price">${moedaBR(preco)}</p>
        <div class="row gap">
          <button class="btn" data-action="add" data-id="${id}">Adicionar</button>
          <button class="btn ghost" data-action="detalhes" data-id="${id}">Detalhes</button>
        </div>
      </div>
    </article>
  `;
}

export async function renderHome(root, state) {
  root.innerHTML = `
    <section>
      <div class="row space-between center">
  <h2>${searchQuery ? `Resultados para "${searchQuery}"` : 'Catálogo'}</h2>
      </div>
      <div id="grid" class="grid">Carregando...</div>
    </section>
  `;

  const grid = root.querySelector('#grid');

  async function carregarJogos() {
    try {
      let jogos = [];
      if (searchQuery) {
        try {
          jogos = await api.buscarJogos(searchQuery);
        } catch (e) {
          // se endpoint de busca não existir, cair para lista completa
          jogos = await api.listarJogos();
        }
      } else {
        jogos = await api.listarJogos();
      }
      if (!Array.isArray(jogos) || jogos.length === 0) {
        grid.innerHTML = searchQuery
          ? `<p>Nenhum jogo encontrado para "${searchQuery}".</p>`
          : '<p>Nenhum jogo encontrado.</p>';
        return;
      }
      grid.innerHTML = jogos.map(cardTemplate).join('');
    } catch (e) {
      console.error(e);
      grid.innerHTML = '<p class="error">Erro ao carregar jogos.</p>';
    }
  }

  await carregarJogos();

  grid.addEventListener('click', async (e) => {
    const btn = e.target.closest('button[data-action]');
    if (!btn) return;
    const idJogo = Number(btn.dataset.id);
    const action = btn.dataset.action;

    if (action === 'add') {
      const idCliente = getClienteId();
      if (!idCliente) { window.location.hash = '#login'; return; }
      try {
        await api.adicionarAoCarrinho(idCliente, { idJogo, qtd: 1 });
        alert('Adicionado ao carrinho.');
      } catch (err) {
        alert('Erro ao adicionar: ' + err.message);
      }
    }
    if (action === 'detalhes') {
      window.location.hash = `#detalhes/${idJogo}`;
    }
  });

  // Busca/filtros removidos a pedido
}
