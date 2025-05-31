package com.inter.shipping_service.repository;

import com.inter.shipping_service.dto.BalanceResponseDto;
import com.inter.shipping_service.dto.UserDto;
import com.inter.shipping_service.model.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;


    @Test
    @DisplayName("Should return true when document number exists")
    void shouldReturnTrueWhenDocumentNumberExists() {
        persistUser(MOCK_DTO);
        boolean result = userRepository.existsByDocumentNumber(MOCK_DTO.documentNumber());
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should return false when document number does not exist")
    void shouldReturnFalseWhenDocumentNumberDoesNotExist() {
        boolean result = userRepository.existsByDocumentNumber("82724875001");
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should return true when ID exists")
    void shouldReturnTrueWhenIdExists() {
        User user = persistUser(MOCK_DTO);
        boolean result = userRepository.existsById(user.getId());
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should return false when ID does not exist")
    void shouldReturnFalseWhenIdDoesNotExist() {
        boolean result = userRepository.existsById(999L);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should return true when email exists")
    void shouldReturnTrueWhenEmailExists() {
        persistUser(MOCK_DTO);
        boolean result = userRepository.existsByEmail(MOCK_DTO.email());
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should return false when email does not exist")
    void shouldReturnFalseWhenEmailDoesNotExist() {
        boolean result = userRepository.existsByEmail("naoexiste@inter.com");
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should find user by document number")
    void shouldFindUserByDocumentNumber() {
        persistUser(MOCK_DTO);
        Optional<User> user = Optional.ofNullable(userRepository.findUserByDocumentNumber(MOCK_DTO.documentNumber()));
        assertThat(user).isPresent();
    }

    @Test
    @DisplayName("Should not find user by document number")
    void shouldNotFindUserByDocumentNumber() {
        Optional<User> user = Optional.ofNullable(userRepository.findUserByDocumentNumber("00000000000"));
        assertThat(user).isEmpty();
    }

    @Test
    @DisplayName("Should find balance by document number")
    void shouldFindBalanceByDocumentNumber() {
        persistUser(MOCK_DTO);
        Optional<BalanceResponseDto> balance = userRepository.findBalanceByDocumentNumber(MOCK_DTO.documentNumber());
        assertThat(balance).isPresent();
        assertThat(balance.get().balanceReal()).isEqualTo(150.00);
        assertThat(balance.get().balanceDollar()).isEqualTo(180.00);
    }

    @Test
    @DisplayName("Should not find balance by document number")
    void shouldNotFindBalanceByDocumentNumber() {
        Optional<BalanceResponseDto> balance = userRepository.findBalanceByDocumentNumber("00000000000");
        assertThat(balance).isEmpty();
    }

    @Test
    @DisplayName("Should return balanceReal by document number")
    void shouldReturnBalanceRealByDocumentNumber() {
        persistUser(MOCK_DTO);
        Double balance = userRepository.findBalanceRealByDocumentNumber(MOCK_DTO.documentNumber());
        assertThat(balance).isEqualTo(150.00);
    }

    @Test
    @DisplayName("Should return null for balanceReal if not found")
    void shouldReturnNullForBalanceRealIfNotFound() {
        Double balance = userRepository.findBalanceRealByDocumentNumber("00000000000");
        assertThat(balance).isNull();
    }

    @Test
    @DisplayName("Should return balanceDollar by document number")
    void shouldReturnBalanceDollarByDocumentNumber() {
        persistUser(MOCK_DTO);
        Double balance = userRepository.findBalanceDollarByDocumentNumber(MOCK_DTO.documentNumber());
        assertThat(balance).isEqualTo(180.00);
    }

    @Test
    @DisplayName("Should return null for balanceDollar if not found")
    void shouldReturnNullForBalanceDollarIfNotFound() {
        Double balance = userRepository.findBalanceDollarByDocumentNumber("00000000000");
        assertThat(balance).isNull();
    }

    private static final UserDto MOCK_DTO = new UserDto("Inter PF", "Inter@2025-", "teste@inter.com", "82724875001");

    private User persistUser(UserDto dto) {
        User user = new User(dto);
        entityManager.persist(user);
        return user;
    }
}
