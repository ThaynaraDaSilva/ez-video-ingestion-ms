# 📥 Ingestion Service (`ez-frame-ingestion-ms`)

## 📌 Contextualização

O **Ingestion Service** é um microserviço da solução `ez-frame`, responsável por gerenciar o **upload de vídeos** por usuários autenticados.

- Ele **salva vídeos** no Amazon S3 e **armazena metadados** no DynamoDB (tabela `Videos`).
- **Enfileira tarefas** de processamento no Amazon SQS.
- Oferece **endpoints** para consulta de status via Swagger.
- **Atualiza o status** dos vídeos com base em chamadas do `Generator Service` e, se o status for `"FAILED"`, chama o `Notification Service` para enviar e-mails.

---

## 🧩 Desenho de Arquitetura

O diagrama abaixo representa o fluxo do `Ingestion Service` dentro da solução `ez-frame`, incluindo autenticação, upload, consulta de status, e notificação de falhas:

![image](https://github.com/user-attachments/assets/ce0767ec-63ca-4025-aa2e-f633623e44c8)

> Para visualizar o diagrama, cole o script abaixo in [PlantText](https://www.planttext.com/).

```
@startuml
!define RECTANGLE class
!includeurl https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Component.puml

Person(client, "Cliente", "Usuário que envia vídeos")
Container(cognito, "Cognito", "Autenticação de usuários")
Container(apiGateway, "API Gateway", "Valida tokens e expõe endpoints")
Container(ingestionService, "Ingestion Service", "Spring Boot (Java 21)", "Recebe vídeos, enfileira tarefas e atualiza status")
Container(sqs, "SQS", "Fila de tarefas")
Container(generatorService, "Generator Service", "Spring Boot (Java 21)", "Processa vídeos")
Container(notificationService, "Notification Service", "Spring Boot (Java 21)", "Envia e-mails")
Container(dynamodb, "DynamoDB", "Armazena metadados")
Container(videosTable, "Videos", "Tabela", "Metadados de vídeos")
Container(s3, "S3", "Armazena vídeos")
Container(ses, "SES", "Envia e-mails")

' Relacionamentos
dynamodb --> videosTable
client --> ingestionService : "1. Acessa endpoint de upload"
ingestionService --> cognito : "2. Redireciona para login"
cognito --> client : "3. Usuário faz login"
cognito --> apiGateway : "4. Envia dados de login para autenticação"
apiGateway --> ingestionService : "5. Redireciona com permissões"
ingestionService --> s3 : "6. Salva vídeo"
ingestionService --> videosTable : "7. Salva metadados (Videos)"
ingestionService --> sqs : "8. Enfileira tarefa"
generatorService --> ingestionService : "9. Atualiza status (POST /update-status)"
ingestionService --> notificationService : "10. Chama endpoint (falhas, HTTP)"
notificationService --> ses : "11. Envia e-mail (falha)"
ingestionService --> videosTable : "12. Atualiza status (Videos)"
client --> ingestionService : "13. Consulta status via Swagger"
ingestionService --> videosTable : "14. Consulta metadados (Videos)"
ingestionService --> client : "15. Retorna status do vídeo"

' Estilização
skinparam monochrome true
skinparam shadowing false
skinparam backgroundColor #FFFFFF

@enduml
```

---

## ✅ Pré-requisitos

- ☕ Java 21 instalado  
- 📦 Maven instalado  
- 🔐 Credenciais AWS configuradas (`AWS CLI` ou arquivo `~/.aws/credentials`)  
- 🌐 Acesso a serviços AWS (S3, SQS, DynamoDB, Cognito) com permissões adequadas  
- 📧 Endereço de e-mail verificado no Cognito para autenticação (ex.: `seu-email@dominio.com`)

---

## 📏 Limites Definidos com Relação a Upload de Vídeos

Com base na política de upload implementada (classe `VideoUploadPolicyService`):
- **Tamanho Máximo por Arquivo**: 100 MB por vídeo  
- **Limite Diário de Uploads por Usuário**: 10 vídeos por dia  
- **Número Máximo de Arquivos por Requisição**: 5 vídeos por requisição  
- **Tamanho Total por Requisição**: 300 MB no total por requisição  

Esses limites garantem o uso eficiente dos recursos e evitam abusos no sistema.

---

## 🗂️ Estrutura de Diretórios do Projeto

A estrutura segue o padrão **Clean Architecture**:

```
ez-frame-ingestion-ms/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── br/duosilva/tech/solutions/ez/frame/ingestion/ms/
│   │   │       ├── adapters/
│   │   │       │   ├── in/
│   │   │       │   │   └── controller/       # Controladores REST (ex.: VideoIngestionController)
│   │   │       │   └── out/
│   │   │       │       └── repository/       # Repositórios (ex.: VideoMetadataRepository)
│   │   │       ├── application/
│   │   │       │   ├── dto/                 # DTOs (ex.: VideoStatusResponse)
│   │   │       │   └── usecases/            # Casos de uso (ex.: ListVideoStatusUseCase)
│   │   │       ├── domain/
│   │   │       │   └── model/               # Modelos de domínio (ex.: VideoMetadata)
│   │   │       └── config/                  # Configurações (ex.: SecurityConfig)
│   │   └── resources/
│   │       └── application.yml              # Configurações do Spring Boot
├── pom.xml                                     # Arquivo Maven com dependências
└── README.md                                   # Documentação do projeto
```

---

## 📊 Modelagem do Banco de Dados

O `Ingestion Service` utiliza o **DynamoDB** para armazenar metadados dos vídeos na tabela `Videos`. Estrutura da tabela:

- **Nome da Tabela**: `Videos`  
- **Partition Key**: `videoId` (String, ex.: `vid123`)  
- **Atributos**:  
  - `filename`: Nome do arquivo (String, ex.: `video.mp4`)  
  - `email`: E-mail do usuário que fez o upload (String, ex.: `seu-email@dominio.com`)  
  - `status`: Status do vídeo (String, ex.: `UPLOADED`, `PROCESSING`, `COMPLETED`, `FAILED`)  
  - `errorMessage`: Mensagem de erro, se aplicável (String, ex.: `Erro no processamento`)  
  - `timestamp`: Data/hora do upload (String, ex.: `2025-04-19T10:00:00Z`)  
- **Índice Secundário Global (GSI)**:  
  - Nome: `EmailIndex`  
  - Partition Key: `email`  
  - Sort Key: `timestamp` (para ordenar vídeos por data de upload)

---

## 🛠️ Como Compilar o Projeto

### 1️⃣ Clone o repositório

```bash
git clone https://github.com/ThaynaraDaSilva/ez-frame-ingestion-ms.git
cd ez-frame-ingestion-ms
```

### 2️⃣ Configure o arquivo `application.yml`

```yaml
aws:
  region: us-east-1
  s3:
    bucket: <NOME_DO_BUCKET>
  sqs:
    queue-url: <URL_DA_FILA_SQS>
  dynamodb:
    table-name: Videos
notification-service:
  url: http://notification-service:8080/send
springdoc:
  swagger-ui:
    path: /swagger-ui.html
```

### 3️⃣ Compile e execute o projeto

```bash
mvn clean install
mvn spring-boot:run
```

---

📡 Teste os Endpoints HTTP

```http
POST /upload
```
📤 Exemplo de Payload

```json
{
  "filename": "video.mp4",
  "email": "seu-email@dominio.com"
}
```
🧪 Teste com cURL

```bash
curl -X POST http://localhost:8080/upload \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <SEU_TOKEN_JWT>" \
  -d '{"filename":"video.mp4","email":"seu-email@dominio.com"}'
```

```http
GET /videos/status
```
🧪 Teste com cURL

```bash
curl -X GET http://localhost:8080/videos/status \
  -H "Authorization: Bearer <SEU_TOKEN_JWT>"
```

```http
POST /update-status
```
📤 Exemplo de Payload

```json
{
  "videoId": "123",
  "status": "FAILED",
  "errorMessage": "Erro no processamento"
}
```
🧪 Teste com cURL

```bash
curl -X POST http://localhost:8080/update-status \
  -H "Content-Type: application/json" \
  -d '{"videoId":"123","status":"FAILED","errorMessage":"Erro no processamento"}'
```

Ou utilize ferramentas como **Swagger UI** (`http://localhost:8080/swagger-ui.html`).

---

## 🧱 Componentes da Solução Global ez-frame

| **Componente**               | **Finalidade**                                                                 | **Justificativa**                                                                                                                                                                                                                                                                                      |
|------------------------------|--------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Clean Architecture**       | Organização interna da solução                                                 | Foi escolhida para garantir uma estrutura modular, de fácil manutenção e testes. Essa separação clara entre regras de negócio e infraestrutura facilita a escalabilidade da solução ao longo do tempo, conforme o sistema evolui.                                                                     |
| **Java 21**                  | Linguagem principal para implementação                                          | A linguagem Java foi adotada em substituição ao .NET por uma decisão estratégica, considerando a expertise da equipe com o ecossistema Java. Essa escolha visa otimizar o desenvolvimento, reduzir a curva de aprendizado e garantir eficiência na evolução e manutenção da solução.                   |
| **DynamoDB**                 | Armazenamento dos metadados dos vídeos                                         | Optamos pelo DynamoDB por ser altamente escalável e disponível, atendendo bem à necessidade de processar múltiplos vídeos em paralelo. Seu modelo NoSQL permite evoluir a estrutura dos dados sem migrações complexas, o que é útil caso futuramente a solução precise armazenar também os vídeos.     |
| **Apache Maven**             | Gerenciamento de dependências e build                                          | Ferramenta amplamente utilizada no ecossistema Java, facilita a organização do projeto, o versionamento de dependências e o processo de build e deploy.                                                                                                                                                |
| **Amazon Cognito**           | Autenticação e segurança no microsserviço                                      | Solução gerenciada que facilita a implementação de autenticação com usuário e senha, atendendo ao requisito de proteger o sistema e controlando o acesso de forma segura e padronizada.                                                                                                               |
| **Amazon SQS**               | Gerenciamento da fila de processamento de vídeos                               | Utilizamos SQS para garantir que os vídeos sejam processados de forma assíncrona e segura, sem perda de requisições, mesmo em momentos de pico. Isso também ajuda a escalar o sistema com segurança.                                                                                                   |
| **Amazon EKS**               | Orquestração dos microsserviços da solução                                     | Solução gerenciada baseada em Kubernetes, que facilita o deploy, a escalabilidade e o gerenciamento dos microsserviços (`generator`, `ingestion`, `notification`), mantendo a consistência da infraestrutura.                                                                                         |
| **GitHub Actions**           | Automatização de build, testes e deploys                                       | O GitHub Actions foi escolhido por estar amplamente consolidado no mercado e por oferecer uma integração direta com repositórios GitHub, simplificando pipelines de entrega contínua. Além disso, a equipe já possui familiaridade com a ferramenta, o que reduz tempo de configuração e acelera o processo de entrega contínua. |

---

## 🔗 Demais Projetos Relacionados

**ez-frame-generator-ms** — Microserviço que escuta a fila SQS para processar vídeos e atualiza o status no `Ingestion Service`.  

**ez-frame-notification-ms** — Microserviço que envia notificações por e-mail em caso de falha no processamento, chamado pelo `Ingestion Service`.

---

## 👨‍💻 Desenvolvido por

@tchfer — RM357414  

@ThaynaraDaSilva — RM357418
