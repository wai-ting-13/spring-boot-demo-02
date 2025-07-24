package me.specter.spring_boot_demo_02.entity.book;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.specter.spring_boot_demo_02.exception.DataNotFoundException;
import me.specter.spring_boot_demo_02.exception.ErrorDescriptionConstants;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {
    private final BookRepository bookRepository;
    // Public Available
    // Retrieval
    public List<Book> findAll(){
        return this.bookRepository.findAll();
    }

    public Book findByIsbn(String isbn){
        return this.bookRepository
            .findByIsbn(isbn)
            .orElseThrow( 
                () -> new DataNotFoundException(
                    ErrorDescriptionConstants
                        .BOOK_WITH_ISBN_NOT_FOUND
                        .formatted(isbn)
                )
            );
    }

    public List<Book> findAllByTitleContainingIgnoreCase(String title){
        return 
            this.bookRepository
                .findAllByTitleContainingIgnoreCase(title)
        ;
    }

    // Use by external entity thus use DTO
    public List<BookDto> findByCreatedAtToday(){
        return this.bookRepository
            .findByCreatedAt(LocalDate.now())
            .stream()
            .map(b -> new BookDto(b))
            .toList()
        ;
    }
}
