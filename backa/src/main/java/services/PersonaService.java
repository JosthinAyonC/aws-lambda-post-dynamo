package services;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.enterprise.context.ApplicationScoped;

import models.Persona;

@ApplicationScoped
public class PersonaService {

    public void enviarDatosALambda(Persona Persona) {
        try {
            // Construye la URL del endpoint de tu API Gateway
            URL url = new URL("https://in9xho0nci.execute-api.us-east-1.amazonaws.com/dev/persona");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Construye los datos que deseas enviar a la función Lambda
            String payload = "{\"nombre\": \"" + Persona.getNombre() + "\", \"apellido\": \"" + Persona.getApellido()
                    + "\" , \"ci\": \"" + Persona.getCi() + "\", \"edad\": \"" + Persona.getEdad()
                    + "\", \"imagen\": \"" + Persona.getImagen() + "\"}";

            // Establece la propiedad Content-Length
            byte[] input = payload.getBytes(StandardCharsets.UTF_8);
            connection.setRequestProperty("Content-Length", String.valueOf(input.length));

            // Obtiene el flujo de salida y escribe en él
            try (OutputStream os = connection.getOutputStream()) {
                os.write(input);
            }

            // Obtiene la respuesta del servidor
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Solicitud POST enviada exitosamente al API Gateway");
            } else {
                System.out.println("Error al enviar la solicitud POST al API Gateway");
            }

            connection.disconnect();
        } catch (Exception e) {
            System.out.println("Error en la solicitud:" + e.getMessage());
        }
    }
}
