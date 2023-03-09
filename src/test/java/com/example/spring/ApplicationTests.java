package com.example.spring;

import com.example.spring.repository.LoginHistoryRepository;
import com.example.spring.repository.StakingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {


    @Autowired
    StakingRepository stakingRepository;
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

//    @Test
//    public void getEthClientVersionASync() throws Exception {
//        Web3j web3j = Web3j.build(new HttpService("https://api.baobab.klaytn.net:8651"));
////
//        ContractGasProvider gasProvider = new DefaultGasProvider();
//        BigInteger gasPrice = Convert.toWei("250", Convert.Unit.GWEI).toBigInteger();
//        BigInteger GasLimit = BigInteger.valueOf(300000L);
//        EthGasPrice ethGasPrice = web3j.ethGasPrice().send();
//        File file = new File("./UTC--2023-02-28T06-22-54.425506000Z--87e02340c9c5dab434d2e9f5cdbc3da06b8f47da.json");
//        Credentials credentials = WalletUtils.loadCredentials("test", file);
//        System.out.println(credentials.getAddress());
//
//        Abi abi = Abi.load("0x981AeB68B7A9d1B3d9341636D0f45660995C6Af5", web3j, credentials, gasPrice, GasLimit);
////        Abi abi1 = Abi.load("0xe92C60dfEc285704b8394212faf40C8CDC42997e", web3j, credentials, gasProvider);
//        BigInteger value = Convert.toWei("5", Convert.Unit.ETHER).toBigInteger();
//        BigInteger balances = Convert.fromWei(abi._balances(credentials.getAddress()).send().toString(), Convert.Unit.ETHER).toBigInteger();
////        abi.transfer("0x1de5e7CEF591A57C9f9Ed263A320563c5726A056", value).send();
//        System.out.println(balances);
//
//
//    }
    @Autowired
    LoginHistoryRepository loginHistoryRepository;
    @Test
    public void test(){

//        List<StakingTb> stakingTbList = stakingRepository.getStakingTbThatStateTrue();
//
//        for(int i=0;i<stakingTbList.size();i++){
//            BigDecimal number1 = BigDecimal.valueOf(stakingTbList.get(i).getStart_amount());
//            BigDecimal number2 = new BigDecimal("0.1");
//            stakingTbList.get(i).setAdd_amount(number1.multiply(number2).doubleValue());
//            stakingRepository.save(stakingTbList.get(i));
//        }
//        Date now_date = new Date();
//        Timestamp timeStamp = new Timestamp(now_date.getTime());

//        LocalDate test = LocalDate.now();
//        test = test.plusDays(90);
//        String format_year = test.format(DateTimeFormatter.ofPattern("yyyy"));
//        String format_month = test.format(DateTimeFormatter.ofPattern("MM"));
//        String format_day = test.format(DateTimeFormatter.ofPattern("dd"));
//
//        int year = Integer.parseInt(format_year);
//        int month = Integer.parseInt(format_month);
//        int day = Integer.parseInt(format_day);
//        System.out.println(year + " " + month + " " + day);
//         LocalDate localDate = LocalDate.of(year, month, day);
//         Date date = new Date();
//        System.out.println(test);
//        System.out.println(localDate);
//
//        System.out.println();

//        LocalDate test = LocalDate.now();
//        Calendar cal = Calendar.getInstance();
//        cal.set(test.getYear(), test.getMonth().getValue()+3, test.getDayOfMonth(), 0, 0, 0);
//
//        Date nextThreeMonth = cal.getTime();
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        System.out.println(format.format(nextThreeMonth));
        }

}
