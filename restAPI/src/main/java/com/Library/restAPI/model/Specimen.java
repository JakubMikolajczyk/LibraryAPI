package com.Library.restAPI.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Specimen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    //Make unnecessary select we can still get Borrow using specimen id
//    @OneToOne(mappedBy = "specimen", fetch = FetchType.LAZY)
//    private Borrow borrow;

}
