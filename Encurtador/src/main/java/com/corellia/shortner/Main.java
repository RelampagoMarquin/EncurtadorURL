package com.corellia.shortner;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Main implements RequestHandler<Map<String, Object>, Map<String, String>>{
    
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, String> handleRequest(Map<String, Object> input, Context context) {

        final ObjectMapper objectMapper = new ObjectMapper();

        String body = input.get("body").toString();

        Map<String, String> bodyMap;
        try {
            bodyMap = objectMapper.readValue(body, Map.class);
        } catch (Exception error) {
            throw new RuntimeException("Error parsing JSON body: " + error.getMessage(), error);
        }

        // manipular valores
        String urlOriginal = bodyMap.get("urlOriginal");

        if (urlOriginal == null) {
            throw new RuntimeJsonMappingException("Adicione uma URL para realizar o encurtamento");
        }

        String expirationTime = bodyMap.get("expirationTime");

        String shortUrlCode = UUID.randomUUID().toString().substring(0, 8);

        Map<String, String> response = new HashMap<>();

        response.put("code", shortUrlCode);

        return response;
    };

 
}
