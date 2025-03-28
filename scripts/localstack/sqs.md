# AWS SQS

export AWS_DEFAULT_REGION=us-east-1

```
create queue

awslocal --endpoint-url=https://localhost.localstack.cloud:4566 sqs create-queue --queue-name video-processing-queue --region us-east-1 --attributes file://C:\THAYNARA_DEV\workspaces\ez-video-ingestion-ms\scripts\localstack\sqs-attributes.json


```

```
list queue

aws sqs list-queues --endpoint-url=http://localhost:4566 --region us-east-1

```

