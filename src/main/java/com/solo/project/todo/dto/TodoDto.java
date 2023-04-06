package com.solo.project.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;


public class TodoDto {

    @Getter
    public static class Post {
        @NotBlank
        private String title;
        private int todoOrder;
        private Boolean completed;
    }

    @Getter
    @Setter
    public static class Patch {
        private long todoId;
        private String title;
        private int todoOrder;
        private Boolean completed;
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private long id;
        private String title;
        private int todoOrder;
        private Boolean completed;
    }
}
