package com.solo.project.todo.exception;

import lombok.Getter;

public enum ExceptionCode {
    TODO_IS_NOT(404, "Todo Not Found");

    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
