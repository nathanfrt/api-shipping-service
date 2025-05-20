package com.inter.shipping_service.service;

import com.inter.shipping_service.dto.UserDto;
import com.inter.shipping_service.exception.InvalidDocument;
import com.inter.shipping_service.exception.NotExist;
import com.inter.shipping_service.model.BalanceResponse;
import com.inter.shipping_service.model.TypeUser;
import com.inter.shipping_service.model.User;
import com.inter.shipping_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private final String docPF = "54670205013";
    private final String docPJ = "18581550000107";
    private final User userPF = new User(1, "Inter", "123@Inter", "inter@test.com", docPF, TypeUser.PF, 100.0, 50.0);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUsers_ReturnsAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(userPF));

        List<User> users = userService.getUsers();

        assertEquals(1, users.size());
        assertEquals("Inter", users.get(0).getName());
    }

    @Test
    void getUserById_Found() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userPF));

        User result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals("Inter", result.getName());
    }

    @Test
    void getUserById_NotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        User result = userService.getUserById(2L);

        assertNull(result);
    }

    @Test
    void getUserByDocumentNumber_ReturnsUser() {
        when(userRepository.findUserByDocumentNumber(docPF)).thenReturn(userPF);

        User result = userService.getUserByDocumentNumber(docPF);

        assertEquals(docPF, result.getDocumentNumber());
    }

    @Test
    void saveUser_PF_Success() {
        UserDto userDto = new UserDto("Inter", "123@Inter", "joao@test.com", docPF);

        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        User saved = userService.saveUser(userDto);

        assertEquals(TypeUser.PF, saved.getType());
        assertEquals(docPF, saved.getDocumentNumber());
    }

    @Test
    void saveUser_PJ_Success() {
        UserDto userDto = new UserDto("Inter", "123@Inter", "joao@test.com", docPJ);

        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        User saved = userService.saveUser(userDto);

        assertEquals(TypeUser.PJ, saved.getType());
        assertEquals(docPJ, saved.getDocumentNumber());
    }

    @Test
    void existsUserByDocumentNumber_ReturnsTrue() {
        when(userRepository.existsByDocumentNumber(docPF)).thenReturn(true);

        assertTrue(userService.existsUserByDocumentNumber(docPF));
    }

    @Test
    void exceptionDocumentNumber_ThrowsIfNotExist() {
        when(userRepository.existsByDocumentNumber(docPF)).thenReturn(false);

        assertThrows(NotExist.class, () -> userService.exceptionDocumentNumber(docPF));
    }

    @Test
    void getBalanceRealByDocumentNumber_ReturnsBalance() {
        when(userRepository.existsByDocumentNumber(docPF)).thenReturn(true);
        when(userRepository.findBalanceRealByDocumentNumber(docPF)).thenReturn(150.0);

        Double balance = userService.getBalanceRealByDocumentNumber(docPF);

        assertEquals(150.0, balance);
    }

    @Test
    void getBalanceDollarByDocumentNumber_ReturnsBalance() {
        when(userRepository.existsByDocumentNumber(docPF)).thenReturn(true);
        when(userRepository.findBalanceDollarByDocumentNumber(docPF)).thenReturn(75.0);

        Double balance = userService.getBalanceDollarByDocumentNumber(docPF);

        assertEquals(75.0, balance);
    }

    @Test
    void getBalanceByDocumentNumber_ReturnsOptional() {
        BalanceResponse response = new BalanceResponse(1L, 100.0, 20.0);

        when(userRepository.existsByDocumentNumber(docPF)).thenReturn(true);
        when(userRepository.findBalanceByDocumentNumber(docPF)).thenReturn(Optional.of(response));

        Optional<BalanceResponse> result = userService.getBalanceByDocumentNumber(docPF);

        assertTrue(result.isPresent());
        assertEquals(100.0, result.get().getBalanceReal());
    }

    @Test
    void removeNonNumericCharacters_RemovesCorrectly() {
        String input = "123.456.789-00";
        String result = userService.removeNonNumericCharacters(input);

        assertEquals("12345678900", result);
    }
}
