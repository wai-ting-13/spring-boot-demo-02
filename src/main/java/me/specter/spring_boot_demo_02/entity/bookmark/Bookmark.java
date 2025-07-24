package me.specter.spring_boot_demo_02.entity.bookmark;

import java.time.LocalDateTime;

import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.specter.spring_boot_demo_02.entity.appUser.AppUser;
import me.specter.spring_boot_demo_02.entity.book.Book;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Bookmark {
    
    // Must init (For unique relationship)
    @EmbeddedId
    private BookmarkId id = new BookmarkId();

    @ManyToOne
    @MapsId("bookId")
    private Book book;
    
    @ManyToOne
    @MapsId("userId")
    private AppUser user;

    @LastModifiedDate
    private LocalDateTime lastUpdated;
}
