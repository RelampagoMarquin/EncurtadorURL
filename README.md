# EncurtadorURL
Projeto para criar um encurtador de URL

## Tecnologias

- Java 17
- AWS
  - lambda (execução do codigo como serveless)
  - S3 (armazenamento)


## Link de acesso

```
  https://vqa7l3qtfgs6j65ouej7osbdj40lkhry.lambda-url.us-east-2.on.aws
```

obs: por enquanto só está funcionando via requisição post (user o postman, thunder client ou outro app para fazer a requisição)

### Exemplo de json

```
  {
    "urlOriginal": "https://github.com/RelampagoMarquin/EncurtadorURL/tree/main",
    "expirationTime": "172859200000"
  }
```


## link 'encurtado'

```
  https://2hzh2ssvvgz5m5tx4zvpfhmcva0qjbdr.lambda-url.us-east-2.on.aws/{idrecebidonarequicicao}
```

