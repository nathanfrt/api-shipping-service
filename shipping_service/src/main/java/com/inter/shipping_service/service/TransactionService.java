package com.inter.shipping_service.service;

import com.inter.shipping_service.dto.TransactionDto;
import com.inter.shipping_service.exception.InsufficientBalance;
import com.inter.shipping_service.exception.NotExist;
import com.inter.shipping_service.exception.TransactionFail;
import com.inter.shipping_service.model.Transaction;
import com.inter.shipping_service.model.TypeUser;
import com.inter.shipping_service.model.User;
import com.inter.shipping_service.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

        var quote = exchangeService.getQuote_External(LocalDate.now());
        var conversion = exchangeService.conversionCurrency(transactionDto.transactionBy(), transactionDto.amount(), quote);

        Double limitDay = limitExceeded(transactionDto.transactionBy(), transactionDto.amount());

        if (limitDay <= 0.1) {
            cancelledTransaction(transactionDto.transactionBy(), conversion);
            throw new InsufficientBalance("Insufficient balance");
        }

        return transactionBank(transactionDto, "USD", quote, true);
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
        return transactionBank(transactionDto, "BRL", 0.0,false);
    }

    // Verifica se existe valor para trasnf. BR
    public Boolean existsBalanceToTransactionBR(String documentNumber, Double amount){
        return userService.getBalanceRealByDocumentNumber(documentNumber) >= amount;
    }

    @Transactional
    public Transaction transactionUSAToUSA(TransactionDto transactionDto){
        var quote = exchangeService.getQuote_External(LocalDate.now());
        if (!exchangeService.existsBalanceToTransactionUSD(transactionDto.transactionBy(),transactionDto.amount(), quote)) {
            throw new InsufficientBalance("Insufficient balance");
        }
        return transactionBank(transactionDto, "USA", quote, false);
    }


    // Transferência bancária
    @Transactional
    public Transaction transactionBank(TransactionDto transactionDto, String typeBalance, Double quote, Boolean isConvertion){
        boolean equals = Objects.equals(transactionDto.transactionBy(), transactionDto.transactionTo());
        if ((!isConvertion && equals) ||
                ((isConvertion && !equals))){
            throw new InsufficientBalance("Transaction not permitted");
        }

        var send = userService.getUserByDocumentNumber(transactionDto.transactionBy());
        var receiver = userService.getUserByDocumentNumber(transactionDto.transactionTo());

        Double limitDay = limitExceeded(transactionDto.transactionBy(), transactionDto.amount());

        if (typeBalance.equals("BRL")) {
            var balanceTo = userService.getBalanceRealByDocumentNumber(transactionDto.transactionTo());
            var balanceBy = userService.getBalanceRealByDocumentNumber(transactionDto.transactionBy());

            send.setBalanceReal(balanceBy - transactionDto.amount());
            receiver.setBalanceReal(balanceTo + transactionDto.amount());
        }
        else if (typeBalance.equals("USD")) {
            var balanceTo = userService.getBalanceDollarByDocumentNumber(transactionDto.transactionTo());
            var balanceBy = userService.getBalanceDollarByDocumentNumber(transactionDto.transactionBy());

            send.setBalanceDollar(balanceBy - transactionDto.amount());
            receiver.setBalanceDollar(balanceTo + transactionDto.amount());
        }
        else throw new InsufficientBalance("Transaction not permitted");

        userService.save(send);
        userService.save(receiver);

        Transaction transaction = new Transaction(transactionDto);
        transaction.setCreatedAt(LocalDate.now());
        transaction.setLimitDay(limitDay);
        transaction.setLimitUsed(transactionDto.amount());
        transaction.setQuote(quote);

        return transactionRepository.save(transaction);
    }

    public Double limitExceeded(String documentNumber, Double amount) {
        Double total = transactionRepository.limitDay(documentNumber, LocalDate.now());
        User user = userService.getUserByDocumentNumber(documentNumber);
        TypeUser typeUser = user.getType();

        double dailyLimit;
        if (typeUser == TypeUser.PF) {
            dailyLimit = 10000.0;
        } else if (typeUser == TypeUser.PJ) {
            dailyLimit = 50000.0;
        } else {
            throw new NotExist("User Type not found");
        }

        double totalAfterTransaction = total + amount;
        if (totalAfterTransaction > dailyLimit) {
            throw new TransactionFail("Daily transaction limit exceeded");
        }

        return dailyLimit - totalAfterTransaction;
    }
}
