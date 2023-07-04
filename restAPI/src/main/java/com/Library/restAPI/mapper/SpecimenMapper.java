package com.Library.restAPI.mapper;

import com.Library.restAPI.dto.request.SpecimenRequest;
import com.Library.restAPI.dto.response.Link;
import com.Library.restAPI.dto.response.SpecimenDto;
import com.Library.restAPI.model.Borrow;
import com.Library.restAPI.model.Specimen;
import com.Library.restAPI.repository.BookRepository;
import com.Library.restAPI.repository.BorrowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class SpecimenMapper {
    private final BorrowRepository borrowRepository;
    private final BookRepository bookRepository;


    public List<SpecimenDto> toDto(List<Specimen> specimenList){
        List<Long> specimenId = specimenList.stream().map(Specimen::getId).collect(Collectors.toList());
        HashSet<Long> borrowIds = (HashSet<Long>) borrowRepository
                .findAllById(specimenId)
                .stream()
                .map(Borrow::getId)
                .collect(Collectors.toSet());

        return specimenList
                .stream()
                .map(specimen -> SpecimenDto.builder()
                        .id(specimen.getId())
                        .book(new Link(specimen.getBook().getId(), "api/v1/books/"))
                        .borrow(borrowIds.contains(specimen.getId()) ?
                                new Link(specimen.getId(), "api/v1/borrows/"): null)
                        .build())
                .collect(Collectors.toList());
    }

    public SpecimenDto toDto(Specimen specimen){
        return SpecimenDto.builder()
                .id(specimen.getId())
                .book(new Link(specimen.getBook().getId(), "api/v1/books/"))
                .borrow(borrowRepository.existsById(specimen.getId()) ?
                        new Link(specimen.getId(), "api/v1/borrows/"): null)
                .build();
    }

    public Specimen toEntity(SpecimenRequest specimenRequest){
        return Specimen.builder()
                .book(bookRepository.getReferenceById(specimenRequest.bookId()))
                .build();
    }

    public Specimen toEntity(Long bookId){
        return Specimen.builder()
                .book(bookRepository.getReferenceById(bookId))
                .build();
    }
}
