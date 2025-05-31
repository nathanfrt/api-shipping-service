# API Serviço de Remessa - (Shipping Service API) 

## Projeto de API RESTful que simula um sistema de **remessas entre usuários Pessoa Física (PF) e Pessoa Jurídica (PJ)** com conversão de moeda (Real para Dólar).

---

## Objetivo do Projeto
Construir uma API que permite:

- Criar e gerenciar usuários (PF e PJ);
- Realizar remessas com **conversão de moeda de Real para Dólar**;
- Validar limites de transações diárias;
- Integrar com a API pública do Banco Central para cotação do Dólar;
- Transferência de valores entre usuários cadastrados;
- Manter dados em banco **em memória**.

---

## Tecnologias Utilizadas

- **Java Spring Boot**
- **Spring JPA**
- **H2 Database (em memória)**
- **Lombok**
- **Swagger**
- **JUnit / Mockito** para testes

---

## Funcionalidades

### 👤 Usuários

- Cadastro de PF (com CPF único)
- Cadastro de PJ (com CNPJ único)
- Ambos cadastros com E-mail único

### 💰 Carteiras

- Cada usuário possui uma carteira com saldo em **Real (BRL)** e **Dólar (USD)**
- Carteiras são inicializadas com 0.00
- É necessário adicionar valor para os usuários atrvés de uma das rotas criadas

### 💸 Remessas

- Realiza **conversão automática** de BRL para USD usando a cotação mais recente da API pública do Banco Central
- Valida saldo antes da transação
- Aplica limites diários conforme o tipo do usuário
- Se houver falhas durante o processo o valor é revertido (validação com @Transaction e método).

---

## Testes

- Cobertura com testes unitários usando **JUnit** e **Mockito**

---

## Regras de Negócio

- Usuários PF têm limite diário de **R$10.000**
- Usuários PJ têm limite diário de **R$50.000**
- Cotação do dólar é obtida via:
  > [API pública do Banco Central](https://dadosabertos.bcb.gov.br/dataset/dolar-americano-usd-todos-os-boletins-diarios/resource/22ab054c-b3ff-4864-82f7-b2815c7a77ec)
  - Utiliza-se o campo `cotacaoCompra`
  - Em finais de semana, utiliza-se a última cotação disponível
- Remessas podem ocorrer entre qualquer tipo de usuário (PF <-> PJ)

---

## Instalação e Execução

### 🛠️ Pré-requisitos

- Java 17+
- Maven 3.8+

### Executando localmente

```bash
git clone https://github.com/nathanfrt/api-shipping-service.git
cd api-shipping-service/api-shipping-service
./mvnw spring-boot:run
````

A API estará disponível em:

```
http://localhost:8080
```

### Acesse a documentação Swagger:

```
http://localhost:8080/swagger-ui/index.html
```

---

## 📁 Estrutura de Pastas

```
shipping_service/
├── controller/        # Endpoints da API
├── dto/              # Data Transfer Objects
├── model/            # Entidades de domínio
├── exception/        # Tratamento de erros
├── service/          # Regras de negócio
├── repository/       # Interfaces de persistência
├── config/           # Configurações (Swagger, etc.)
└── resources/
    └── application.properties
```

---

## 📌 Melhorias Futuras

* Autenticação para permissão de transferência após validado
* Persistência em banco de dados real
* Implementação de Logs 
* Modelo mais trabalhado para extrato de transferência como nome e descrição

---


## 📌 Sobre Mim

Oie, Sou Nathan Freitas, desenvolvedor com 4 anos de experiência em **C# .NET**, incluindo construção de APIs, modelagem BPMN e codificação também com JavaScript. Recentemente, venho me a tecnologia Spring Java buscando novos aprendizados e oportunidades para encarar desafios no mercado. 

Para esse projeto foi necessário o estudo mais afundo no quesito da sintaxe da linguagem, embora seja bem similar ao ambiente do visual studio .net C# onde estou habituado. 

Vamos nos conectar e comentar um pouquinho do projeto? 

* Email: \[[nathanfrt@gmail.com](mailto:nathanfrt@gmail.com)]
* LinkedIn: \[[https://linkedin.com/in/nathan-freitas-](https://linkedin.com/in/nathan-freitas-)]

