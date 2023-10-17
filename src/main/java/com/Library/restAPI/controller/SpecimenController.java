package com.Library.restAPI.controller;

import com.Library.restAPI.dto.request.SpecimenRequest;
import com.Library.restAPI.dto.response.SpecimenDto;
import com.Library.restAPI.mapper.SpecimenMapper;
import com.Library.restAPI.service.SpecimenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/")
public class SpecimenController {

    private final SpecimenService specimenService;
    private final SpecimenMapper specimenMapper;

    @GetMapping("specimens/{id}")
    public SpecimenDto getSpecimenById(@PathVariable("id") Long id){
        return SpecimenMapper.toDto(specimenService.getSpecimenById(id));
    }

    @GetMapping("specimens")
    public List<SpecimenDto> getAllSpecimen(){
        return specimenService
                .getAllSpecimen()
                .stream()
                .map(SpecimenMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping("specimens")
    public void createSpecimen(@RequestBody SpecimenRequest specimenRequest){
        specimenService.createSpecimen(specimenMapper.toEntity(specimenRequest));
    }

    @PostMapping("books/{bookId}/specimens")
    public void createSpecimenByBookId(@PathVariable("bookId") Long bookId){
        specimenService.createSpecimen(specimenMapper.toEntity(bookId));
    }

    @DeleteMapping("specimens/{id}")
    public void deleteSpecimen(@PathVariable("id") Long id){
        specimenService.deleteSpecimenById(id);
    }
}
