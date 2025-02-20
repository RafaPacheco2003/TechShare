package com.techshare;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;

public class SubcategoryClient {

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        // URL del endpoint
        String url = "http://localhost:8080/admin/subcategory";

        // JSON que se enviará en el campo 'subcategory'
        String json = "{\"name\":\"hola\", \"category_id\":2}";

        // Crear el archivo de imagen (asegúrate de que el archivo esté en el lugar correcto)
        FileSystemResource image = new FileSystemResource(new File("C:\\\\Users\\\\rodri\\\\OneDrive\\\\Imágenes\\\\xs.jpeg"));

        // Configuración de las cabeceras
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Crear el cuerpo de la solicitud
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("subcategory", json);  // Enviar el JSON como un campo 'subcategory'
        body.add("image", image);       // Enviar el archivo de imagen

        // Crear la entidad HTTP con el cuerpo y las cabeceras
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);

        // Enviar la solicitud POST
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // Imprimir la respuesta
        System.out.println(response.getBody());
    }
}
