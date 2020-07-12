package fr.clementdessoude.product.controller;

import fr.clementdessoude.product.exception.UnknownCustomerException;
import fr.clementdessoude.product.service.CustomerService;
import fr.clementdessoude.product.security.AuthoritiesConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("customer")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/{userId}/transactions/sum")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ROLE_ADMIN + "\")")
    public ResponseEntity storeConsentInformation(@PathVariable String userId) {
        try {
            return ResponseEntity.ok(customerService.getAmountSpentByCustomer(userId));
        } catch (UnknownCustomerException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("below/{age}/transactions/sum")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ROLE_ADMIN + "\")")
    public ResponseEntity getTotalAmount(@PathVariable Long age) {
        return ResponseEntity.ok(customerService.getAmountSpentByCustomerUnderAge(age));
    }

    @GetMapping("below/{age}/transactions/sum/v2")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ROLE_ADMIN + "\")")
    public ResponseEntity getTotalAmountV2(@PathVariable Long age) {
        return ResponseEntity.ok(customerService.getAmountSpentByCustomerUnderAgeV2(age));
    }
}
