import { renderHome, setHomeSearchQuery } from './pages-home.js';
import { renderCart } from './pages-cart.js';
import { renderLogin } from './pages-login.js';
import { renderPerfil } from './pages-perfil.js';
import { renderDetalhes } from './pages-detalhes.js';
import { isAutenticado, getCliente, logout } from './auth.js';

const routes = {
  '#login': renderLogin,
  '': renderHome,
  '#': renderHome,
  '#carrinho': renderCart,
  '#perfil': renderPerfil,
  '#checkout': renderCart, // simplificado
};

const root = document.getElementById('app');
const clienteInfo = document.getElementById('clienteInfo');
const logoutBtn = document.getElementById('logoutBtn');
const navLogin = document.getElementById('navLogin');
const navCart = document.getElementById('navCart');
const navPerfil = document.getElementById('navPerfil');
const searchForm = document.getElementById('searchForm');
const searchInput = document.getElementById('searchInput');
const navSignup = document.getElementById('navSignup');

function updateAuthUI() {
  const c = getCliente();
  if (c) {
    const id = c.idCliente ?? c.id ?? c.id_cliente;
    clienteInfo && (clienteInfo.textContent = `Cliente: ${c.nome || ''} (ID ${id})`.trim());
    if (logoutBtn) logoutBtn.style.display = '';
    if (navLogin) navLogin.style.display = 'none';
    if (navSignup) navSignup.style.display = 'none';
    if (navPerfil) navPerfil.style.display = '';
    if (navCart) navCart.style.display = '';
  } else {
    if (clienteInfo) clienteInfo.textContent = 'Não autenticado';
    if (logoutBtn) logoutBtn.style.display = 'none';
    if (navLogin) navLogin.style.display = '';
    if (navSignup) navSignup.style.display = '';
    if (navPerfil) navPerfil.style.display = 'none';
    if (navCart) navCart.style.display = 'none';
  }
}

async function render() {
  const hash = window.location.hash || '#';
  const isLoginRoute = hash === '#login';
  
  // Verificar se é uma rota de detalhes
  if (hash.startsWith('#detalhes/')) {
    const jogoId = hash.split('/')[1];
    await renderDetalhes(root, { jogoId: parseInt(jogoId) });
  } else {
    const page = routes[hash] || renderHome;
    await page(root, {});
  }
  
  updateAuthUI();
  if (searchForm) searchForm.style.display = isLoginRoute ? 'none' : '';
}

window.addEventListener('hashchange', render);
window.addEventListener('DOMContentLoaded', () => {
  // Página inicial deve ser o catálogo (Home)
  if (!window.location.hash) window.location.hash = '#';
  render();
});
window.addEventListener('auth:changed', render);

// Acessibilidade simples para busca no header
if (searchForm) {
  searchForm.addEventListener('submit', (e) => {
    e.preventDefault();
    const q = (searchInput?.value || '').trim();
    setHomeSearchQuery(q);
    if (window.location.hash !== '#' && window.location.hash !== '') {
      window.location.hash = '#';
    } else {
      render();
    }
  });
}

if (logoutBtn) {
  logoutBtn.addEventListener('click', () => {
    logout();
  window.location.hash = '#';
  });
}
