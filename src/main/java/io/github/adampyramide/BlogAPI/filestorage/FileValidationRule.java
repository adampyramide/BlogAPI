package io.github.adampyramide.BlogAPI.filestorage;

import io.github.adampyramide.BlogAPI.error.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record FileValidationRule (
        List<String> allowedMimeTypes, long maxSizeBytes
) {
    public void validate(MultipartFile file) {
        String mimeType = file.getContentType();
        long size = file.getSize();

        if (mimeType == null || !allowedMimeTypes.contains(mimeType)) {
            throw new ApiException(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                    "FILE_TYPE_UNSUPPORTED",
                    "Unsupported file type: " + mimeType + ". Supported types: " + allowedMimeTypes
            );
        }

        if (size > maxSizeBytes) {
            throw new ApiException(
                    HttpStatus.PAYLOAD_TOO_LARGE,
                    "FILE_SIZE_EXCEEDED",
                    "Uploaded file is %d bytes, but maximum allowed is %d bytes."
                            .formatted(size, maxSizeBytes)
            );
        }

    }
}