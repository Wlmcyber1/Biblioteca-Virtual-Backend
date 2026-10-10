# Biblioteca Virtual — Backend

API REST para cadastro de livros, feita com **Spring Boot** e **PostgreSQL**.

## Tecnologias

- Java 17
- Spring Boot 4.1 (Web MVC, Data JPA, Validation, Security)
- PostgreSQL 16
- Lombok
- MapStruct (conversão entre Model e DTO)
- Docker e Docker Compose

## Estrutura do projeto

```
src/main/java/com/bibliotecaVrtual/Biblioteca
├── config/        SecurityConfig: libera os endpoints e desliga o CSRF
├── controller/    LivroController: endpoints REST em /livros
├── dto/LivroDTO/  LivroRequestDTO (entrada) e LivroResponseDTO (saída)
├── mapper/        LivroMapper (MapStruct)
├── model/         LivroModel: entidade JPA (tabela tb_livro)
├── repository/    LivroRepository (JpaRepository)
└── service/       LivroService: regras de negócio
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

- Adicionar categoria ao livro (já há um campo comentado no model e no DTO)
- Implementar autenticação
- Escrever testes para o service e o controller
