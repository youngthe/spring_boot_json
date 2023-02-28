package com.example.spring;

import com.example.spring.dao.AskingTb;
import com.example.spring.dao.CommentTb;
import com.example.spring.dao.CommunityTb;
import com.example.spring.dao.TestContent;
import com.example.spring.repository.AskingRepository;
import com.example.spring.repository.CommunityRepository;
import com.example.spring.repository.WithoutContent;
import com.example.spring.utils.Calculator;
import com.querydsl.core.Tuple;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;
import org.web3j.tx.response.TransactionReceiptProcessor;

import java.io.File;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

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
    public void getEthClientVersionASync() throws Exception
    {
//        Web3j web3j = Web3j.build(new HttpService("https://mainnet.infura.io/v3/0x1de5e7CEF591A57C9f9Ed263A320563c5726A056"));

//        지갑 파일 생성
//        File file = new File("./");
//        String fileName = WalletUtils.generateFullNewWalletFile("test", file); {password}, {파일 위치}
//        System.out.println(fileName);

        File file = new File("./UTC--2023-02-28T06-22-54.425506000Z--87e02340c9c5dab434d2e9f5cdbc3da06b8f47da.json");
        Credentials credentials = WalletUtils.loadCredentials("test", file); //지갑 파일 가져오기
        System.out.println(credentials.getAddress());

//        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.PENDING).send();

        byte ret = Byte.parseByte("1001");
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

//    @Test
//    public void getEthClientVersionRx() throws Exception
//    {
//        Web3j web3 = Web3j.build(new HttpService("https://mainnet.infura.io/v3/7d566c8797794d60978743f1d9837032"));
//        web3.web3ClientVersion().flowable().subscribe(x -> {
//            System.out.println(x.getWeb3ClientVersion());
//        });
//
//        Thread.sleep(5000);
//    }



}
