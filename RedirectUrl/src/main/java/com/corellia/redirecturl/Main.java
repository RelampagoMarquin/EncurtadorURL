package com.corellia.redirecturl;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author marco
 */
public class Main implements RequestHandler<Map<String, Object>, Map<String, Object>>{

    final ObjectMapper objectMapper = new ObjectMapper();

    final S3Client s3Client = S3Client.builder().build();

    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
        
        String pathParameters = (String) input.get("rawPath");
        String shortUrlCode = pathParameters.replace("/", "");

        if (shortUrlCode == null){
            throw new IllegalArgumentException("Input invalido: 'shortUrlCode' é necessario");
        }

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
            .bucket("shorter-url-bucket-katchau")
            .key(shortUrlCode + ".json")
            .build();

        InputStream s3ObjectStream;

        // tenta buscar o dado na S3
        try {
            s3ObjectStream = s3Client.getObject(getObjectRequest);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao recuperar dado do S3: " + e.getMessage(), e);
        }
        
        // Desserializa o objeto
        UrlData urlData;
        try {
            urlData = objectMapper.readValue(s3ObjectStream, UrlData.class);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao desserializar URL data: " + e.getMessage(), e);
        }

        
        long currentTimeInSec = System.currentTimeMillis()/1000;
        Map<String, Object> res = new HashMap<>();

        // Verifica o tempo de expiração
        if (currentTimeInSec > urlData.getExpirationTime()) {
            res.put("statusCode", 410);
            res.put("body", "URL expirou");
            return res;
        }

        // Url valida
        res.put("statusCode", 302);

        Map<String, String> headers = new HashMap<>();
        headers.put("location", urlData.getOriginalUrl());
        res.put("headers", headers);

        return res;
    }
    
}