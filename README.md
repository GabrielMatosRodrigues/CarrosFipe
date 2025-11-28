# 🚗 Sistema de Integração FIPE

> Desenvolvido por **Gabriel Matos Rodrigues**  
> [GitHub](https://github.com/GabrielMatosRodrigues)

Sistema de integração com a API FIPE para gerenciamento de veículos, utilizando arquitetura de microserviços com mensageria assíncrona.

---

## 📋 Sobre o Projeto

Este projeto foi desenvolvido como parte de um teste técnico e implementa uma solução completa de integração com a [API FIPE](https://deividfortuna.github.io/fipe/) para gerenciamento de marcas e modelos de veículos.

### 🎯 Funcionalidades Implementadas

- ✅ Carga inicial de dados da FIPE via API REST
- ✅ Processamento assíncrono com RabbitMQ
- ✅ Persistência em banco de dados PostgreSQL
- ✅ APIs REST para consulta e atualização
- ✅ Documentação automática com Swagger/OpenAPI
- ✅ Arquitetura limpa (Clean Architecture)
- ✅ Separação em microserviços

---

## 🏗️ Arquitetura

O projeto é composto por **2 microserviços**:

### **API-1** (Porta 8080)
- Endpoint para iniciar carga de dados
- Publica marcas na fila RabbitMQ
- APIs para consulta de marcas e veículos
- API para atualização de veículos

### **API-2** (Porta 8081)
- Consome mensagens da fila
- Busca modelos na API FIPE
- Persiste dados no PostgreSQL

### Fluxo de Dados
```
API FIPE → API-1 → RabbitMQ → API-2 → PostgreSQL
                      ↓
                  API-1 (Consultas)
```

---

## 🛠️ Tecnologias Utilizadas

- **Java 21**
- **Quarkus 3.17.3** (Framework)
- **PostgreSQL 17** (Banco de dados)
- **RabbitMQ 3.13** (Message Broker)
- **Hibernate/Panache** (ORM)
- **REST Client** (Integração HTTP)
- **Docker** (Containerização)
- **Maven** (Gerenciamento de dependências)

---

## 📦 Pré-requisitos

Antes de executar o projeto, certifique-se de ter instalado:

- ✅ **Java 21** - [Download](https://www.oracle.com/java/technologies/downloads/#java21)
- ✅ **Maven 3.8+** - [Download](https://maven.apache.org/download.cgi)
- ✅ **Docker Desktop** - [Download](https://www.docker.com/products/docker-desktop/)
- ✅ **Git** - [Download](https://git-scm.com/downloads)

### Verificar instalações:
```bash
java -version    # Deve mostrar Java 21
mvn -version     # Deve mostrar Maven 3.8+
docker --version # Deve mostrar Docker
```

---

## 🚀 Como Executar

### 1️⃣ Clonar o repositório
```bash
git clone https://github.com/GabrielMatosRodrigues/CarrosFipe.git
cd CarrosFipe
```

### 2️⃣ Subir infraestrutura (PostgreSQL + RabbitMQ)
```bash
docker-compose up -d
```

**Aguarde ~30 segundos** para os serviços iniciarem completamente.

### 3️⃣ Executar a API-1

Em um terminal:
```bash
cd fipe-api-1
mvn quarkus:dev
```

Aguarde até aparecer: `Listening on: http://localhost:8080`

### 4️⃣ Executar a API-2

Em **outro terminal**:
```bash
cd fipe-api-2
mvn quarkus:dev
```

Aguarde até aparecer: `Listening on: http://localhost:8081`

---

## 🧪 Testando a Aplicação

### 1️⃣ Acessar o Swagger da API-1

Abra no navegador: **http://localhost:8080/q/swagger-ui**

### 2️⃣ Executar a Carga Inicial

No Swagger:
1. Expanda **POST** `/api/veiculos/carga-inicial`
2. Clique em **Try it out**
3. Clique em **Execute**

**Resultado esperado:**
- API-1 publica 103 marcas na fila
- API-2 processa e salva ~6.000+ veículos no banco
- Processamento leva ~5 minutos

### 3️⃣ Consultar Marcas
```bash
GET http://localhost:8080/api/veiculos/marcas
```

### 4️⃣ Consultar Veículos por Marca
```bash
GET http://localhost:8080/api/veiculos/marca/Toyota
```

### 5️⃣ Atualizar um Veículo
```bash
PUT http://localhost:8080/api/veiculos/{id}
Content-Type: application/json

{
  "modelo": "Corolla 2.0 XEI",
  "observacoes": "Veículo em excelente estado"
}
```

---

## 📊 Acessar Serviços

| Serviço | URL | Credenciais |
|---------|-----|-------------|
| **Swagger API-1** | http://localhost:8080/q/swagger-ui | - |
| **RabbitMQ Management** | http://localhost:15672 | admin / admin123 |
| **PostgreSQL** | localhost:5432 | fipe_user / fipe123 |

---

## 🗂️ Estrutura do Projeto
```
CarrosFipe/
├── fipe-api-1/                 # Microserviço 1
│   ├── src/main/java/com/fipe/
│   │   ├── domain/
│   │   │   └── entities/       # Entidades (Veiculo)
│   │   ├── application/
│   │   │   └── usecases/       # Casos de uso
│   │   ├── infrastructure/
│   │   │   ├── http/           # Cliente REST FIPE
│   │   │   ├── messaging/      # Publisher RabbitMQ
│   │   │   └── persistence/    # Repositórios
│   │   └── presentation/
│   │       └── controllers/    # Controllers REST
│   └── src/main/resources/
│       └── application.properties
│
├── fipe-api-2/                 # Microserviço 2
│   ├── src/main/java/com/fipe/
│   │   ├── domain/
│   │   │   └── entities/       # Entidades (Veiculo)
│   │   ├── application/
│   │   │   └── usecases/       # Casos de uso
│   │   └── infrastructure/
│   │       ├── http/           # Cliente REST FIPE
│   │       ├── messaging/      # Consumer RabbitMQ
│   │       └── persistence/    # Repositórios
│   └── src/main/resources/
│       └── application.properties
│
├── docker-compose.yml          # Infraestrutura
└── README.md                   # Este arquivo
```

---

## 🧩 Padrões e Boas Práticas Aplicadas

### Arquitetura
- ✅ **Clean Architecture** - Separação em camadas (Domain, Application, Infrastructure, Presentation)
- ✅ **DDD** - Modelagem orientada ao domínio
- ✅ **Microserviços** - Separação de responsabilidades

### Design Patterns
- ✅ **Repository Pattern** - Abstração de acesso a dados
- ✅ **Use Case Pattern** - Lógica de negócio isolada
- ✅ **REST Client Pattern** - Integração com APIs externas

### SOLID
- ✅ **Single Responsibility** - Cada classe tem uma única responsabilidade
- ✅ **Dependency Inversion** - Dependência de abstrações, não de implementações

### API
- ✅ **RESTful** - Endpoints seguindo padrões REST
- ✅ **OpenAPI/Swagger** - Documentação automática
- ✅ **HTTP Status corretos** - Uso adequado de códigos de status

---

## 🐛 Troubleshooting

### Porta já em uso

Se aparecer erro de porta ocupada:
```bash
# Verificar processos usando a porta
netstat -ano | findstr :8080
netstat -ano | findstr :8081

# Matar processo (substitua <PID>)
taskkill /PID <PID> /F
```

### Docker não está rodando
```bash
# Verificar status
docker ps

# Reiniciar Docker Desktop
# Fechar e abrir novamente
```

### Banco de dados não conecta
```bash
# Verificar se PostgreSQL está rodando
docker ps | findstr postgres

# Ver logs
docker-compose logs postgres
```

### RabbitMQ não conecta
```bash
# Verificar se RabbitMQ está rodando
docker ps | findstr rabbitmq

# Ver logs
docker-compose logs rabbitmq
```

---

## 📝 Licença

Este projeto foi desenvolvido para fins educacionais e de avaliação técnica.

---

## 👤 Autor

**Gabriel Matos Rodrigues**

- GitHub: [@GabrielMatosRodrigues](https://github.com/GabrielMatosRodrigues)

---

## 📞 Contato

Para dúvidas ou sugestões sobre este projeto, entre em contato através do GitHub.

---

⭐ **Se este projeto foi útil, considere dar uma estrela no repositório!**