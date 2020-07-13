package fr.clementdessoude.accounting.controller;

import fr.clementdessoude.accounting.client.ProductClient;
import fr.clementdessoude.accounting.security.AuthoritiesConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("accounting")
@RequiredArgsConstructor
public class AccountingController {
    private final ProductClient productClient;

    @GetMapping("/customers/{age}/report")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ROLE_ADMIN + "\")")
    public Long storeConsentInformation(@PathVariable Long age) {
        return productClient.getAmountOrderedByCustomerBelowAge(age);
    }

    @GetMapping("/customers/{age}/report/v2")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ROLE_ADMIN + "\")")
    public Long storeConsentInformationV2(@PathVariable Long age) {
        return productClient.getAmountOrderedByCustomerBelowAgeV2(age);
    }
}
