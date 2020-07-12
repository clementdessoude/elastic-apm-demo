package fr.clementdessoude.product.service;

import fr.clementdessoude.product.exception.UnknownCustomerException;
import fr.clementdessoude.product.model.Customer;
import fr.clementdessoude.product.model.Transaction;
import fr.clementdessoude.product.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public Long getAmountSpentByCustomer(String customerId)
        throws UnknownCustomerException {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);

        if (customerOptional.isEmpty()) {
            throw new UnknownCustomerException();
        }

        Customer customer = customerOptional.get();
        List<Transaction> transactions = customer.getTransactions();
        return transactions.stream().map(Transaction::getAmountInCents).reduce(0L, Long::sum);
    }

    public Long getAmountSpentByCustomerUnderAge(Long age) {
        LocalDate date = LocalDate.now().minusYears(age);

        List<Customer> customers = customerRepository.findByBirthDateAfter(date);

        return customers
            .stream()
            .map(customer -> customer.getTransactions().stream().map(Transaction::getAmountInCents).reduce(0L, Long::sum))
            .reduce(0L, Long::sum);
    }

    public Long getAmountSpentByCustomerUnderAgeV2(Long age) {
        LocalDate date = LocalDate.now().minusYears(age);
        return customerRepository.getTotalAmount(date);
    }
}
