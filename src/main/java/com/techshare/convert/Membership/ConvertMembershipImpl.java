package com.techshare.convert.Membership;

import org.springframework.stereotype.Service;

import com.techshare.DTO.MembershipDTO;
import com.techshare.entities.Membership;
import com.techshare.http.request.MembershipRequest;

@Service
public class ConvertMembershipImpl implements ConvertMembership {

    @Override
    public Membership convertMembershipRequestToMembership(MembershipRequest membershipRequest) {
        Membership membership = new Membership();
        membership.setName(membershipRequest.name());
        membership.setDescription(membershipRequest.description());
        membership.setDefaultDurationDays(membershipRequest.defaultDurationDays());
        return membership;
    }

    @Override
    public void convertUpdateMembershipRequestToMembership(MembershipRequest membershipRequest,
            Membership existingMembership) {
        existingMembership.setName(membershipRequest.name());
        existingMembership.setDescription(membershipRequest.description());
        existingMembership.setDefaultDurationDays(membershipRequest.defaultDurationDays());
    }

    @Override
    public MembershipDTO convertMembershipToMembershipDTO(Membership membership) {
        MembershipDTO membershipDTO = new MembershipDTO();
        membershipDTO.setMembership_id(membership.getMembership_id());
        membershipDTO.setName(membership.getName());
        membershipDTO.setDescription(membership.getDescription());
        membershipDTO.setDefaultDurationDays(membership.getDefaultDurationDays());
        return membershipDTO;
    }

}