package com.Library.restAPI.controller;

import com.Library.restAPI.CookieProvider;
import com.Library.restAPI.config.SecurityConfiguration;
import com.Library.restAPI.dto.request.AuthorRequest;
import com.Library.restAPI.exception.AuthorDeleteException;
import com.Library.restAPI.exception.AuthorNotFoundException;
import com.Library.restAPI.exceptionHandler.AuthorExceptionHandler;
import com.Library.restAPI.exceptionHandler.GlobalExceptionHandler;
import com.Library.restAPI.mapper.AuthorMapper;
import com.Library.restAPI.model.Author;
import com.Library.restAPI.model.Book;
import com.Library.restAPI.model.Role;
import com.Library.restAPI.service.AuthorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Random;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {
        AuthorController.class,
        GlobalExceptionHandler.class,
        AuthorExceptionHandler.class,
        SecurityConfiguration.class
})
class AuthorControllerITest {

    @Autowired
    private MockMvc mockMvc;

    private final CookieProvider cookieProvider;
    private final ObjectMapper objectMapper;

    @MockBean
    private AuthorService authorService;

    @SpyBean
    private AuthorMapper authorMapper;

    @SpyBean
    private AuthorController authorController;

    @SpyBean
    private GlobalExceptionHandler globalExceptionHandler;

    @SpyBean
    private AuthorExceptionHandler authorExceptionHandler;
    @Captor
    ArgumentCaptor<Author> authorCaptor;

    public AuthorControllerITest() {
        this.cookieProvider = new CookieProvider();
        this.objectMapper = new ObjectMapper();
    }

    @ParameterizedTest
    @NullSource
    @EnumSource(Role.class)
    void getAuthor(Role role) throws Exception {
        //given
        Author author = Instancio.create(Author.class);
        Long id = author.getId();
        when(authorService.getAuthorById(id)).thenReturn(author);
        //when
        mockMvc.perform(get("/api/v1/authors/" + id)
                        .cookie(cookieProvider.generateCookie(role)))
                .andDo(print())
        //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(author.getName()))
                .andExpect(jsonPath("$.surname").value(author.getSurname()));

        verify(authorService).getAuthorById(id);
        verify(authorController).getAuthor(id);
    }

    @ParameterizedTest
    @NullSource
    @EnumSource(Role.class)
    void getAuthorNotFound(Role role) throws Exception {
        //given
        Long id = new Random().nextLong();
        doThrow(AuthorNotFoundException.class).when(authorService).getAuthorById(anyLong());
        //when
        mockMvc.perform(get("/api/v1/authors/" + id)
                        .cookie(cookieProvider.generateCookie(role)))
                .andDo(print())
        //then
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Author not found."));

        verify(authorService).getAuthorById(id);
        verify(authorController).getAuthor(id);
        verify(authorExceptionHandler).authorNotFound();
    }

//    @ParameterizedTest
//    @NullSource
//    @EnumSource(Role.class)
//    void getAllAuthors(Role role) throws Exception {
//        //given
//        List<Author> authorList = Instancio.ofList(Author.class).size(10).create();
//        Page page = new
//        when(authorService.getAll(any())).thenReturn(authorList);
//        //when
//        mockMvc.perform(get("/api/v1/authors")
//                        .cookie(cookieProvider.generateCookie(role)))
//                .andDo(print())
//                //then
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.*", isA(ArrayList.class)));
//
//
//        verify(authorService).getAll(any());
//        verify(authorController).getAllAuthors(any());
//    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"STAFF", "ADMIN"})
    void createAuthor(Role role) throws Exception{
        //given
        AuthorRequest authorRequest = Instancio.create(AuthorRequest.class);
        //when
        mockMvc.perform(post("/api/v1/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorRequest))
                        .cookie(cookieProvider.generateCookie(role)))
                .andDo(print())
                //then
                .andExpect(status().isOk());

        verify(authorController).createAuthor(authorRequest);
        verify(authorMapper).toEntity(authorRequest);

        verify(authorService).createAuthor(authorCaptor.capture());
        Author authorCapture = authorCaptor.getValue();

        assertNull(authorCapture.getId());
        assertEquals(authorRequest.name(), authorCapture.getName());
        assertEquals(authorRequest.surname(), authorCapture.getSurname());
    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"STAFF", "ADMIN"})
    void createAuthorDataIntegrityViolationBadRequest(Role role) throws Exception{
        //given
        doThrow(DataIntegrityViolationException.class).when(authorService).createAuthor(any());
        AuthorRequest authorRequest = Instancio.create(AuthorRequest.class);
        //when
        mockMvc.perform(post("/api/v1/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorRequest))
                        .cookie(cookieProvider.generateCookie(role)))
                .andDo(print())
                //then
                .andExpect(status().isBadRequest());

        verify(authorController).createAuthor(authorRequest);
        verify(authorMapper).toEntity(authorRequest);
        verify(globalExceptionHandler).badData(any());

        verify(authorService).createAuthor(authorCaptor.capture());
        Author authorCapture = authorCaptor.getValue();

        assertNull(authorCapture.getId());
        assertEquals(authorRequest.name(), authorCapture.getName());
        assertEquals(authorRequest.surname(), authorCapture.getSurname());
    }

    @Test
    void createAuthorUnauthorized() throws Exception{
        //given
        AuthorRequest authorRequest = Instancio.create(AuthorRequest.class);
        //when
        mockMvc.perform(post("/api/v1/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorRequest)))
                .andDo(print())
        //then
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(authorController, authorService, authorMapper);
    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"USER"})
    void createAuthorForbidden(Role role) throws Exception{
        //given
        AuthorRequest authorRequest = Instancio.create(AuthorRequest.class);
        //when
        mockMvc.perform(post("/api/v1/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorRequest))
                        .cookie(cookieProvider.generateCookie(role)))
                .andDo(print())
        //then
                .andExpect(status().isForbidden());

        verifyNoInteractions(authorController, authorService, authorMapper);
    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"STAFF", "ADMIN"})
    void editAuthor(Role role) throws Exception{
        //given
        Long id = new Random().nextLong();
        AuthorRequest authorRequest = Instancio.create(AuthorRequest.class);
        //when
        mockMvc.perform(put("/api/v1/authors/" + id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(authorRequest))
                    .cookie(cookieProvider.generateCookie(role)))
                .andDo(print())
        //then
                .andExpect(status().isOk());

        verify(authorController).editAuthor(id, authorRequest);
        verify(authorMapper).toEntity(id, authorRequest);

        verify(authorService).editAuthor(authorCaptor.capture());
        Author authorCapture = authorCaptor.getValue();

        assertEquals(id, authorCapture.getId());
        assertEquals(authorRequest.name(), authorCapture.getName());
        assertEquals(authorRequest.surname(), authorCapture.getSurname());
    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"STAFF", "ADMIN"})
    void editAuthorDataIntegrityViolationBadRequest(Role role) throws Exception{
        //given
        Long id = new Random().nextLong();
        doThrow(DataIntegrityViolationException.class).when(authorService).editAuthor(any());
        AuthorRequest authorRequest = Instancio.create(AuthorRequest.class);
        //when
        mockMvc.perform(put("/api/v1/authors/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorRequest))
                        .cookie(cookieProvider.generateCookie(role)))
                .andDo(print())
        //then
                .andExpect(status().isBadRequest());

        verify(authorController).editAuthor(id, authorRequest);
        verify(authorMapper).toEntity(id, authorRequest);
        verify(globalExceptionHandler).badData(any());

        verify(authorService).editAuthor(authorCaptor.capture());
        Author authorCapture = authorCaptor.getValue();

        assertEquals(id, authorCapture.getId());
        assertEquals(authorRequest.name(), authorCapture.getName());
        assertEquals(authorRequest.surname(), authorCapture.getSurname());
    }

    @Test
    void editAuthorUnauthorized() throws Exception{
        //given
        Long id = new Random().nextLong();
        AuthorRequest authorRequest = Instancio.create(AuthorRequest.class);
        //when
        mockMvc.perform(put("/api/v1/authors/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorRequest)))
                .andDo(print())
        //then
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(authorController, authorService, authorMapper);
    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"USER"})
    void editAuthorForbidden(Role role) throws Exception{
        //given
        Long id = new Random().nextLong();
        AuthorRequest authorRequest = Instancio.create(AuthorRequest.class);
        //when
        mockMvc.perform(put("/api/v1/authors/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorRequest))
                        .cookie(cookieProvider.generateCookie(role)))
                .andDo(print())
        //then
                .andExpect(status().isForbidden());

        verifyNoInteractions(authorController, authorService, authorMapper);
    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"STAFF", "ADMIN"})
    void deleteAuthor(Role role) throws Exception {
        //given
        Long id = new Random().nextLong();
        //when
        mockMvc.perform(delete("/api/v1/authors/" + id)
                .cookie(cookieProvider.generateCookie(role)))
                .andDo(print())
        //then
                .andExpect(status().isOk());

        verify(authorController).deleteAuthor(id);
        verify(authorService).deleteAuthor(id);
    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"STAFF", "ADMIN"})
    void deleteAuthorRelatedBooksBadRequest(Role role) throws Exception {
        //given
        Author author = Instancio.of(Author.class)
                        .set(field(Author::getBooks),
                                Instancio.ofList(Book.class)
                                        .size(10)
                                        .create())
                        .create();

        Long id = author.getId();
        doThrow(new AuthorDeleteException(author)).when(authorService).deleteAuthor(id);
        //when
        mockMvc.perform(delete("/api/v1/authors/" + id)
                        .cookie(cookieProvider.generateCookie(role)))
                .andDo(print())
        //then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.*").isArray())
                .andExpect(jsonPath("$.length()").value(10));

        verify(authorController).deleteAuthor(id);
        verify(authorService).deleteAuthor(id);
        verify(authorExceptionHandler).authorDelete(any());
    }

    @Test
    void deleteAuthorUnauthorized() throws Exception{
        //given
        Long id = new Random().nextLong();
        //when
        mockMvc.perform(delete("/api/v1/authors/" + id))
                .andDo(print())
        //then
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(authorController, authorService, authorMapper);
    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"USER"})
    void deleteAuthorForbidden(Role role) throws Exception{
        //given
        Long id = new Random().nextLong();
        //when
        mockMvc.perform(delete("/api/v1/authors/" + id)
                        .cookie(cookieProvider.generateCookie(role)))
                .andDo(print())
        //then
                .andExpect(status().isForbidden());

        verifyNoInteractions(authorController, authorService, authorMapper);
    }
}