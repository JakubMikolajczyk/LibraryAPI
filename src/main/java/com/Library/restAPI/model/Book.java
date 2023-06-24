package com.Library.restAPI.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.Date;
import java.util.List;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE book SET delete_date = now() WHERE id = ?")
@Where(clause = "delete_date IS NULL")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String  tittle;
    @NotBlank
    private String ISBN;
    @NotBlank
    private int year;

    @ColumnDefault("null")
    private Date deleteDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(
            name = "book_category",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private List<Specimen> specimens;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private List<Borrow> borrows;


    public void addCategory(Category category){
        this.categories.add(category);
        category.getBooks().add(this);
    }

    public void removeCategory(Category category){
        this.categories.remove(category);
        category.getBooks().remove(this);
    }
}
