package br.com.erudio.controllers;

import br.com.erudio.data.vo.v1.UploadFileResponseVO;
import br.com.erudio.services.FileStorageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Tag(name = "File Endpoint")
@RestController
@RequestMapping("/api/file/v1")
public class FileController {

    @Autowired
    private FileStorageService service;

    @PostMapping("/uploadFile")
    public UploadFileResponseVO uploadFile(@RequestParam("file")  MultipartFile file) {
        log.info("Storing file to disk");

        var filename = service.storeFile(file);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/file/v1/downloadFile/")
                .path(filename)
                .toUriString();

        return new UploadFileResponseVO(filename, fileDownloadUri, file.getContentType(), file.getSize());
    }

    @PostMapping("/uploadMultipleFiles")
    public List<UploadFileResponseVO> uploadMultipleFiles(@RequestParam("files")  MultipartFile[] files) {
        log.info("Storing files to disk");

        return Arrays.stream(files)
                .map(this::uploadFile)
                .collect(Collectors.toList());
    }


    @GetMapping("/downloadFile/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename, HttpServletRequest request) {
        log.info("Reading a file on disk");

        Resource resource = service.loadFileAsResource(filename);
        String contentType = "";

        try{
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        }catch (Exception e){
            log.info("Could not determine file type!");
        }

        if(contentType.isBlank()) contentType = "application/octet-stream";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}
