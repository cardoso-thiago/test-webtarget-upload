package br.com.cardoso.upload.service;

import okhttp3.*;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Service
public class UploadService {

    private static final String FILE_TO_UPLOAD = "/home/cardoso/Imagens/Wallpapers PC/ocean.jpeg";
    private static final String TARGET_URL = "http://localhost:8080/upload";

    public String uploadOk() throws IOException {
        File file = new File(FILE_TO_UPLOAD);
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), RequestBody.create(file, MediaType.parse("application/octet-stream")))
                .addFormDataPart("other_field", "other_field_value")
                .build();
        Request request = new Request.Builder().url(TARGET_URL).post(formBody).build();
        return Objects.requireNonNull(client.newCall(request).execute().body()).string();
    }

    public String uploadJersey() {
        Client client = ClientBuilder.newBuilder()
                .register(MultiPartFeature.class)
                .build();
        WebTarget webTarget = client.target(TARGET_URL);
        MultiPart multiPart = new MultiPart();
        multiPart.setMediaType(javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA_TYPE);

        FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("file", new File(FILE_TO_UPLOAD),
                javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM_TYPE);
        multiPart.bodyPart(fileDataBodyPart);

        javax.ws.rs.core.Response response = webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(multiPart, multiPart.getMediaType()));
        return response.readEntity(String.class);
    }
}
