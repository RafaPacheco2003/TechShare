package com.techshare.convert.Membership;

import com.techshare.DTO.MembershipDTO;
import com.techshare.entities.Membership;
import com.techshare.http.request.MembershipRequest;

public interface ConvertMembership {
    Membership convertMembershipRequestToMembership(MembershipRequest membershipRequest);
    void convertUpdateMembershipRequestToMembership(MembershipRequest membershipRequest, Membership existingMembership);
    MembershipDTO convertMembershipToMembershipDTO(Membership membership);
}
