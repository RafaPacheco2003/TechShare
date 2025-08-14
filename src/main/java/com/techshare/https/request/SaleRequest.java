package com.techshare.https.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

public class SaleRequest {
    @NotNull
    @NotEmpty
    private Set<SaleDetailRequest> details;

    public Set<SaleDetailRequest> getDetails() {
        return details;
    }

    public void setDetails(Set<SaleDetailRequest> details) {
        this.details = details;
    }
}