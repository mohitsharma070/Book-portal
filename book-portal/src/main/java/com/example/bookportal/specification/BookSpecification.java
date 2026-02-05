package com.example.bookportal.specification;

import com.example.bookportal.entity.Book;
import com.example.bookportal.entity.Author;
import com.example.bookportal.entity.Category;
import com.example.bookportal.entity.Publisher;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;

public class BookSpecification {

    public static Specification<Book> containsTextInAllFields(String q) {
        String like = "%" + q.toLowerCase() + "%";
        return (root, query, cb) -> {
            Join<Book, Author> author = root.join("author");
            Join<Book, Category> category = root.join("category");
            Join<Book, Publisher> publisher = root.join("publisher");

            return cb.or(
                    cb.like(cb.lower(root.get("title")), like),
                    cb.like(cb.lower(author.get("name")), like),
                    cb.like(cb.lower(category.get("categoryName")), like),
                    cb.like(cb.lower(publisher.get("publisherName")), like)
            );
        };
    }
}
