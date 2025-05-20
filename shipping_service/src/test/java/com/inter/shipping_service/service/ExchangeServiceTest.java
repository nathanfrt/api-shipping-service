package com.inter.shipping_service.service;

import com.inter.shipping_service.model.TypeUser;
import com.inter.shipping_service.model.User;
import com.inter.shipping_service.repository.ExchangeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExchangeServiceTest {

    @InjectMocks
    private ExchangeService exchangeService;

    @Mock
    private UserService userService;

    private final String doc = "12345678900";

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void conversionCurrency_Success() {
        double quote = 5.0;
        double amount = 10.0;
        double initialReal = 100.0;
        double initialDollar = 20.0;

        User user = new User(1, "Inter", "123@Inter", "inter@test.com", doc, TypeUser.PF, initialReal, initialDollar);

        when(userService.getBalanceRealByDocumentNumber(doc)).thenReturn(initialReal);
        when(userService.getBalanceDollarByDocumentNumber(doc)).thenReturn(initialDollar);
        when(userService.getUserByDocumentNumber(doc)).thenReturn(user);

        Double transferAmount = exchangeService.conversionCurrency(doc, amount, quote);

        assertEquals(20.0, transferAmount);
        assertEquals(80.0, user.getBalanceReal());
        assertEquals(30.0, user.getBalanceDollar());
        verify(userService).save(user);
    }

    @Test
    void existsBalanceToTransactionUSD_True_WhenSufficient() {
        when(userService.getBalanceDollarByDocumentNumber(doc)).thenReturn(100.0);

        boolean result = exchangeService.existsBalanceToTransactionUSD(doc, 10.0, 5.0);
        assertTrue(result);
    }

    @Test
    void existsBalanceToTransactionUSD_False_WhenInsufficient() {
        when(userService.getBalanceDollarByDocumentNumber(doc)).thenReturn(20.0);

        boolean result = exchangeService.existsBalanceToTransactionUSD(doc, 10.0, 5.0);
        assertFalse(result);
    }

    @Test
    void formatDate_IfWeekday_ShouldReturnSameDateFormatted() {
        LocalDate weekday = LocalDate.of(2025, 5, 14); // quarta-feira
        String formatted = exchangeService.formatDate(weekday);
        assertEquals("05-14-2025", formatted);
    }

    @Test
    void formatDate_IfWeekend_ShouldReturnPreviousWeekdayFormatted() {
        LocalDate sunday = LocalDate.of(2025, 5, 18); // domingo
        String formatted = exchangeService.formatDate(sunday);
        assertEquals("05-16-2025", formatted); // sexta-feira
    }
}
