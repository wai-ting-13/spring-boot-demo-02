package me.specter.spring_boot_demo_02.entity.bookmark;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.specter.spring_boot_demo_02.entity.book.Book;

@RestController
@RequestMapping("/api/v1/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    public BookmarkController(BookmarkService bookmarkService){
        this.bookmarkService = bookmarkService;
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public List<Book> findAllBookmarks(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return this.bookmarkService.findBookmarkedBooks(username);
    }

    @PostMapping("/{bookId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> addBookmarks(@PathVariable String bookId){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
         this.bookmarkService.addBookmark(username, bookId);
         return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{bookId}")
    @PreAuthorize("hasRole('USER')")
    public void deleteBookmarks(@PathVariable String bookId){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        this.bookmarkService.deleteBookmark(username, bookId);
    }
    
}
