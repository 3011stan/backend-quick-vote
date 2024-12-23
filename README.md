# backend-quick-vote

Backend para gestão de pautas e votação em assembleias de cooperativas.

## Tecnologias Utilizadas

- **Java 21**
- **Spring Boot**
- **Gradle**
- **Mongo DB**
- **Mockito** (para testes unitários)
- **ExecutorService** (para processamento assíncrono)

## Funcionalidades

- **Cadastro de Pautas:** Registre novas pautas para assembleias.
- **Gestão de Sessões de Votacão:** Crie e gerencie sessões associadas a pautas.
- **Registro de Votos:** Permite aos associados registrar seus votos em sessões abertas.
- **Validação de Votos:** Impede votos duplicados e verifica a validade da sessão.
- **Persistência de Dados:** Gerencia votos e sessões utilizando o repositório.

## Como Executar

### 1. Requisitos
- **JDK 21** ou superior
- **Gradle**

### 2. Clonar o Repositório
```bash
git clone https://github.com/3011stan/backend-quick-vote.git
cd backend-quick-vote
```

### 3. Rodar a Aplicação
#### Ambiente de Desenvolvimento
```bash
./gradlew bootRun
```

#### Compilando para Produção
```bash
./gradlew clean build
java -jar build/libs/backend-quick-vote-0.0.1-SNAPSHOT.jar
```

### 4. Endpoints Disponíveis
- **Base URL:** `http://localhost:8080`

| Método | Endpoint                                 | Descrição                          |
|---------|-----------------------------------------|--------------------------------------|
| POST    | `/voting-sessions`                      | Cria uma nova sessão de votação     |
| GET     | `/voting-sessions/{id}`                 | Retorna os detalhes de uma sessão    |
| POST    | `/voting-sessions/{id}/votes`           | Registra um voto na sessão          |

## Estrutura do Projeto

```plaintext
src/
├── main/
│   ├── java/com/stan/quick_vote/
│   │   ├── controller/    # Controladores REST
│   │   ├── service/       # Lógica de negócio
│   │   ├── repository/    # Integração com o banco de dados
│   │   └── model/         # Modelos de dados
│   │   └── config/        # Configurações da aplicação
│   │   └── dto/           # Data Transfer Objects
│   └── resources/
│       ├── application.yml  # Configurações da aplicação
└── test/                    # Testes unitários
```

## Testes

Para rodar os testes unitários, utilize o comando:
```bash
./gradlew test
```

Os testes verificam as principais funcionalidades do sistema, incluindo:
- Registro de votos
- Validações de sessões
- Persistência de dados

## Futuras Melhorias

- **Otimização do Registro de Votos:** Integração com mensageria (é.g., Kafka ou SQS).
- **Autenticação e Autorização:** Controle de acesso baseado em papéis.
- **Relatórios de Votação:** Geração automática de relatórios após a votação.

## Contribuição

Contribuições são bem-vindas! Siga os passos:
1. Faça um fork do repositório.
2. Crie um branch para sua feature/bugfix: `git checkout -b feature/nome-da-feature`.
3. Envie um pull request explicando suas mudanças.

## Licença

Este projeto está sob a licença [MIT](LICENSE).

