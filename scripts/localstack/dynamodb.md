# AWS DynamoDB


```
create dynamodb

aws dynamodb create-table --endpoint-url http://localhost:4566 --table-name video_metadata --attribute-definitions AttributeName=userId,AttributeType=S AttributeName=id,AttributeType=S --key-schema AttributeName=userId,KeyType=HASH AttributeName=id,KeyType=RANGE --billing-mode PAY_PER_REQUEST --region us-east-1



```

```

list tables

aws dynamodb list-tables --endpoint-url http://localhost:4566 --region us-east-1

```

