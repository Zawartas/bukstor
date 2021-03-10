package com.sztukakodu.bukstor.uploads.application.ports;

import com.sztukakodu.bukstor.uploads.domain.Upload;
import lombok.Value;

import java.util.Optional;

public interface UploadUseCase {

    Upload save(SaveUploadCommand command);

    Optional<Upload> getById(Long id);

    void removeById(Long id);

    @Value
    class SaveUploadCommand {
        String filename;
        byte[] file;
        String contentType;
    }
}
