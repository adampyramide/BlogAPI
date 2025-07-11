package io.github.adampyramide.BlogAPI.comment;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    final CommentService service;

    public CommentController(CommentService service) {
        this.service = service;
    }



}
