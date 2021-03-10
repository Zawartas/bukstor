package com.sztukakodu.bukstor.uploads.db;

import com.sztukakodu.bukstor.order.domain.Order;
import com.sztukakodu.bukstor.uploads.domain.Upload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadJpaRepository extends JpaRepository<Upload, Long> {

}
