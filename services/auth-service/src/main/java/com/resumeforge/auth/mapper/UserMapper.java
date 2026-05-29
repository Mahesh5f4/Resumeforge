package com.resumeforge.auth.mapper;

import com.resumeforge.auth.dto.response.UserResponse;
import com.resumeforge.auth.entity.User;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting User entity to UserResponse DTO
 */
@Component
public class UserMapper {

    /**
     * Convert User entity to UserResponse DTO
     */
    public UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }

        return UserResponse.builder()
            .id(user.getId())
            .email(user.getEmail())
            .active(user.getActive())
            .emailVerified(user.getEmailVerified())
            .createdAt(user.getCreatedAt())
            .updatedAt(user.getUpdatedAt())
            .build();
    }
}
