package com.ofss.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

@Schema(name = "ReuploadRequest", description = "Request for reuploading a single KYC document")
public class ReuploadRequest {

    @Schema(description = "File to be reuploaded", type = "string", format = "binary")
    private MultipartFile file;

    public MultipartFile getFile() {
        return file;
    }
    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
