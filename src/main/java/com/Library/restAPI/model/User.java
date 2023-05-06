package com.Library.restAPI.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.sql.Date;
import java.util.List;

@Entity
@Getter
@Setter
@SQLDelete(sql = "UPDATE users SET delete_date = now() WHERE id = ?")
@Where(clause = "delete_date IS NULL")
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String email;
    private boolean isAdmin;

    @NotBlank
    private String name;
    @NotBlank
    private String surname;
    @NotBlank
    private String address;
    @NotBlank
    private String city;

    @CreationTimestamp
    private Date registerDate;

    @ColumnDefault("null")
    private Date deleteDate;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Borrow> borrows;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<BorrowArchive> borrowHistory;

}
