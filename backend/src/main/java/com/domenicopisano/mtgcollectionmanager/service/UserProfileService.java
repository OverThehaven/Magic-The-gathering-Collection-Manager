package com.domenicopisano.mtgcollectionmanager.service;

import com.domenicopisano.mtgcollectionmanager.dto.UserEnabledUpdateRequest;
import com.domenicopisano.mtgcollectionmanager.dto.UserProfileCreateRequest;
import com.domenicopisano.mtgcollectionmanager.dto.UserProfileResponse;
import com.domenicopisano.mtgcollectionmanager.exception.BadRequestException;
import com.domenicopisano.mtgcollectionmanager.exception.ResourceNotFoundException;
import com.domenicopisano.mtgcollectionmanager.model.entity.UserProfile;
import com.domenicopisano.mtgcollectionmanager.model.enums.UserRole;
import com.domenicopisano.mtgcollectionmanager.repository.UserProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Transactional
    public UserProfileResponse createUser(UserProfileCreateRequest request) {
        if (userProfileRepository.existsByUsername(request.username())) {
            throw new BadRequestException("Username already exists");
        }

        if (userProfileRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Email already exists");
        }

        UserProfile userProfile = new UserProfile();
        userProfile.setUsername(request.username());
        userProfile.setEmail(request.email());
        userProfile.setRole(request.role() != null ? request.role() : UserRole.USER);
        userProfile.setEnabled(true);

        UserProfile savedUser = userProfileRepository.save(userProfile);

        return toResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public List<UserProfileResponse> getAllUsers() {
        return userProfileRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getUserById(Long id) {
        UserProfile userProfile = getUserEntityById(id);
        return toResponse(userProfile);
    }

    @Transactional
    public UserProfileResponse updateEnabledStatus(Long id, UserEnabledUpdateRequest request) {
        UserProfile userProfile = getUserEntityById(id);
        userProfile.setEnabled(request.enabled());

        return toResponse(userProfile);
    }

    @Transactional(readOnly = true)
    public UserProfile getUserEntityById(Long id) {
        return userProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    private UserProfileResponse toResponse(UserProfile userProfile) {
        return new UserProfileResponse(
                userProfile.getId(),
                userProfile.getUsername(),
                userProfile.getEmail(),
                userProfile.getRole(),
                userProfile.isEnabled(),
                userProfile.getCreatedAt()
        );
    }
}