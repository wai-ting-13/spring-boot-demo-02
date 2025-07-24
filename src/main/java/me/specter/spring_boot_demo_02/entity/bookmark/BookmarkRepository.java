package me.specter.spring_boot_demo_02.entity.bookmark;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import me.specter.spring_boot_demo_02.entity.book.Book;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, BookmarkId>{
    @Query("SELECT b.book FROM Bookmark b GROUP BY b.book ORDER BY COUNT(b.book) DESC")
    List<Book> findTop3Books(PageRequest pageable);
}
