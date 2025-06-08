package com.blockchain.project.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;

@Service
public class EthereumService {
    private Web3j web3j;
    private org.web3j.crypto.Credentials credentials;

    private String privateKey = "2c22a0b64ed3b35630f534f188a001dfd3df874cb98ef1a8cd9f04ac64fc568d";
    private String contractAddress = "0x914e18b9D394C6257bff8CdB8beEDf34f4ACCDF3";//"0xc2766b1fdb7184d7cb84916ec6febfaaa74f2778"; ; // Адрес вашего смарт-контракта

    @PostConstruct
    public void init() {
        this.web3j = Web3j.build(new HttpService("https://sepolia.infura.io/v3/acbefa9ee964448d95289f0ac38d864d"));
        this.credentials = Credentials.create(privateKey);
    }

    /**
     * Отправляет транзакцию создания кампании в Ethereum.
     * Возвращает хэш транзакции.
     */
    public String createCampaignOnBlockchain(BigInteger goalAmount) throws Exception {
        TransactionManager txManager = new RawTransactionManager(web3j, credentials);

        // Упрощённый пример (реальный вызов смарт-контракта будет сложнее)
        String txHash = txManager.sendTransaction(
                DefaultGasProvider.GAS_PRICE,
                DefaultGasProvider.GAS_LIMIT,
                contractAddress,
                "", // Данные для вызова метода контракта
                goalAmount
        ).getTransactionHash();

        return txHash;
    }

    /**
     * Проверяет статус транзакции.
     */
    public boolean isTransactionConfirmed(String txHash) throws Exception {
        TransactionReceipt receipt = web3j.ethGetTransactionReceipt(txHash).send().getResult();
        return receipt != null && receipt.isStatusOK();
    }

    /**
     * Инвестирует в кампанию через смарт-контракт
     */
    public String investInCampaign(BigInteger amountWei) throws Exception {
        TransactionManager txManager = new RawTransactionManager(web3j, credentials);
        // Загружаем контракт
         contract = Crowdfunding.load(
                contractAddress,
                web3j,
                credentials,
                new DefaultGasProvider()
        );

        // Вызываем метод contribute() в контракте
        TransactionReceipt receipt = contract.contribute().send();
        return receipt.getTransactionHash();
        String txHash = txManager.sendTransaction(
                DefaultGasProvider.GAS_PRICE,
                DefaultGasProvider.GAS_LIMIT,
                contractAddress,
                "", // Данные для вызова метода контракта
                amountWei
        ).getTransactionHash();
        return txHash;
    }

    /**
     * Ждет подтверждения транзакции
     */
    public TransactionReceipt waitForTransactionReceipt(String txHash) throws Exception {
        return web3j.ethGetTransactionReceipt(txHash)
                .send()
                .getTransactionReceipt()
                .orElseThrow(() -> new RuntimeException("Транзакция не найдена"));
    }
}
