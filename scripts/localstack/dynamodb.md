# AWS DynamoDB


```
create dynamodb

aws dynamodb create-table --endpoint-url http://localhost:4566 --table-name video_metadata --attribute-definitions AttributeName=userId,AttributeType=S AttributeName=id,AttributeType=S --key-schema AttributeName=userId,KeyType=HASH AttributeName=id,KeyType=RANGE --billing-mode PAY_PER_REQUEST --region us-east-1


aws dynamodb create-table --endpoint-url http://localhost:4566 --region us-east-1 --table-name video_metadata --attribute-definitions AttributeName=videoId,AttributeType=S --key-schema AttributeName=videoId,KeyType=HASH --billing-mode PAY_PER_REQUEST




```

```

list tables

aws dynamodb list-tables --endpoint-url http://localhost:4566 --region us-east-1

```

```

delete table

aws dynamodb delete-table --endpoint-url http://localhost:4566 --table-name video_metadata

```





