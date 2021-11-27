package br.com.cardoso.upload.controller;

import br.com.cardoso.upload.service.UploadService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
public class FileUploadController {

    private final UploadService uploadService;

    public FileUploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @GetMapping("/sendOk")
    public String sendOk() throws IOException {
        return uploadService.uploadOk();
    }

    @GetMapping("/sendJersey")
    public String sendJersey() throws IOException {
        return uploadService.uploadJersey();
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        Path tempFile = Files.createTempFile("tmpFile", "." + StringUtils.getFilenameExtension(file.getOriginalFilename()));
        file.transferTo(tempFile);
        return "Arquivo salvo em: " + tempFile.toAbsolutePath();
    }
}
