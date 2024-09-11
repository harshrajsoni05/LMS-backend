
package com.nucleusteq.backend.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter
@NoArgsConstructor
@Entity
@Table(name = "books")
public class Books {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    private String author;
    private int quantity;

    @Column(name = "imageurl")
    private String imageURL;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public boolean isAvailable() {
        return quantity > 0;
    }
}
