package com.example.bookportal.repository;

import com.example.bookportal.dto.BookDto;
import com.example.bookportal.entity.Book;
import com.example.bookportal.repository.projection.CategoryBookCountProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    @Query(value = """
        SELECT 
            c.category_id   AS categoryId,
            c.category_name AS categoryName,
            COUNT(b.book_id) AS bookCount
        FROM books b
        JOIN categories c ON b.category_id = c.category_id
        WHERE b.author_id = :authorId
        GROUP BY c.category_id, c.category_name
    """, nativeQuery = true)
    List<CategoryBookCountProjection> findCategoryWiseBookCountByAuthor(
            @Param("authorId") Long authorId
    );

        List<Book> findByAuthorIdAndCategoryId(
            Long authorId,
            Long categoryId
        );

    @Query(value = """
        SELECT 
            c.category_id   AS categoryId,
            c.category_name AS categoryName,
            COUNT(b.book_id) AS bookCount
        FROM books b
        JOIN categories c ON b.category_id = c.category_id
        WHERE b.publisher_id = :publisherId
        GROUP BY c.category_id, c.category_name
    """, nativeQuery = true)
    List<CategoryBookCountProjection> findCategoryWiseBookCountByPublisher(
            @Param("publisherId") Long publisherId
    );

        List<Book> findByPublisherIdAndCategoryId(
            Long publisherId,
            Long categoryId
        );

    Long countByCategoryId(Long categoryId);

    List<Book> findByCategoryId(Long categoryId);

    Page<Book> findByAuthor_IdAndCategory_IdAndActiveTrue(Long authorId, Long categoryId, Pageable pageable);
    Page<Book> findByPublisher_IdAndCategory_IdAndActiveTrue(Long publisherId, Long categoryId, Pageable pageable);
    Page<Book> findByCategory_IdAndActiveTrue(Long categoryId, Pageable pageable);
    Page<Book> findByAuthor_IdAndActiveTrue(Long authorId, Pageable pageable);
    Page<Book> findByPublisher_IdAndActiveTrue(Long publisherId, Pageable pageable);

    long countByAuthorId(Long authorId);
    long countByPublisherId(Long publisherId);

    List<Book> findByAuthor_IdAndActiveTrue(Long authorId);
    List<Book> findByPublisher_IdAndActiveTrue(Long publisherId);
    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    @Query("SELECT b FROM Book b WHERE LOWER(b.author.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Book> findByAuthorNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);
    @Query("SELECT b FROM Book b WHERE LOWER(b.publisher.publisherName) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Book> findByPublisherNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);
    @Query("SELECT b FROM Book b WHERE LOWER(b.category.categoryName) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Book> findByCategoryNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);

    @Query("""
        SELECT new com.example.bookportal.dto.BookDto(
            b.id, b.title, b.price, b.imageUrl,
            a.id, a.name,
            c.id, c.categoryName,
            p.id, p.publisherName
        )
        FROM Book b
        LEFT JOIN b.author a
        LEFT JOIN b.category c
        LEFT JOIN b.publisher p
    """)
    Page<BookDto> findAllBookDtos(Pageable pageable);
}
