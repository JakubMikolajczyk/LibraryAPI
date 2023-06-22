package com.Library.restAPI.controller;

import com.Library.restAPI.dto.UserDto;
import com.Library.restAPI.dto.UserEditAdminRequest;
import com.Library.restAPI.dto.UserEditRequest;
import com.Library.restAPI.mapper.UserMapper;
import com.Library.restAPI.security.UsernameAndIdPrincipal;
import com.Library.restAPI.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/me")
    public UserDto getMe(){
        return userMapper.toDto(userService.getUserById(UsernameAndIdPrincipal.getIdFromSecurityCtx()));
    }

    @PutMapping("/me")
    public void editMe(@RequestBody UserEditRequest userEditRequest){
        userService.updateUser(userMapper.toEntity(UsernameAndIdPrincipal.getIdFromSecurityCtx(), userEditRequest));
    }

    @GetMapping
    public List<UserDto> getAllUsers(){
        return userService.getAllUsers().stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id){
        return userMapper.toDto(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public void editUser(@PathVariable Long id, @RequestBody UserEditAdminRequest userEditAdminRequest){
        userService.updateUser(userMapper.toEntity(id, userEditAdminRequest));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){
        userService.deleteUserById(id);
    }
}
