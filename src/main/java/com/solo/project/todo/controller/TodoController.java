package com.solo.project.todo.controller;

import com.solo.project.todo.dto.TodoDto;
import com.solo.project.todo.entity.Todo;
import com.solo.project.todo.mapper.TodoMapper;
import com.solo.project.todo.service.TodoService;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/")
@Validated
public class TodoController {
    private final TodoMapper mapper;
    private final TodoService service;

    public TodoController(TodoMapper mapper, TodoService service) {
        this.mapper = mapper;
        this.service = service;
    }

    @PostMapping
    public ResponseEntity postTodo(@Valid @RequestBody TodoDto.Post post) {
        Todo todo = mapper.PostDtoToTodo(post);
        Todo create = service.createTodo(todo);

        return new ResponseEntity(mapper.EntityToResponse(create), HttpStatus.CREATED);
    }
    @PatchMapping("{id}")
    public ResponseEntity patchTodo(@PathVariable("id") @Positive long todoId,
                                    @RequestBody TodoDto.Patch patch) {
        patch.setTodoId(todoId);
        Todo update = service.updateTodo(mapper.PatchToTodo(patch));

        return new ResponseEntity(mapper.EntityToResponse(update), HttpStatus.OK);
    }
    @GetMapping("{id}")
    public ResponseEntity findTodo (@PathVariable("id") @Positive long todoId) {
        Todo oneTodo = service.singlTodo(todoId);

        return new ResponseEntity(mapper.EntityToResponse(oneTodo), HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity findsTodo() {
        List<Todo> findTodos = service.allTodo();

        return new ResponseEntity(mapper.EntityToList(findTodos), HttpStatus.OK);
    }
    @DeleteMapping
    public ResponseEntity deleteAll() {
        service.allDelete();
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteTodo(@PathVariable("id") @Positive long todoId) {
        service.deleteOne(todoId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
