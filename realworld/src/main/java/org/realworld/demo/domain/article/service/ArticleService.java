package org.realworld.demo.domain.article.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.realworld.demo.domain.Tag;
import org.realworld.demo.domain.article.entity.Article;
import org.realworld.demo.domain.article.repository.ArticleRepository;
import org.realworld.demo.domain.user.entity.User;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ArticleService {

  private final ArticleRepository repository;

  public ArticleService(ArticleRepository repository) {
    this.repository = repository;
  }

  public Article getArticle(String slug) {
    return repository.findBySlug(slug).orElseThrow();
  }

  public Article createArticle(User author, String title, String description, String body,
      List<String> tags) {
    Article article = tags != null ?
        new Article.Builder(author, title, body).description(description)
            .tags(tags.stream().map(Tag::new).collect(Collectors.toList())).build()
        : new Article.Builder(author, title, body).description(description).build();

    return repository.save(article);
  }

  public Article updateArticle(String slug, String title, String description, String body) {
    Optional<Article> articleOptional = repository.findBySlug(slug);
    Article article = articleOptional.orElseThrow();
    article.update(title, description, body);
    return repository.save(article);
  }

  public void deleteArticle(String slug) {
    repository.findBySlug(slug).ifPresent(article -> repository.deleteById(article.getId()));
  }
}
