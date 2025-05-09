package com.techshare.service.Membership;

import java.util.List;
import java.util.Optional;

import com.techshare.DTO.MembershipDTO;
import com.techshare.http.request.MembershipRequest;

public interface MembershipService {

    MembershipDTO createMembership(MembershipRequest membershipRequest);

    Optional<MembershipDTO> getMembershipById(Long id);

    Optional<MembershipDTO> updateMembership(Long id, MembershipRequest membershipRequest);

    List<MembershipDTO> getAllMemberships();

    void deleteMembership(Long id);
    
} 
