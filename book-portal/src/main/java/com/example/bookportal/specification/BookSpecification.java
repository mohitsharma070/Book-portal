package com.example.bookportal.specification;

import com.example.bookportal.entity.Author;
import com.example.bookportal.entity.Book;
import com.example.bookportal.entity.Category;
import com.example.bookportal.entity.Publisher;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {

    public static Specification<Book> active() {
        return (root, query, cb) -> cb.isTrue(root.get("active"));
    }

    public static Specification<Book> containsText(String q, String type) {
        String text = q == null ? "" : q.trim().toLowerCase();
        if (text.isEmpty()) {
            return (root, query, cb) -> cb.conjunction();
        }
        String like = "%" + text + "%";

        Specification<Book> titleSpec = (root, query, cb) -> cb.like(cb.lower(root.get("title")), like);
        Specification<Book> authorSpec = (root, query, cb) -> {
            Join<Book, Author> author = root.join("author", JoinType.LEFT);
            return cb.like(cb.lower(author.get("name")), like);
        };
        Specification<Book> categorySpec = (root, query, cb) -> {
            Join<Book, Category> category = root.join("category", JoinType.LEFT);
            return cb.like(cb.lower(category.get("categoryName")), like);
        };
        Specification<Book> publisherSpec = (root, query, cb) -> {
            Join<Book, Publisher> publisher = root.join("publisher", JoinType.LEFT);
            return cb.like(cb.lower(publisher.get("publisherName")), like);
        };

        return switch (normalizeType(type)) {
            case "TITLE" -> titleSpec;
            case "AUTHOR" -> authorSpec;
            case "CATEGORY" -> categorySpec;
            case "PUBLISHER" -> publisherSpec;
            default -> (root, query, cb) -> {
                Join<Book, Author> author = root.join("author", JoinType.LEFT);
                Join<Book, Category> category = root.join("category", JoinType.LEFT);
                Join<Book, Publisher> publisher = root.join("publisher", JoinType.LEFT);
                query.distinct(true);
                Predicate title = cb.like(cb.lower(root.get("title")), like);
                Predicate authorName = cb.like(cb.lower(author.get("name")), like);
                Predicate categoryName = cb.like(cb.lower(category.get("categoryName")), like);
                Predicate publisherName = cb.like(cb.lower(publisher.get("publisherName")), like);
                return cb.or(title, authorName, categoryName, publisherName);
            };
        };
    }

    private static String normalizeType(String type) {
        return type == null ? "ALL" : type.trim().toUpperCase();
    }
}
