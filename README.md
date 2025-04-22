# 📥 ez-video-ingestion-ms

## 📌 Contextualização

O microsserviço `ez-video-ingestion-ms` é o ponto de entrada para o upload e gerenciamento de vídeos na plataforma **ez-frame**. Ele é responsável por autenticar usuários via AWS Cognito e processar uploads de vídeos, enviando-os para o bucket S3 (`ez-frame-video-storage`). Além disso, salva metadados no DynamoDB (`video_metadata`), envia mensagens para a fila SQS (`video-processing-queue`) com dados do vídeo que precisa ser processado pelo `ez-frame-generator-ms`.

O `ez-video-ingestion-ms` também contem as funcionalidades para consultar o status dos vídeos através do endpoint `http://host:8080/v1/ms/videos/get-video-status` e em caso de falhas, notifica o `ez-frame-notification-ms` via endpoint `http://host:8080/v1/ms/notification/send`.

---

## 🧩 Desenho de Arquitetura

![image](https://github.com/user-attachments/assets/da998aa9-deb2-48fc-9025-06d3e1dfb0d1)

---

## 🛡️ Políticas de Upload de Vídeos

O projeto foi estruturado com suporte à implementação de múltiplas **políticas configuráveis**, facilitando sua evolução para diferentes regras de negócio e, se necessário, a expansão para um serviço com diferentes planos e maior flexibilidade de regras. **Para esta entrega, definimos a implementação de apenas duas políticas**:

- `validateMaxFilesPerRequest`
- `validateTotalSizePerRequest`

Essas regras estão centralizadas na classe `VideoUploadPolicy` (pacote `br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.policy`), permitindo fácil manutenção e extensibilidade.

---

## 🧱 Componentes da Solução Global ez-frame

| **Componente** | **Finalidade** | **Justificativa** |
| --- | --- | --- |
| **Clean Architecture** | Organização interna da solução | Foi escolhida para garantir uma estrutura modular, de fácil manutenção e testes. Essa separação clara entre regras de negócio e infraestrutura facilita a escalabilidade da solução ao longo do tempo, conforme o sistema evolui. |
| **Java 21** | Linguagem principal para implementação | A linguagem Java foi adotada em substituição ao .NET por uma decisão estratégica, considerando a expertise da equipe com o ecossistema Java. Essa escolha visa otimizar o desenvolvimento, reduzir a curva de aprendizado e garantir eficiência na evolução e manutenção da solução. |
| **Apache Maven** | Gerenciamento de dependências e build | Ferramenta amplamente utilizada no ecossistema Java, facilita a organização do projeto, o versionamento de dependências e o processo de build e deploy. |
| **Amazon EKS** | Orquestração dos microsserviços da solução | Solução gerenciada baseada em Kubernetes, que facilita o deploy, a escalabilidade e o gerenciamento dos microsserviços (`generator`, `ingestion`, `notification`), mantendo a consistência da infraestrutura. |
| **Amazon SES** | Envio de e-mails de notificação em caso de erro | Atende ao requisito de notificação automática para o usuário em caso de falha no processamento. É um serviço simples, eficiente e com baixo custo, ideal para esse tipo de comunicação. |
| **GitHub Actions** | Automatização de build, testes e deploys | O GitHub Actions foi escolhido por estar amplamente consolidado no mercado e por oferecer uma integração direta com repositórios GitHub, simplificando pipelines de entrega contínua. Além disso, a equipe já possui familiaridade com a ferramenta, o que reduz tempo de configuração e acelera o processo de entrega contínua. |
| **Amazon Cognito**           | Autenticação e segurança no microsserviço de usuários                          | Solução gerenciada que facilita a implementação de autenticação com usuário e senha, atendendo ao requisito de proteger o sistema e controlando o acesso de forma segura e padronizada.                                                                                                               |
| **Amazon SQS**               | Gerenciamento da fila de processamento de vídeos                               | Utilizamos SQS para garantir que os vídeos sejam processados de forma assíncrona e segura, sem perda de requisições, mesmo em momentos de pico. Isso também ajuda a escalar o sistema com segurança.                                                                                                   |
| **DynamoDB**                 | Armazenamento dos metadados           | Optamos pelo DynamoDB por ser altamente escalável e disponível, atendendo bem à necessidade de processar múltiplos vídeos em paralelo. Seu modelo NoSQL permite evoluir a estrutura dos dados sem migrações complexas, o que é útil caso futuramente a solução precise armazenar também os vídeos.     |
| **Amazon S3** | Armazenamento de vídeos e arquivos ZIP gerados | O S3 foi adotado por ser um serviço de armazenamento de objetos altamente durável, escalável e econômico, perfeito para armazenar vídeos enviados pelos usuários e arquivos ZIP gerados pelo `ez-frame-generator-ms` (bucket `ez-frame-video-storage`). Permite o compartilhamento seguro dos arquivos gerados via presigned URLs e suporta vídeos grandes e múltiplos uploads com facilidade. |

---

## 🧩 Fluxo de Interação entre Serviços

O diagrama abaixo ilustra o fluxo do `ez-video-ingestion-ms` ***(em azul)*** e suas interações com outros componentes do sistema.

![image](https://github.com/user-attachments/assets/8081bc86-2c7a-4041-affb-ba3841e22d92)

---

## ✅ Pré-requisitos

- ☕ **Java 21 instalado**
- 📦 **Maven instalado**
- 🔐 **Credenciais AWS configuradas no repositório como GitHub Secrets**  
  - `AWS_ACCESS_KEY_ID`  
  - `AWS_SECRET_ACCESS_KEY`
- 🔐 **Credenciais do SonarQube configuradas no repositório como GitHub Secrets**  
  - `SONAR_TOKEN`
- 👤 **Criar UserPool e AppClient no Amazon Cognito**
- 📧 **Criar entity (e-mail verificado) no Amazon SES**
- 🛡️ **Criar usuário IAM com política SES para envio de e-mails**  
  - Permissões necessárias: `ses:SendEmail` e `ses:SendRawEmail`
  - Exemplo de **policy JSON** para colar na criação da política no IAM:
- 📄 Configurar as filas:
  - `video-processing-queue`
  - `video-processing-queue-dlq`

---

## ✅ Requisito para execução da solução

### 🚀 Criar ambiente e realizar deploy na seguinte ordem:
1. [Infra](https://github.com/ThaynaraDaSilva/ez-frame-infrastructure)
2. [Ingestion](https://github.com/ThaynaraDaSilva/ez-video-ingestion-ms)
3. [Generator](https://github.com/ThaynaraDaSilva/ez-frame-generator-ms)
4. [Notification](https://github.com/ThaynaraDaSilva/ez-frame-notification-ms)

---

## 🗂️ Estrutura de Diretórios do Projeto

A estrutura segue o padrão **Clean Architecture**:

```
ez-video-ingestion-ms/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── br/duosilva/tech/solutions/ez/frame/ingestion/ms/
│   │   │       ├── adapters/
│   │   │       │   ├── in/
│   │   │       │   │   └── controller/       # Controladores REST (upload, status)
│   │   │       │   └── out/
│   │   │       │       ├── storage/         # Integração com S3
│   │   │       │       ├── queue/           # Integração com SQS
│   │   │       │       └── database/        # Integração com DynamoDB
│   │   │       ├── application/
│   │   │       │   ├── dto/                 # DTOs
│   │   │       │   └── usecases/            # Casos de uso
│   │   │       ├── domain/
│   │   │       │   └── model/               # Modelos de domínio
│   │   │       └── config/                  # Configurações
│   │   └── resources/
│   │       └── application.yml              # Configurações do Spring Boot
├── pom.xml                                     # Arquivo Maven com dependências
└── README.md                                   # Documentação do projeto
```

---

## 📊 Modelagem do Banco de Dados

O `ez-video-ingestion-ms` utiliza o **DynamoDB** para armazenar metadados dos vídeos processados na tabela `video_metadata`. Estrutura da tabela:

- **Nome da Tabela**: `video_metadata`
- **Partition Key**: `videoId` (String, ex.: `vid123`)
- **Atributos**:
  - `filename`: Nome do arquivo processado (String, ex.: `video_processed.mp4`)
  - `status`: Status do processamento (String, ex.: `COMPLETED`, `FAILED`)
  - `errorMessage`: Mensagem de erro, se aplicável (String, ex.: `Erro no processamento`)
  - `timestamp`: Data/hora do processamento (String, ex.: `2025-04-19T10:10:00Z`)

---

## 🎥 Vídeos de apresentação

[📐 Desenho de Arquitetura](https://youtu.be/ry-GS9WqmaU)

[🔧 Github Rulesets, Pipelines e Sonarqube](https://youtu.be/jqO4ldizBwY)

[🔐 Jornada de Login e Upload de Vídeo](https://youtu.be/sk-AvQ9TnIw)

[📧 Jornada de Envio de Notificação](https://youtu.be/mE9PhuUo4Co)

[🖼️ Jornada de Geração de Frames](https://youtu.be/bfRUG1w-S8w)

---

## 👨‍💻 Desenvolvido por

@tchfer — RM357414  

@ThaynaraDaSilva — RM357418
