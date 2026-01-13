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

/**
 * Service for handling file storage operations using Cloudinary.
 * <p>
 * Provides methods to save files, retrieve their URLs, and delete files from Cloudinary.
 * All operations enforce validation rules defined by {@link FileValidationRule} and wrap
 * Cloudinary-specific exceptions in {@link ApiException}.
 */
@Service
@RequiredArgsConstructor
public class CloudinaryFileStorageService implements FileStorageService {

    private final Cloudinary cloudinary;
    private final CloudinaryProperties properties;

    /**
     * Returns the public URL for a file stored in Cloudinary.
     *
     * @param publicId the Cloudinary public ID of the file
     * @param validationRule the rule describing the file category and validation
     * @return the full URL to access the file, or {@code null} if the publicId is null or blank
     */

    @Override
    public String getUrl(String publicId, FileValidationRule validationRule) {
        if (publicId == null || publicId.isBlank()) {
            return null;
        }

        return "https://res.cloudinary.com/" + properties.getCloudName() + "/" + validationRule.fileCategory() + "/upload/" + publicId;
    }

    /**
     * Uploads a file to Cloudinary to the specified folder and key (public ID).
     *
     * @param file the file to upload
     * @param validationRule the rule to validate the file type
     * @param folder the Cloudinary folder where the file will be stored
     * @param key the public ID for the file; if {@code null}, Cloudinary auto-generates one
     * @return a {@link FileUploadResult} containing the public ID and secure URL
     * @throws ApiException if the upload fails
     */
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

    /**
     * Uploads a file to Cloudinary to the specified folder, letting Cloudinary assign the public ID.
     *
     * @param file the file to upload
     * @param validationRule the rule to validate the file type
     * @param folder the Cloudinary folder where the file will be stored
     * @return a {@link FileUploadResult} containing the public ID and secure URL
     * @throws ApiException if the upload fails
     */
    @Override
    public FileUploadResult save(MultipartFile file, FileValidationRule validationRule, String folder) {
        return save(file, validationRule, folder, null);
    }

    /**
     * Deletes a file from Cloudinary by its public ID.
     *
     * @param publicId the public ID of the file to delete
     * @throws ApiException if the deletion fails
     */
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
