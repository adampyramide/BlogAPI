package io.github.adampyramide.BlogAPI.filestorage;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.github.adampyramide.BlogAPI.error.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryFileStorageService implements FileStorageService {

    private final Cloudinary cloudinary;
    private final CloudinaryProperties properties;

    @Override
    public String getUrl(String publicId, FileValidationRule validationRule) {
        if (publicId == null || publicId.isBlank()) {
            return null;
        }

        return "https://res.cloudinary.com/" + properties.getCloudName() + "/" + validationRule.fileCategory() + "/upload/" + publicId;
    }

    @Override
    public FileUploadResult save(MultipartFile file, FileValidationRule validationRule, String folder, String key) {
        validationRule.validate(file);

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> uploadOptions = ObjectUtils.asMap(
                    "folder", folder
            );

            if (key != null && !key.isBlank()) {
                uploadOptions.put("public_id", key);
                uploadOptions.put("overwrite", true);
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    uploadOptions
            );

            return new FileUploadResult(
                    (String) uploadResult.get("public_id"),
                    (String) uploadResult.get("secure_url")
            );
        } catch (IOException e) {
            throw new ApiException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "CLOUD_UPLOAD_FAILED",
                    "Failed to upload file to cloudinary."
            );
        }
    }

    @Override
    public FileUploadResult save(MultipartFile file, FileValidationRule validationRule, String folder) {
        return save(file, validationRule, folder, null);
    }

    @Override
    public void delete(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, Map.of());
        } catch (IOException e) {
            throw new ApiException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "CLOUD_DELETE_FAILED",
                    "Failed to delete file from cloudinary."
            );
        }
    }

}
