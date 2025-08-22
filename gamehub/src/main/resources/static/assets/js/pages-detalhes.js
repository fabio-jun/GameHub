import { api } from './api.js';
import { getClienteId } from './auth.js';

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
    return data.toLocaleDateString('pt-BR');
  } catch {
    return dataStr;
  }
}

function reviewTemplate(review) {
  const nota = review.revNota || review.nota || review.rating || 0;
  const comentario = review.revComentario || review.comentario || review.comment || '';
  const cliente = review.nomeCliente || review.cliente || 'Anônimo';
  const data = formatarData(review.revData || review.dataReview || review.data || new Date());
  
  const estrelas = '★'.repeat(Math.max(0, Math.min(5, nota))) + '☆'.repeat(5 - Math.max(0, Math.min(5, nota)));
  
  return `
    <div class="review-item">
      <div class="review-header">
        <span class="review-cliente">${cliente}</span>
        <span class="review-nota">${estrelas} (${nota}/5)</span>
        <span class="review-data">${data}</span>
      </div>
      <p class="review-comentario">${comentario}</p>
    </div>
  `;
}

export async function renderDetalhes(root, state) {
  const jogoId = state.jogoId;
  
  if (!jogoId) {
    root.innerHTML = '<p class="error">ID do jogo não especificado.</p>';
    return;
  }

  root.innerHTML = `
    <section>
      <div id="jogo-detalhes">Carregando detalhes do jogo...</div>
    </section>
  `;

  const detalhesContainer = root.querySelector('#jogo-detalhes');

  try {
    // Buscar detalhes do jogo
    const jogo = await api.obterJogo(jogoId);
    
    if (!jogo) {
      detalhesContainer.innerHTML = '<p class="error">Jogo não encontrado.</p>';
      return;
    }

    const nome = jogo.nome || jogo.titulo || 'Jogo';
    const preco = jogo.preco ?? jogo.valor ?? 0;
    const descricao = jogo.descricao || 'Sem descrição disponível.';
    const categoria = jogo.categoria || 'Não informado';
    const desenvolvedor = jogo.desenvolvedor || 'Não informado';
    const plataforma = jogo.plataforma || 'Não informado';
    const dataLancamento = formatarData(jogo.lancamento || jogo.dataLancamento);
    const img = jogo.imagePath ? `/imagens/${jogo.imagePath}` : `/imagens/default.jpg`;

    detalhesContainer.innerHTML = `
      <div class="jogo-detalhes">
        <button class="btn-voltar" onclick="history.back()">← Voltar</button>
        
        <div class="jogo-hero">
          <div class="jogo-imagem">
            <img src="${img}" alt="${nome}" />
          </div>
          <div class="jogo-info">
            <h1>${nome}</h1>
            <p class="jogo-preco">${moedaBR(preco)}</p>
            <div class="jogo-acoes">
              <button class="btn btn-primary" id="addToCartBtn" data-id="${jogoId}">
                Adicionar ao Carrinho
              </button>
            </div>
          </div>
        </div>

        <div class="jogo-detalhes-grid">
          <div class="jogo-descricao">
            <h3>Descrição</h3>
            <p>${descricao}</p>
          </div>
          
          <div class="jogo-specs">
            <h3>Informações</h3>
            <div class="spec-item">
              <strong>Categoria:</strong> ${categoria}
            </div>
            <div class="spec-item">
              <strong>Desenvolvedor:</strong> ${desenvolvedor}
            </div>
            <div class="spec-item">
              <strong>Plataforma:</strong> ${plataforma}
            </div>
            <div class="spec-item">
              <strong>Data de Lançamento:</strong> ${dataLancamento}
            </div>
          </div>
        </div>

        <div class="reviews-section">
          <h3>Avaliações</h3>
          <div id="reviews-lista">Carregando avaliações...</div>
        </div>
      </div>
    `;

    // Carregar reviews
    const reviewsLista = detalhesContainer.querySelector('#reviews-lista');
    try {
      console.log('Buscando reviews para jogo ID:', jogoId);
      const reviews = await api.obterReviews(jogoId);
      console.log('Reviews recebidas:', reviews);
      
      if (!reviews || !Array.isArray(reviews) || reviews.length === 0) {
        reviewsLista.innerHTML = '<p class="muted">Nenhuma avaliação ainda.</p>';
      } else {
        console.log('Renderizando', reviews.length, 'reviews');
        reviewsLista.innerHTML = reviews.map(reviewTemplate).join('');
      }
    } catch (error) {
      console.error('Erro ao carregar reviews:', error);
      reviewsLista.innerHTML = '<p class="muted">Erro ao carregar avaliações: ' + error.message + '</p>';
    }

    // Event listener para adicionar ao carrinho
    const addToCartBtn = detalhesContainer.querySelector('#addToCartBtn');
    addToCartBtn?.addEventListener('click', async () => {
      const clienteId = getClienteId();
      if (!clienteId) { 
        window.location.hash = '#login'; 
        return; 
      }
      
      try {
        await api.adicionarAoCarrinho(clienteId, { idJogo: jogoId, qtd: 1 });
        alert('Adicionado ao carrinho!');
      } catch (err) {
        alert('Erro ao adicionar: ' + err.message);
      }
    });

  } catch (error) {
    console.error('Erro ao carregar detalhes do jogo:', error);
    detalhesContainer.innerHTML = '<p class="error">Erro ao carregar detalhes do jogo.</p>';
  }
}
