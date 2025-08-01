package io.github.adampyramide.BlogAPI.filestorage;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService  {

    String getUrl(String fileId, FileValidationRule validationRule);

    FileUploadResult save(MultipartFile file, FileValidationRule validationRule, String folder);
    FileUploadResult save(MultipartFile file, FileValidationRule validationRule, String folder, String key);

    void delete(String fileId);

}
