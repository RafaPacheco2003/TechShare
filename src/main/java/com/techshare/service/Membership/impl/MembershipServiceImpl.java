package com.techshare.service.Membership.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techshare.DTO.MembershipDTO;
import com.techshare.convert.Membership.ConvertMembership;
import com.techshare.entities.Membership;
import com.techshare.exception.MembershipNotFoundException;
import com.techshare.http.request.MembershipRequest;
import com.techshare.repositories.MembershipRepository;
import com.techshare.service.Membership.MembershipService;

@Service
public class MembershipServiceImpl implements MembershipService {

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private ConvertMembership convertMembership;

    @Override
    public MembershipDTO createMembership(MembershipRequest membershipRequest) {
        Membership membership = convertMembership.convertMembershipRequestToMembership(membershipRequest);
        membershipRepository.save(membership);
        return convertMembership.convertMembershipToMembershipDTO(membership);
    }

    @Override
    public Optional<MembershipDTO> getMembershipById(Long id) {
        Membership membership = membershipRepository.findById(id)
                .orElseThrow(() -> new MembershipNotFoundException(id));
        
        MembershipDTO dto = convertMembership.convertMembershipToMembershipDTO(membership);
        return Optional.of(dto);
    }

    @Override
    public Optional<MembershipDTO> updateMembership(Long id, MembershipRequest membershipRequest) {
        Membership existingMembership = membershipRepository.findById(id)
                .orElseThrow(() -> new MembershipNotFoundException(id));
        
        // Actualizar los campos con los valores de la solicitud
        convertMembership.convertUpdateMembershipRequestToMembership(membershipRequest, existingMembership);
        
        Membership updatedMembership = membershipRepository.save(existingMembership);
        MembershipDTO dto = convertMembership.convertMembershipToMembershipDTO(updatedMembership);
        return Optional.of(dto);
    }

    @Override
    public List<MembershipDTO> getAllMemberships() {
        return membershipRepository.findAll().stream()
                .map(convertMembership::convertMembershipToMembershipDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteMembership(Long id) {
        if (!membershipRepository.existsById(id)) {
            throw new MembershipNotFoundException(id);
        }
        membershipRepository.deleteById(id);
    }
}
