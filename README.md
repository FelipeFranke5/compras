# Compras

**Compras** é uma API REST desenvolvida com **Spring Boot 3.4.4** e **Java 17**, voltada para o gerenciamento de produtos, compradores e compras em um sistema de compras simplificado.

---

## Tecnologias e Ferramentas

- **[Docker](https://www.docker.com/)** - O recomendado é utilizar a imagem Docker e o arquivo **compose.yml** disponibilizados na pasta raíz do projeto para executar a aplicação e para que as dependências externas carreguem de forma automática
- **[Java](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)**
- **[Spring Boot](https://start.spring.io/)**
- **Maven**
- **[MapStruct](https://mapstruct.org/)** – Utilizado para mapeamento automático entre DTOs e entidades
- **[JPA](https://spring.io/projects/spring-data-jpa)** - Para criação dos repositórios e manipulação do Banco de Dados H2. Será utilizado junto com o JDBC
- **[Spring HATEOAS](https://spring.io/projects/spring-hateoas)** - Para que a API possa ser consumida em JSON e JSON+HAL
- **Spring Boot Starter Web**
- **[Log4j2](https://logging.apache.org/log4j/2.x/index.html)** - Para logging estruturado
- **H2 Database** – Banco de dados em memória. Apesar de essencial para funcionamento do projeto, o objetivo não é focar na configuração do banco
- **Lombok** – Redução de boilerplate
- **Spring Boot Starter Test** – Testes com Mockito + JUnit
- **Spotless Maven Plugin** – Formatação automática do código com:
  - `palantirJavaFormat`
  - `removeUnusedImports`

---

## Funcionalidades (Trabalho em progresso)

- **Monitoramento e visualização facilitada de logs**: Acessando http://localhost:5601, é possível acompanhar os logs da aplicação e utilizar filtros para buscar por registros específicos. Por exemplo: Filtrar logs em que o nível (level) seja igual à "ERROR" ou "WARN". ![exemplo](https://github.com/user-attachments/assets/2ece6157-1563-4894-8ba4-a5e9a25ee909)
- **Produto**: CRUD completo implementado
- **Comprador**: Implementação parcial (controller ainda pendente, testes iniciados)
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

| Método | Endpoint                             | Descrição                                                                 |
|--------|--------------------------------------|---------------------------------------------------------------------------|
| GET    | `/existe/{nome}`                     | Verifica se existe um produto com o nome informado                       |
| GET   | `/soma_preco_produtos`               | Recebe uma lista de IDs e retorna a soma dos preços                      |
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

## Testes

Atualmente, há testes cobrindo:
- Entidade e lógica de negócio para **Produto**
- Parte da entidade e lógica de negócio de **Comprador**

---

## Como Executar o Projeto

### Pré-requisitos

- **Java;**
- **Docker;**
- Para o correto funcionamento do sistema de monitorização, será necessário também inicializar: **Kibana**, **Elasticsearch** e **Filebeat** com as configurações default (Isso é feito de forma automática, caso realize o procedimento abaixo)
- **Terminal Linux**, **Git Bash** ou alguma forma de executar comandos **Linux.**

---

### Passos

1. Clone o repositório:
   ```bash
   git clone https://github.com/FelipeFranke5/compras.git
   cd compras
   ```

2. Dê permissão de execução ao script:
    ```bash
    chmod +x exec_projeto.sh
    ```

3. Execute o script:
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
