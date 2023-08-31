package com.awslambda;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MyLambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final String BUCKET_NAME = "bucket-quarkus-interceptor";
    private static final String DYNAMO_TABLE_NAME = "Persona";

    private AmazonS3 s3Client;
    private AmazonDynamoDB dynamoDBClient;

    public MyLambdaHandler() {
        s3Client = AmazonS3ClientBuilder.defaultClient();
        dynamoDBClient = AmazonDynamoDBClientBuilder.defaultClient();
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(input.getBody());
            System.out.println("Datos recibidos: " + jsonNode.toString());

            String id = UUID.randomUUID().toString();
            String nombre = jsonNode.get("nombre").asText();
            String apellido = jsonNode.get("apellido").asText();
            String ci = jsonNode.get("ci").asText();
            String edad = jsonNode.get("edad").asText();
            String imagenBase64 = jsonNode.get("imagen").asText();

            byte[] imageBytes = Base64.getDecoder().decode(imagenBase64);

            String imageKey = "image-" + System.currentTimeMillis() + ".jpg";

            InputStream imageStream = new ByteArrayInputStream(imageBytes);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(imageBytes.length);

            s3Client.putObject(new PutObjectRequest(BUCKET_NAME, imageKey, imageStream, metadata));

            String imageUrl = "https://s3.amazonaws.com/" + BUCKET_NAME + "/" + imageKey;

            System.out.println("URL de la imageeeeen: " + imageUrl);

            AttributeValue idAttr = new AttributeValue().withS(id);
            AttributeValue nombreAttr = new AttributeValue().withS(nombre);
            AttributeValue apellidoAttr = new AttributeValue().withS(apellido);
            AttributeValue ciAttr = new AttributeValue().withS(ci);
            AttributeValue edadAttr = new AttributeValue().withS(edad);
            AttributeValue imagenAttr = new AttributeValue().withS(imageUrl);

            // Crea un mapa de atributos para el elemento en DynamoDB
            Map<String, AttributeValue> item = new HashMap<>();
            item.put("id", idAttr);
            item.put("nombre", nombreAttr);
            item.put("apellido", apellidoAttr);
            item.put("ci", ciAttr);
            item.put("edad", edadAttr);
            item.put("imagen", imagenAttr);
            System.out.println(item);

            // Crea la solicitud de inserción y guárdala en DynamoDB
            System.out.println("insertando en dynamo");
            PutItemRequest putItemRequest = new PutItemRequest(DYNAMO_TABLE_NAME, item);
            System.out.println("creando obj");
            dynamoDBClient.putItem(putItemRequest);
            System.out.println("objcreado");
            response.setStatusCode(200);
            response.setBody("Datos procesados correctamente");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setBody("Error en el procesamiento: " + e.getMessage());
        }

        return response;
    }
}
