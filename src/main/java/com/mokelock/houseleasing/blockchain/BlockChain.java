package com.mokelock.houseleasing.blockchain;

import com.alibaba.fastjson.JSONObject;
import com.mokelock.houseleasing.model.HouseModel.House;
import org.graalvm.compiler.core.common.type.ArithmeticOpTable;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint32;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tuples.generated.Tuple6;
import org.web3j.tx.Transfer;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;
import rx.Subscription;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BlockChain {

    private final static String url = "http://211.87.230.34:9988/";
    private static Web3j web3j = Web3j.build(new HttpService(url));

    private final static String filePath = "E:\\Geth\\data\\keystore";
    private final static String rootAddress = "";
    private final static String rootPassword = "";
    private final static String rootFile = "";
    private final static String contractAddress = "";

    /**
     * 创建用户账户
     *
     * @return 账户hash地址
     */
    public String creatCredentials(String ethPassword) {
        String fileName_local;
        Credentials credentials = null;
        try {
            fileName_local = WalletUtils.generateNewWalletFile(ethPassword, new File(filePath), false);
            credentials = WalletUtils.loadCredentials(ethPassword, filePath + "/" + fileName_local);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert credentials != null;
        System.out.println(credentials.toString());
        return credentials.getAddress();
    }


    /**
     * 获得环境版本信息
     *
     * @return 版本号
     */
    public String getVersion() {
        Web3ClientVersion web3ClientVersion = null;
        try {
            web3ClientVersion = web3j.web3ClientVersion().send();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert web3ClientVersion != null;
        return web3ClientVersion.getWeb3ClientVersion();
    }

    /**
     * 获取用户余额
     *
     * @param fromAddress 查询账户地址
     * @return 余额
     */
    public BigInteger getBalance(String fromAddress) {
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
        BigInteger wei = ethGetBalance.getBalance();
        System.out.println(wei);
        return wei;
    }

    /**
     * 转账
     *
     * @param ethPassword 以太坊密码
     * @param fileName    密钥文件
     * @param toAddress   收款人地址
     * @return 交易hash
     */
    //充值
    public String transaction(String ethPassword, String fileName, String toAddress) {
        Credentials credentials = null;
        TransactionReceipt transactionReceipt = null;
        try {
            credentials = WalletUtils.loadCredentials(ethPassword, fileName);
            System.out.println("local address = " + credentials.getAddress());
            transactionReceipt = Transfer.sendFunds(
                    web3j, credentials, toAddress,
                    BigDecimal.valueOf(1.0), Convert.Unit.ETHER).send();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert transactionReceipt != null;
        System.out.println("receipt = " + transactionReceipt.toString());
        return transactionReceipt.getTransactionHash();
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

    /**
     * 获取交易详细信息
     *
     * @param transactionAddress 交易hash
     * @return json
     */
    public String getReceipt(String transactionAddress) {
        EthGetTransactionReceipt receipt = null;
        try {
            receipt = web3j
                    .ethGetTransactionReceipt(transactionAddress)
                    .sendAsync()
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        assert receipt != null;
        assert receipt.getTransactionReceipt().isPresent();
        String result = receipt.getTransactionReceipt().get().toString();
        System.out.println("result = " + result);
        return JSONObject.toJSONString(receipt);
    }

//    /**
//     * 查询历史记录
//     * @return 历史hash
//     */
//    public String replayFilter(String userAddress) {
//        Web3j web3j = Web3j.build(new HttpService(url));
//        BigInteger startBlock = BigInteger.valueOf(0);
//        BigInteger endBlock = BigInteger.valueOf(2010000);
//        Subscription subscription = web3j.transactionFlowable().subscribe(tx ->{
//
//        });
////        /**
////         * 遍历旧区块
////         */
////        Subscription subscription = web3j.
////                replayBlocksObservable(
////                        DefaultBlockParameter.valueOf(startBlock),
////                        DefaultBlockParameter.valueOf(endBlock),
////                        false).
////                subscribe(ethBlock -> {
////                    System.out.println("replay block");
////                    System.out.println(ethBlock.getBlock().getNumber());
////                });
//
//        /*
//          遍历旧交易
//         */
//        List list = new ArrayList();
//        Subscription subscription1 = web3j.().
//                subscribe(transaction -> {
//                    System.out.println("replay transaction");
//                    System.out.println("txHash " + transaction.getHash());
//                    list.add(transaction.getHash());
//                });
//        System.out.println(subscription1.isUnsubscribed());
//        System.out.println(list.toString());
//        return "end";
//    }

    /**
     * 获取账户合约中的信息
     *
     * @param userAddress 用户账户
     * @return 用户信息
     */
    public String getMessage(String userAddress, String ethFile, String ethPassword) {
        HouseContract houseContract = loadContract(userAddress, ethFile, contractAddress, ethPassword);
        Tuple6<Utf8String, Utf8String, Utf8String, Utf8String, Uint256, Uint256> tuple6 = null;
        Address address = new Address(userAddress);
        try {
            tuple6 = houseContract.findUser(address).send();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Utf8String username = tuple6 != null ? tuple6.getValue1() : null;
        Utf8String id = tuple6 != null ? tuple6.getValue2() : null;
        Utf8String IPFS_hash = tuple6 != null ? tuple6.getValue3() : null;
        Utf8String phone = tuple6 != null ? tuple6.getValue4() : null;
        Uint256 gender = tuple6 != null ? tuple6.getValue5() : null;
        Uint256 credit = tuple6 != null ? tuple6.getValue6() : null;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        jsonObject.put("id", id);
        jsonObject.put("IPFS_hash", IPFS_hash);
        jsonObject.put("phone", phone);
        jsonObject.put("gender", gender);
        jsonObject.put("credit", credit);
        return jsonObject.toJSONString();
    }

    /**
     * 添加用户信息
     *
     * @param userAddress
     * @param ethFile
     * @param ethPassword
     * @param username
     * @param id
     * @param IPFS_hash
     * @param tel
     * @param gender
     * @param credit
     */
    public void addUser(String userAddress, String ethFile, String ethPassword,
                        String username, String id, String IPFS_hash, String tel, int gender, int credit) {
        HouseContract houseContract = loadContract(userAddress, ethFile, contractAddress, ethPassword);
        Address address = new Address(userAddress);
        Utf8String _username = new Utf8String(username);
        Utf8String _id = new Utf8String(id);
        Utf8String _IPFS_hash = new Utf8String(IPFS_hash);
        Utf8String _tel = new Utf8String(tel);
        Uint256 _gender = new Uint256(gender);
        Uint256 _credit = new Uint256(credit);
        try {
            TransactionReceipt receipt =
                    houseContract.addUser(address, _username, _id, _IPFS_hash, _tel, _gender, _credit).send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户提交合约
     *
     * @param userAddress  租客地址
     * @param ownerAddress 房主地址
     * @param coins        押金
     */
    public void submitOrder(String userAddress, String ethFile, String ethPassword, String ownerAddress, BigInteger coins) {

        HouseContract houseContract = loadContract(userAddress, ethFile, contractAddress, ethPassword);
        Address address = new Address(userAddress);
        Uint32 coin = new Uint32(coins);
        //???wei value是个啥么东西
        try {
            TransactionReceipt receipt = houseContract.submitOrder(address, coin, BigInteger.valueOf(1L)).send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 房主回应合约
     *
     * @param ownerAddress 房主地址
     * @param userAddress  租客地址
     * @param coins        押金
     */
    public void responseOrder(String ownerAddress, String ethFile, String ethPassword, String userAddress, BigInteger coins) {
        HouseContract houseContract = loadContract(ownerAddress, ethFile, contractAddress, ethPassword);
        Address address = new Address(userAddress);
        Uint32 coin = new Uint32(coins);
        try {
            TransactionReceipt receipt = houseContract.respondOrder(address, coin, BigInteger.valueOf(1L)).send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 房主拒绝合约
     *
     * @param ownerAddress 房主地址
     * @param userAddress  租客地址
     */
    public void rejectOrder(String ownerAddress, String ethFile, String ethPassword, String userAddress) {
        HouseContract houseContract = loadContract(ownerAddress, ethFile, contractAddress, ethPassword);
        Address address = new Address(userAddress);
        try {
            TransactionReceipt receipt = houseContract.rejectOrder(address).send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 第二次签名
     *
     * @param i            调用主体，0：房主| 1：房客
     * @param ownerAddress 房主地址
     * @param userAddress  租客地址
     */
    public void confirmSecond(int i, String ownerAddress, String ethFile, String userAddress, String ethPassword) {
        HouseContract houseContract = null;
        if (i == 0) {
            houseContract = loadContract(ownerAddress, ethFile, contractAddress, ethPassword);
        } else if (i == 1) {
            houseContract = loadContract(userAddress, ethFile, contractAddress, ethPassword);
        }
        Address u_address = new Address(userAddress);
        Address o_address = new Address(ownerAddress);
        try {
            TransactionReceipt receipt = houseContract != null ? houseContract.confirmSecond(u_address, o_address).send()
                    : null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 退押金
     *
     * @param i            调用主体，0：房主| 1：房客
     * @param ownerAddress 房主地址
     * @param userAddress  租客地址
     */
    public void withdraw(int i, String ownerAddress, String ethFile, String userAddress, String ethPassword) {
        HouseContract houseContract = null;
        if (i == 0) {
            houseContract = loadContract(ownerAddress, ethFile, contractAddress, ethPassword);
        } else if (i == 1) {
            houseContract = loadContract(userAddress, ethFile, contractAddress, ethPassword);
        }
        Address u_address = new Address(userAddress);
        Address o_address = new Address(ownerAddress);
        try {
            TransactionReceipt receipt = houseContract != null ? houseContract.withdraw(u_address, o_address).send() : null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 待修改
     */
    public void withdrawAppeal(String userAddress, String ownerAddress, String _Address) {
        HouseContract houseContract = loadContract(rootAddress, rootFile, contractAddress, rootPassword);
        Address u_address = new Address(userAddress);
        Address o_address = new Address(ownerAddress);
        Address __address = new Address(_Address);
        try {
            TransactionReceipt receipt = houseContract.withdrawAppeal(u_address, o_address, __address).send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取ipfs数据表地址
     *
     * @param type 0: 账户-账户对应表| 1: 在线房子概要信息表| 2: 下线房子概要信息表
     * @return hash地址 | error
     */
    public String getHash(int type) {
        HouseContract houseContract = loadContract(rootAddress, rootFile, contractAddress, rootPassword);
//        Credentials credentials = null;
//        try {
//            credentials = WalletUtils.loadCredentials(rootPassword, rootFile);
//        } catch (IOException | CipherException e) {
//            e.printStackTrace();
//        }
//        HouseContract houseContract = HouseContract.load(contractAddress, web3j, credentials, Convert.toWei("10",
//                Convert.Unit.GWEI).toBigInteger(), BigInteger.valueOf(100000));
        Tuple3 tuple3 = null;
        try {
            tuple3 = houseContract.findAdminTable().send();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (tuple3 == null) {
            return "error";
        }
        String hash = "";
        switch (type) {
            case 0:
                hash = (String) tuple3.getValue1();
                break;
            case 1:
                hash = (String) tuple3.getValue2();
                break;
            case 2:
                hash = (String) tuple3.getValue3();
                break;
        }
        return hash;
    }

    /**
     * 修改ipfs数据表地址f
     *
     * @param type 0: 账户-账户对应表| 1: 在线房子概要信息表| 2: 下线房子概要信息表
     */
    public void changeTable(int type, String hash) {
        HouseContract houseContract = loadContract(rootAddress, rootFile, contractAddress, rootPassword);
        Utf8String _hash = new Utf8String(hash);
        TransactionReceipt receipt = null;
        try {
            switch (type) {
                case 0:
                    receipt = houseContract.postUser_account(_hash).send();
                    break;
                case 1:
                    receipt = houseContract.postAccount_house_online(_hash).send();
                    break;
                case 2:
                    receipt = houseContract.postAccount_house_downline(_hash).send();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改用户电话信息
     *
     * @param ownerAddress 房主地址
     * @param phone        电话
     */
    public void changeTelInfo(String ownerAddress, String ethFile, String ethPassword, String phone) {
        HouseContract houseContract = loadContract(ownerAddress, ethFile, contractAddress, ethPassword);
        Address address = new Address(ownerAddress);
        Utf8String _phone = new Utf8String(phone);
        try {
            TransactionReceipt receipt = houseContract.postUserPhone(address, _phone).send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改hash信息
     *
     * @param ownerAddress 房主地址
     * @param hash         hash
     */
    public void changeHashInfo(String ownerAddress, String ethFile, String ethPassword, String hash) {
        HouseContract houseContract = loadContract(ownerAddress, ethFile, contractAddress, ethPassword);
        Utf8String _hash = new Utf8String(hash);
        Address address = new Address(ownerAddress);
        try {
            TransactionReceipt receipt = houseContract.postUserPhone(address, _hash).send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String findOrders(String ownerAddress, String ethFile, String ethPassword) {
        HouseContract houseContract = loadContract(ownerAddress, ethFile, contractAddress, ethPassword);
        Address address = new Address(ownerAddress);
        try {
            TransactionReceipt receipt = houseContract.findOrdersNum(address).send();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 部署智能合约
     *
     * @param userAddress 用户地址
     * @param ethPassword eth密码
     * @return 合约地址
     */
    public String deplay(String userAddress, String ethPassword) {
        Credentials credentials = null;
        try {
            credentials = WalletUtils.loadCredentials(ethPassword, filePath + "???");
        } catch (IOException | CipherException e) {
            e.printStackTrace();
        }
        DefaultGasProvider gasProvider = new DefaultGasProvider();
        HouseContract houseContract = null;
        try {
            houseContract = HouseContract.deploy(web3j, credentials, gasProvider).send();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return houseContract != null ? houseContract.getContractAddress() : null;
    }

    /**
     * 加载合约
     *
     * @param userAddress     账户地址
     * @param contractAddress 合约地址
     * @param ethPassword     账户密码
     * @return HouseContract对象
     */
    private HouseContract loadContract(String userAddress, String userFile, String contractAddress,
                                       String ethPassword) {
        Credentials credentials = null;
        try {
            credentials = WalletUtils.loadCredentials(ethPassword, userFile);
        } catch (IOException | CipherException e) {
            e.printStackTrace();
        }
        return HouseContract.load(contractAddress, web3j, credentials, Convert.toWei("10",
                Convert.Unit.GWEI).toBigInteger(), BigInteger.valueOf(100000));
    }
}