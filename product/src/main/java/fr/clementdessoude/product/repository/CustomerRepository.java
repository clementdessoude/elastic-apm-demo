package fr.clementdessoude.product.repository;

import fr.clementdessoude.product.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    Optional<Customer> findById(String customerId);
    List<Customer> findByBirthDateAfter(LocalDate date);
    @Query(
        "SELECT sum(t.amountInCents) FROM Transaction t JOIN t.customer c WHERE c.birthDate > :date"
    )
    Long getTotalAmount(@Param("date") LocalDate date);
}
