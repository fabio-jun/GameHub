const API_BASE = window.location.origin;

export async function fetchJson(path, options = {}) {
  const res = await fetch(API_BASE + path, {
    headers: { 'Content-Type': 'application/json' },
    ...options,
  });
  if (!res.ok) {
    let text = '';
    try { text = await res.text(); } catch {}
    const msg = text || res.statusText || `HTTP ${res.status}`;
    const err = new Error(msg);
    err.status = res.status;
    throw err;
  }
  if (res.status === 204) return [];
  return res.json();
}

export const api = {
  // Jogos
  listarJogos: () => fetchJson('/jogos'),
  obterJogo: (id) => fetchJson(`/jogos/${id}`),
  buscarJogos: (q) => fetchJson(`/jogos/buscar?keyword=${encodeURIComponent(q)}`),
  porCategoria: (nome) => fetchJson(`/jogos/categoria/${encodeURIComponent(nome)}`),
  porPlataforma: (nome) => fetchJson(`/jogos/plataforma/${encodeURIComponent(nome)}`),
  porDesenvolvedor: (nome) => fetchJson(`/jogos/desenvolvedor/${encodeURIComponent(nome)}`),

  // Reviews
  obterReviews: (jogoId) => fetchJson(`/reviews/jogo/${jogoId}`),

  // Carrinho
  obterCarrinho: (clienteId) => fetchJson(`/carrinho/${clienteId}`),
  adicionarAoCarrinho: (clienteId, body) =>
    fetchJson(`/carrinho/cliente/${clienteId}`, { method: 'POST', body: JSON.stringify(body) }),
  atualizarQtd: (clienteId, jogoId, quantidade) =>
    fetchJson(`/carrinho/${clienteId}/${jogoId}`, { method: 'PUT', body: JSON.stringify({ qtd: quantidade }) }),
  removerDoCarrinho: (clienteId, jogoId) =>
    fetchJson(`/carrinho/${clienteId}/${jogoId}`, { method: 'DELETE' }),

  // Clientes
  obterCliente: (id) => fetchJson(`/clientes/${id}`),
  criarCliente: (data) => fetchJson('/clientes', { method: 'POST', body: JSON.stringify(data) }),
  login: (email, senha) => fetchJson(`/clientes/login?email=${encodeURIComponent(email)}&senha=${encodeURIComponent(senha)}`, { method: 'POST' }),

  // Compras
  realizarCompra: (clienteId) => fetchJson(`/compras/realizar/${clienteId}`, { method: 'POST' }),
  obterCompras: (clienteId) => fetchJson(`/compras/cliente/${clienteId}`),
  obterItensCompra: (compraId) => fetchJson(`/compras/itens/${compraId}`),
};