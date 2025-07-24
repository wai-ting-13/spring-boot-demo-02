package me.specter.spring_boot_demo_02.entity.bookmark;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkId {
    private String bookId;
    private Integer userId;
}
