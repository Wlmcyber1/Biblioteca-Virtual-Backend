# Biblioteca Virtual — Backend

API REST para cadastro de livros e categorias, feita com **Spring Boot** e **PostgreSQL**.

## Tecnologias

- Java 17
- Spring Boot 4.1 (Web MVC, Data JPA, Validation, Security)
- PostgreSQL 16
- Flyway (migrações versionadas do banco)
- Lombok
- MapStruct (conversão entre Model e DTO)
- Docker e Docker Compose

## Estrutura do projeto

```
src/main/java/com/bibliotecaVrtual/Biblioteca
├── config/        SecurityConfig: libera os endpoints e desliga o CSRF
├── controller/    LivroController (/livros) e CategoriaController (/categorias)
├── dto/           DTOs de entrada e saída de Livro e Categoria
├── mapper/        LivroMapper (MapStruct)
├── model/         LivroModel e CategoriaModel
├── repository/    Repositórios JPA de Livro e Categoria
└── service/       Regras de negócio de Livro e Categoria
```

## Endpoints

URL base: `http://localhost:8080/livros`

| Método | Rota           | Descrição                                   | Sucesso          |
|--------|----------------|---------------------------------------------|------------------|
| POST   | `/livros`      | Cadastra um livro                           | `201 Created`    |
| GET    | `/livros`      | Lista todos os livros                       | `200 OK`         |
| GET    | `/livros/{id}` | Busca um livro pelo id                      | `200 OK`         |
| PUT    | `/livros/{id}` | Atualiza todos os campos do livro           | `200 OK`         |
| PATCH  | `/livros/{id}` | Atualiza só os campos enviados              | `200 OK`         |
| DELETE | `/livros/{id}` | Remove um livro                             | `204 No Content` |

### Corpo da requisição (POST / PUT / PATCH)

```json
{
  "titulo": "Dom Casmurro",
  "autor": "Machado de Assis"
}
```

### Resposta

```json
{
  "id": 1,
  "titulo": "Dom Casmurro",
  "autor": "Machado de Assis"
}
```

### Validação e erros

- No **POST** e no **PUT**, `titulo` e `autor` são obrigatórios (`@NotBlank`). Se faltar algum, a API responde **400 Bad Request**.
- No **PATCH**, os campos são opcionais: só o que vier preenchido é alterado.
- Se o id não existir (GET por id, PUT, PATCH e DELETE), a API responde **404 Not Found** com a mensagem "Livro não encontrado".

## CRUD de categorias

URL base: `http://localhost:8080/categorias`

| Método | Rota | Descrição | Sucesso |
|---|---|---|---|
| POST | `/categorias` | Cadastra categoria | `201 Created` |
| GET | `/categorias` | Lista categorias | `200 OK` |
| GET | `/categorias/{id}` | Busca por ID | `200 OK` |
| PUT | `/categorias/{id}` | Altera o nome | `200 OK` |
| DELETE | `/categorias/{id}` | Exclui categoria | `204 No Content` |

Corpo do POST/PUT:

```json
{
  "nome": "Literatura"
}
```

Regras: o nome é obrigatório, tem limite de 100 caracteres e é normalizado removendo espaços extras. Nomes repetidos, ignorando maiúsculas/minúsculas, retornam `409 Conflict`; IDs inexistentes retornam `404 Not Found`. A migration `V1__create_tb_categoria.sql` cria a tabela e o índice único case-insensitive.

## Como rodar

### Opção 1: tudo no Docker

Sobe a API e o banco juntos:

```bash
docker compose up --build
```

A API fica em `http://localhost:8080` e o Postgres na porta `5432`.

### Opção 2: só o banco no Docker, API pela IDE

```bash
docker compose up db
```

Depois rode a classe `BibliotecaApplication` pela IDE, ou:

```bash
./mvnw spring-boot:run
```

A conexão com o banco fica em `src/main/resources/application.properties`:

| Propriedade | Valor                                          |
|-------------|------------------------------------------------|
| URL         | `jdbc:postgresql://localhost:5432/db-biblioteca` |
| Usuário     | `admin`                                        |
| Senha       | `admin`                                        |

O Hibernate cria e atualiza as tabelas sozinho (`ddl-auto=update`).

## Segurança

Por enquanto, todos os endpoints estão liberados, sem login, e o CSRF está desligado (ver `config/SecurityConfig.java`). A dependência do Spring Security continua no projeto, então para adicionar autenticação basta alterar essa classe.

## Próximos passos

- Relacionar livros/materiais às categorias
- Implementar autenticação
- Escrever testes para o service e o controller
