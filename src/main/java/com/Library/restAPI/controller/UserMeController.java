package com.Library.restAPI.controller;

import com.Library.restAPI.dto.request.PasswordChangeRequest;
import com.Library.restAPI.dto.request.UserEditRequest;
import com.Library.restAPI.dto.response.BorrowDto;
import com.Library.restAPI.dto.response.BorrowHistoryDto;
import com.Library.restAPI.dto.response.UserDto;
import com.Library.restAPI.mapper.BorrowHistoryMapper;
import com.Library.restAPI.mapper.BorrowMapper;
import com.Library.restAPI.mapper.UserMapper;
import com.Library.restAPI.security.UsernameAndIdPrincipal;
import com.Library.restAPI.service.AuthService;
import com.Library.restAPI.service.BorrowHistoryService;
import com.Library.restAPI.service.BorrowService;
import com.Library.restAPI.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/")
public class UserMeController {

    private final UserService userService;
    private final BorrowService borrowService;
    private final AuthService authService;
    private final UserMapper userMapper;

    @GetMapping("users/me")
    public UserDto getMe(){
        return UserMapper.toDto(userService.getUserById(UsernameAndIdPrincipal.getIdFromSecurityCtx()));
    }

    @PutMapping("users/me")
    public void editMe(@RequestBody UserEditRequest userEditRequest){
        userService.updateUser(userMapper.toEntity(UsernameAndIdPrincipal.getIdFromSecurityCtx(), userEditRequest));
    }

    @GetMapping("users/me/borrows")
    public List<BorrowDto> getMyBorrows(){
        return borrowService
                .getAllBorrowsByUserId(UsernameAndIdPrincipal.getIdFromSecurityCtx())
                .stream()
                .map(BorrowMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping("users/me/change-password")
    public void changePassword(HttpServletResponse response, PasswordChangeRequest passwordChangeRequest){
        authService.changePassword(
                UsernameAndIdPrincipal.getIdFromSecurityCtx(),
                passwordChangeRequest,
                response);
    }
}
