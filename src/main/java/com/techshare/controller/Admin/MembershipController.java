package com.techshare.controller.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techshare.http.request.MembershipRequest;
import com.techshare.service.Membership.MembershipService;

@RestController
@RequestMapping("/admin/membership")
public class MembershipController {
    

    @Autowired
    private MembershipService membershipService;

    @PostMapping("/save")
    public ResponseEntity<?> createMembership(@RequestBody MembershipRequest membershipRequest){
        return new ResponseEntity<>(membershipService.createMembership(membershipRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMembershipById(@PathVariable Long id){
        return new ResponseEntity<>(membershipService.getMembershipById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMembership(@PathVariable Long id, @RequestBody MembershipRequest membershipRequest){
        return new ResponseEntity<>(membershipService.updateMembership(id, membershipRequest), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllMemberships(){
        return new ResponseEntity<>(membershipService.getAllMemberships(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMembership(@PathVariable Long id){
        membershipService.deleteMembership(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }   
    
    
    
}
