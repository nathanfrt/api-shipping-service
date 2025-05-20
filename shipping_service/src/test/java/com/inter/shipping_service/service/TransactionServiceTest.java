package com.inter.shipping_service.service;

import com.inter.shipping_service.dto.TransactionDto;
import com.inter.shipping_service.exception.InsufficientBalance;
import com.inter.shipping_service.exception.NotExist;
import com.inter.shipping_service.exception.TransactionFail;
import com.inter.shipping_service.model.Transaction;
import com.inter.shipping_service.model.TypeUser;
import com.inter.shipping_service.model.User;
import com.inter.shipping_service.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserService userService;

    @Mock
    private ExchangeService exchangeService;

    private User userPF;
    private User userPJ;

    private TransactionDto transactionDto;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        userPF = new User();
        userPF.setDocumentNumber("12345678901");
        userPF.setType(TypeUser.PF);
        userPF.setBalanceReal(2000.0);
        userPF.setBalanceDollar(500.0);

        userPJ = new User();
        userPJ.setDocumentNumber("99999999999");
        userPJ.setType(TypeUser.PJ);
        userPJ.setBalanceReal(60000.0);
        userPJ.setBalanceDollar(20000.0);

        transactionDto = new TransactionDto(100.0, userPF.getDocumentNumber(), userPJ.getDocumentNumber());

        when(userService.getUserByDocumentNumber(userPF.getDocumentNumber())).thenReturn(userPF);
        when(userService.getUserByDocumentNumber(userPJ.getDocumentNumber())).thenReturn(userPJ);
        when(userService.getBalanceRealByDocumentNumber(userPF.getDocumentNumber())).thenReturn(2000.0);
        when(userService.getBalanceRealByDocumentNumber(userPJ.getDocumentNumber())).thenReturn(60000.0);
        when(userService.getBalanceDollarByDocumentNumber(userPF.getDocumentNumber())).thenReturn(500.0);
        when(userService.getBalanceDollarByDocumentNumber(userPJ.getDocumentNumber())).thenReturn(20000.0);
    }

    @Test
    void getTransactionsByUser_success() {
        when(transactionRepository.existsByTransactionBy(userPF.getDocumentNumber())).thenReturn(true);
        when(transactionRepository.findAllByTransactionBy(userPF.getDocumentNumber())).thenReturn(List.of(new Transaction()));

        var result = transactionService.getTransactionsByUser(userPF.getDocumentNumber());

        assertFalse(result.isEmpty());
        verify(transactionRepository).findAllByTransactionBy(userPF.getDocumentNumber());
    }

    @Test
    void getTransactionsByUser_notFound() {
        when(transactionRepository.existsByTransactionBy(userPF.getDocumentNumber())).thenReturn(false);
        assertThrows(NotExist.class, () -> transactionService.getTransactionsByUser(userPF.getDocumentNumber()));
    }

    @Test
    void transactionBRLToBRL_success() {
        Transaction transaction = new Transaction(transactionDto);
        when(transactionRepository.save(any())).thenReturn(transaction);

        var result = transactionService.transactionBRLToBRL(transactionDto);

        assertNotNull(result);
        verify(transactionRepository).save(any());
    }

    @Test
    void transactionBRLToBRL_insufficientBalance() {
        when(userService.getBalanceRealByDocumentNumber(userPF.getDocumentNumber())).thenReturn(10.0);
        assertThrows(InsufficientBalance.class, () -> transactionService.transactionBRLToBRL(transactionDto));
    }

    @Test
    void limitExceeded_passedForPF() {
        when(transactionRepository.limitDay(userPF.getDocumentNumber(), LocalDate.now())).thenReturn(200.0);

        double result = transactionService.limitExceeded(userPF.getDocumentNumber(), 100.0);
        assertTrue(result <= 10000.0);
    }

    @Test
    void limitExceeded_throwsForExceedLimit() {
        when(transactionRepository.limitDay(userPF.getDocumentNumber(), LocalDate.now())).thenReturn(9990.0);

        assertThrows(TransactionFail.class, () -> transactionService.limitExceeded(userPF.getDocumentNumber(), 100.0));
    }

    @Test
    void transactionBank_transactionByEqualsTransactionTo_throwException() {
        transactionDto = new TransactionDto(100.0, userPF.getDocumentNumber(), userPF.getDocumentNumber() );
        assertThrows(InsufficientBalance.class, () -> transactionService.transactionBank(transactionDto, "BRL", 0.0, false));
    }

    @Test
    void transactionBank_invalidType_throwException() {
        assertThrows(InsufficientBalance.class, () -> transactionService.transactionBank(transactionDto, "XXX", 0.0, false));
    }

    @Test
    void cancelledTransaction_updatesBalance() {
        transactionService.cancelledTransaction(userPF.getDocumentNumber(), 200.0);
        verify(userService).save(argThat(user -> user.getBalanceReal() == 2200.0));
    }
}
