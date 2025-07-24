package me.specter.spring_boot_demo_02.entity.book;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@NoArgsConstructor
@Getter
@ToString
public class BookDto {
    private String title;
    private String isbn;

    public BookDto(Book book){
        this.title = book.getTitle();
        this.isbn = book.getIsbn();
    }
}
