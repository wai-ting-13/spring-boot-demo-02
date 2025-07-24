package me.specter.spring_boot_demo_02.entity.book;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
    Optional<Book> findByIsbn(String isbn);
    
    // LIKE Operation
    List<Book> findAllByTitleContainingIgnoreCase(String title);

    List<Book> findByCreatedAt(LocalDate date);
}
