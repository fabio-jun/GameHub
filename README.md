# 🎮 GameHub

Uma plataforma de e-commerce de jogos digitais desenvolvida com Spring Boot e JavaScript.

## 📋 Sobre o Projeto

GameHub é uma loja online para jogos digitais que oferece uma experiência moderna de compra e gerenciamento. O sistema foi desenvolvido seguindo arquitetura MVC com separação clara entre frontend e backend, proporcionando uma interface responsiva e intuitiva para usuários e administradores.

## 🚀 Tecnologias Utilizadas

### Backend
- **Java 21** - Linguagem principal
- **Spring Boot 3.x** - Framework principal
- **Spring Web** - Para desenvolvimento de APIs REST
- **PostgreSQL** - Banco de dados relacional
- **Maven** - Gerenciamento de dependências

### Frontend
- **JavaScript ES6+** - Linguagem principal do frontend
- **HTML5** - Estrutura das páginas
- **CSS3** - Estilização com Flexbox e Grid
- **SPA (Single Page Application)** - Navegação dinâmica

### Arquitetura
- **REST API** - Comunicação entre frontend e backend
- **MVC Pattern** - Organização do código backend
- **DAO Pattern** - Acesso a dados
- **CORS** - Configuração para requisições cross-origin

## 🎯 Funcionalidades Principais

### Para Usuários
- **Catálogo de Jogos** - Navegação e busca por jogos
- **Sistema de Carrinho** - Adicionar/remover itens
- **Autenticação** - Login e cadastro de usuários
- **Perfil do Usuario** - Visualização de dados pessoais e histórico de compras
- **Avaliações** - Sistema de reviews com estrelas
- **Detalhes do Produto** - Informações completas sobre jogos

### Para Administradores
- **Gerenciamento de Jogos** - CRUD completo
- **Gerenciamento de Usuários** - Controle de clientes
- **Relatórios** - Análise de vendas e performance
- **Categorias e Plataformas** - Organização do catálogo

## 🛠️ Como Executar

### Pré-requisitos
- Java 21+
- PostgreSQL
- Maven

### Passos
1. **Clone o repositório**
   ```bash
   git clone <url-do-repositorio>
   cd gamehub
   ```

2. **Configure o banco de dados**
   - Crie um banco PostgreSQL chamado `gamehub`
   - Configure as credenciais em `src/main/resources/application.properties`

3. **Execute o projeto**
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Acesse a aplicação**
   - Frontend: http://localhost:8080
   - API: http://localhost:8080/api

## 📁 Estrutura do Projeto

```
gamehub/
├── src/main/java/br/uel/gamehub/
│   ├── controller/          # Controladores REST
│   ├── dao/                 # Acesso a dados
│   ├── model/              # Entidades do sistema
│   └── config/             # Configurações
├── src/main/resources/
│   ├── static/             # Frontend (HTML, CSS, JS)
│   └── application.properties
└── pom.xml                 # Dependências Maven
```
