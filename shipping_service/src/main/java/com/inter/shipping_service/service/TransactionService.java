package com.inter.shipping_service.service;

import com.inter.shipping_service.dto.TransactionDto;
import com.inter.shipping_service.exception.InsufficientBalance;
import com.inter.shipping_service.exception.NotExist;
import com.inter.shipping_service.exception.TransactionFail;
import com.inter.shipping_service.model.Transaction;
import com.inter.shipping_service.model.TypeBalance;
import com.inter.shipping_service.model.TypeUser;
import com.inter.shipping_service.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ExchangeService exchangeService;

    // Obtem as transações feitas por det. usuário
    public List<Transaction> getTransactionsByUser(String documentNumber){
        userService.exceptionDocumentNumber(documentNumber);

        if (!transactionRepository.existsByTransactionBy(documentNumber)){
            throw new NotExist("Transaction not found by user");
        }
        return transactionRepository.findAllByTransactionBy(documentNumber);
    }

    // Obtem as transações feitas para det. usuário
    public List<Transaction> getTransactionsToUser(String documentNumber){
        userService.exceptionDocumentNumber(documentNumber);

        if (!transactionRepository.existsByTransactionTo(documentNumber)){
            throw new NotExist("Transaction not found to user");
        }
        return transactionRepository.findAllByTransactionTo(documentNumber);
    }

    @Transactional
    public Transaction postRemessaService(TransactionDto transactionDto) {
        userService.exceptionDocumentNumber(transactionDto.transactionTo());
        userService.exceptionDocumentNumber(transactionDto.transactionBy());

        var quote = exchangeService.getQuote_External(LocalDateTime.now().toString());
        var conversion = exchangeService.conversionCurrency(transactionDto.transactionBy(), transactionDto.amount(), quote);

        limitExceeded(transactionDto.transactionBy(), transactionDto.amount());

        if (!exchangeService.existsBalanceToTransactionUSD(transactionDto.transactionBy(), transactionDto.amount(), quote)) {
            cancelledTransaction(transactionDto.transactionBy(), conversion);
            throw new InsufficientBalance("Insufficient balance");
        }

        return transactionBank(transactionDto, "USD");
    }

    public void cancelledTransaction(String documentNumber, Double reversal){
        var user = userService.getUserByDocumentNumber(documentNumber);
        user.setBalanceReal(user.getBalanceReal() + reversal);
        userService.save(user);
    }

    @Transactional
    public Transaction transactionBRLToBRL(TransactionDto transactionDto){
        if (!existsBalanceToTransactionBR(transactionDto.transactionBy(),transactionDto.amount())) {
            throw new InsufficientBalance("Insufficient balance");
        }
        limitExceeded(transactionDto.transactionBy(), transactionDto.amount());
        return transactionBank(transactionDto, "BRL");
    }

    // Verifica se existe valor para trasnf. BR
    public Boolean existsBalanceToTransactionBR(String documentNumber, Double amount){
        return userService.getBalanceRealByDocumentNumber(documentNumber) >= amount;
    }

    @Transactional
    public Transaction transactionUSAToUSA(TransactionDto transactionDto){
        var quote = exchangeService.getQuote_External(LocalDateTime.now().toString());
        if (!exchangeService.existsBalanceToTransactionUSD(transactionDto.transactionBy(),transactionDto.amount(), quote)) {
            throw new InsufficientBalance("Insufficient balance");
        }
        limitExceeded(transactionDto.transactionBy(), transactionDto.amount());
        return transactionBank(transactionDto, "USA");
    }


    // Transferência bancária
    @Transactional
    public Transaction transactionBank(TransactionDto transactionDto, String typeBalance){
        if (Objects.equals(transactionDto.transactionBy(), transactionDto.transactionTo())){
            throw new InsufficientBalance("Transaction not permitted");
        }

        var send = userService.getUserByDocumentNumber(transactionDto.transactionBy());
        var receiver = userService.getUserByDocumentNumber(transactionDto.transactionTo());

        limitExceeded(transactionDto.transactionBy(), transactionDto.amount());

        if (typeBalance.equals("BRL")) {
            send.setBalanceReal(send.getBalanceReal() - transactionDto.amount());
            receiver.setBalanceReal(receiver.getBalanceReal() + transactionDto.amount());
        }
        else if (typeBalance.equals("USD")) {
            send.setBalanceDollar(send.getBalanceDollar() - transactionDto.amount());
            receiver.setBalanceDollar(receiver.getBalanceDollar() + transactionDto.amount());
        }
        else throw new InsufficientBalance("Transaction not permitted");

        userService.save(send);
        userService.save(receiver);

        Transaction transaction = new Transaction(transactionDto);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setLimitDay(transactionDto.amount());

        return transactionRepository.save(transaction);
    }

    public void limitExceeded(String documentNumber, Double amount) {
        Double total = transactionRepository.limitDay(documentNumber, LocalDate.now());
        TypeUser typeUser = userService.getTypeUserByDocument(documentNumber);

        Boolean limitExceeded;

        if (Objects.equals(typeUser, TypeUser.PF)) {
            limitExceeded = total + amount >= 100000;
        } else if (Objects.equals(typeUser, TypeUser.PJ)) {
            limitExceeded = total + amount >= 50000;
        }
        else
            throw new NotExist("Balance Type not found");

        if (limitExceeded) {
            throw new TransactionFail("Daily transaction limit exceeded");
        }
    }
}
