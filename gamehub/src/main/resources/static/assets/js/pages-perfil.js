import { api } from './api.js';
import { getClienteId, getClienteInfo } from './auth.js';

function moedaBR(v) {
  try { 
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(Number(v||0)); 
  } catch { 
    return `R$ ${(Number(v||0)).toFixed(2)}`; 
  }
}

function formatarData(dataStr) {
  try {
    const data = new Date(dataStr);
    return data.toLocaleDateString('pt-BR', {
      day: '2-digit',
      month: '2-digit', 
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  } catch {
    return dataStr;
  }
}

function compraTemplate(compra) {
  const dataFormatada = formatarData(compra.dataHoraCompra || compra.data || new Date());
  const total = compra.preco || compra.valorTotal || compra.total || 0;
  const id = compra.idCompra || compra.id || 'N/A';
  
  return `
    <div class="compra-item">
      <div class="compra-header">
        <h4>Compra #${id}</h4>
        <span class="compra-data">${dataFormatada}</span>
        <button class="btn-toggle" onclick="toggleItensCompra(${id})">
          <span id="toggle-text-${id}">Ver Detalhes</span>
          <span id="toggle-icon-${id}">▼</span>
        </button>
      </div>
      <div class="compra-total">
        <strong>Total: ${moedaBR(total)}</strong>
      </div>
      <div class="compra-itens" id="itens-${id}" style="display: none;">
        <div class="loading-itens">Carregando itens...</div>
      </div>
    </div>
  `;
}

function itemCompraTemplate(item) {
  const nome = item.itemNomeJogo || item.nome || 'Jogo';
  const preco = item.itemPrecoJogo || item.preco || 0;
  const quantidade = item.itemQtd || item.quantidade || 1;
  const subtotal = preco * quantidade;
  
  return `
    <div class="item-compra">
      <div class="item-info">
        <span class="item-nome">${nome}</span>
        <span class="item-detalhes">
          ${quantidade}x ${moedaBR(preco)} = ${moedaBR(subtotal)}
        </span>
      </div>
    </div>
  `;
}

// Função global para toggle dos itens (será anexada ao window)
window.toggleItensCompra = async function(compraId) {
  const itensDiv = document.getElementById(`itens-${compraId}`);
  const toggleText = document.getElementById(`toggle-text-${compraId}`);
  const toggleIcon = document.getElementById(`toggle-icon-${compraId}`);
  
  if (itensDiv.style.display === 'none') {
    // Mostrar itens
    itensDiv.style.display = 'block';
    toggleText.textContent = 'Ocultar Detalhes';
    toggleIcon.textContent = '▲';
    
    // Carregar itens se ainda não foram carregados
    if (itensDiv.querySelector('.loading-itens')) {
      try {
        const itens = await api.obterItensCompra(compraId);
        
        if (!Array.isArray(itens) || itens.length === 0) {
          itensDiv.innerHTML = '<p class="muted">Nenhum item encontrado.</p>';
        } else {
          itensDiv.innerHTML = `
            <h5>Itens da Compra:</h5>
            ${itens.map(itemCompraTemplate).join('')}
          `;
        }
      } catch (error) {
        console.error('Erro ao carregar itens da compra:', error);
        itensDiv.innerHTML = '<p class="error">Erro ao carregar itens da compra.</p>';
      }
    }
  } else {
    // Ocultar itens
    itensDiv.style.display = 'none';
    toggleText.textContent = 'Ver Detalhes';
    toggleIcon.textContent = '▼';
  }
};

export async function renderPerfil(root, state) {
  const clienteId = getClienteId();
  const clienteInfo = getClienteInfo();
  
  if (!clienteId) {
    window.location.hash = '#login';
    return;
  }

  root.innerHTML = `
    <section>
      <h2>Meu Perfil</h2>
      
      <div class="perfil-info">
        <h3>Informações da Conta</h3>
        <p><strong>Nome:</strong> ${clienteInfo?.nome || 'Não informado'}</p>
        <p><strong>Email:</strong> ${clienteInfo?.email || 'Não informado'}</p>
      </div>

      <div class="compras-historico">
        <h3>Histórico de Compras</h3>
        <div id="compras-lista">Carregando compras...</div>
      </div>
    </section>
  `;

  const comprasLista = root.querySelector('#compras-lista');

  try {
    // Tentar buscar compras do cliente
    const compras = await api.obterCompras(clienteId);
    
    if (!Array.isArray(compras) || compras.length === 0) {
      comprasLista.innerHTML = '<p class="muted">Nenhuma compra realizada ainda.</p>';
      return;
    }

    comprasLista.innerHTML = compras.map(compraTemplate).join('');
    
  } catch (error) {
    console.error('Erro ao carregar compras:', error);
    comprasLista.innerHTML = '<p class="error">Erro ao carregar histórico de compras.</p>';
  }
}
