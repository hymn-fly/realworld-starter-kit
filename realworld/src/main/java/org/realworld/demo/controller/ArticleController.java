package org.realworld.demo.controller;

import lombok.RequiredArgsConstructor;
import org.realworld.demo.controller.dto.ArticleDto.ArticleCreateRequest;
import org.realworld.demo.controller.dto.ArticleDto.ArticleResponse;
import org.realworld.demo.controller.dto.ArticleDto.ArticleUpdateRequest;
import org.realworld.demo.domain.article.entity.Article;
import org.realworld.demo.domain.article.service.ArticleService;
import org.realworld.demo.domain.follow.service.FollowService;
import org.realworld.demo.domain.user.entity.User;
import org.realworld.demo.domain.user.service.UserService;
import org.realworld.demo.jwt.JwtPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

  private final ArticleService articleService;

  private final FollowService followService;

  private final UserService userService;

  @GetMapping("/{slug}")
  public ArticleResponse getArticle(@PathVariable String slug) {
    Article article = articleService.getArticle(slug);

    boolean following = followService.checkFollowing(null, article.getAuthor());

    return new ArticleResponse(article, following);
  }

  @PostMapping
  public ArticleResponse createArticle(@RequestBody ArticleCreateRequest request,
      @AuthenticationPrincipal Object principal) {
    JwtPrincipal jwtPrincipal = (JwtPrincipal) principal;
    User loginUser = userService.getById(jwtPrincipal.getUserId());
    
    Article article = articleService.createArticle(loginUser, request.getTitle(),
        request.getDescription(), request.getBody(), request.getTags());

    boolean following = followService.checkFollowing(loginUser, article.getAuthor());

    return new ArticleResponse(article, following);
  }

  @PutMapping("/{slug}")
  public ArticleResponse updateArticle(
      @PathVariable String slug,
      @RequestBody ArticleUpdateRequest request,
      @AuthenticationPrincipal Object principal
  ) {
    JwtPrincipal jwtPrincipal = (JwtPrincipal) principal;
    User loginUser = userService.getById(jwtPrincipal.getUserId());

    Article updatedArticle = articleService.updateArticle(slug, request.getTitle(),
        request.getDescription(), request.getBody());

    boolean following = followService.checkFollowing(loginUser, updatedArticle.getAuthor());

    return new ArticleResponse(updatedArticle, following);
  }

  @DeleteMapping("/{slug}")
  public ResponseEntity<Object> deleteArticle(@PathVariable String slug) {
    articleService.deleteArticle(slug);

    return ResponseEntity.ok().build();
  }

}
