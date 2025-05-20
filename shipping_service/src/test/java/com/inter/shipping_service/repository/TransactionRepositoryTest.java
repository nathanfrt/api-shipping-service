package com.inter.shipping_service.repository;

import com.inter.shipping_service.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    private Transaction transaction1;
    private Transaction transaction2;

    @BeforeEach
    void setUp() {
        transaction1 = new Transaction();
        transaction1.setAmount(100.0);
        transaction1.setTransactionBy("11111111111");
        transaction1.setTransactionTo("22222222222");
        transaction1.setCreatedAt(LocalDate.now());
        transaction1.setLimitUsed(100.0);

        transaction2 = new Transaction();
        transaction2.setAmount(200.0);
        transaction2.setTransactionBy("11111111111");
        transaction2.setTransactionTo("33333333333");
        transaction2.setCreatedAt(LocalDate.now());
        transaction2.setLimitUsed(200.0);

        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);
    }

    @Test
    @DisplayName("Should return true if transactions exist by sender")
    void shouldExistByTransactionBy() {
        assertTrue(transactionRepository.existsByTransactionBy("11111111111"));
        assertFalse(transactionRepository.existsByTransactionBy("99999999999"));
    }

    @Test
    @DisplayName("Should return true if transactions exist to receiver")
    void shouldExistByTransactionTo() {
        assertTrue(transactionRepository.existsByTransactionTo("22222222222"));
        assertFalse(transactionRepository.existsByTransactionTo("88888888888"));
    }

    @Test
    @DisplayName("Should find all transactions made by user")
    void shouldFindAllByTransactionBy() {
        List<Transaction> result = transactionRepository.findAllByTransactionBy("11111111111");
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Should find all transactions received by user")
    void shouldFindAllByTransactionTo() {
        List<Transaction> result = transactionRepository.findAllByTransactionTo("22222222222");
        assertEquals(1, result.size());
        assertEquals("22222222222", result.get(0).getTransactionTo());
    }

    @Test
    @DisplayName("Should return the daily limit used for a user")
    void shouldCalculateLimitDay() {
        Double total = transactionRepository.limitDay("11111111111", LocalDate.now());
        assertEquals(300.0, total);
    }

    @Test
    @DisplayName("Should return 0 if no transactions are found for that day")
    void shouldReturnZeroWhenNoTransactionsOnDay() {
        Double total = transactionRepository.limitDay("00000000000", LocalDate.now());
        assertEquals(0.0, total);
    }
}
