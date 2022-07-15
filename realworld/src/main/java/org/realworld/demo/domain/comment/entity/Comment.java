package org.realworld.demo.domain.comment.entity;

import static com.google.common.base.Preconditions.checkArgument;
import static org.springframework.util.StringUtils.hasText;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.realworld.demo.domain.article.entity.Article;
import org.realworld.demo.domain.base.BaseTimeEntity;
import org.realworld.demo.domain.user.entity.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

  @ManyToOne
  @JoinColumn(name = "author_id", nullable = false)
  private User author;

  @ManyToOne
  @JoinColumn(name = "article_id", nullable = false)
  private Article article;

  private String body;

  public Comment(User author, Article article, String body) {
    checkArgument(author != null);
    checkArgument(article != null);
    checkArgument(hasText(body));

    this.author = author;
    this.body = body;
    this.article = article;
  }
}
