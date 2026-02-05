
package com.example.bookportal.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "books")
@AttributeOverride(name = "id", column = @Column(name = "book_id"))
public class Book extends BaseEntity {

    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    private Double price;

    @Column(name = "image_url")
    private String imageUrl;

    public Book(){}

    public Book(Long id, String title, Author author, Category category, Publisher publisher, Double price, String imageUrl) {
        this.setId(id);
        this.title = title;
        this.author = author;
        this.category = category;
        this.publisher = publisher;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    // id is inherited from BaseEntity

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

