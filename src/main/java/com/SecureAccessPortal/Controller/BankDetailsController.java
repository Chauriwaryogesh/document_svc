package com.SecureAccessPortal.Controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.SecureAccessPortal.Modal.BankDetailsDTO;
import com.SecureAccessPortal.Service.BankDetailsService;

import java.util.List;

@RestController
@RequestMapping("/bankService")
public class BankDetailsController {

    private final BankDetailsService bankDetailsService;

    public BankDetailsController(BankDetailsService bankDetailsService) {
        this.bankDetailsService = bankDetailsService;
    }

    @GetMapping(value="/bank-details")
    public ResponseEntity<List<BankDetailsDTO>> getAllBankDetails() {
        List<BankDetailsDTO> bankDetails = bankDetailsService.getAllBankDetails();
        return ResponseEntity.ok(bankDetails);
    }
}
