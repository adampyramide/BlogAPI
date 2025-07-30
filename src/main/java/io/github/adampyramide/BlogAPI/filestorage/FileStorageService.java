package io.github.adampyramide.BlogAPI.filestorage;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService  {

    String save(MultipartFile file, String folder, FileValidationRule validationRule);
    void delete(String fileId);

}
