package org.realworld.demo.repository;

import org.realworld.demo.domain.article.Article;
import org.realworld.demo.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByArticle(Article article);
}
