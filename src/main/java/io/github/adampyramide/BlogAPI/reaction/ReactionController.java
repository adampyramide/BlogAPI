package io.github.adampyramide.BlogAPI.reaction;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blog-posts/{postId}/reactions")
public class ReactionController {

    private ReactionService service;

    public ReactionController(ReactionService service) {
        this.service = service;
    }

    @GetMapping
    public List<ReactionResponseDTO> getReactionsByPostId(@PathVariable Long postId, @RequestParam(required = false) ReactionType reactionType) {
        return service.getReactionsByPostId(postId, reactionType);
    }

    @PostMapping
    public void addReactionByPostId(@PathVariable Long postId, @RequestBody ReactionRequestDTO reactionDTO) {
        service.addReactionByPostId(postId, reactionDTO);
    }

    @DeleteMapping
    public void deleteReactionByPostId(@PathVariable Long postId) {
        service.deleteReactionByPostId(postId);
    }

}
