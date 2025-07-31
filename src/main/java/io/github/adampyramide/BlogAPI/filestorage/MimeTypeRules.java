package io.github.adampyramide.BlogAPI.filestorage;

import java.util.List;

public class MimeTypeRules {

    public static final FileValidationRule IMAGE_ONLY = new FileValidationRule(
            List.of("image/jpeg", "image/png", "image/gif", "image/webp"),
            2 * 1024 * 1024 // 2MB
    );

    public static final FileValidationRule VIDEO_ONLY = new FileValidationRule(
            List.of("video/mp4", "video/webm", "video/ogg"),
            20 * 1024 * 1024 // 20MB
    );

    public static final FileValidationRule AUDIO_ONLY = new FileValidationRule(
            List.of("audio/mpeg", "audio/wav", "audio/ogg"),
            10 * 1024 * 1024 // 10MB
    );

}
