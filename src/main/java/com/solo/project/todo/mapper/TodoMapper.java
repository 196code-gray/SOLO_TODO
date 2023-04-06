package com.solo.project.todo.mapper;

import com.solo.project.todo.dto.TodoDto;
import com.solo.project.todo.entity.Todo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TodoMapper {
    Todo PostDtoToTodo (TodoDto.Post post);
    Todo PatchToTodo (TodoDto.Patch patch);

    default TodoDto.Response EntityToResponse (Todo todo) {
        TodoDto.Response response = new TodoDto.Response(
                todo.getTodoId(),
                todo.getTitle(),
                todo.getTodoOrder(),
                todo.getCompleted()
        );
        return response;
    }
    List<TodoDto.Response> EntityToList (List<Todo> todos);
}
