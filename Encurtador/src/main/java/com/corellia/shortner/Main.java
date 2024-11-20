package com.corellia.shortner;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Main implements RequestHandler<Map<String, Object>, Map<String, String>>{
    
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, String> handleRequest(Map<String, Object> input, Context context) {


        final ObjectMapper objectMapper = new ObjectMapper();

        // instacia o S3 da amazon
        final S3Client s3Client = S3Client.builder().build();

        String body = input.get("body").toString();

        // Verifica se existe um Json na requisição
        Map<String, String> bodyMap;
        try {
            bodyMap = objectMapper.readValue(body, Map.class);
        } catch (Exception error) {
            throw new RuntimeException("Error parsing JSON body: " + error.getMessage(), error);
        }

        // manipular valores com if que retornam erro caso falte
        String urlOriginal = bodyMap.get("urlOriginal");

        if (urlOriginal == null) {
            throw new RuntimeJsonMappingException("Adicione uma URL para realizar o encurtamento");
        }

        String expirationTime = bodyMap.get("expirationTime");

        if (expirationTime == null) {
            throw new RuntimeJsonMappingException("Adicione o tempo de duração da url");
        }

        long expirationTimeInSec = Long.parseLong(expirationTime);
        String shortUrlCode = UUID.randomUUID().toString().substring(0, 8);

        UrlData urlData = new UrlData(urlOriginal, expirationTimeInSec);

        // Tenta fazer a conexão com o S3
        try {
            // transforma o Json em uma string
            String urlDataJson = objectMapper.writeValueAsString(urlData);

            // cria uma requisição para salvar na S3
            PutObjectRequest request = PutObjectRequest.builder()
                .bucket("shorter-url-bucket-katchau")
                .key(shortUrlCode + ".json")
                .build();
            
                s3Client.putObject(request, RequestBody.fromString(urlDataJson));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar dado no S3: " + e.getMessage());
        }

        Map<String, String> response = new HashMap<>();

        response.put("code", shortUrlCode);

        return response;
    };

 
}
