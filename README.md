# Compras

**Compras** é uma API REST desenvolvida com **Spring Boot 3.4.4** e **Java 17**, voltada para o gerenciamento de produtos, compradores e compras em um sistema de compras simplificado.

---

## Tecnologias e Ferramentas

- **[Docker](https://www.docker.com/)** – O recomendado é utilizar a imagem Docker e o arquivo **compose.yml** disponibilizados na pasta raíz do projeto para executar a aplicação e para que as dependências externas carreguem de forma automática
- **[Elasticsearch](https://www.elastic.co/elasticsearch)** – Mecanismo de busca e centralização distribuída de logs
- **[Kibana](https://www.elastic.co/kibana)** – Gera os dashboards e relatórios
- **[Filebeat](https://www.elastic.co/beats/filebeat/)** – Essencial para enviar os logs estruturados para processamento do Elasticsearch + Kibana
- **[Java](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)** - Versão 17+
- **[Spring Boot](https://start.spring.io/)** – Utilizando a versão 3.4.4
- **[MapStruct](https://mapstruct.org/)** – Utilizado para mapeamento automático entre DTOs e entidades
- **[JPA](https://spring.io/projects/spring-data-jpa)** – Para criação dos repositórios e manipulação do Banco de Dados H2. Será utilizado junto com o JDBC
- **[Flyway](https://github.com/flyway/flyway)** – Para carregar as tabelas e executar migrações no banco de dados automáticamente
- **[Spring HATEOAS](https://spring.io/projects/spring-hateoas)** – Para que a API possa ser consumida em JSON e JSON+HAL
- **[Log4j2](https://logging.apache.org/log4j/2.x/index.html)** – Para logging estruturado
- **[H2 Database](https://www.h2database.com/html/main.html)** – Banco de dados em memória. Apesar de essencial para funcionamento do projeto, o objetivo não é focar na configuração do banco
- **[PostgreSQL](https://www.postgresql.org/)** – Banco relacional principal da aplicação
- **[Lombok](https://projectlombok.org/)** – Redução de boilerplate
- **[Spring Boot Starter Test](https://docs.spring.io/spring-boot/docs/1.5.7.RELEASE/reference/html/boot-features-testing.html)** – Testes com Mockito + JUnit
- **[Spotless Maven Plugin](https://github.com/diffplug/spotless)** – Formatação automática do código Java com:
  - `palantirJavaFormat`
  - `removeUnusedImports`

---

## Funcionalidades e Notas

- **Monitoramento e visualização facilitada de logs**: Acessando http://localhost:5601, é possível acompanhar os logs da aplicação e utilizar filtros para buscar por registros específicos. Por exemplo: Filtrar logs em que o nível (level) seja igual à "ERROR" ou "WARN". ![exemplo](https://github.com/user-attachments/assets/2ece6157-1563-4894-8ba4-a5e9a25ee909) - Já implementado com configurações simples
- **Migrações do banco com Flyway**: Inicializa o banco e cria as tabelas por db migrations. Implementado
- **PostgreSQL como banco principal**: Deixa o H2 para os testes do repositório e PostgreSQL na aplicação principal. Implementado
- **Produto**: CRUD completo implementado
- **Comprador**: CRUD completo implementado
- **Compra**: Ainda não implementado

### Objetivo Final
Permitir que um **Comprador** registre a **Compra** de um ou mais **Produtos**, com atualização automática de:
- Total de compras realizadas
- Saldo de débito
- Saldo do vale-alimentação
- Saldo total (soma dos dois saldos anteriores)

---

## Endpoints Disponíveis (Produto)

Base URL: `/api/v1/produto`

| Método | Endpoint                             | Descrição                                                                |
|--------|--------------------------------------|--------------------------------------------------------------------------|
| GET    | `/existe/{nome}`                     | Verifica se existe um produto com o nome informado                       |
| GET    | `/soma_preco_produtos`               | Recebe uma lista de IDs e retorna a soma dos preços                      |
| GET    | `/lista_preco_abaixo/{preco}`        | Lista produtos com preço abaixo do valor informado                       |
| GET    | `/lista_preco_acima/{preco}`         | Lista produtos com preço acima do valor informado                        |
| GET    | `/lista_padrao`                      | Lista todos os produtos ordenados por ID                                 |
| GET    | `/lista_ordenada`                    | Lista todos os produtos por ordem de criação (mais recentes primeiro)    |
| GET    | `/{id}`                              | Busca um produto pelo ID                                                 |
| GET    | `/nome/{nome}`                       | Busca um produto pelo nome                                               |
| POST   | `/cadastro`                          | Cadastra um novo produto                                                 |
| PUT    | `/alteracao/{id}`                    | Altera nome e preço de um produto pelo ID                                |
| DELETE | `/delecao/{id}`                      | Deleta um produto pelo ID                                                |

---

## Endpoints Disponíveis (Comprador)

Base URL: `/api/v1/comprador`

| Método | Endpoint                             | Descrição                                                                |
|--------|--------------------------------------|--------------------------------------------------------------------------|
| POST   | `/cadastra`                          | Cadastra um novo comprador                                               |
| GET    | `/lista`                             | Lista todos os compradores                                               |
| GET    | `/lista_ativos`                      | Lista todos os compradores ativos                                        |
| GET    | `/lista_negativados`                 | Lista todos os compradores negativados                                   |
| GET    | `/{id}`                              | Busca um comprador pelo ID                                               |
| PUT    | `/altera_nome/{id}`                  | Altera o nome cadastrado do comprador                                    |
| PATCH  | `/altera_saldo_debito/{id}`          | Altera o saldo de débito do comprador                                    |
| PATCH  | `/altera_saldo_va/{id}`              | Altera o saldo de vale-alimentação do comprador                          |
| PATCH  | `/atualiza_total_compras/{id}`       | Altera o total de compras do comprador                                   |
| DELETE | `/apaga/{id}`                        | Deleta um comprador pelo ID                                              |

---

## Testes

- **Testcontainers no repositório**: Complementar os testes do repositório subindo um Testcontainer (Usar H2 talvez?). Ainda não implementado
- **Testes do Produto [Repository, Service, Controller]**: Feito!
- **Testes do Comprador [Repository, Service, Controller]**: Feito!
- **Testes da Compra [Repository, Service, Controller]**: Ainda não iniciado

---

## Como Executar o Projeto

### Pré-requisitos

- **Java;**
- **Docker;**
- **PostgreSQL;**
- Para o correto funcionamento do sistema de monitorização, será necessário também inicializar: **Kibana**, **Elasticsearch** e **Filebeat** com as configurações default (Isso é feito de forma automática, caso realize o procedimento abaixo)
- **Terminal Linux**, **Git Bash** ou alguma forma de executar comandos **Linux.**

---

### Passos

1. Clone o repositório:
   ```bash
   git clone https://github.com/FelipeFranke5/compras.git
   ```

2. Abra a pasta do repositório:
   ```bash
   cd compras
   ```

3. Dê permissão de execução ao script:
    ```bash
    chmod +x exec_projeto.sh
    ```

4. Defina o usuário e senha do banco PostgreSQL (Exemplo: usuariopsql e senhapsql):
    ```bash
    export API_COMPRAS_PSQL_USUARIO=usuariopsql && export API_COMPRAS_PSQL_SENHA=senhapsql
    ```

5. Execute o script:
    ```bash
    ./exec_projeto.sh
    ```

A aplicação estará disponível em: http://localhost:8081

---

## Frontend

Um frontend simples está em desenvolvimento utilizando HTML, CSS e JavaScript, para facilitar a interação com a API REST. É possível acessá-lo na pasta **frontend**.

---

## Considerações

Este projeto está em constante evolução e busca praticar domínio de:

- Estruturação de código em camadas
- Evolução e uso de melhores práticas com Spring Boot
- Mapeamento de DTOs com MapStruct
- Logging estruturado (Para o terminal e em arquivo)
- Utilização de ferramentas de formatação e limpeza de código
- Build de pipeline para CI/CD (criação de workflows)
