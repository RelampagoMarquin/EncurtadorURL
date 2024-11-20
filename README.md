# EncurtadorURL
Projeto para criar um encurtador de URL

## Tecnologias

- Java 17
- AWS
  - lambda (execução do codigo como serveless)
  - S3 (armazenamento)
  - Api gateway


## Criando o link encurtado

```
  https://lwpocooq43.execute-api.us-east-2.amazonaws.com/create
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
  https://lwpocooq43.execute-api.us-east-2.amazonaws.com/{idrecebidonarequicicao}
```

