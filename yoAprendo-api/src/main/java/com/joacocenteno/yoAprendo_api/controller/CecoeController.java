package com.joacocenteno.yoAprendo_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.joacocenteno.yoAprendo_api.dto.CecoeRequest;
import com.joacocenteno.yoAprendo_api.dto.CecoeResponse;
import com.joacocenteno.yoAprendo_api.dto.UserResponse;
import com.joacocenteno.yoAprendo_api.model.UserRol;
import com.joacocenteno.yoAprendo_api.service.ICecoeService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/cecoes")
public class CecoeController {

    @Autowired
    ICecoeService cecoe_serv;

    @GetMapping
    public ResponseEntity<List<CecoeResponse>> getAllCecoesController() {
        return ResponseEntity.status(HttpStatus.OK).body(cecoe_serv.getAllCecoe());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CecoeResponse> getCecoeByIdController(@RequestParam Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(cecoe_serv.getCecoeById(id));
    }

    @PostMapping
    public ResponseEntity<CecoeResponse> createCecoeController(@Valid @RequestBody CecoeRequest cecoe_request) {
        
        CecoeResponse cecoe_created = cecoe_serv.createCecoe(cecoe_request);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(cecoe_created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CecoeResponse> editCecoeController(@PathVariable Long id, @Valid @RequestBody CecoeRequest cecoe_request) {
        
        return ResponseEntity.status(HttpStatus.OK).body(cecoe_serv.editCecoe(id, cecoe_request));
    }

    @PatchMapping("/{id}/toggle-active")
    public ResponseEntity<Void> toggleActiveController(@PathVariable Long id) {

        cecoe_serv.toggleActiveCecoe(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<List<UserResponse>> getUsersByCecoe(@PathVariable Long id) {
        
        return ResponseEntity.status(HttpStatus.OK).body(cecoe_serv.getUsersByCecoe(id));
    }

    @GetMapping("/{id}/users/{role}")
    public ResponseEntity<List<UserResponse>> getMethodName(@PathVariable Long id, @RequestParam UserRol role) {
        
        return ResponseEntity.status(HttpStatus.OK).body(cecoe_serv.getUsersByCecoeAndRole(id, role));
    }
    
    
    
}
