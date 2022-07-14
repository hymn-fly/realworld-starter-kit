package org.realworld.demo.controller;

import org.realworld.demo.controller.dto.ArticleDto.ArticleCreateRequest;
import org.realworld.demo.controller.dto.ArticleDto.ArticleResponse;
import org.realworld.demo.controller.dto.ArticleDto.ArticleUpdateRequest;
import org.realworld.demo.domain.article.entity.Article;
import org.realworld.demo.domain.user.entity.User;
import org.realworld.demo.domain.article.service.ArticleService;
import org.realworld.demo.domain.follow.service.FollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;

    private final FollowService followService;

    public ArticleController(ArticleService articleService, FollowService followService) {
        this.articleService = articleService;
        this.followService = followService;
    }

    @GetMapping("/{slug}")
    public ArticleResponse getArticle(@PathVariable String slug){
        Article article = articleService.getArticle(slug);

        boolean following = followService.checkFollowing(null, article.getAuthor());

        return new ArticleResponse(article, following);
    }

    @PostMapping
    public ArticleResponse createArticle(@RequestBody ArticleCreateRequest request, @AuthenticationPrincipal Object principal){
        User loginUser = (User) principal;

        Article article = articleService.createArticle(loginUser, request.getTitle(), request.getDescription(), request.getBody(), request.getTags());

        boolean following = followService.checkFollowing(loginUser, article.getAuthor());

        return new ArticleResponse(article, following);
    }

    @PutMapping("/{slug}")
    public ArticleResponse updateArticle(
            @PathVariable String slug,
            @RequestBody ArticleUpdateRequest request,
            @AuthenticationPrincipal Object principal
    ){
        User loginUser = (User) principal;

        Article updatedArticle = articleService.updateArticle(slug, request.getTitle(), request.getDescription(), request.getBody());

        boolean following = followService.checkFollowing(loginUser, updatedArticle.getAuthor());

        return new ArticleResponse(updatedArticle, following);
    }

    @DeleteMapping("/{slug}")
    public ResponseEntity<Object> deleteArticle(@PathVariable String slug){
        articleService.deleteArticle(slug);

        return ResponseEntity.ok().build();
    }

}
