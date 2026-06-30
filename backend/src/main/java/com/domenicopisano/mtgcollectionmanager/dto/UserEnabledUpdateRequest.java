package com.domenicopisano.mtgcollectionmanager.dto;

import jakarta.validation.constraints.NotNull;

public record UserEnabledUpdateRequest(
        @NotNull(message = "Enabled value is required")
        Boolean enabled
) {
}