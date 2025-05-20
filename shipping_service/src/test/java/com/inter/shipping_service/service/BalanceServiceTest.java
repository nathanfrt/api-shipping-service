package com.inter.shipping_service.service;

import com.inter.shipping_service.dto.BalanceDto;
import com.inter.shipping_service.exception.InvalidDocument;
import com.inter.shipping_service.model.Balance;
import com.inter.shipping_service.model.TypeUser;
import com.inter.shipping_service.model.User;
import com.inter.shipping_service.repository.BalanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BalanceServiceTest {

    @InjectMocks
    private BalanceService balanceService;

    @Mock
    private BalanceRepository balanceRepository;

    @Mock
    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateBalance_Success() {

        String documentNumber = "12345678900";
        Double initialBalance = 100.0;
        Double depositAmount = 50.0;

        BalanceDto balanceDto = new BalanceDto(documentNumber, depositAmount);
        User user = new User(1, "Inter", "123@Inter", "inter@test.com", documentNumber, TypeUser.PF, initialBalance, 0.0);

        when(userService.existsUserByDocumentNumber(documentNumber)).thenReturn(true);
        when(userService.getUserByDocumentNumber(documentNumber)).thenReturn(user);

        balanceService.updateBalance(balanceDto);

        assertEquals(150.0, user.getBalanceReal());
        verify(userService).save(user);
        verify(balanceRepository).save(any(Balance.class));
    }

    @Test
    void updateBalance_ThrowsException() {

        String doc = "99999999999";
        BalanceDto balanceDto = new BalanceDto(doc, 100.0);

        when(userService.existsUserByDocumentNumber(doc)).thenReturn(false);

        assertThrows(InvalidDocument.class, () -> balanceService.updateBalance(balanceDto));
        verify(userService, never()).getUserByDocumentNumber(anyString());
        verify(balanceRepository, never()).save(any());
    }
}
