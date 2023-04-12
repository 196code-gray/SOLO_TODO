package com.solo.project.todo.mapper;

import com.solo.project.todo.dto.TodoDto;
import com.solo.project.todo.entity.Todo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TodoMapper {
    default Todo PostDtoToTodo (TodoDto.Post post) {
        Todo todo = new Todo();


        todo.setTitle(post.getTitle());
        todo.setTodoOrder(post.getOrder());
        todo.setCompleted(post.getCompleted());

        return todo;
    }
    default Todo PatchToTodo (TodoDto.Patch patch) {
        Todo todo = new Todo();

        todo.setTodoId(patch.getTodoId());
        todo.setTitle(patch.getTitle());
        todo.setTodoOrder(patch.getOrder());
        todo.setCompleted(patch.getCompleted());

        return todo;
    }

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
