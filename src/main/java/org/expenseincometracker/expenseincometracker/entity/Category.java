package org.expenseincometracker.expenseincometracker.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.expenseincometracker.expenseincometracker.enums.CategoryType;

@Entity
@Table(name = "categories")
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private CategoryType type;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
}
