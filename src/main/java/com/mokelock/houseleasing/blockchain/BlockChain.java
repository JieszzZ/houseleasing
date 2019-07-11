package com.mokelock.houseleasing.blockchain;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint32;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple6;
import org.web3j.tuples.generated.Tuple7;
import org.web3j.tuples.generated.Tuple8;
import org.web3j.tx.Transfer;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BlockChain {

    //    @Value(value = "${BlockChain.url}")
    private String url = "http://211.87.230.8:9988/";
    private Web3j web3j = Web3j.build(new HttpService(url));
    //    @Value(value = "${BlockChain.filePath}")
    private String filePath = "D:\\Geth\\data\\keystore";
    //    @Value(value = "${BlockChain.root.Address")
    private String rootAddress = "0xed87e99eb4ec72678f1912c329d1c1cbf44f9c36";
    //    @Value(value = "${BlockChain,root.Password}")
    private String rootPassword = "123";
    //    @Value(value = "${BlockChain.root.File}")
    private String rootFile = "D:\\Geth\\data\\keystore\\UTC--2019-07-10T08-43-43.932731400Z--ed87e99eb4ec72678f1912c329d1c1cbf44f9c36";
    //    @Value(value = "${BlockChain.contract.address}")
    private String contractAddress = "0x6b9d6f53b69847568ae06f5cad8062a2f4176aa6";
//            "0xa0f490e57d4ddf2c5a526d99aea5125fd310466c";

    /**
     * 创建用户账户
     *
     * @return 账户hash地址
     */
    public Map creatCredentials(String ethPassword) {
        String fileName_local = "";
        Credentials credentials = null;
        try {
            System.out.println(filePath);
            fileName_local = WalletUtils.generateNewWalletFile(ethPassword, new File(filePath), false);
            credentials = WalletUtils.loadCredentials(ethPassword, filePath + "\\" + fileName_local);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert credentials != null;
        String ethAddress = credentials.getAddress();
        String ethPath = filePath + "\\" + fileName_local;

        System.out.println(ethPath);

        Map<String, String> map = new HashMap<String, String>();
        map.put("ethAddress", ethAddress);
        map.put("ethPath", ethPath);
        return map;
    }
//0xdfec136611676641542ce3dff6e930db3259f372

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
        House_sol_blockChain houseContract = loadContract(userAddress, ethFile, ethPassword);
        Tuple6<Utf8String, Utf8String, Utf8String, Utf8String, Uint256, Uint256> tuple6 = null;
        Address address = new Address(userAddress);
        try {
            tuple6 = houseContract.findUser(address).send();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("tuple6 is " + tuple6.toString());
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

    public JSONObject getMessage3(String userAddress, String ethFile, String ethPassword) {
        House_sol_blockChain houseContract = loadContract(userAddress, ethFile, ethPassword);
        Tuple6<Utf8String, Utf8String, Utf8String, Utf8String, Uint256, Uint256> tuple6 = null;
        Address address = new Address(userAddress);
        try {
            tuple6 = houseContract.findUser(address).send();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("tuple6 is " + tuple6.toString());
        Utf8String username = tuple6 != null ? tuple6.getValue1() : null;
        Utf8String id = tuple6 != null ? tuple6.getValue2() : null;
        Utf8String IPFS_hash = tuple6 != null ? tuple6.getValue3() : null;
        Utf8String phone = tuple6 != null ? tuple6.getValue4() : null;
        Uint256 gender = tuple6 != null ? tuple6.getValue5() : null;
        Uint256 credit = tuple6 != null ? tuple6.getValue6() : null;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username.getValue());
        jsonObject.put("id", id.getValue());
        jsonObject.put("IPFS_hash", IPFS_hash.getValue());
        jsonObject.put("phone", phone.getValue());
        jsonObject.put("gender", gender.getValue().intValue());
        jsonObject.put("credit", credit.getValue().intValue());
        System.out.println("json in getMessage3 is " + jsonObject.toJSONString());
        return jsonObject;
    }

    public void getMessage2(String userAddress, String ethFile, String ethPassword) {
        Address address = new Address(userAddress);
        List<Type> inputParameters = new ArrayList<>();
        inputParameters.add(address);
        Function function = new Function("findUser", inputParameters, Collections.<TypeReference<?>>emptyList());
        String encodedFunction = FunctionEncoder.encode(function);
        EthCall response = null;
        try {
            response = web3j.ethCall(
                    Transaction.createEthCallTransaction(userAddress, contractAddress, encodedFunction),
                    DefaultBlockParameterName.LATEST)
                    .sendAsync().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        assert response != null;
        System.out.println("value : " + response.getValue());
        System.out.println("response : " + response.toString());
//        return "";
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
        House_sol_blockChain houseContract = loadContract(userAddress, ethFile, ethPassword);
        Address address = new Address(userAddress);
        Utf8String _username = new Utf8String(username);
        Utf8String _id = new Utf8String(id);
        Utf8String _IPFS_hash = new Utf8String(IPFS_hash);
        Utf8String _tel = new Utf8String(tel);
        Uint256 _gender = new Uint256(gender);
        Uint256 _credit = new Uint256(credit);
        try {
            TransactionReceipt receipt = houseContract.addUser(address, _username, _id, _IPFS_hash, _tel, _gender,
                    _credit).send();
            System.out.println("addUser's receipt = " + receipt.toString());
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
    public void submitOrder(String userAddress, String ethFile, String ethPassword, String ownerAddress,
                            String houseHash, BigInteger coins) {

        House_sol_blockChain houseContract = loadContract(userAddress, ethFile, ethPassword);
        Address address = new Address(ownerAddress);
        Utf8String _houseHash = new Utf8String(houseHash);
        Uint256 coin = new Uint256(coins);
        //???wei value是个啥么东西
        try {
            TransactionReceipt receipt =
                    houseContract.submitOrder(address, _houseHash, coin, BigInteger.valueOf(1L)).send();
            System.out.println("receipt in submitOrder is " + receipt.toString());
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
    public void responseOrder(String ownerAddress, String ethFile, String ethPassword, String userAddress,
                              boolean res, BigInteger coins) {
        House_sol_blockChain houseContract = loadContract(ownerAddress, ethFile, ethPassword);
        Address address = new Address(userAddress);
        Bool _res = new Bool(res);
        Uint32 coin = new Uint32(coins);
        try {
            TransactionReceipt receipt = houseContract.respondOrder(address, _res, coin, BigInteger.valueOf(1L)).send();
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
    public void confirmSecond(int i, String ownerAddress, String ethFile, String userAddress, String ethPassword,
                              boolean res) {
        House_sol_blockChain houseContract = null;
        if (i == 0) {
            houseContract = loadContract(ownerAddress, ethFile, ethPassword);
        } else if (i == 1) {
            houseContract = loadContract(userAddress, ethFile, ethPassword);
        }
        Bool _res = new Bool(res);
        Address u_address = new Address(userAddress);
        Address o_address = new Address(ownerAddress);
        try {
            TransactionReceipt receipt = houseContract != null ?
                    houseContract.resSecond(u_address, o_address, _res).send()
                    : null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 退押金
     *
     * @param i            调用主体，0：房主| 1：房客 | 2: 管理员
     * @param ownerAddress 房主地址
     * @param userAddress  租客地址
     */
    public void withdraw(int i, String ownerAddress, String ethFile, String userAddress, String ethPassword) {
        House_sol_blockChain houseContract = null;
        Address resAddress = null;
        String res_address = null;
        if (i == 0) {
            houseContract = loadContract(ownerAddress, ethFile, ethPassword);
            resAddress = new Address(ownerAddress);
            res_address = ownerAddress;
        } else if (i == 1) {
            houseContract = loadContract(userAddress, ethFile, ethPassword);
            resAddress = new Address(userAddress);
            res_address = userAddress;
        } else if (i == 2) {
            houseContract = loadContract(rootAddress, rootFile, rootPassword);
            resAddress = new Address(rootAddress);
            res_address = rootAddress;
        }
        Address u_address = new Address(userAddress);
        Address o_address = new Address(ownerAddress);
        try {
            TransactionReceipt receipt = houseContract != null ?
                    houseContract.withdraw(u_address, o_address, resAddress, new BigInteger("1")).send() : null;
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
        House_sol_blockChain houseContract = loadContract(rootAddress, rootFile, rootPassword);
        System.out.println("getHash_houseContract " + houseContract.getContractAddress());
//        Credentials credentials = null;
//        try {
//            credentials = WalletUtils.loadCredentials(rootPassword, rootFile);
//        } catch (IOException | CipherException e) {
//            e.printStackTrace();
//        }
//        HouseContract houseContract = HouseContract.load(contractAddress, web3j, credentials, Convert.toWei("10",
//                Convert.Unit.GWEI).toBigInteger(), BigInteger.valueOf(100000));
        String hash = "";
        try {
            switch (type) {
                case 0:
                    hash = houseContract.findUser_account_table().send().toString();
                    break;
                case 1:
                    hash = houseContract.findAccount_house_online().send().toString();
                    break;
                case 2:
                    hash = houseContract.findAccount_house_downline().send().toString();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hash;
    }

    /**
     * 修改ipfs数据表地址f
     *
     * @param type 0: 账户-账户对应表| 1: 在线房子概要信息表| 2: 下线房子概要信息表
     */
    public void changeTable(int type, String hash) {
        House_sol_blockChain houseContract = loadContract(rootAddress, rootFile, rootPassword);
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
        System.out.println("receipt in changeTable is " + receipt.toString());
    }

    /**
     * 修改用户电话信息
     *
     * @param ownerAddress 房主地址
     * @param phone        电话
     */
    public void changeTelInfo(String ownerAddress, String ethFile, String ethPassword, String phone) {
        House_sol_blockChain houseContract = loadContract(ownerAddress, ethFile, ethPassword);
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
        House_sol_blockChain houseContract = loadContract(ownerAddress, ethFile, ethPassword);
        Utf8String _hash = new Utf8String(hash);
        Address address = new Address(ownerAddress);
        try {
            TransactionReceipt receipt = houseContract.postUserPhone(address, _hash).send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONArray findOrders(String ownerAddress, String ethFile, String ethPassword) {
        House_sol_blockChain houseContract = loadContract(ownerAddress, ethFile, ethPassword);
        Address address = new Address(ownerAddress);
        Uint256 num;
        Tuple8<Address, Address, Uint256, Uint256, Uint256, Uint256, Uint256, Uint256> tuple8 = null;
        Tuple7<Uint256, Uint256, Uint256, Uint256, Uint256, Uint256, Utf8String> tuple7 = null;
        JSONArray jsonArray = new JSONArray();
        try {
            num = houseContract.findOrdersNum(address).send();
            for (int i = num.getValue().intValue() - 1; i >= 0; i--) {
                tuple8 = houseContract.findOrder_1(address, new Uint256(i)).send();
                tuple7 = houseContract.findOrder_2(address, new Uint256(i)).send();
                JSONObject temp = new JSONObject();
                System.out.println("getValue in find orders is " + tuple8.getValue1().toString());
                System.out.print("   " +tuple8.getValue1().getValue());
                temp.put("submiter", tuple8.getValue1().getValue());
                temp.put("responder", tuple8.getValue2().getValue());
                System.out.println("sub and res in blockChain is "+tuple8.getValue1().getValue()+" "+tuple8.getValue2().getValue());
                temp.put("submiterEthCoin", tuple8.getValue3().getValue());
                temp.put("aimerEthCoin", tuple8.getValue4().getValue());
                temp.put("subFirstSign", tuple8.getValue5().getValue());
                temp.put("resFirstSign", tuple8.getValue6().getValue());
                temp.put("subSecondSign", tuple8.getValue7().getValue());
                temp.put("resSecondSign", tuple8.getValue8().getValue());
                temp.put("sub_time", tuple7.getValue1().getValue());
                temp.put("effect_time", tuple7.getValue2().getValue());
                temp.put("finish_time", tuple7.getValue3().getValue());
                temp.put("role", tuple7.getValue4().getValue());
                temp.put("state", tuple7.getValue5().getValue());
                temp.put("money", tuple7.getValue6().getValue());
                temp.put("house_hash", tuple7.getValue7().getValue());
                jsonArray.add(temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    /**
     * 部署智能合约
     *
     * @return 合约地址
     */
    protected String deploy() {
        Credentials credentials = null;
        try {
            credentials = WalletUtils.loadCredentials(rootPassword, rootFile);
        } catch (IOException | CipherException e) {
            e.printStackTrace();
        }
        DefaultGasProvider gasProvider = new DefaultGasProvider();
        House_sol_blockChain houseContract = null;
        try {
            houseContract = House_sol_blockChain.deploy(web3j, credentials, gasProvider).send();
            System.out.println("isValid : " + houseContract.isValid());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return houseContract != null ? houseContract.getContractAddress() : null;
    }

    /**
     * 加载合约
     *
     * @param userAddress 账户地址
     * @param ethPassword 账户密码
     * @return HouseContract对象
     */
    private House_sol_blockChain loadContract(String userAddress, String userFile, String ethPassword) {
        Credentials credentials = null;
        try {
            System.out.println("ethPass and file in loadContract is " + ethPassword + " " + userFile);
            credentials = WalletUtils.loadCredentials(ethPassword, userFile);
        } catch (IOException | CipherException e) {
            e.printStackTrace();
        }
//        System.out.println("private key is " + credentials.getEcKeyPair().getPrivateKey());
//        System.out.println("public key is " + credentials.getEcKeyPair().getPublicKey());
//        DefaultGasProvider gasProvider = new DefaultGasProvider();
//        ContractGasProvider gasProvider = new StaticGasProvider(BigInteger.valueOf(22000000000L), BigInteger.valueOf(22000000000L));
        House_sol_blockChain houseContract = House_sol_blockChain.load(contractAddress, web3j, credentials,
                new BigInteger("21000"), new BigInteger("5000000000"));
        try {
//            System.out.println("binary = " + houseContract.getContractBinary());
            System.out.println("In loadContract, the value of isValid is " + houseContract.isValid());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return houseContract;
    }

//    public static void main(String[] args) throws IOException {
//        BlockChain blockChain = new BlockChain();
//        System.out.println(blockChain.getVersion());
//        System.out.println("root's balance is " + blockChain.getBalance(rootAddress));
////        System.out.println("test's balance is " + blockChain.getBalance(testAddress));
//        System.out.println("isValid() is " + blockChain.loadContract(rootAddress, rootFile, rootPassword).isValid());
////        System.out.println(blockChain.getVersion());
////        String contractAddress = blockChain.deploy();
////        System.out.println(contractAddress);
////        System.out.println(blockChain.creatCredentials("test"));
////        System.out.println(blockChain.transaction(rootPassword, rootFile, "0xdfec136611676641542ce3dff6e930db3259f372"));
////        blockChain.addUser(rootAddress,
////                rootFile,
////                "123", "孙梓杰", "123456", "", "12345678910", 0, 10);
////        System.out.println("add user");
////        blockChain.addUser(rootAddress, rootFile, rootPassword, "孙梓杰", "123456", "", "12345678910", 0, 10);
////        System.out.println("get message");
//        System.out.println(blockChain.getMessage(rootAddress,
//                rootFile,
//                "123"));
//
//        // blockChain.changeTable(0,"wanyanaguda");
//        System.out.println(blockChain.getHash(0));
//        // blockChain.changeTable(1,"bilishi");
//        System.out.println(blockChain.getHash(1));
//        // blockChain.changeTable(2,"alibaba");
//        System.out.println(blockChain.getHash(2));
//
//        //blockChain.changeTelInfo(rootAddress,rootFile,rootPassword,"987654321");
//        System.out.println(blockChain.getMessage(rootAddress, rootFile, rootPassword));
//        //    private final static String contractAddress = "0xf7afa5662dcca6f75aa8f6fd0168cd3c861f09a9";
//        //            "0xA802412997277907849B29f7ea1361CEdc2E224D";
//        String testAddress = "";
//        blockChain.submitOrder(rootAddress, rootFile, rootPassword, testAddress, "lishiquyuan", BigInteger.valueOf(1));
//        System.out.println(blockChain.findOrders(rootAddress, rootFile, rootPassword));
//        String testPassword = "";
//        String testFile = "";
//        blockChain.responseOrder(testAddress, testFile, testPassword, rootAddress, true, BigInteger.valueOf(1));
//        System.out.println(blockChain.findOrders(testAddress, testFile, testPassword));
//        blockChain.confirmSecond(0, testAddress, testFile, rootAddress, testPassword, true);
//        System.out.println(blockChain.findOrders(testAddress, testFile, testPassword));
//        blockChain.confirmSecond(1, rootAddress, rootFile, testAddress, rootPassword, true);
//        System.out.println(blockChain.findOrders(rootAddress, rootFile, rootPassword));
//        //blockChain.transaction(rootPassword,rootFile,testAddress);
////        System.out.println("get message2");
////        blockChain.getMessage2("0xdfec136611676641542ce3dff6e930db3259f372",
////                "E:\\Geth\\data\\keystore\\UTC--2019-07-04T06-19-35.677670200Z--dfec136611676641542ce3dff6e930db3259f372.json",
////                "test");
//    }
}