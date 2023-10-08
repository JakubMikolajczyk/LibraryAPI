package com.Library.restAPI.mapper;

import com.Library.restAPI.dto.response.BorrowHistoryDto;
import com.Library.restAPI.model.BorrowHistory;


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
}
