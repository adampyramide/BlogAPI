package io.github.adampyramide.BlogAPI.filestorage;

import jakarta.annotation.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService  {

    String save(MultipartFile file, String folder);
    void delete(String fileUrl);
    Resource load(String fileUrl);

}
