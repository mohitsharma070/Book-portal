
package com.example.bookportal.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "publishers")
@AttributeOverride(name = "id", column = @Column(name = "publisher_id"))
public class Publisher extends BaseEntity {

    @Column(name = "publisher_name")
    private String publisherName;

    public Publisher(){}

    public Publisher(Long id, String publisherName) {
        this.setId(id);
        this.publisherName = publisherName;
    }

    // id is inherited from BaseEntity

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }
}

