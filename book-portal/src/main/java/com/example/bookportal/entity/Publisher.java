package com.example.bookportal.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "publishers",
    indexes = {
        @Index(name = "idx_publishers_name", columnList = "publisher_name"),
        @Index(name = "idx_publishers_active", columnList = "active")
    }
)
@AttributeOverride(name = "id", column = @Column(name = "publisher_id"))
public class Publisher extends BaseEntity {

    @Column(name = "publisher_name")
    private String publisherName;

    @Column(name = "active", nullable = false, columnDefinition = "BOOLEAN DEFAULT 1")
    private boolean active = true;

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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
