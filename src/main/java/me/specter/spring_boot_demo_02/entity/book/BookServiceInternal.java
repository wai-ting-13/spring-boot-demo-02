package me.specter.spring_boot_demo_02.entity.book;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.specter.spring_boot_demo_02.exception.DataNotFoundException;
import me.specter.spring_boot_demo_02.exception.ErrorDescriptionConstants;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceInternal {
    private final BookRepository bookRepository;

// Internal Use
    // Create
    public Book createBook(Book book){
        book.setId(null);
        book.setCreatedAt(LocalDate.now());
        return this.bookRepository.save(book);
    }

    // Retrieval
    public List<Book> findAllBooks(){
        return this.bookRepository.findAll();
    }


    public Book findById(String id){
        Optional<Book> book = this.bookRepository.findById(id);
        if (book.isEmpty()){
            throw new DataNotFoundException(ErrorDescriptionConstants.BOOK_WITH_ID_NOT_FOUND);
        }
        return book.get();
    }

    // Update
    public void updateBook(Book book){
        String id = book.getId();
        Optional<Book> bookFromDB = this.bookRepository.findById(id);
        if(bookFromDB.isPresent()){
            this.bookRepository.save(book);
        }else{
            throw new DataNotFoundException(ErrorDescriptionConstants.BOOK_WITH_ID_NOT_FOUND.formatted(id));
        }
    }

    // Delete
    public void deleteBook(String id){
        // Doc: If the entity is not found in the persistence store it is silently ignored.
        this.bookRepository.deleteById(id);
    }

}
