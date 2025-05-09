package com.techshare.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techshare.entities.Membership;

public interface MembershipRepository extends JpaRepository<Membership, Long>{
    
}
