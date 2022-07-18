package org.realworld.demo.domain.article.entity;

import static com.google.common.base.Preconditions.checkArgument;
import static org.realworld.demo.domain.article.util.Utility.toSlug;
import static org.springframework.util.StringUtils.hasText;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.realworld.demo.domain.Tag;
import org.realworld.demo.domain.base.BaseTimeEntity;
import org.realworld.demo.domain.user.entity.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends BaseTimeEntity {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "author_id")
  private User author;

  private String title;

  @Column(unique = true)
  private String slug;

  private String description;

  private String body;

  @OneToMany
  private final List<Tag> tags = new ArrayList<>();

  public Article(Builder builder) {
    checkArgument(hasText(builder.title));
    checkArgument(builder.author != null);
    checkArgument(hasText(builder.body));

    this.author = builder.author;
    this.title = builder.title;
    this.slug = toSlug(this.title);
    this.description = builder.description;
    this.body = builder.body;
    this.tags.addAll(builder.tags);
  }

  public void update(String title, String description, String body) {
    if (hasText(title)) {
      this.title = title;
      this.slug = toSlug(title);
    }
    if (hasText(description)) {
      this.description = description;
    }
    if (hasText(body)) {
      this.body = body;
    }
    this.updatedAt = LocalDateTime.now();
  }

  public static class Builder {

    private final User author;
    private final String title;
    private String description;
    private final String body;
    private final List<Tag> tags = new ArrayList<>();

    // 필수적인 필드 : brand
    public Builder(User author, String title, String body) {
      this.author = author;
      this.title = title;
      this.body = body;
    }

    public Builder description(String description) {
      this.description = description;
      return this;
    }


    public Builder tags(List<Tag> tags) {
      this.tags.addAll(tags);
      return this;
    }

    public Article build() {
      return new Article(this);
    }
  }
}
