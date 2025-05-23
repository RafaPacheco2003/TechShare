package com.techshare.http.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

public class SaleRequest {
    @NotNull
    private Long user_id;
    
    @NotEmpty
    private Set<SaleDetailRequest> details;

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Set<SaleDetailRequest> getDetails() {
        return details;
    }

    public void setDetails(Set<SaleDetailRequest> details) {
        this.details = details;
    }
} 