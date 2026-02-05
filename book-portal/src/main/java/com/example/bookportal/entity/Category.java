
package com.example.bookportal.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
@AttributeOverride(name = "id", column = @Column(name = "category_id"))
public class Category extends BaseEntity {

    @Column(name = "category_name")
    private String categoryName;

    public Category(){}

    public Category(Long id, String categoryName) {
        this.setId(id);
        this.categoryName = categoryName;
    }

    // id is inherited from BaseEntity

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}

