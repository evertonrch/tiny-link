# 🔗 Tiny-link

Uma API REST para encurtamento de URLs e geração de QR Codes, construída com Spring Boot e executada em um ambiente Docker com Docker Compose.
Este projeto foi inspirado em um dos desafios propostos pelo repositório **Back-End Brasil**. Mais detalhes você encontra [aqui](https://github.com/backend-br/desafios/blob/master/url-shortener/PROBLEM.md).

---

## 🚀 Tecnologias

- **Java 17**
- **Spring Boot 3.x**
- **MongoDB**
- **Docker & Docker Compose**
- **ZXing** (QR Code)
- **JUnit e Mockito**

---

## 🏗️ Arquitetura e Padrões de Projeto

A aplicação segue **padrões arquiteturais e de projeto** para garantir escalabilidade e manutenção:

- **Padrão em Camadas**: Separação entre Controller, Service e Repository.
- **Builder**: Construção fluida de objetos complexos.
- **Static Factory Method**: Criação de instâncias de forma encapsulada.
- **Rule Pattern**: Encapsulamento de regras de negócio.

---

## 📋 Requisitos

- **Docker** e **Docker Compose** instalados

---

## ⚙️ Instalação e Configuração

1. **Clone o repositório**:
   ```
   git clone https://github.com/seu-usuario/url-shortener-api.git
   cd url-shortener-api
   ```
   
2. **Configure o banco de dados** no `application.yml` (os dados da URL de conexão devem corresponder aos do `docker-compose.yml`):
   ```
   spring:
     data:
       mongodb:
         uri: mongodb://seu-usuario:sua-senha@seu-host:27017/encurtador_url
   ```
   
3. **Suba os containers com Docker Compose**:
   ```
   docker-compose up -d
   ```
   
4. **A API estará disponível em**:
   ```
   http://localhost:8080
   ```

---

## 📡 Endpoints da API

### 🔹 Criar um link encurtado
**POST /api/links**

#### 📥 Request:
```
{
  "url": "https://www.exemplo.com"
}
```

#### 📤 Response:
```
{
    "urlOriginal": "https://www.exemplo.com",
    "urlEncurtada": "uxddf",
    "criadaEm": "2025-01-29T03:29:33.704476228",
    "qrcode": "qrcode em base64..."
}
```

---

### 🔹 Redirecionar para a URL original
**GET /{urlEncurtada}**

```
curl -X GET http://localhost:8080/uxddf
```
➡️ **Redireciona para:** `https://www.exemplo.com`

---

### 🔹 Gerar QR Code
**GET /{urlEncurtada}/qrcode**

```
curl -X GET http://localhost:8080/uxddf/qrcode --output qrcode.png
```
➡️ **Retorna uma imagem PNG do QR Code**

---

## 🧪 Testes 

Para rodar os testes automatizados:

```
mvn test
```

---

## 🤝 Contribuição

Contribuições são bem-vindas! Para contribuir:

1. **Fork** o repositório.
2. **Crie uma branch** (`git checkout -b feature/nova-funcionalidade`).
3. **Faça commit das mudanças** (`git commit -m 'Adiciona nova funcionalidade'`).
4. **Envie um PR**!

