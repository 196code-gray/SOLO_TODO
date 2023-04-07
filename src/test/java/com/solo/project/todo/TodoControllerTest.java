package com.solo.project.todo;

import com.google.gson.Gson;
import com.solo.project.todo.controller.TodoController;
import com.solo.project.todo.dto.TodoDto;
import com.solo.project.todo.entity.Todo;
import com.solo.project.todo.mapper.TodoMapper;
import com.solo.project.todo.service.TodoService;
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

import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static com.solo.project.document.ApiDocumentUtils.*;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
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

}