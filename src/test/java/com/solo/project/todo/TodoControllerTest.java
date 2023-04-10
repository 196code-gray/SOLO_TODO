package com.solo.project.todo;

import com.google.gson.Gson;
import com.solo.project.todo.controller.TodoController;
import com.solo.project.todo.dto.TodoDto;
import com.solo.project.todo.entity.Todo;
import com.solo.project.todo.mapper.TodoMapper;
import com.solo.project.todo.service.TodoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static com.solo.project.document.ApiDocumentUtils.*;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(TodoController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class TodoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoMapper mapper;

    @MockBean
    private TodoService service;

    @Autowired
    private Gson gson;

    @Test
    public void postTodoTest() throws Exception {
        // given 테스트 준비 과정
        TodoDto.Post post = new TodoDto.Post(
                "밥 먹기!" ,
                0,
                false
        );
        String content = gson.toJson(post);

        TodoDto.Response response = new TodoDto.Response(
                1, "밥 먹기!", 0, false
        );

        given(mapper.PostDtoToTodo(Mockito.any(TodoDto.Post.class)))
                .willReturn(new Todo());
        Todo todo = new Todo();
        todo.setTodoId(1L);

        given(service.createTodo(Mockito.any(Todo.class)))
                .willReturn(todo);

        given(mapper.EntityToResponse(Mockito.any(Todo.class)))
                .willReturn(response);

        // when 테스트 대상
        ResultActions actions =
                mockMvc.perform(
                        post("/")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

        // then 테스트 결과
        actions
                .andExpect(status().isCreated())
                .andDo(document(
                        "post-todo",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                List.of(
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("할 일"),
                                        fieldWithPath("order").type(JsonFieldType.NUMBER).description("우선순위").optional(),
                                        fieldWithPath("completed").type(JsonFieldType.BOOLEAN).description("완료 / 미완료").optional()
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("할 일 식별자"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("할 일"),
                                        fieldWithPath("todoOrder").type(JsonFieldType.NUMBER).description("우선 순위"),
                                        fieldWithPath("completed").type(JsonFieldType.BOOLEAN).description("완료 / 미완료")
                                )
                        )
                ));
    }
    @Test
    public void patchTodoTest() throws Exception {
        // given
        long todoId = 1L;
        TodoDto.Patch patch = new TodoDto.Patch(
                todoId, "오늘 할 일은?", 1, false
        );

        TodoDto.Response response = new TodoDto.Response(
                1, "오늘 할 일은?", 1, false
        );

        String content = gson.toJson(patch);

        given(mapper.PatchToTodo(Mockito.any(TodoDto.Patch.class)))
                .willReturn(new Todo());
        given(service.updateTodo(Mockito.any(Todo.class)))
                .willReturn(new Todo());
        given(mapper.EntityToResponse(Mockito.any(Todo.class)))
                .willReturn(response);

        // when
        ResultActions actions =
                mockMvc.perform(
                        patch("/{todoId}", todoId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(patch.getTodoId()))
                .andExpect(jsonPath("$.title").value(patch.getTitle()))
                .andExpect(jsonPath("$.todoOrder").value(patch.getOrder()))
                .andDo(
                        document("patch-todo",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("todoId").description("todo 식별자")
                                ),
                                requestFields(
                                        List.of(
                                                fieldWithPath("todoId").type(JsonFieldType.NUMBER).description("todo 식별자").ignored(),
                                                fieldWithPath("title").type(JsonFieldType.STRING).description("할 일").optional(),
                                                fieldWithPath("order").type(JsonFieldType.NUMBER).description("우선 순위").optional(),
                                                fieldWithPath("completed").type(JsonFieldType.BOOLEAN).description("완료 / 미완료").optional()
                                        )
                                ),
                                responseFields(
                                        List.of(
                                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("todo 식별자").ignored(),
                                                fieldWithPath("title").type(JsonFieldType.STRING).description("할 일"),
                                                fieldWithPath("todoOrder").type(JsonFieldType.NUMBER).description("우선 순위"),
                                                fieldWithPath("completed").type(JsonFieldType.BOOLEAN).description("완료 / 미완료")
                                        )
                                )
                        )
                );
    }
    @Test
    public void findTodoTest() throws Exception {
        // given
        Todo todo = new Todo(
                "양치하기", 0, true
        );
        long todoId = 1L;
        todo.setTodoId(todoId);

        TodoDto.Response response = new TodoDto.Response(
             todoId, "양치하기", 0, true
        );

        given(service.singlTodo(Mockito.anyLong()))
                .willReturn(new Todo());
        given(mapper.EntityToResponse(Mockito.any(Todo.class)))
                .willReturn(response);

        // when
        ResultActions actions =
                mockMvc.perform(get("/{todoId}", todoId)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(todo.getTodoId()))
                .andExpect(jsonPath("$.title").value(todo.getTitle()))
                .andExpect(jsonPath("$.todoOrder").value(todo.getTodoOrder()))
                .andDo(
                        document("get-todo",
                                getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("todoId").description("todo 식별자")
                                ),
                                responseFields(
                                        List.of(
                                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("todo 식별자"),
                                                fieldWithPath("title").type(JsonFieldType.STRING).description("할 일"),
                                                fieldWithPath("todoOrder").type(JsonFieldType.NUMBER).description("우선 순위"),
                                                fieldWithPath("completed").type(JsonFieldType.BOOLEAN).description("완료 / 미완료")
                                        )
                                )

                                )
                );
    }
    @Test
    public void findsTodoTest() throws Exception {
        // given
        ArrayList<Todo> todoList = new ArrayList<>();
        Todo todo1 = new Todo("비타민 먹기", 1, false);
        Todo todo2 = new Todo("운동하기", 0, true);
        Todo todo3 = new Todo("청소하기", 3, false);
        todo1.setTodoId(1L);
        todo2.setTodoId(2L);
        todo3.setTodoId(3L);

        todoList.add(todo1);
        todoList.add(todo2);
        todoList.add(todo3);

        List<TodoDto.Response> responses = new ArrayList<>(todoList.size());
        for (Todo todo : todoList) {
            TodoDto.Response sample = new TodoDto.Response(todo.getTodoId(), todo.getTitle(), todo.getTodoOrder(), todo.getCompleted());
            responses.add(sample);
        }

        given(service.allTodo())
                .willReturn(todoList);
        given(mapper.EntityToList(Mockito.anyList()))
                .willReturn(responses);

        // when
        ResultActions actions =
                mockMvc.perform(get("/")
                        .accept(MediaType.APPLICATION_JSON));

        // then
        actions
                .andExpect(status().isOk())
                .andDo(
                        document("getAll-todo",
                                getDocumentResponse()
                        ,
                        responseFields(
                                List.of(
//                                        fieldWithPath("").type(JsonFieldType.OBJECT),
                                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("todo 식별자"),
                                        fieldWithPath("[].title").type(JsonFieldType.STRING).description("할 일"),
                                        fieldWithPath("[].todoOrder").type(JsonFieldType.NUMBER).description("우선 순위"),
                                        fieldWithPath("[].completed").type(JsonFieldType.BOOLEAN).description("완료 / 미완료")
                                )
                        )
                        )
                );

    }
    @Test
    public void deleteAllTest() throws Exception {
        // given
        Todo todo1 = new Todo("삭제하기", 0, false);
        Todo todo2 = new Todo("음악듣기", 0, false);
        todo1.setTodoId(1L);
        todo2.setTodoId(2L);

        doNothing().when(service).allDelete();

        // when
        ResultActions actions = mockMvc.perform(delete("/"));

        // then
        actions
                .andExpect(status().isNoContent())
                .andDo(
                        document("deleteAll-todo")
                );

    }
    @Test
    public void deleteTodoTest() throws Exception {
        // given
        Todo todo1 = new Todo("삭제하기", 0, false);
        long todoId = 1L;
        todo1.setTodoId(todoId);

        doNothing().when(service).deleteOne(todoId);

        // when
        ResultActions actions = mockMvc.perform(delete("/{todoId}", todoId));

        // then
        actions
                .andExpect(status().isNoContent())
                .andDo(
                        document("delete-todo",
                                pathParameters(
                                        parameterWithName("todoId").description("todo 식별자")
                                ))
                );
    }

}