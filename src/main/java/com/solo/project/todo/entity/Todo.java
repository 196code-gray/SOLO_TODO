package com.solo.project.todo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long todoId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int todoOrder;

    @Column
    private Boolean completed;

    public Todo(String title, int todoOrder, Boolean completed) {
        this.title = title;
        this.todoOrder = todoOrder;
        this.completed = completed;
    }
}
