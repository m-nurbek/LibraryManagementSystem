package nurbek.librarymanagementsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import nurbek.librarymanagementsystem.dto.Author;

import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@Table(name = "AUTHOR")
public class AuthorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME")
    private String name;

    public Author toDto() {
        return new Author(id, name);
    }

    public static AuthorEntity fromDto(Author author) {
        return new AuthorEntity(author.getId(), author.getName());
    }
}