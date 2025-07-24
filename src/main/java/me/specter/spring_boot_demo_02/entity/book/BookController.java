package me.specter.spring_boot_demo_02.entity.book;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.specter.spring_boot_demo_02.aspect.RateLimited;


@RestController
@RequestMapping("api/v1/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping
    public List<Book> findAllBooks(){
        return this.bookService.findAll();
    }

    // Url: http://localhost:8080/api/v1/books/search?title=<#some title#>
    @GetMapping("/search")
    @PreAuthorize("hasRole('USER')")
    @RateLimited
    public List<Book> findAllBooksByTitleContaining(
        @RequestParam(
            required = true, 
            defaultValue = ""
        ) 
        String title
    ){
        return this.bookService.findAllByTitleContainingIgnoreCase(title);
    }
}
