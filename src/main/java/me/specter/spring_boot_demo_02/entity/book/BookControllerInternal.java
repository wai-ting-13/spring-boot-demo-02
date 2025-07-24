package me.specter.spring_boot_demo_02.entity.book;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/internal/books")
@RequiredArgsConstructor
public class BookControllerInternal {
    private final BookServiceInternal bookServiceInternal;

    // Internal Use
    // Create
    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> createBook(@RequestBody @Valid Book book){ 
        this.bookServiceInternal.createBook(book);
        return ResponseEntity.created(null).build();
    }

    // Retrieval
    @GetMapping
    @PreAuthorize("hasRole('MANAGER')")
    public List<Book> findAllBooks(){
        return this.bookServiceInternal.findAllBooks();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public Book findById(@PathVariable String id){
        return this.bookServiceInternal.findById(id);
    }

    // Update
    @PutMapping
    @PreAuthorize("hasRole('MANAGER')")
    public void updateBook(@RequestBody Book book) {    
        this.bookServiceInternal.updateBook(book);
    }

    // Delete
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> deleteBook(@PathVariable String id){
        this.bookServiceInternal.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
