# Estocadão API 📦

Este é o backend do sistema Estocadão, desenvolvido em Kotlin Multiplatform (KMP) utilizando o framework Ktor para o servidor HTTP e Supabase para persistência de dados.

Atividade desenvolvida para a disciplina de Laboratório de Desenvolvimento Multiplataforma.

---

## Tecnologias 

* Kotlin Multiplatform (KMP)
* Ktor Server (Engine Netty)
* Supabase (Postgrest-kt)
* Kotlinx Serialization (JSON)

---

## Estrutura do Projeto 

O projeto segue a divisão responsabilidades exigida no enunciado:
* `/shared` - Contém os modelos de dados comuns (`Product`, `StockItem`, `StockSummary`) e a lógica de serialização compartilhada.
* `/server` - Contém a aplicação do servidor Ktor, incluindo a injeção de dependências do Supabase e as rotas de API (`ProductRoutes`, `StockRoutes`).
* `/composeApp` - Espaço reservado para o desenvolvimento da interface e telas da aplicação Compose Multiplatform.

---

## Endpoints 

### Produtos (`/products`)
* `GET /products` - Listar todos os produtos
* `GET /products/{id}` - Buscar um produto pelo ID
* `POST /products` - Cadastrar um novo produto (Recebe JSON)
* `PUT /products/{id}` - Atualizar os dados de um produto
* `DELETE /products/{id}` - Remover um produto

### Estoque (`/stock`)
* `GET /stock` - Listar todos os itens de estoque
* `GET /stock/{id}` - Buscar um item de estoque pelo ID
* `POST /stock` - Adicionar um item ao estoque (Recebe JSON)
* `PUT /stock/{id}` - Atualizar um item do estoque
* `DELETE /stock/{id}` - Remover um item do estoque

### Sumário (`/stock/summary`)
* `GET /stock/summary` - Retorna a quantidade total agregada de cada produto em estoq
* 

## Como Configurar e Executar Localmente

### 1. Clonar o Repositório
```shell
git clone [https://github.com/gibasatyro/Estocadao.git](https://github.com/gibasatyro/Estocadao.git)
cd Estocadao