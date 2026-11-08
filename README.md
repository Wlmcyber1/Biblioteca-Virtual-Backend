# Biblioteca Virtual — Backend

API REST de uma **biblioteca virtual escolar**, feita para o trabalho da **NP2**. Com ela dá para cadastrar livros, categorias, autores, editoras, usuários (bibliotecários, professores e estudantes) e turmas. Os professores podem recomendar livros para as turmas, e os alunos podem buscar livros com filtros combinados.

> **Uso de Inteligência Artificial:** este projeto foi desenvolvido **com a ajuda do Claude** (IA da Anthropic), usado por todos os integrantes. A seção [Uso de IA](#uso-de-inteligência-artificial) conta como e onde ele foi usado.

---

## Sumário

- [Tecnologias](#tecnologias)
- [Equipe](#equipe)
- [Arquitetura](#arquitetura)
- [Modelo de dados](#modelo-de-dados)
- [Endpoints](#endpoints)
- [Busca avançada e livros recomendados](#busca-avançada-e-livros-recomendados)
- [Validações e erros](#validações-e-erros)
- [Migrations (Flyway)](#migrations-flyway)
- [Como rodar](#como-rodar)
- [Decisões de projeto](#decisões-de-projeto)
- [Uso de Inteligência Artificial](#uso-de-inteligência-artificial)
- [Próximos passos](#próximos-passos)

---

## Tecnologias

| Tecnologia | Para que serve |
|---|---|
| Java 17 | Linguagem |
| Spring Boot 4.1 | Web MVC, Data JPA, Validation |
| PostgreSQL 16 | Banco de dados |
| Flyway | Migrations versionadas do banco |
| Spring Data JPA + Specification | Persistência e consultas com filtros opcionais |
| MapStruct 1.6 | Conversão entre Model e DTO |
| Lombok | Gera getters, setters e construtores |
| springdoc-openapi (Swagger) | Documentação interativa em `/docs` |
| Docker e Docker Compose | Sobe a API e o banco |

---

## Equipe

| Integrante | Principais contribuições |
|---|---|
| **Letícia Santos** ([@Wlmcyber1](https://github.com/Wlmcyber1)) | Setup do projeto (Spring Boot, Docker, Flyway), Livro, MapStruct, Swagger, seed de categorias, CRUD de Usuário, ligação Livro ↔ Editora, busca avançada e livros recomendados |
| **Adriel Coutinho** ([@AdrielCout](https://github.com/AdrielCout)) | CRUD de Categoria, CRUD de Autor, tratamento global de erros, CRUD de Editora, CRUD de Turma, V6 (categoria obrigatória) e V11 (bloqueio de exclusão de editora com livros) |

O histórico completo está nos commits e nos Pull Requests do repositório.

---

## Arquitetura

O projeto é organizado em camadas:

```
src/main/java/com/bibliotecaVrtual/Biblioteca
├── controller/      Recebe as requisições HTTP (Autor, Categoria, Editora, Livro, Turma, Usuario)
├── dto/             O que entra (RequestDTO) e o que sai (ResponseDTO) da API
├── exception/       GlobalExceptionHandler + ErroResponse (formato padrão de erro)
├── mapper/          Conversões Model ↔ DTO com MapStruct
├── model/           Entidades JPA (tabelas do banco)
├── repository/      Acesso ao banco (Spring Data JPA)
│   └── specification/  Filtros dinâmicos da busca avançada (LivroSpecification, TurmaSpecification)
└── service/         Regras de negócio
```

Caminho de uma requisição: **Controller → Service → Repository → Banco**. Os dados entram e saem como DTO e são convertidos para Model pelo Mapper.

---

## Modelo de dados

```
tb_categoria 1 ───< N tb_livro N >─── 0..1 tb_editora
                         │
                         N
                         │
                  tb_turma_livro      (N:N — livros recomendados)
                         │
                         N
                         │
tb_usuario (PROFESSOR) 1 ───< N tb_turma

tb_autor   (cadastro independente; no livro, o autor é um texto)
```

| Entidade | Atributos | Observações |
|---|---|---|
| **Categoria** | `id`, `nome` | Nome único, sem diferenciar maiúsculas de minúsculas |
| **Livro** | `id`, `titulo`, `autor`, `categoria`, `editora`, `descricao`, `anoPublicacao`, `urlArquivo` | Categoria **obrigatória**; editora **opcional** |
| **Autor** | `id`, `nome`, `nacionalidade` | Ver [Decisões de projeto](#decisões-de-projeto) |
| **Editora** | `id`, `nome`, `cnpj`, `email` | CNPJ e e-mail validados por formato |
| **Usuário** | `id`, `nome`, `email`, `matricula`, `perfil`, `ativo`, `dataCadastro` | Perfil: `BIBLIOTECARIO`, `PROFESSOR` ou `ESTUDANTE`; e-mail e matrícula únicos |
| **Turma** | `id`, `serie`, `letra`, `anoLetivo`, `professor`, `livros` | Série de 1 a 3; série + letra + ano é único; o professor precisa ter perfil `PROFESSOR` |

---

## Endpoints

URL base: `http://localhost:8080`. A documentação interativa (Swagger) fica em **`http://localhost:8080/docs`**.

Todos os recursos seguem o mesmo padrão:

| Método | Rota | Descrição | Sucesso |
|---|---|---|---|
| POST | `/{recurso}` | Cadastra | `201 Created` |
| GET | `/{recurso}` | Lista todos | `200 OK` |
| GET | `/{recurso}/{id}` | Busca pelo id | `200 OK` |
| PUT | `/{recurso}/{id}` | Atualiza **todos** os campos | `200 OK` |
| PATCH | `/{recurso}/{id}` | Atualiza **só os campos enviados** | `200 OK` |
| DELETE | `/{recurso}/{id}` | Exclui | `204 No Content` |

| Recurso | Rota | Observação |
|---|---|---|
| Categorias | `/categorias` | Sem PATCH (só tem o campo `nome`) |
| Livros | `/livros` | Também tem `/livros/busca` e `/livros/recomendados` |
| Autores | `/autores` | |
| Editoras | `/editoras` | |
| Usuários | `/usuarios` | |
| Turmas | `/turmas` | |

### Exemplos de corpo (POST/PUT)

**Livro**
```json
{
  "titulo": "Dom Casmurro",
  "autor": "Machado de Assis",
  "categoriaId": 1,
  "editoraId": 2,
  "descricao": "Romance narrado por Bentinho.",
  "anoPublicacao": 1899,
  "urlArquivo": "https://exemplo.com/dom-casmurro.pdf"
}
```

**Usuário**
```json
{
  "nome": "Maria Souza",
  "email": "maria@escola.com",
  "matricula": "2026001",
  "perfil": "PROFESSOR"
}
```

**Turma**
```json
{
  "serie": 2,
  "letra": "B",
  "anoLetivo": 2026,
  "professorId": 1,
  "livroIds": [1, 3, 5]
}
```

**Editora**
```json
{
  "nome": "Editora Exemplo",
  "cnpj": "12.345.678/0001-90",
  "email": "contato@exemplo.com"
}
```

---

## Busca avançada e livros recomendados

### `GET /livros/busca`

Todos os filtros são **opcionais** e se combinam com **E**. Os que não forem enviados são ignorados.

| Parâmetro | Tipo | Como filtra |
|---|---|---|
| `titulo` | texto | Parte do título, sem diferenciar maiúsculas de minúsculas |
| `autor` | texto | Parte do nome do autor |
| `categoriaId` | número | Categoria exata |
| `editoraId` | número | Editora exata |
| `anoMin` / `anoMax` | número | Faixa de ano de publicação (`anoMin` > `anoMax` → `400`) |

Exemplo: `GET /livros/busca?titulo=dom&categoriaId=1&anoMin=1800&anoMax=1950`

### `GET /livros/recomendados`

Para o aluno ver os livros que os professores recomendaram. Os filtros também são opcionais e se combinam:

| Parâmetro | Descrição |
|---|---|
| `turmaId` | Id da turma |
| `serie`, `letra`, `anoLetivo` | Identificam a turma sem precisar do id |
| `professor` | Parte do nome do professor |

Exemplo: `GET /livros/recomendados?serie=2&letra=B&anoLetivo=2026`

O resultado junta os livros de todas as turmas encontradas, **sem repetir**.

> Usamos `Specification` (JPA Criteria) em vez de `@Query` porque, no PostgreSQL, uma `@Query` com parâmetros que podem vir nulos dá o erro *"could not determine data type of parameter"*. Com Specification, cada filtro só entra na consulta se tiver valor.

---

## Validações e erros

Todos os erros voltam no mesmo formato (`ErroResponse`), tratados pelo `GlobalExceptionHandler`:

```json
{
  "status": 400,
  "mensagem": "Dados inválidos",
  "erros": ["titulo: O título é obrigatório"]
}
```

| Situação | Status |
|---|---|
| Campo obrigatório faltando, tamanho ou formato inválido (Bean Validation) | `400 Bad Request` |
| JSON malformado ou tipo errado no parâmetro | `400 Bad Request` |
| Id inexistente (livro, categoria, editora, professor…) | `404 Not Found` |
| Nome de categoria, e-mail/matrícula de usuário ou turma duplicados | `409 Conflict` |
| Excluir um registro que ainda tem outros ligados a ele (ex.: categoria ou editora com livros) | `409 Conflict` |

No **PATCH** não existe `@Valid`, porque os campos podem vir nulos. As mesmas regras são conferidas no service, só para os campos enviados.

---

## Migrations (Flyway)

O banco é criado e alterado **só pelas migrations**. O Hibernate roda com `ddl-auto=validate`, ou seja, só confere se as entidades batem com o banco.

| Versão | O que faz |
|---|---|
| V1 | Cria `tb_categoria` com índice único (sem diferenciar maiúsculas de minúsculas) |
| V2 | Cria `tb_livro` |
| V3 | Adiciona categoria, descrição, ano e URL no livro |
| V4 | Seed de categorias iniciais |
| V5 | Cria `tb_autor` |
| V6 | Torna a categoria do livro obrigatória (livros antigos vão para "Material de apoio") |
| V7 | Cria `tb_editora` |
| V8 | Cria `tb_usuario` |
| V9 | Cria `tb_turma` e `tb_turma_livro` (N:N) |
| V10 | Liga o livro à editora (opcional) |
| V11 | Impede excluir editora que tem livros (mesmo padrão da categoria) |

> Uma migration que já rodou **não é editada**: o Flyway guarda o checksum dela e a aplicação não sobe se o arquivo mudar. Correções viram uma migration nova, como a V6 e a V11.

---

## Como rodar

### Opção 1: tudo no Docker

```bash
docker compose up --build
```

A API fica em `http://localhost:8080` (Swagger em `/docs`) e o PostgreSQL na porta `5432`.

### Opção 2: só o banco no Docker, API pela IDE

```bash
docker compose up db
./mvnw spring-boot:run
```

Ou rode a classe `BibliotecaApplication` pela IDE.

| Propriedade | Valor |
|---|---|
| URL | `jdbc:postgresql://localhost:5432/db-biblioteca` |
| Usuário | `admin` |
| Senha | `admin` |

As migrations rodam sozinhas quando a aplicação sobe.

---

## Decisões de projeto

- **Sem login/JWT.** O enunciado não exige autenticação, então todos os endpoints são abertos. Regras como "só professor recomenda livro" são garantidas pela regra de negócio (o usuário da turma precisa ter perfil `PROFESSOR`), não por login.
- **Perfil do usuário é `enum`**, porque é uma lista fixa (`BIBLIOTECARIO`, `PROFESSOR`, `ESTUDANTE`) e não precisa de tabela própria.
- **Categoria obrigatória e editora opcional** no livro. Nos dois casos, o banco **bloqueia** excluir um registro que ainda tem livros, e a API responde `409`.
- **Autor no livro ainda é texto.** O CRUD de Autor existe, mas a ligação Livro ↔ Autor (N:N) ainda não foi feita (ver [Próximos passos](#próximos-passos)).
- **Uma turma tem um professor**, e é ele quem recomenda os livros daquela turma.
- **MapStruct** como padrão para converter Model ↔ DTO em todas as entidades.

---

## Uso de Inteligência Artificial

Usamos o **Claude**, da Anthropic, pelo **Claude Code** (assistente de programação no terminal). Ele foi usado por **todos os integrantes**, nos modelos **Claude Sonnet 5.5** e **Claude Opus 5.5**. Achamos importante deixar isso claro: a IA fez parte do desenvolvimento, e quem decidiu, revisou e é responsável pelo código somos nós.

### Para que usamos

- **Gerar código** de CRUDs, DTOs, mappers, services, specifications e migrations, sempre a partir do que a gente pedia e combinava.
- **Explicar conceitos**: camadas, JPA, relacionamentos N:1 e N:N, Specification (`root`, `query`, `cb`), Flyway, chave estrangeira e `ON DELETE`.
- **Revisar código** (code review) antes dos merges.
- **Achar e corrigir erros** de compilação, de migration e do PostgreSQL.
- **Organizar o Git**: branches por funcionalidade, mensagens de commit no padrão Conventional Commits e Pull Requests.
- **Documentação**, incluindo este README.

### Como usamos

1. A IA **mostrava o código no chat antes** de alterar qualquer arquivo. Só depois da nossa aprovação a alteração era feita, **um arquivo por vez**.
2. Cada integrante **revisou e entende** o código que entregou e consegue explicá-lo.
3. As decisões de projeto (sem login, editora opcional, um professor por turma, usar Specification etc.) foram **nossas**. A IA apresentava as opções e as consequências de cada uma.
4. Os commits feitos com ajuda da IA têm a linha `Co-Authored-By: Claude …` na mensagem, o que deixa isso registrado no histórico do Git.

### A IA também erra

A IA não substituiu a nossa revisão. Dois exemplos deste projeto:

- O filtro `editoraId` da busca avançada foi escrito numa branch em que o `LivroModel` ainda não tinha o campo `editora`. Compilava, mas daria erro quando alguém usasse o filtro. Isso só ficou certo depois que as branches foram juntadas.
- A V10 criou a ligação Livro → Editora com `ON DELETE SET NULL`. Isso fugia do padrão do projeto, porque excluir uma editora apagaria a editora dos livros sem avisar. Percebemos isso **numa revisão** e corrigimos com a **V11**.

---

## Próximos passos

- Ligar Livro ↔ Autor (N:N) usando a tabela `tb_autor`, no lugar do autor em texto
- Registrar acessos/leituras dos alunos e montar um ranking dos livros mais acessados
- Escrever testes automatizados para os services e controllers
