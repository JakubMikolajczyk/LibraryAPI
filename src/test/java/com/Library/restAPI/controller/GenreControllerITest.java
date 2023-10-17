package com.Library.restAPI.controller;

import com.Library.restAPI.CookieProvider;
import com.Library.restAPI.config.ApplicationConfig;
import com.Library.restAPI.config.SecurityConfiguration;
import com.Library.restAPI.dto.request.GenreRequest;
import com.Library.restAPI.exception.GenreDeleteException;
import com.Library.restAPI.exception.GenreNotFoundException;
import com.Library.restAPI.exceptionHandler.GenreExceptionHandler;
import com.Library.restAPI.exceptionHandler.GlobalExceptionHandler;
import com.Library.restAPI.mapper.GenreMapper;
import com.Library.restAPI.model.Genre;
import com.Library.restAPI.model.Role;
import com.Library.restAPI.service.GenreService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {
        GenreController.class,
        SecurityConfiguration.class,
        ApplicationConfig.class
})
class GenreControllerITest {

    @Autowired
    private MockMvc mockMvc;

    private final CookieProvider cookieProvider;
    private final ObjectMapper objectMapper;

    @MockBean
    private GenreService genreService;

    @SpyBean
    private GenreMapper genreMapper;

    @SpyBean
    private GenreController genreController;

    @SpyBean
    private GlobalExceptionHandler globalExceptionHandler;

    @SpyBean
    private GenreExceptionHandler genreExceptionHandler;

    @Captor
    private ArgumentCaptor<Genre> genreCaptor;

    public GenreControllerITest() {
        this.cookieProvider = new CookieProvider();
        this.objectMapper = new ObjectMapper();
    }

    @ParameterizedTest
    @NullSource
    @EnumSource(Role.class)
    void getGenre(Role role) throws Exception {
        //given
        Genre genre = Instancio.create(Genre.class);
        Long id = genre.getId();
        when(genreService.getGenreById(id)).thenReturn(genre);
        //when
        mockMvc.perform(get("/api/v1/genres/" + id)
                        .cookie(cookieProvider.generateCookie(role)))
                .andDo(print())
        //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(genre.getName()))
                .andExpect(jsonPath("$.books.length()").value(genre.getBooks().size()));

        verify(genreController).getGenre(id);
        verify(genreService).getGenreById(id);
    }

    @ParameterizedTest
    @NullSource
    @EnumSource(Role.class)
    void getGenreNotFound(Role role) throws Exception {
        //given
        Long id = new Random().nextLong();
        doThrow(GenreNotFoundException.class).when(genreService).getGenreById(id);
        //then
        mockMvc.perform(get("/api/v1/genres/" + id)
                        .cookie(cookieProvider.generateCookie(role)))
                .andDo(print())
        //then
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Genre not found."));

        verify(genreController).getGenre(id);
        verify(genreService).getGenreById(id);
        verify(genreExceptionHandler).genreNotFound();
    }

    @ParameterizedTest
    @NullSource
    @EnumSource(Role.class)
    void getAllGenres(Role role) throws Exception {
        //given
        List<Genre> genresList = Instancio.ofList(Genre.class).size(10).create();
        when(genreService.getAllGenres()).thenReturn(genresList);
        //when
        mockMvc.perform(get("/api/v1/genres")
                        .cookie(cookieProvider.generateCookie(role)))
                .andDo(print())
        //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*").isArray())
                .andExpect(jsonPath("$.length()").value(10));

        verify(genreController).getAllGenres();
        verify(genreService).getAllGenres();
    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"STAFF", "ADMIN"})
    void createGenre(Role role) throws Exception {
        //given
        GenreRequest genreRequest = Instancio.create(GenreRequest.class);
        //when
        mockMvc.perform(post("/api/v1/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(genreRequest))
                        .cookie(cookieProvider.generateCookie(role)))
        //then
                .andExpect(status().isOk());

        verify(genreController).createGenre(genreRequest);
        verify(genreMapper).toEntity(genreRequest);

        verify(genreService).createGenre(genreCaptor.capture());
        Genre genreCaptured = genreCaptor.getValue();

        assertNull(genreCaptured.getId());
        assertEquals(genreRequest.name(), genreCaptured.getName());
        assertNull(genreCaptured.getBooks());
    }

    @Test
    void createGenreUnauthorized() throws Exception {
        //given
        GenreRequest genreRequest = Instancio.create(GenreRequest.class);
        //when
        mockMvc.perform(post("/api/v1/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(genreRequest)))
                .andDo(print())
        //then
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(genreController, genreMapper, genreService);
    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"USER"})
    void createGenreForbidden(Role role) throws Exception {
        //given
        GenreRequest genreRequest = Instancio.create(GenreRequest.class);
        //when
        mockMvc.perform(post("/api/v1/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(genreRequest))
                        .cookie(cookieProvider.generateCookie(role)))
                .andDo(print())
        //then
                .andExpect(status().isForbidden());

        verifyNoInteractions(genreController, genreMapper, genreService);
    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"STAFF", "ADMIN"})
    void editGenre(Role role) throws Exception {
        //given
        Long id = new Random().nextLong();
        GenreRequest genreRequest = Instancio.create(GenreRequest.class);
        //when
        mockMvc.perform(put("/api/v1/genres/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(genreRequest))
                        .cookie(cookieProvider.generateCookie(role)))
        //then
                .andExpect(status().isOk());

        verify(genreController).editGenre(id, genreRequest);
        verify(genreMapper).toEntity(id, genreRequest);

        verify(genreService).editGenre(genreCaptor.capture());
        Genre genreCaptured = genreCaptor.getValue();

        assertEquals(id, genreCaptured.getId());
        assertEquals(genreRequest.name(), genreCaptured.getName());
        assertNull(genreCaptured.getBooks());
    }

    @Test
    void editGenreUnauthorized() throws Exception {
        //given
        Long id = new Random().nextLong();
        GenreRequest genreRequest = Instancio.create(GenreRequest.class);
        //when
        mockMvc.perform(put("/api/v1/genres/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(genreRequest)))
                .andDo(print())
        //then
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(genreController, genreMapper, genreService);
    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"USER"})
    void editGenreForbidden(Role role) throws Exception {
        //given
        Long id = new Random().nextLong();
        GenreRequest genreRequest = Instancio.create(GenreRequest.class);
        //when
        mockMvc.perform(put("/api/v1/genres/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(genreRequest))
                        .cookie(cookieProvider.generateCookie(role)))
                .andDo(print())
        //then
                .andExpect(status().isForbidden());

        verifyNoInteractions(genreController, genreMapper, genreService);
    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"STAFF", "ADMIN"})
    void deleteGenre(Role role) throws Exception {
        //given
        Long id = new Random().nextLong();
        //when
        mockMvc.perform(delete("/api/v1/genres/" + id)
                        .cookie(cookieProvider.generateCookie(role)))
                .andDo(print())
        //then
                .andExpect(status().isOk());

        verify(genreController).deleteGenre(id);
        verify(genreService).deleteGenreById(id);
    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"STAFF", "ADMIN"})
    void deleteGenreRelatedBookBadRequest(Role role) throws Exception {
        //given
        Long id = new Random().nextLong();
        Genre genre = Instancio.create(Genre.class);
        doThrow(new GenreDeleteException(genre)).when(genreService).deleteGenreById(id);
        //when
        mockMvc.perform(delete("/api/v1/genres/" + id)
                        .cookie(cookieProvider.generateCookie(role)))
                .andDo(print())
        //then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.*").isArray())
                .andExpect(jsonPath("$.length()").value(genre.getBooks().size()));

        verify(genreController).deleteGenre(id);
        verify(genreService).deleteGenreById(id);
        verify(genreExceptionHandler).genreDelete(any());
    }

    @Test
    void deleteGenreUnauthorized() throws Exception {
        //given
        Long id = new Random().nextLong();
        GenreRequest genreRequest = Instancio.create(GenreRequest.class);
        //when
        mockMvc.perform(delete("/api/v1/genres/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(genreRequest)))
                .andDo(print())
        //then
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(genreController, genreMapper, genreService);
    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"USER"})
    void deleteGenreForbidden(Role role) throws Exception {
        //given
        Long id = new Random().nextLong();
        GenreRequest genreRequest = Instancio.create(GenreRequest.class);
        //when
        mockMvc.perform(delete("/api/v1/genres/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(genreRequest))
                        .cookie(cookieProvider.generateCookie(role)))
                .andDo(print())
        //then
                .andExpect(status().isForbidden());

        verifyNoInteractions(genreController, genreMapper, genreService);
    }
}