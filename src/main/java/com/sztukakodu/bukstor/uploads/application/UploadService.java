package com.sztukakodu.bukstor.uploads.application;

import com.sztukakodu.bukstor.uploads.application.ports.UploadUseCase;
import com.sztukakodu.bukstor.uploads.db.UploadJpaRepository;
import com.sztukakodu.bukstor.uploads.domain.Upload;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
public class UploadService implements UploadUseCase {

    private final UploadJpaRepository repository;

    @Override
    public Upload save(SaveUploadCommand command) {
//        final Long id = Long.parseLong(RandomStringUtils.randomAlphanumeric(8).toLowerCase());
        Upload upload = new Upload(
                command.getFilename(),
                command.getContentType(),
                command.getFile()
        );
        repository.save(upload);
        System.out.println("Upload saved: " + upload.getFilename() + ", id: " + upload.getId());
        return upload;
    }

    @Override
    public Optional<Upload> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void removeById(Long id) {
        repository.deleteById(id);
    }
}
