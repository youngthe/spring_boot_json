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
        Web3j web3j = Web3j.build(new HttpService("https://api.baobab.klaytn.net:8651"));
//
        ContractGasProvider gasProvider = new DefaultGasProvider();
        BigInteger gasPrice = Convert.toWei("250", Convert.Unit.GWEI).toBigInteger();
        BigInteger GasLimit = BigInteger.valueOf(300000L);
        EthGasPrice ethGasPrice = web3j.ethGasPrice().send();
        File file = new File("./UTC--2023-02-28T06-22-54.425506000Z--87e02340c9c5dab434d2e9f5cdbc3da06b8f47da.json");
        Credentials credentials = WalletUtils.loadCredentials("test", file);
        System.out.println(credentials.getAddress());

        Abi abi = Abi.load("0x981AeB68B7A9d1B3d9341636D0f45660995C6Af5", web3j, credentials, gasPrice, GasLimit);
//        Abi abi1 = Abi.load("0xe92C60dfEc285704b8394212faf40C8CDC42997e", web3j, credentials, gasProvider);
        BigInteger value = Convert.toWei("5", Convert.Unit.ETHER).toBigInteger();
        BigInteger balances = Convert.fromWei(abi._balances(credentials.getAddress()).send().toString(), Convert.Unit.ETHER).toBigInteger();
//        abi.transfer("0x1de5e7CEF591A57C9f9Ed263A320563c5726A056", value).send();
        System.out.println(balances);


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
