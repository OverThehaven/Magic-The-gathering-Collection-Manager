package com.domenicopisano.mtgcollectionmanager.controller;

import com.domenicopisano.mtgcollectionmanager.dto.UserEnabledUpdateRequest;
import com.domenicopisano.mtgcollectionmanager.dto.UserProfileCreateRequest;
import com.domenicopisano.mtgcollectionmanager.dto.UserProfileResponse;
import com.domenicopisano.mtgcollectionmanager.service.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserProfileResponse createUser(@Valid @RequestBody UserProfileCreateRequest request) {
        return userProfileService.createUser(request);
    }

    @GetMapping
    public List<UserProfileResponse> getAllUsers() {
        return userProfileService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserProfileResponse getUserById(@PathVariable Long id) {
        return userProfileService.getUserById(id);
    }

    @PatchMapping("/{id}/enabled")
    public UserProfileResponse updateEnabledStatus(
            @PathVariable Long id,
            @Valid @RequestBody UserEnabledUpdateRequest request
    ) {
        return userProfileService.updateEnabledStatus(id, request);
    }
}