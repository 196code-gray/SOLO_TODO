package com.solo.project.todo.service;

import com.solo.project.todo.entity.Todo;
import com.solo.project.todo.exception.BusinessException;
import com.solo.project.todo.exception.ExceptionCode;
import com.solo.project.todo.repository.TodoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {
    private final TodoRepository repository;

    public TodoService(TodoRepository repository) {
        this.repository = repository;
    }

    public Todo createTodo (Todo todo) {

        return repository.save(todo);
    }
    public Todo updateTodo (Todo todo) {
        Todo findtodo = verifiedTodo(todo.getTodoId());

        Optional.ofNullable(todo.getTitle())
                .ifPresent(title -> findtodo.setTitle(title));
        Optional.ofNullable(todo.getTodoOrder())
                .ifPresent(order -> findtodo.setTodoOrder(order));
        if (findtodo.getCompleted() != todo.getCompleted()) {
            findtodo.setCompleted(todo.getCompleted());
        }

        return repository.save(findtodo);
    }
    @Transactional(readOnly = true)
    public Todo singlTodo (long todoId) {
        return verifiedTodo(todoId);
    }
    public List<Todo> allTodo () {
        List<Todo> findall = repository.findAll(Sort.by(Sort.Direction.ASC, "todoOrder"));
        return findall;
    }
    public void deleteOne(long todoId) {
        repository.delete(verifiedTodo(todoId));
    }
    public void allDelete() {
        repository.deleteAll();
    }

    public Todo verifiedTodo (long todoId) {
        Optional<Todo> OpTodo = repository.findById(todoId);
        Todo findTodo = OpTodo.orElseThrow(() -> new BusinessException(ExceptionCode.TODO_IS_NOT));
        return findTodo;
    }
}
