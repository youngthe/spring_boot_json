package com.example.spring;

import com.example.spring.dao.LoginHistoryTb;
import com.example.spring.repository.LoginHistoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Scheduled;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.FastRawTransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;
import org.web3j.utils.Convert;

import java.io.File;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
class ApplicationTests {


//    @Autowired
//    AskingRepository askingRepository;
//
//    @Autowired
//    CommunityRepository communityRepository;
//
//    @Autowired
//    PasswordEncoder passwordEncoder;
//
//    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
//    @Test
//    void contextLoads() {
//
//        Calculator calculator = new Calculator();
//        System.out.println(calculator.getReward(1000, 0.0123746F));
//        Date now = new Date();
//        Date beforeOneHour = new Date();
//        System.out.println(beforeOneHour.getTime());
//        long now_time = now.getTime();
//        long test = beforeOneHour.getTime() - (2 * 24 * 60 * 60 * 1000);
//
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd / hh-mm");
//        System.out.println( format.format(test));
//        //하루 추출
//        DayParse(now_time-test);
//        calculator.getRewardHalfTermination(1000, 0.5, test);
////        calculator.getRewardHalfTermination(1000, 0.05F, );
//    }
//
//    //하루 추출, 내림형태, 만약 3일이 되기 1시간 전이면 2일로 나타남
//    public void DayParse(long Time){
//        long day = Time/24/60/60/1000;
//        System.out.println(day);
//    }

//    @Test
//    public void getEthClientVersionSync() throws Exception
//    {
//        Web3j web3j = Web3j.build(new HttpService("https://mainnet.infura.io/v3/7d566c8797794d60978743f1d9837032"));
//        Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().send();
//        System.out.println(web3ClientVersion.getWeb3ClientVersion());
//    }

    @Test
    public void getEthClientVersionASync() throws Exception {
//        Web3j web3j = Web3j.build(new HttpService("https://api.baobab.klaytn.net:8651"));
////
//        ContractGasProvider gasProvider = new DefaultGasProvider();
//        BigInteger gasPrice = new BigInteger("750");
//        BigInteger GasPrice = BigInteger.valueOf(25000000000L);
//        BigInteger GasLimit = BigInteger.valueOf(30000000L);
//        EthGasPrice ethGasPrice = web3j.ethGasPrice().send();
//        System.out.println("eth :" + ethGasPrice.getGasPrice());
//        File file = new File("./UTC--2023-02-28T06-22-54.425506000Z--87e02340c9c5dab434d2e9f5cdbc3da06b8f47da.json");
//        Credentials credentials = WalletUtils.loadCredentials("test", file);
//
//        Abi abi = Abi.load("0xe92C60dfEc285704b8394212faf40C8CDC42997e", web3j, credentials, GasPrice, GasLimit);
////        Abi abi1 = Abi.load("0xe92C60dfEc285704b8394212faf40C8CDC42997e", web3j, credentials, gasProvider);
//
//        BigInteger value = Convert.toWei("10.1", Convert.Unit.ETHER).toBigInteger();
//        abi.transfer("0x1de5e7CEF591A57C9f9Ed263A320563c5726A056", value).send();
//        abi1.transfer("0x1de5e7CEF591A57C9f9Ed263A320563c5726A056", BigInteger.valueOf(2000000000)).send();


//        TransactionReceiptProcessor transactionReceiptProcessor = new PollingTransactionReceiptProcessor(web3j, 10,3);
//        RawTransactionManager rawTransactionManager = new RawTransactionManager(web3j, credentials, (byte) 1001, transactionReceiptProcessor);

//        List<Type> input = new ArrayList<>();


//        Function function = new org.web3j.abi.datatypes.Function(("transfer", input, Collections.EMPTY_LIST));
//        String encodedFunction = FunctionEncoder.encode((org.web3j.abi.datatypes.Function) function);
//        System.out.println(encodedFunction.toString());

//        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.PENDING).send();
//        BigInteger nonce = ethGetTransactionCount.getTransactionCount();


//
//





//        TransactionManager transactionManager = new RawTransactionManager(web3j, credentials, (byte) 1001);
//        EthSendTransaction ethSendTransaction = rawTransactionManager.sendTransaction(BigInteger.valueOf(10) , BigInteger.valueOf(100) , "0x1de5e7CEF591A57C9f9Ed263A320563c5726A056", "klay", BigInteger.valueOf(5));
//        transactionManager.sendTransaction()
//        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(nonce, DefaultGasProvider.GAS_PRICE, DefaultGasProvider.GAS_LIMIT, "0x1de5e7CEF591A57C9f9Ed263A320563c5726A056", BigInteger.valueOf(1));
//        Web3j web3 = Web3j.build(new HttpService());  // defaults to http://localhost:8545/

//// get the next available nonce
//        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST).send();
//        BigInteger nonce = ethGetTransactionCount.getTransactionCount();
//
//// create our transaction
//        RawTransaction rawTransaction  = RawTransaction.createEtherTransaction(nonce, DefaultGasProvider.GAS_PRICE, DefaultGasProvider.GAS_LIMIT, "0x1de5e7CEF591A57C9f9Ed263A320563c5726A056", BigInteger.valueOf(1));
//        System.out.println("rawTransaction : " + rawTransaction);
//        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
//        System.out.println("signMessage : " + signedMessage);
//        String hexValue = Numeric.toHexString(signedMessage);
//        System.out.println("hexValue : " + hexValue);
//        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(rawTransactionManager.sign(rawTransaction)).send();
//        System.out.println("result : " + ethSendTransaction.getResult());



//
//        CompletableFuture<EthSendTransaction> result = web3j.ethSendRawTransaction(rawTransactionManager.sign(rawTransaction)).sendAsync();
//        System.out.println(result);
//
//
//        Transfer.sendFunds(
//                        web3j, credentials, "0x1de5e7CEF591A57C9f9Ed263A320563c5726A056",
//                        BigDecimal.valueOf(5.0), Convert.Unit.ETHER)
//                .toString();
//        System.out.println(Transfer.sendFunds(
//                        web3j, credentials, "0x1de5e7CEF591A57C9f9Ed263A320563c5726A056",
//                        BigDecimal.valueOf(5.0), Convert.Unit.ETHER));

//        Credentials credentials = WalletUtils.loadCredentials("test", file); //지갑 파일 가져오기
//        int chainId = 1001;


//        System.out.println(web3j.ethBlockNumber().sendAsync().get());
//        Function function = new Function("tokenURI", Arrays.<Type>asList(new Uint256(nftVo.getTokenId())), Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
//        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.PENDING).send();

//        String encodedFunction = FunctionEncoder.encode(function);
//        BigInteger nonce = gasUtils.getNonce(web3j, credentials.getAddress());
//        BigInteger ethGasPrice = web3j.ethGasPrice().send().getGasPrice();
//        BigInteger ethGasLimit = gasUtils.getGasLimit(web3j, credentials.getAddress(), nonce, nftVo.getTokenAddress(), function);
//        EthCall res = web3j.ethCall(Transaction.createEthCallTransaction(credentials.getAddress(),
//                nftVo.getTokenAddress(),
//                encodedFunction), DefaultBlockParameterName.LATEST).sendAsync().get();
//        List<Type> ttt = FunctionReturnDecoder.decode(res.getValue(), function.getOutputParameters());
//        for(Type tt: ttt){
//            url = tt.toString();
//        }

//        지갑 파일 생성
//        File file = new File("./");
//        String fileName = WalletUtils.generateFullNewWalletFile("test", file); {password}, {파일 위치}
//        System.out.println(fileName);


//        System.out.println(credentials.getAddress());
//


//        byte ret = Byte.parseByte("1001");
//        BigInteger nonce = ethGetTransactionCount.getTransactionCount();
//        TransactionReceiptProcessor transactionReceiptProcessor = new PollingTransactionReceiptProcessor(web3j, 0, 3);
//        RawTransactionManager rawTransactionManager = new RawTransactionManager(web3j, credentials, ret, transactionReceiptProcessor);
//        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(nonce, BigInteger.valueOf(0), BigInteger.valueOf(0), "0x1de5e7cef591a57c9f9ed263a320563c5726a056", BigInteger.valueOf(1));
//        CompletableFuture<EthSendTransaction> result = web3j.ethSendRawTransaction(rawTransactionManager.sign(rawTransaction)).sendAsync();
//        System.out.println(result);

//        Web3j web3j = Web3j.build(new HttpService("https://mainnet.infura.io/v3/0x1de5e7CEF591A57C9f9Ed263A320563c5726A056"));
//        int chainId = web3j.ethChainId().send().getChainId().intValue();


//        web3j.ethCall().send().getValue();
//        Function function = new Function("tokenURI", Arrays.<Type>asList(new Uint256(nftVo.getTokenId())), Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
//        String encodedFunction = FunctionEncoder.encode(function);
//        BigInteger nonce = gasUtils.getNonce(web3j, credentials.getAddress());
//        BigInteger ethGasPrice = web3j.ethGasPrice().send().getGasPrice();
//        BigInteger ethGasLimit = gasUtils.getGasLimit(web3j, credentials.getAddress(), nonce, nftVo.getTokenAddress(), function);
//        EthCall res = web3j.ethCall(Transaction.createEthCallTransaction(credentials.getAddress(),
//                nftVo.getTokenAddress(),
//                encodedFunction), DefaultBlockParameterName.LATEST).sendAsync().get();
//        List<Type> ttt = FunctionReturnDecoder.decode(res.getValue(), function.getOutputParameters());
//        for(Type tt: ttt){
//            url = tt.toString();
//        }
//
//    }




    }
    @Autowired
    LoginHistoryRepository loginHistoryRepository;
    @Test
    public void test(){
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            System.out.println(format.format(date));

//            LoginHistoryTb loginHistoryTb = new LoginHistoryTb();
//            loginHistoryTb.setDate(format.format(date));
//            loginHistoryTb.setCount(0);
//            loginHistoryRepository.save(loginHistoryTb);

        int count = loginHistoryRepository.getCountByDate(format.format(date));

        System.out.println(count);

        loginHistoryRepository.setIncreaseCount(format.format(date));



        }

}
