package ru.skypro.controller;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.exception.MessageException;
import ru.skypro.service.FilesService;
import ru.skypro.service.SocksService;

import java.io.*;

@RestController
@RequestMapping("/files")
public class FilesController {
    private final FilesService filesService;

    public SocksService sockService;


    public FilesController(FilesService filesService, SocksService sockService) {
        this.filesService = filesService;
        this.sockService = sockService;
    }

    @GetMapping(value = "/export/sock", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InputStreamResource> downloadSockFile() throws FileNotFoundException {
        File file = filesService.getFileSock();

        if (file.exists()) {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .contentLength(file.length())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment ; filename=\"Sock.json\"")
                    .body(resource);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping(value = "/import/sock", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadIngredientFile(@RequestParam MultipartFile file) throws FileNotFoundException, MessageException {
        filesService.cleanSocksFile();
        File sockFile = filesService.getFileSock();
        try (FileOutputStream fosSock = new FileOutputStream(sockFile)) {
            IOUtils.copy(file.getInputStream(), fosSock);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("/export/socks/txt")
    public ResponseEntity<InputStreamResource> downloadSockTxtFile() throws IOException {
        File file = sockService.createSocksTxtFile();
        InputStreamResource ior = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(file.length())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"socks.txt\"")
                .body(ior);
    }
}
