package com.Library.restAPI.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;

import java.util.Date;
import java.util.List;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE book SET delete_date = now() WHERE id = ?")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String  tittle;
    @NotBlank
    private String ISBN;
    @NotNull
    private int year;

    @ColumnDefault("null")
    private Date deleteDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    @NotNull
    private Author author;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(
            name = "book_genre",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres;


    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private List<SpecimenBorrow> specimenBorrows;


    public void addGenre(Genre genre){
        this.genres.add(genre);
        genre.getBooks().add(this);
    }

    public void removeGenre(Genre genre){
        this.genres.remove(genre);
        genre.getBooks().remove(this);
    }
}
