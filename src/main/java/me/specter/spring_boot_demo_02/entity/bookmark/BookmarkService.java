package me.specter.spring_boot_demo_02.entity.bookmark;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.specter.spring_boot_demo_02.entity.appUser.AppUser;
import me.specter.spring_boot_demo_02.entity.appUser.AppUserRepository;
import me.specter.spring_boot_demo_02.entity.book.Book;
import me.specter.spring_boot_demo_02.entity.book.BookRepository;
import me.specter.spring_boot_demo_02.exception.DataNotFoundException;
import me.specter.spring_boot_demo_02.exception.ErrorDescriptionConstants;
import me.specter.spring_boot_demo_02.exception.UserNotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookmarkService {
    
    private final AppUserRepository appUserRepository;
    private final BookRepository bookRepository;
    private final BookmarkRepository bookmarkRepository;

    public List<Book> findTop3Books() {
        PageRequest pageRequest = PageRequest.of(0, 3);
        return bookmarkRepository.findTop3Books(pageRequest);
    }

    @Transactional
    public List<Book> findBookmarkedBooks(String username){
        List<Book> returnedObject = this.appUserRepository
            .findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException(
                ErrorDescriptionConstants.USER_WITH_USERNAME_NOT_FOUND.formatted(username)
            ))
            .getBookmarks()
            .stream()
            .map( bm -> bm.getBook())
            .toList();
        
        return returnedObject;
    }

    public Book addBookmark(String username, String bookId){
        Book book = this.bookRepository
            .findById(bookId)
            .orElseThrow(
                () -> new DataNotFoundException(ErrorDescriptionConstants.BOOK_WITH_ID_NOT_FOUND.formatted(bookId))
            );
        AppUser user = this.appUserRepository
            .findByUsername(username)
            .orElseThrow( 
                () -> new UserNotFoundException(ErrorDescriptionConstants.USER_WITH_USERNAME_NOT_FOUND.formatted(username)) 
            );

        Bookmark bookmark = new Bookmark();
        bookmark.setBook(book);
        bookmark.setUser(user);
        bookmark.setLastUpdated(LocalDateTime.now());

        this.bookmarkRepository.save(bookmark);

        return book;
    }

    public void deleteBookmark(String username, String bookId){

        AppUser user = this.appUserRepository
            .findByUsername(username)
            .orElseThrow(
                () -> new UserNotFoundException(ErrorDescriptionConstants.USER_WITH_USERNAME_NOT_FOUND.formatted(username)) 
            );

        Bookmark bookmark = this.bookmarkRepository
            .findById(new BookmarkId(bookId, user.getId()))
            .orElseThrow(() -> 
                new DataNotFoundException(ErrorDescriptionConstants.BOOKMARK_NOT_FOUND
            ));

        this.bookmarkRepository.delete(bookmark);

    }

}
