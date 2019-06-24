package com.mokelock.houseleasing.blockchain;

import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;
import rx.Subscription;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class myTest {

    private String filePath;
    private String fileName;
    private String ethPassword;
    private String url;
    private String fromAddress;
    private String toAddress;
    private String tansactionAddress;

    public static void main(String[] args) {

        myTest test = new myTest();
        test.filePath = "E:\\Geth\\data\\keystore";
        test.fileName = "E:\\Geth\\data\\keystore\\UTC--2019-06-20T02-55-19.079955800Z--5e283b353b65baf5f18640e8a3228c8e764a9c29";
        test.ethPassword = "123456";
        test.url = "http://211.87.230.12:9988/";
        test.fromAddress = "0x5e283b353b65baf5f18640e8a3228c8e764a9c29";
        test.toAddress = "0x40b4dd13e90c55c1f3c62e5eea0e9074742256d9";
        test.tansactionAddress = "0x087e35de3fcfce828f3da369b4b0a468cfd302592d0dfc5d8e76f5b3408c7bd0";

        String version = test.getVersion();
        System.out.println(version);

//        getBalance();

//        transaction();
//        transaction2();

//        getReceipt();

//        replayFilter();

        System.out.println(test.replayFilter());
        version = test.getVersion();
        System.out.println(version);

    }

    private void creatCredentials() {
//        String filePath = "E:\\Geth\\data\\keystore";
        String fileName_local = null;
        Credentials credentials = null;
        try {
            fileName_local = WalletUtils.generateNewWalletFile(ethPassword, new File(filePath), false);
            credentials = WalletUtils.loadCredentials(ethPassword, filePath + "/" + fileName_local);
        } catch (IOException | NoSuchAlgorithmException | CipherException | NoSuchProviderException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        assert credentials != null;
        System.out.println(credentials.toString());
    }


    private String getVersion() {
        Web3j web3j = Web3j.build(new HttpService(url));
        Web3ClientVersion web3ClientVersion = null;
        try {
            web3ClientVersion = web3j.web3ClientVersion().send();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert web3ClientVersion != null;
        return web3ClientVersion.getWeb3ClientVersion();
    }

    private void getBalance() {
        Web3j web3j = Web3j.build(new HttpService(url));
        EthGetBalance ethGetBalance = null;
        try {
            ethGetBalance = web3j
                    .ethGetBalance(fromAddress, DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        assert ethGetBalance != null;
//        BigInteger wei = ethGetBalance.getBalance();
        System.out.println(ethGetBalance.getBalance());
    }

    private void transaction() {
        Web3j web3 = Web3j.build(new HttpService(url));  // defaults to http://localhost:8545/
        Credentials credentials = null;
        TransactionReceipt transactionReceipt = null;
        try {
            credentials = WalletUtils.loadCredentials(ethPassword, fileName);
            System.out.println("local address = " + credentials.getAddress());
            transactionReceipt = Transfer.sendFunds(
                    web3, credentials, toAddress,
                    BigDecimal.valueOf(1.0), Convert.Unit.ETHER).send();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert transactionReceipt != null;
        System.out.println("receipt = " + transactionReceipt.toString());

    }
    // eth.getTransactionReceipt('0x81277c80b9b0465812a846361dc5d1284a0d34a80524703f9196a935904d1824')
    //{
    //  blockHash: "0xe53a8bbe8ba31e2b014943e4b822276df45fa30572c12468e51dab1cf79b383c",
    //  blockNumber: 3451,
    //  contractAddress: null,
    //  cumulativeGasUsed: 21000,
    //  from: "0x5e283b353b65baf5f18640e8a3228c8e764a9c29",
    //  gasUsed: 21000,
    //  logs: [],
    //  logsBloom: "0x00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
    //  root: "0xe31a6fb00febdd8add22311d8957ebf9c64093a8a90f9feefa829a80fc6cf71c",
    //  to: "0x40b4dd13e90c55c1f3c62e5eea0e9074742256d9",
    //  transactionHash: "0x81277c80b9b0465812a846361dc5d1284a0d34a80524703f9196a935904d1824",
    //  transactionIndex: 0
    //}

    private void transaction2() {
        //设置需要的矿工费
        BigInteger GAS_PRICE = BigInteger.valueOf(22_000_000_000L);
        BigInteger GAS_LIMIT = BigInteger.valueOf(4_300_000);

        //调用的是kovan测试环境，这里使用的是infura这个客户端
        Web3j web3j = Web3j.build(new HttpService(url));
        //转账人账户地址
//        String ownAddress = "0x5e283b353b65baf5f18640e8a3228c8e764a9c29";
        //被转人账户地址
//        String toAddress = "0x40b4dd13e90c55c1f3c62e5eea0e9074742256d9";
        //转账人私钥
//        Credentials credentials = Credentials.create("xxxxxxxxxxxxx");
        Credentials credentials = null;
        try {
            credentials = WalletUtils.loadCredentials(ethPassword, fileName);
        } catch (IOException | CipherException e) {
            e.printStackTrace();
        }


        //getNonce（这里的Nonce我也不是很明白，大概是交易的笔数吧）
        EthGetTransactionCount ethGetTransactionCount = null;
        try {
            ethGetTransactionCount = web3j.ethGetTransactionCount(
                    fromAddress, DefaultBlockParameterName.LATEST).sendAsync().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        assert ethGetTransactionCount != null;
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();

        //创建交易，这里是转0.5个以太币
        BigInteger value = Convert.toWei("0.5", Convert.Unit.ETHER).toBigInteger();
        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                nonce, GAS_PRICE, GAS_LIMIT, toAddress, value);

        //签名Transaction，这里要对交易做签名
        assert credentials != null;
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);

        //发送交易
        String transactionHash = "";
        try {
            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
            transactionHash = ethSendTransaction.getTransactionHash();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        //获得到transactionHash后就可以到以太坊的网站上查询这笔交易的状态了
        System.out.println("hash = " + transactionHash);
    }
    // eth.getTransactionReceipt("0x087e35de3fcfce828f3da369b4b0a468cfd302592d0dfc5d8e76f5b3408c7bd0")
    //{
    //  blockHash: "0xda8dbe4de6d3a494423449fb90a35e09c0893380529b9424dd7ea40d6c485439",
    //  blockNumber: 3449,
    //  contractAddress: null,
    //  cumulativeGasUsed: 21000,
    //  from: "0x5e283b353b65baf5f18640e8a3228c8e764a9c29",
    //  gasUsed: 21000,
    //  logs: [],
    //  logsBloom: "0x00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
    //  root: "0xe8718f1549bacdc59018276bd41842e6dd5b00bb058fcc29c66eed7b47b6c39e",
    //  to: "0x40b4dd13e90c55c1f3c62e5eea0e9074742256d9",
    //  transactionHash: "0x087e35de3fcfce828f3da369b4b0a468cfd302592d0dfc5d8e76f5b3408c7bd0",
    //  transactionIndex: 0
    //}

    private void getReceipt() {
        Web3j web3j = Web3j.build(new HttpService(url));
        EthGetTransactionReceipt receipt = null;
        try {
            receipt = web3j
                    .ethGetTransactionReceipt(tansactionAddress)
                    .sendAsync()
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        assert receipt != null;
        assert receipt.getTransactionReceipt().isPresent();
        String result = receipt.getTransactionReceipt().get().toString();
        System.out.println("result = " + result);
    }

    private String replayFilter() {
        Web3j web3j = Web3j.build(new HttpService(url));
        BigInteger startBlock = BigInteger.valueOf(0);
        BigInteger endBlock = BigInteger.valueOf(2010000);
        /**
         * 遍历旧区块
         */
//        Subscription subscription = web3j.
//                replayBlocksObservable(
//                        DefaultBlockParameter.valueOf(startBlock),
//                        DefaultBlockParameter.valueOf(endBlock),
//                        false).
//                subscribe(ethBlock -> {
//                    System.out.println("replay block");
//                    System.out.println(ethBlock.getBlock().getNumber());
//                });

        /**
         * 遍历旧交易
         */
        List list = new ArrayList();
        Subscription subscription1 = web3j.
                replayTransactionsObservable(
                        DefaultBlockParameterName.EARLIEST,
                        DefaultBlockParameterName.LATEST).
                subscribe(transaction -> {
                    System.out.println("replay transaction");
                    System.out.println("txHash " + transaction.getHash());
                    list.add(transaction.getHash());
                });
        System.out.println(subscription1.isUnsubscribed());
        System.out.println(list.toString());
        return "end";
    }

}