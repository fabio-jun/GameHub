# GameHub

Uma plataforma de e-commerce de jogos digitais desenvolvida com Spring Boot e JavaScript.

## Sobre o Projeto

Inicialmente concebido como um projeto acadêmico de criação do backend de uma loja virtual. O sistema segue o padrão MVC com o backend implementando uma API REST, trafegando dados no formato JSON, tanto para atualizações quanto para consultas. A implementação utiliza a stack Java EE, Spring Boot e o SGBD PostgreSQL. A camada de persistência, bem como a interação com a mesma, foi implementada manualmente. O objetivo do trabalho era adquirir conhecimentos práticos de integração de bancos de dados em aplicações web em camadas, seguindo boas práticas de desenvolvimento. Após a conclusão, um frontend foi desenvolvido a fim de complementar a aplicação.

## Tecnologias Utilizadas

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

## Funcionalidades Principais

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

## Como Executar

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

## Estrutura do Projeto

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
