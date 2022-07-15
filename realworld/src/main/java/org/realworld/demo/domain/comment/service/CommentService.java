package org.realworld.demo.domain.comment.service;

import org.realworld.demo.domain.article.entity.Article;
import org.realworld.demo.domain.comment.entity.Comment;
import org.realworld.demo.domain.user.entity.User;
import org.realworld.demo.domain.comment.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository repository;

    public CommentService(CommentRepository repository) {
        this.repository = repository;
    }

    public Comment registerComment(User author, Article article, String body){
        return repository.save(new Comment(author, article, body));
    }

    public List<Comment> getCommentsFromArticle(Article article){
        return repository.findByArticle(article);
    }

    public void deleteComment(Long id){
        repository.deleteById(id);
    }
}
