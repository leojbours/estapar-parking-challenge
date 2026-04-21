# Estapar Parking Challenge

Sistema de gerenciamento de estacionamento desenvolvido como parte do desafio técnico da Estapar.

## Tecnologias

- Java 21
- Spring Boot
- Spring Data JPA
- MySQL
- Docker
- Docker Compose
- Testcontainers
- Lombok

## Pré-requisitos

- Java 21+
- Docker
- Maven

## Como rodar

### 1. Inicie o simulador

O simulador é responsável por fornecer a configuração da garagem e enviar eventos de veículos para o webhook.

```bash
docker run -d --net=host --platform linux/amd64 cfontes0estapar/garage-sim:1.0.0
```

### 2. Inicie a aplicação

```bash
./mvnw spring-boot:run
```

O banco de dados MySQL sobe automaticamente via Docker Compose ao iniciar a aplicação. Ao subir, a aplicação busca automaticamente a configuração da garagem no simulador (`GET /garage`) e armazena os dados de setores e vagas.

## Persistência de dados

Por padrão, os dados do banco são perdidos ao derrubar o container. Para persistir os dados entre reinicializações, descomente e configure o volume no `compose.yml`:

```yaml
services:
  dbservice:
    container_name: parkingdb
    image: mysql:latest
    ports:
      - "3306:3306"
    environment:
      MYSQL_DATABASE: parking
      MYSQL_ROOT_PASSWORD: root
    volumes:
      - /caminho/local/para/volume:/var/lib/mysql
```

## API

### Webhook

Recebe eventos do simulador.

**POST** `/webhook`

**ENTRY**
```json
{
  "license_plate": "ZUL0001",
  "entry_time": "2025-01-01T12:00:00.000Z",
  "event_type": "ENTRY"
}
```

**PARKED**
```json
{
  "license_plate": "ZUL0001",
  "lat": -23.561684,
  "lng": -46.655981,
  "event_type": "PARKED"
}
```

**EXIT**
```json
{
  "license_plate": "ZUL0001",
  "exit_time": "2025-01-01T12:00:00.000Z",
  "event_type": "EXIT"
}
```

### Receita

Retorna a receita total de um setor em uma data específica.

**GET** `/revenue`

Request:
```json
{
  "date": "2025-01-01",
  "sector": "A"
}
```

Response:
```json
{
  "amount": 0.00,
  "currency": "BRL",
  "timestamp": "2025-01-01T12:00:00.000Z"
}
```

## Regras de negócio

- Primeiros 30 minutos de estacionamento são gratuitos
- Após 30 minutos, a cobrança é por hora cheia (arredondada para cima)
- Quando a lotação atinge 100%, novas entradas são bloqueadas até a saída de um veículo

### Preço dinâmico

O preço por hora é ajustado com base na ocupação no momento da entrada:

| Ocupação | Ajuste |
|----------|--------|
| Até 25%  | -10%   |
| Até 50%  | 0%     |
| Até 75%  | +10%   |
| Até 100% | +25%   |

## Testes

```bash
./mvnw test
```

Os testes de repositório utilizam Testcontainers com MySQL real. Certifique-se de que o Docker esteja rodando antes de executar os testes.