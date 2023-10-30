package com.Library.restAPI.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;

import java.util.Date;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE borrow_history SET hidden = TRUE WHERE id = ?")
public class BorrowHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    @Column(updatable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Column(updatable = false)
    private User user;

    @Column(updatable = false)
    private Date startTime;

    @CreationTimestamp
    @Column(updatable = false)
    private Date endTime;

    @ColumnDefault("FALSE")
    private boolean hidden;

}
