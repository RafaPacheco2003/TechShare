package com.techshare.http.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record MembershipRequest(
        @NotBlank @Size(max = 100) String name,
        @Size(max = 500) String description,

        @NotNull @Size(max = 2)   Integer percentage,

        @NotNull @PositiveOrZero Integer defaultDurationDays
) {}
