package com.Library.restAPI.mapper;

import com.Library.restAPI.dto.request.BorrowHistoryEditRequest;
import com.Library.restAPI.dto.response.BorrowHistoryDto;
import com.Library.restAPI.model.BorrowHistory;
import org.springframework.stereotype.Component;

@Component
public class BorrowHistoryMapper {

    public static BorrowHistoryDto toDto(BorrowHistory borrowHistory){
        return BorrowHistoryDto.builder()
                .id(borrowHistory.getId())
                .book(LinkMapper.toLink(borrowHistory.getBook()))
                .user(LinkMapper.toLink(borrowHistory.getUser()))
                .start(borrowHistory.getStartTime())
                .end(borrowHistory.getEndTime())
                .hidden(borrowHistory.isHidden())
                .build();
    }

    public BorrowHistory toEntity(BorrowHistoryEditRequest borrowHistoryEditRequest, Long id){
        return BorrowHistory.builder()
                .id(id)
                .hidden(borrowHistoryEditRequest.hidden())
                .build();
    }
}
