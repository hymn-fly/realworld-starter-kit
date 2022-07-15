package org.realworld.demo.controller;

import java.util.List;
import org.realworld.demo.controller.dto.CommentDto.CommentCreateRequest;
import org.realworld.demo.controller.dto.CommentDto.CommentCreateResponse;
import org.realworld.demo.controller.dto.CommentDto.MultipleCommentResponse;
import org.realworld.demo.domain.article.entity.Article;
import org.realworld.demo.domain.comment.entity.Comment;
import org.realworld.demo.domain.user.entity.User;
import org.realworld.demo.domain.article.service.ArticleService;
import org.realworld.demo.domain.comment.service.CommentService;
import org.realworld.demo.domain.follow.service.FollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/articles")
public class CommentController {

  private final CommentService commentService;

  private final ArticleService articleService;

  private final FollowService followService;

  public CommentController(CommentService commentService, ArticleService articleService,
      FollowService followService) {
    this.commentService = commentService;
    this.articleService = articleService;
    this.followService = followService;
  }

  @PostMapping("/{slug}/comments")
  public CommentCreateResponse addCommentToArticle(
      CommentCreateRequest request,
      @PathVariable String slug,
      @AuthenticationPrincipal Object principal) {
    User loginUser = (User) principal;
    Article article = articleService.getArticle(slug);
    Comment registeredComment = commentService.registerComment(loginUser, article,
        request.getBody());
    boolean following = followService.checkFollowing(loginUser, article.getAuthor());
    return new CommentCreateResponse(registeredComment, loginUser, following);
  }

  @GetMapping("/{slug}/comments")
  public MultipleCommentResponse getComments(@PathVariable String slug,
      @AuthenticationPrincipal Object principal) {
    User loginUser = principal instanceof String ? null : (User) principal;
    Article article = articleService.getArticle(slug);
    List<Comment> comments = commentService.getCommentsFromArticle(article);
    boolean[] followings = new boolean[comments.size()];
    for (int i = 0; i < comments.size(); i++) {
      followings[i] = followService.checkFollowing(loginUser, comments.get(i).getAuthor());
    }
    return new MultipleCommentResponse(comments, followings);
  }

  @DeleteMapping("/{slug}/comments/{id}")
  public ResponseEntity<Object> deleteComment(@PathVariable String slug, @PathVariable Long id) {
    commentService.deleteComment(id);
    return ResponseEntity.ok().build();
  }
}
