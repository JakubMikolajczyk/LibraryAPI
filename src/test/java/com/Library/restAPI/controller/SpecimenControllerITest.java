package com.Library.restAPI.controller;

import com.Library.restAPI.CookieProvider;
import com.Library.restAPI.config.ApplicationConfig;
import com.Library.restAPI.config.SecurityConfiguration;
import com.Library.restAPI.dto.request.SpecimenRequest;
import com.Library.restAPI.exception.BookNotExists;
import com.Library.restAPI.exception.SpecimenDeleteException;
import com.Library.restAPI.exception.SpecimenNotFoundException;
import com.Library.restAPI.exceptionHandler.GlobalExceptionHandler;
import com.Library.restAPI.exceptionHandler.SpecimenExceptionHandler;
import com.Library.restAPI.mapper.SpecimenMapper;
import com.Library.restAPI.model.Book;
import com.Library.restAPI.model.Role;
import com.Library.restAPI.model.SpecimenBorrow;
import com.Library.restAPI.repository.BookRepository;
import com.Library.restAPI.service.SpecimenService;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {
        SpecimenController.class,
        SecurityConfiguration.class,
        ApplicationConfig.class
})
class SpecimenControllerITest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper;
    private final CookieProvider cookieProvider;

    @SpyBean
    private GlobalExceptionHandler globalExceptionHandler;

    @MockBean
    private SpecimenService specimenService;

    @MockBean
    private BookRepository bookRepository;

    @SpyBean
    private SpecimenMapper specimenMapper;

    @SpyBean
    private SpecimenController specimenController;

    @SpyBean
    private SpecimenExceptionHandler specimenExceptionHandler;

    @Captor
    private ArgumentCaptor<SpecimenBorrow> specimenBorrowCaptor;

    public SpecimenControllerITest() {
        this.objectMapper = new ObjectMapper();
        this.cookieProvider = new CookieProvider();
    }

    @ParameterizedTest
    @NullSource
    @EnumSource(Role.class)
    void getSpecimenById(Role role) throws Exception {
        //given
        SpecimenBorrow specimenBorrow = Instancio.create(SpecimenBorrow.class);
        Long id = specimenBorrow.getId();
        when(specimenService.getSpecimenById(id)).thenReturn(specimenBorrow);
        //when
        mockMvc.perform(get("/api/v1/specimens/" + id)
                    .cookie(cookieProvider.generateCookie(role)))
                .andDo(print())
        //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(specimenBorrow.getId()))
                .andExpect(jsonPath("$.isBorrowed").value(specimenBorrow.getUser() != null))
                .andExpect(jsonPath("$.book.id").value(specimenBorrow.getBook().getId()))
                .andExpect(jsonPath("$.book.href").value("http://localhost:8080/api/v1/books/" + specimenBorrow.getBook().getId()));

        verify(specimenController).getSpecimenById(id);
        verify(specimenService).getSpecimenById(id);
    }

    @ParameterizedTest
    @NullSource
    @EnumSource(Role.class)
    void getSpecimenByIdNotFound(Role role) throws Exception {
        //given
        Long id = new Random().nextLong();
        when(specimenService.getSpecimenById(anyLong())).thenThrow(SpecimenNotFoundException.class);
        //when
        mockMvc.perform(get("/api/v1/specimens/" + id)
                        .cookie(cookieProvider.generateCookie(role)))
                .andDo(print())
        //then
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Specimen not found."));

        verify(specimenController).getSpecimenById(id);
        verify(specimenService).getSpecimenById(id);
    }

    @ParameterizedTest
    @NullSource
    @EnumSource(Role.class)
    void getAllSpecimen(Role role) throws Exception {
        //given
        List<SpecimenBorrow> specimenBorrowsList = Instancio.ofList(SpecimenBorrow.class).size(10).create();
        when(specimenService.getAllSpecimen()).thenReturn(specimenBorrowsList);
        //when
        mockMvc.perform(get("/api/v1/specimens")
                        .cookie(cookieProvider.generateCookie(role)))
                .andDo(print())
        //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*").isArray())
                .andExpect(jsonPath("$.length()").value(10));

        verify(specimenController).getAllSpecimen();
        verify(specimenService).getAllSpecimen();
    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"STAFF", "ADMIN"})
    void createSpecimen(Role role) throws Exception {
        //given
        Book book = Instancio.create(Book.class);
        when(bookRepository.getReferenceById(book.getId())).thenReturn(book);
        SpecimenRequest specimenRequest = new SpecimenRequest(book.getId());
        //when
        mockMvc.perform(post("/api/v1/specimens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(specimenRequest))
                        .cookie(cookieProvider.generateCookie(role)))
                .andDo(print())
        //then
                .andExpect(status().isOk());

        verify(specimenController).createSpecimen(specimenRequest);
        verify(bookRepository).getReferenceById(book.getId());
        verify(specimenMapper).toEntity(specimenRequest);

        verify(specimenService).createSpecimen(specimenBorrowCaptor.capture());
        SpecimenBorrow specimenBorrowCaptured = specimenBorrowCaptor.getValue();

        assertNull(specimenBorrowCaptured.getId());
        assertEquals(book, specimenBorrowCaptured.getBook());
        assertNull(specimenBorrowCaptured.getUser());
        assertNull(specimenBorrowCaptured.getStartTime());
    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"STAFF", "ADMIN"})
    void createSpecimenBookNotExists(Role role) throws Exception {
        //given
        Long id = new Random().nextLong();
        when(bookRepository.getReferenceById(id)).thenReturn(null);
        SpecimenRequest specimenRequest = new SpecimenRequest(id);
        doThrow(BookNotExists.class).when(specimenService).createSpecimen(any());
        //when
        mockMvc.perform(post("/api/v1/specimens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(specimenRequest))
                        .cookie(cookieProvider.generateCookie(role)))
                .andDo(print())
                //then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Book not exists."));

        verify(specimenController).createSpecimen(specimenRequest);
        verify(bookRepository).getReferenceById(id);
        verify(specimenMapper).toEntity(specimenRequest);

        verify(specimenService).createSpecimen(specimenBorrowCaptor.capture());
        SpecimenBorrow specimenBorrowCaptured = specimenBorrowCaptor.getValue();

        assertNull(specimenBorrowCaptured.getId());
        assertNull(specimenBorrowCaptured.getBook());
        assertNull(specimenBorrowCaptured.getUser());
        assertNull(specimenBorrowCaptured.getStartTime());
    }

    @Test
    void createSpecimenUnauthorized() throws Exception {
        //given
        Long id = new Random().nextLong();
        SpecimenRequest specimenRequest = new SpecimenRequest(id);
        //when
        mockMvc.perform(post("/api/v1/specimens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(specimenRequest)))
                .andDo(print())
                //then
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(specimenController, specimenMapper, specimenService, bookRepository);
    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"USER"})
    void createSpecimenForbidden(Role role) throws Exception {
        //given
        Long id = new Random().nextLong();
        SpecimenRequest specimenRequest = new SpecimenRequest(id);
        //when
        mockMvc.perform(post("/api/v1/specimens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(specimenRequest))
                        .cookie(cookieProvider.generateCookie(role)))
                .andDo(print())
                //then
                .andExpect(status().isForbidden());

        verifyNoInteractions(specimenController, specimenMapper, specimenService, bookRepository);
    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"STAFF", "ADMIN"})
    void createSpecimenByBookId(Role role) throws Exception {
        //given
        Book book = Instancio.create(Book.class);
        Long bookId = book.getId();
        when(bookRepository.getReferenceById(bookId)).thenReturn(book);
        //when
        mockMvc.perform(post("/api/v1/books/" + bookId +"/specimens")
                        .cookie(cookieProvider.generateCookie(role)))
                .andDo(print())
                //then
                .andExpect(status().isOk());

        verify(specimenController).createSpecimenByBookId(bookId);
        verify(bookRepository).getReferenceById(bookId);
        verify(specimenMapper).toEntity(bookId);

        verify(specimenService).createSpecimen(specimenBorrowCaptor.capture());
        SpecimenBorrow specimenBorrowCaptured = specimenBorrowCaptor.getValue();

        assertNull(specimenBorrowCaptured.getId());
        assertEquals(book, specimenBorrowCaptured.getBook());
        assertNull(specimenBorrowCaptured.getUser());
        assertNull(specimenBorrowCaptured.getStartTime());
    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"STAFF", "ADMIN"})
    void createSpecimenByBookIdNotExists(Role role) throws Exception {
        //given
        Long bookId = new Random().nextLong();
        when(bookRepository.getReferenceById(bookId)).thenReturn(null);
        doThrow(BookNotExists.class).when(specimenService).createSpecimen(any());
        //when
        mockMvc.perform(post("/api/v1/books/" + bookId +"/specimens")
                        .cookie(cookieProvider.generateCookie(role)))
                .andDo(print())
                //then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Book not exists."));

        verify(specimenController).createSpecimenByBookId(bookId);
        verify(bookRepository).getReferenceById(bookId);
        verify(specimenMapper).toEntity(bookId);

        verify(specimenService).createSpecimen(specimenBorrowCaptor.capture());
        SpecimenBorrow specimenBorrowCaptured = specimenBorrowCaptor.getValue();

        assertNull(specimenBorrowCaptured.getId());
        assertNull(specimenBorrowCaptured.getBook());
        assertNull(specimenBorrowCaptured.getUser());
        assertNull(specimenBorrowCaptured.getStartTime());
    }

    @Test
    void createSpecimenByBookIdUnauthorized() throws Exception {
        //given
        Long bookId = new Random().nextLong();
        //when
        mockMvc.perform(post("/api/v1/books/" + bookId +"/specimens"))
                .andDo(print())
                //then
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(specimenController, specimenMapper, specimenService, bookRepository);
    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"USER"})
    void createSpecimenByBookIdForbidden(Role role) throws Exception {
        //given
        Long bookId = new Random().nextLong();
        //when
        mockMvc.perform(post("/api/v1/books/" + bookId +"/specimens")
                        .cookie(cookieProvider.generateCookie(role)))
                .andDo(print())
                //then
                .andExpect(status().isForbidden());

        verifyNoInteractions(specimenController, specimenMapper, specimenService, bookRepository);
    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"STAFF", "ADMIN"})
    void deleteSpecimen(Role role) throws Exception {
        //given
        Long id = new Random().nextLong();
        //when
        mockMvc.perform(delete("/api/v1/specimens/" + id)
                        .cookie(cookieProvider.generateCookie(role)))
                .andDo(print())
        //then
                .andExpect(status().isOk());

        verify(specimenController).deleteSpecimen(id);
        verify(specimenService).deleteSpecimenById(id);
    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"STAFF", "ADMIN"})
    void deleteSpecimenIsBorrowedBadRequest(Role role) throws Exception {
        //given
        SpecimenBorrow specimenBorrow = Instancio.create(SpecimenBorrow.class);
        Long id = specimenBorrow.getId();
        Long userId = specimenBorrow.getUser().getId();
        doThrow(new SpecimenDeleteException(specimenBorrow)).when(specimenService).deleteSpecimenById(id);
        //when
        mockMvc.perform(delete("/api/v1/specimens/" + id)
                .cookie(cookieProvider.generateCookie(role)))
                .andDo(print())
        //then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.href").value("http://localhost:8080/api/v1/users/" + userId));

        verify(specimenController).deleteSpecimen(id);
        verify(specimenService).deleteSpecimenById(id);
        verify(specimenExceptionHandler).deleteBorrowed(any());
    }

    @Test
    void deleteSpecimenUnauthorized() throws Exception {
        //given
        Long id = new Random().nextLong();
        //when
        mockMvc.perform(delete("/api/v1/specimens/" + id))
                .andDo(print())
        //then
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(specimenController, specimenMapper, specimenService, bookRepository);
    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"USER"})
    void deleteSpecimenForbidden(Role role) throws Exception {
        //given
        Long id = new Random().nextLong();
        //when
        mockMvc.perform(delete("/api/v1/specimens/" + id)
                        .cookie(cookieProvider.generateCookie(role)))
                .andDo(print())
                //then
                .andExpect(status().isForbidden());

        verifyNoInteractions(specimenController, specimenMapper, specimenService, bookRepository);
    }
}