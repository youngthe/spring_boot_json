package com.example.spring.controller;

import com.example.spring.Abi;
import com.example.spring.dao.*;
import com.example.spring.dto.CategoryDto;
import com.example.spring.dto.UserAssetDto;
import com.example.spring.dto.UserDto;
import com.example.spring.repository.*;
import com.example.spring.utils.JwtTokenProvider;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;


@RestController
public class AdminController {
    @Autowired
    private AskingRepository askingRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private StakingRepository stakingRepository;

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    @ApiOperation(value = "입출금 내역 조회", notes = "admin 계정만 관리자 페이지 접근 가능, 입출금 요청 승인 확인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value="/admin/asking", method = RequestMethod.GET)
    public HashMap cash_input_output(@RequestHeader("token") String tokenHeader,@RequestParam ("nowpage") int nowpage, @RequestParam ("count") int countpage){

        HashMap<String,Object> result = new HashMap<>();
        UserTb usertb = userRepository.getUserTbByUserId(jwtTokenProvider.getUserId(tokenHeader));

        int start = countpage * nowpage;
        int end = countpage*(nowpage+1) - 1;

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }
        if(Objects.equals(usertb.getRole(), "admin")){

            List<AskingTb> askinglist_paging = new ArrayList<>();
            List<AskingTb> askinglist = askingRepository.findAll();
            if(end>=askinglist.size()) end = askinglist.size()-1;

            for(int i=start;i<=end;i++){
                askinglist_paging.add(askinglist.get(i));
            }
            result.put("askinglist", askinglist_paging);
            result.put("total", askinglist.size());
            return result;
        }else{
            result.put("message", "not admin");
            result.put("resultCode", "false");
            return result;
        }

    }

    @ApiOperation(value = "입금 내역 조회", notes = "입금 내역 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value="/admin/asking/input", method = RequestMethod.GET)
    public HashMap cash_input(@RequestHeader("token") String tokenHeader ,@RequestParam ("nowpage") int nowpage, @RequestParam ("count") int countpage){

        HashMap<String,Object> result = new HashMap<>();
        UserTb usertb = userRepository.getUserTbByUserId(jwtTokenProvider.getUserId(tokenHeader));
        int start = countpage * nowpage;
        int end = countpage*(nowpage+1) - 1;

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }
        List<AskingTb> askinglist_paging = new ArrayList<>();
        if(Objects.equals(usertb.getRole(), "admin")){
            List<AskingTb> askinglist = askingRepository.getAskingListByInputOutput(true);
            if(end>=askinglist.size()) end = askinglist.size()-1;
            for(int i=start;i<=end;i++){
                askinglist_paging.add(askinglist.get(i));
            }
            result.put("askinglist", askinglist_paging);
            result.put("total", askinglist.size());
            return result;
        }else{
            result.put("message", "not admin");
            result.put("resultCode", "false");
            return result;
        }

    }
    @ApiOperation(value = "출금 내역 조회", notes = "출금 내역 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value="/admin/asking/output", method = RequestMethod.GET)
    public HashMap cash_output(@RequestHeader("token") String tokenHeader, @RequestParam ("nowpage") int nowpage, @RequestParam ("count") int countpage){

        HashMap<String,Object> result = new HashMap<>();
        UserTb usertb = userRepository.getUserTbByUserId(jwtTokenProvider.getUserId(tokenHeader));

        int start = countpage * nowpage;
        int end = countpage*(nowpage+1) - 1;

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }
        List<AskingTb> askinglist_paging = new ArrayList<>();
        if(Objects.equals(usertb.getRole(), "admin")){
            List<AskingTb> askinglist = askingRepository.getAskingListByInputOutput(false);
            if(end>=askinglist.size()) end = askinglist.size()-1;
            for(int i=start;i<=end;i++){
                askinglist_paging.add(askinglist.get(i));
            }
            result.put("askinglist", askinglist_paging);
            result.put("total", askinglist.size());
            return result;
        }else{
            result.put("message", "not admin");
            result.put("resultCode", "false");
            return result;
        }

    }


    @ApiOperation(value = "관리자 회원가입", notes = "아이디 비밀번호 별명을 통한 회원가입")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "아이디", required = true),
            @ApiImplicitParam(name = "pw", value = "비밀번호", required = true),
            @ApiImplicitParam(name = "nickname", value = "별명", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value = "/admin/register", method = RequestMethod.POST)
    public HashMap Register(@RequestBody HashMap<String, Object> data){

        HashMap<String, Object> result = new HashMap<>();

        if(ObjectUtils.isEmpty(data.get("account"))){
            result.put("message", "account is null");
            result.put("resultCode", "false");
            return result;
        }
        if(ObjectUtils.isEmpty(data.get("pw"))){
            result.put("message", "pw is null");
            result.put("resultCode", "false");
            return result;
        }
        if(ObjectUtils.isEmpty(data.get("nickname"))){
            result.put("message", "nickname is null");
            result.put("resultCode", "false");
            return result;
        }

        String account = data.get("account").toString();
        String pw = data.get("pw").toString();
        String name = data.get("nickname").toString();


        if(userRepository.AccountCheck(account)) {
            try{
                UserTb userTb = new UserTb();
                Date now = new Date();
                userTb.setAccount(account);
                userTb.setPw(passwordEncoder.encode(pw));
                userTb.setName(name);
                userTb.setRole("admin");
                userTb.setState(true);
                userTb.setCreate_date(now);
                userTb.setCoin(0);
                userRepository.save(userTb);
                result.put("resultCode", "true");
                return result;
            }catch(Exception e){
                result.put("resultCode", "false");
                return result;
            }
        }else{
            result.put("message", "already");
            result.put("resultCode", "false");
            return result;
        }
    }


    @ApiOperation(value = "입금 또는 출금 요청 승인", notes = "요청이 입금 요청이였으면 해당 유저에게 입금, 출금 요청이였으면 등록된 주소로 출금")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value="/admin/asking/{asking_id}", method = RequestMethod.PUT)
    public HashMap admin_asking(@RequestHeader("token") String tokenHeader, @PathVariable("asking_id") int asking_id){

        HashMap<String,Object> result = new HashMap<>();

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }


        UserTb asking_user = userRepository.getUserTbByUserId(jwtTokenProvider.getUserId(tokenHeader));
        if(Objects.equals(asking_user.getRole(), "admin")){

            AskingTb askingTb = askingRepository.getAskingTbByAskingId(asking_id);
            UserTb usertb = userRepository.getUserTbByUserId(askingTb.getUser_id());

            if(askingTb.getType() != 0){
                result.put("message", "already");
                result.put("resultCode", "false");
                return result;
            }

            if(askingTb.isInput_output()){
                //서버 계정으로 입금, 여기서 수수료 적용 아니면 입금요청을 할 때 수수료 적용
                BigDecimal bigNumber1 = BigDecimal.valueOf(usertb.getCoin());
                BigDecimal bigNumber2 = BigDecimal.valueOf(askingTb.getCoin());
                usertb.setCoin(bigNumber1.add(bigNumber2).doubleValue());
                userRepository.save(usertb);
                Date now = new Date();
                askingTb.setType(1);
                askingTb.setCompleted_time(now);
                askingRepository.save(askingTb);
                result.put("resultCode", "true");

//                Date now = new Date();
//                CashFlowHistoryTb cashFlowHistoryTb = new CashFlowHistoryTb();
//                cashFlowHistoryTb.setState(1);
//                cashFlowHistoryTb.setFrom_address("server");
//                cashFlowHistoryTb.setTo_address(askingTb.getAddress());
//                cashFlowHistoryTb.setUser_id(jwtTokenProvider.getUserId(tokenHeader));
//                cashFlowHistoryTb.setDate(now);
//                cashFlowHistoryTb.setCoin(askingTb.getCoin());
//                cashFlowHistoryRepository.save(cashFlowHistoryTb);
                result.put("resultCode", "true");
                return result;

            }else{
                //사용자 지갑으로 출금
                if(usertb.getCoin() < askingTb.getCoin()){
                    result.put("message", "coin over");
                    result.put("resultCode","false");
                   return result;
                }
                try{

                    Web3j web3j = Web3j.build(new HttpService("https://api.baobab.klaytn.net:8651"));

                    BigInteger gasPrice = Convert.toWei("250", Convert.Unit.GWEI).toBigInteger();
                    BigInteger GasLimit = BigInteger.valueOf(20000000L);
                    String contractAddress = "0x981AeB68B7A9d1B3d9341636D0f45660995C6Af5";
                    File file = new File("./UTC--2023-02-28T06-22-54.425506000Z--87e02340c9c5dab434d2e9f5cdbc3da06b8f47da.json");
                    Credentials credentials = WalletUtils.loadCredentials("test", file);

                    Abi abi = Abi.load(contractAddress, web3j, credentials, gasPrice, GasLimit);
                    BigInteger send_value = Convert.toWei(String.valueOf(askingTb.getCoin()), Convert.Unit.ETHER).toBigInteger();
                    abi.transfer(askingTb.getAddress(), send_value).send();

//                    Date now = new Date();
//                    CashFlowHistoryTb cashFlowHistoryTb = new CashFlowHistoryTb();
//                    cashFlowHistoryTb.setState(0);
//                    cashFlowHistoryTb.setFrom_address("server");
//                    cashFlowHistoryTb.setTo_address(askingTb.getAddress());
//                    cashFlowHistoryTb.setUser_id(jwtTokenProvider.getUserId(tokenHeader));
//                    cashFlowHistoryTb.setDate(now);
//                    cashFlowHistoryTb.setCoin(askingTb.getCoin());
//                    cashFlowHistoryRepository.save(cashFlowHistoryTb);

                    BigDecimal bigNumber1 = BigDecimal.valueOf(usertb.getCoin());
                    BigDecimal bigNumber2 = BigDecimal.valueOf(askingTb.getCoin());
                    BigDecimal value = bigNumber1.subtract(bigNumber2);
                    usertb.setCoin(value.doubleValue());
                    userRepository.save(usertb);
                    Date now = new Date();
                    askingTb.setType(1);
                    askingTb.setCompleted_time(now);
                    askingRepository.save(askingTb);
                    result.put("resultCode", "true");
                    return result;

                }catch(Exception e){
                    result.put("message", "web3 error");
                    result.put("resultCode", "false");
                    return result;
                }

            }

        }else{
            result.put("message", "Token validate or not admin");
            result.put("resultCode", "false");
            return result;
        }

    }

    @ApiOperation(value = "요청 거절", notes = "입금 또는 출금 승인 거절")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value="/admin/asking/{asking_id}", method = RequestMethod.DELETE)
    public HashMap admin_delete(@RequestHeader("token") String tokenHeader, @PathVariable("asking_id") int asking_id){

        HashMap<String,Object> result = new HashMap<>();

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        UserTb usertb = userRepository.getUserTbByUserId(jwtTokenProvider.getUserId(tokenHeader));

        if(!usertb.getRole().equals("admin")){
            result.put("message", "not admin");
            result.put("resultCode","false");
            return result;
        }

        if(jwtTokenProvider.validateToken(tokenHeader) && Objects.equals(usertb.getAccount(), "admin")){
            Date now = new Date();
            AskingTb askingTb = askingRepository.getAskingTbByAskingId(asking_id);
            askingTb.setType(2);
            askingTb.setCompleted_time(now);
            askingRepository.save(askingTb);
            return result;

        }else{
            result.put("message", "Token validate or not admin");
            result.put("resultCode", "false");
            return result;
        }
    }

    @ApiOperation(value = "서버 자산 확인", notes = "클레이튼 및 팁스 코인 자산 확인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value="/admin/balances", method = RequestMethod.GET)
    public HashMap balance(@RequestHeader("token") String tokenHeader) throws Exception {

        HashMap<String,Object> result = new HashMap<>();

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        Web3j web3j = Web3j.build(new HttpService("https://api.baobab.klaytn.net:8651"));

        BigInteger gasPrice = Convert.toWei("250", Convert.Unit.GWEI).toBigInteger();
        BigInteger GasLimit = BigInteger.valueOf(30000000L);
        String tips_contractAddress = "0x981AeB68B7A9d1B3d9341636D0f45660995C6Af5";
        EthGasPrice ethGasPrice = web3j.ethGasPrice().send();
        System.out.println("eth :" + ethGasPrice.getGasPrice());
        File file = new File("./UTC--2023-02-28T06-22-54.425506000Z--87e02340c9c5dab434d2e9f5cdbc3da06b8f47da.json");
        Credentials credentials = WalletUtils.loadCredentials("test", file);

        Abi abi = Abi.load(tips_contractAddress, web3j, credentials, gasPrice, GasLimit);

        BigInteger value = Convert.toWei("5", Convert.Unit.ETHER).toBigInteger();
        System.out.println(abi._balances(credentials.getAddress()).send());
        BigDecimal tips_balances_wei = Convert.fromWei(abi._balances(credentials.getAddress()).send().toString(), Convert.Unit.ETHER);
        System.out.println(tips_balances_wei);


        EthGetBalance klay_balances = web3j.ethGetBalance(credentials.getAddress(), DefaultBlockParameter.valueOf("latest"))
                .sendAsync()
                .get();
        System.out.println(klay_balances);
        BigDecimal klay_balances_wei = Convert.fromWei(String.valueOf(klay_balances.getBalance()), Convert.Unit.ETHER);

        System.out.println(klay_balances);
        result.put("klay", klay_balances_wei);
        result.put("tips", tips_balances_wei);
        return result;
    }

    @ApiOperation(value = "회원 조회 - 회원 리스트", notes = "회원 리스트")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value="/admin/account", method = RequestMethod.GET)
    public HashMap user_list(@RequestHeader("token") String tokenHeader) throws Exception {

        HashMap<String,Object> result = new HashMap<>();

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        UserTb user = userRepository.getUserTbByUserId(jwtTokenProvider.getUserId(tokenHeader));
        if(!user.getRole().equals("admin")){
            result.put("message", "not admin");
            result.put("resultCode","false");
            return result;
        }

        List<UserTb> user_list =  userRepository.getUserTbByRole("user");
        List<UserDto> userDtos = new ArrayList<>();

        for(int i=0;i<user_list.size();i++){
            UserDto userDto = new UserDto(user_list.get(i));
            userDtos.add(userDto);
        }

        result.put("user_list", userDtos);
        return result;
    }

    @ApiOperation(value = "회원 조회 - 회원 활성화 or 비활성화", notes = "회원 활성화상태였으면 비활성화로, 비활성화 상태였으면 활성화 상태로 변경")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user_id", value = "유저 id", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value="/admin/account/{user_id}", method = RequestMethod.PUT)
    public HashMap deactivation(@RequestHeader("token") String tokenHeader, @PathVariable("user_id") int user_id) throws Exception {

        HashMap<String,Object> result = new HashMap<>();

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }


        UserTb userTb = userRepository.getUserTbByUserId(jwtTokenProvider.getUserId(tokenHeader));
        if(!userTb.getRole().equals("admin")){
            result.put("message", "not admin");
            result.put("resultCode","false");
            return result;
        }

        try{
            UserTb user = userRepository.getUserTbByUserId(user_id);

            if(user.isState()){
                user.setState(false);
                userRepository.save(user);
            }else{
                user.setState(true);
                userRepository.save(user);
            }
            result.put("user", user);
            result.put("resultCode", "true");
            return result;

        }catch(Exception e){
            result.put("message", "db error");
            result.put("resultCode", "false");
            return result;
        }
    }

    @ApiOperation(value = "회원 조회 - 회원 탈퇴", notes = "회원을 삭제할 id로 강제 회원 탈퇴")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value="/admin/account/{user_id}", method = RequestMethod.DELETE)
    public HashMap account_delete(@RequestHeader("token") String tokenHeader, @PathVariable("user_id") int user_id) throws Exception {

        HashMap<String,Object> result = new HashMap<>();

        try{

            if(!jwtTokenProvider.validateToken(tokenHeader)){
                result.put("message", "Token validate");
                result.put("resultCode", "false");
                return result;
            }

            UserTb userTb = userRepository.getUserTbByUserId(jwtTokenProvider.getUserId(tokenHeader));
            if(!userTb.getRole().equals("admin")){
                result.put("message", "not admin");
                result.put("resultCode","false");
                return result;
            }

            UserTb user = userRepository.getUserTbByUserId(user_id);
            userRepository.delete(user);
            result.put("resultCode", "true");
            return result;
        }catch(Exception e){
            result.put("message", "db error");
            result.put("resultCode", "false");
            return result;
        }

    }


    @ApiOperation(value = "회원 조회 - 회원 자산 리스트 조회", notes = "")
    @ApiImplicitParams({
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value="/admin/account/asset/{user_id}", method = RequestMethod.GET)
    public HashMap asset(@RequestHeader("token") String tokenHeader, @PathVariable("user_id") int user_id) throws Exception {

        HashMap<String,Object> result = new HashMap<>();

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        UserTb userTb = userRepository.getUserTbByUserId(jwtTokenProvider.getUserId(tokenHeader));
        if(!userTb.getRole().equals("admin")){
            result.put("message", "not admin");
            result.put("resultCode","false");
            return result;
        }

        List<StakingTb> stakingTb = stakingRepository.getStakingTbByUserId(user_id);
        Date now = new Date();
        StakingTb stakingTb1 = new StakingTb();
        stakingTb1.setStart_amount(0);
        stakingTb1.setExpire_date(now);
        stakingTb1.setAdd_amount(0);
        if(stakingTb.size() != 0){
            stakingTb1.setAdd_amount(stakingTb.get(0).getAdd_amount());
            stakingTb1.setExpire_date(stakingTb.get(0).getExpire_date());
            stakingTb1.setStart_amount(stakingTb.get(0).getStart_amount());
        }
        UserAssetDto userAssetDto = new UserAssetDto(userTb, stakingTb1);
        result.put("user_list", userAssetDto);
        result.put("resultCode", "true");

        return result;
    }

    @ApiOperation(value = "강제 입금", notes = "사용자의 지갑에 코인(포인트) 입금")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user_id", value = "입금할 user id", required = true),
            @ApiImplicitParam(name = "coin", value = "입금할 coin 갯수", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value="/admin/order", method = RequestMethod.POST)
    public HashMap input_order(@RequestHeader("token") String tokenHeader, @RequestBody HashMap<String, Object> data) throws Exception {

        HashMap<String,Object> result = new HashMap<>();

        if(ObjectUtils.isEmpty(data.get("user_id"))){
            result.put("message", "user_id is null");
            result.put("resultCode", "false");
            return result;
        }
        if(ObjectUtils.isEmpty(data.get("coin"))){
            result.put("message", "coin is null");
            result.put("resultCode", "false");
            return result;
        }

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        UserTb user = userRepository.getUserTbByUserId(jwtTokenProvider.getUserId(tokenHeader));
        if(!user.getRole().equals("admin")){
            result.put("message", "not admin");
            result.put("resultCode","false");
            return result;
        }

        int user_id = Integer.parseInt(data.get("user_id").toString());
        double coin = Double.parseDouble(data.get("coin").toString());

        UserTb userTb = userRepository.getUserTbByUserId(user_id);
        BigDecimal number1 = BigDecimal.valueOf(userTb.getCoin());
        BigDecimal number2 = BigDecimal.valueOf(coin);
        userTb.setCoin(number1.add(number2).doubleValue());
        userRepository.save(userTb);

        Date now = new Date();
        AskingTb askingTb = new AskingTb();
        askingTb.setType(4);
        askingTb.setInput_output(true);
        askingTb.setCoin(coin);
        askingTb.setAddress("관리자");
        askingTb.setCompleted_time(now);
        askingTb.setAsking_time(now);
        askingTb.setUser_id(user_id);
        askingRepository.save(askingTb);
        result.put("resultCode", "true");
        return result;
    }

    @ApiOperation(value = "강제 출금", notes = "사용자의 지갑에 코인(포인트) 입금")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user_id", value = "입금할 user id", required = true),
            @ApiImplicitParam(name = "coin", value = "입금할 coin 갯수", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value="/admin/order", method = RequestMethod.PUT)
    public HashMap output_order(@RequestHeader("token") String tokenHeader, @RequestBody HashMap<String, Object> data) throws Exception {

        HashMap<String,Object> result = new HashMap<>();

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        if(ObjectUtils.isEmpty(data.get("user_id"))){
            result.put("message", "user_id is null");
            result.put("resultCode", "false");
            return result;
        }
        if(ObjectUtils.isEmpty(data.get("coin"))){
            result.put("message", "coin is null");
            result.put("resultCode", "false");
            return result;
        }

        int user_id = Integer.parseInt(data.get("user_id").toString());
        double coin = Double.parseDouble(data.get("coin").toString());


        UserTb user = userRepository.getUserTbByUserId(jwtTokenProvider.getUserId(tokenHeader));
        if(!user.getRole().equals("admin")){
            result.put("message", "not admin");
            result.put("resultCode","false");
            return result;
        }

        try{
            UserTb userTb = userRepository.getUserTbByUserId(user_id);
            BigDecimal number1 = BigDecimal.valueOf(userTb.getCoin());
            BigDecimal number2 = BigDecimal.valueOf(coin);
            userTb.setCoin(number1.subtract(number2).doubleValue());
            userRepository.save(userTb);

            Date now = new Date();
            AskingTb askingTb = new AskingTb();
            askingTb.setType(4);
            askingTb.setInput_output(true);
            askingTb.setCoin(coin);
            askingTb.setAddress("관리자");
            askingTb.setAsking_time(now);
            askingTb.setCompleted_time(now);
            askingTb.setUser_id(user_id);
            askingRepository.save(askingTb);
            result.put("resultCode", "true");
            return result;
        } catch (Exception e){
            result.put("message", "db error");
            result.put("resultCode", "false");
            return result;
        }

    }

    @ApiOperation(value = "강제 게시글 삭제", notes = "부적절한 게시글 삭제")
    @ApiImplicitParams({
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value="/admin/community/{community_id}", method = RequestMethod.DELETE)
    public HashMap community_delete(@RequestHeader("token") String tokenHeader, @PathVariable("community_id") int community_id) throws Exception {

        HashMap<String,Object> result = new HashMap<>();

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }


        UserTb user = userRepository.getUserTbByUserId(jwtTokenProvider.getUserId(tokenHeader));
        if(!user.getRole().equals("admin")){
            result.put("message", "not admin");
            result.put("resultCode","false");
            return result;
        }

        try{
            CommunityTb communityTb = communityRepository.getCommunityById(community_id);
            communityRepository.delete(communityTb);
            result.put("resultCode", "true");
            return result;
        } catch (Exception e){
            result.put("message", "db error");
            result.put("resultCode", "false");
            return result;
        }
    }

    @ApiOperation(value = "게시글 블라인드", notes = "부적절한 게시글 보이지 않게 관리")
    @ApiImplicitParams({
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value="/admin/community/{community_id}", method = RequestMethod.PUT)
    public HashMap community_blind(@RequestHeader("token") String tokenHeader, @PathVariable("community_id") int community_id) throws Exception {

        HashMap<String,Object> result = new HashMap<>();

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        UserTb user = userRepository.getUserTbByUserId(jwtTokenProvider.getUserId(tokenHeader));
        if(!user.getRole().equals("admin")){
            result.put("message", "not admin");
            result.put("resultCode","false");
            return result;
        }

        try{
            CommunityTb communityTb = communityRepository.getCommunityById(community_id);
            if(communityTb.isState()){
                communityTb.setState(false);
            }else{
                communityTb.setState(true);
            }
            communityRepository.save(communityTb);
            result.put("state", communityTb.isState());
            result.put("resultCode", "true");
            return result;
        } catch (Exception e){
            result.put("message", "db error");
            result.put("resultCode", "false");
            return result;
        }
    }

    @ApiOperation(value = "현재 로그인된 관리자 계정 비밀번호 변경", notes = "비밀번호 변경")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pw", value = "변경할 패스워드", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value="/admin/setting", method = RequestMethod.PATCH)
    public HashMap community_blind(@RequestHeader("token") String tokenHeader, @RequestBody HashMap<String, Object> data){

        HashMap<String,Object> result = new HashMap<>();

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        UserTb user = userRepository.getUserTbByUserId(jwtTokenProvider.getUserId(tokenHeader));
        if(!user.getRole().equals("admin")){
            result.put("message", "not admin");
            result.put("resultCode","false");
            return result;
        }

        if(ObjectUtils.isEmpty(data.get("pw"))){
            result.put("message", "pw is null");
            result.put("resultCode", "false");
            return result;
        }

        String pw = data.get("pw").toString();

        try{
            user.setPw(passwordEncoder.encode(pw));
            userRepository.save(user);
            result.put("resultCode", "true");
            return result;
        } catch (Exception e){
            result.put("message", "db error");
            result.put("resultCode", "false");
            return result;
        }
    }

    @ApiOperation(value = "메인 카테고리 생성", notes = "카테고리 생성")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "카테고리 이름", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value="/admin/category", method = RequestMethod.POST)
    public HashMap create_category(@RequestHeader("token") String tokenHeader, @RequestBody HashMap<String, Object> data){

        HashMap<String,Object> result = new HashMap<>();

        UserTb user = userRepository.getUserTbByUserId(jwtTokenProvider.getUserId(tokenHeader));
        if(!jwtTokenProvider.validateToken(tokenHeader) || ObjectUtils.isEmpty(user.getCoin())){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }


        if(!user.getRole().equals("admin")){
            result.put("message", "not admin");
            result.put("resultCode","false");
            return result;
        }

        if(ObjectUtils.isEmpty(data.get("name"))){
            result.put("message", "name is null");
            result.put("resultCode", "false");
            return result;
        }

        String category_name = data.get("name").toString();

        try{
            CategoryTb categoryTb = new CategoryTb();
            categoryTb.setCategory_name(category_name);
            categoryTb.setParent(0);
            categoryRepository.save(categoryTb);
            result.put("resultCode", "true");
            return result;
        } catch (Exception e){
            result.put("message", "db error");
            result.put("resultCode", "false");
            return result;
        }
    }

    @ApiOperation(value = "서브 카테고리 생성", notes = "URL에 있는 category_id를 부모로 하는 서브(자녀) 카테고리 생성")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "카테고리 이름", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value="/admin/category/{category_id}", method = RequestMethod.POST)
    public HashMap create_category(@RequestHeader("token") String tokenHeader, @RequestBody HashMap<String, Object> data, @PathVariable("category_id") int parent_id){

        HashMap<String,Object> result = new HashMap<>();

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        UserTb user = userRepository.getUserTbByUserId(jwtTokenProvider.getUserId(tokenHeader));
        if(!user.getRole().equals("admin")){
            result.put("message", "not admin");
            result.put("resultCode","false");
            return result;
        }

        if(ObjectUtils.isEmpty(data.get("name"))){
            result.put("message", "name is null");
            result.put("resultCode", "false");
            return result;
        }

        String category_name = data.get("name").toString();

        try{
            CategoryTb categoryTb = new CategoryTb();
            categoryTb.setCategory_name(category_name);
            categoryTb.setParent(parent_id);
            categoryRepository.save(categoryTb);
            result.put("resultCode", "true");
            return result;
        } catch (Exception e){
            result.put("message", "db error");
            result.put("resultCode", "false");
            return result;
        }
    }

    @ApiOperation(value = "카테고리 조회하기", notes = "카테고리 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value="/admin/category", method = RequestMethod.GET)
    public HashMap get_cateogry(@RequestHeader("token") String tokenHeader){

        HashMap<String, Object> result = new HashMap<>();

        UserTb user = userRepository.getUserTbByUserId(jwtTokenProvider.getUserId(tokenHeader));
        if(!jwtTokenProvider.validateToken(tokenHeader) || ObjectUtils.isEmpty(user)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        if(!user.getRole().equals("admin")){
            result.put("message", "not admin");
            result.put("resultCode","false");
            return result;
        }

        try{

            List<CategoryTb> mainCategory = categoryRepository.getCategoryParent();
            List<CategoryDto> categoryDtoList = new ArrayList<>();

            for(int i=0;i<mainCategory.size();i++){
                List<CategoryTb> subCategory = categoryRepository.getCategoryChildByParent(mainCategory.get(i).getCategory_id());
                CategoryDto categoryDto = new CategoryDto(mainCategory.get(i), subCategory);
                categoryDtoList.add(categoryDto);
            }

            result.put("category_list", categoryDtoList);
            result.put("resultCode", "true");
            return result;

        }catch (Exception e ){
            result.put("message", "db error");
            result.put("resultCode", "false");
            return result;
        }
    }

    @ApiOperation(value = "카테고리 삭제", notes = "카테고리 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value="/admin/category/{category_id}", method = RequestMethod.DELETE)
    public HashMap get_cateogry(@RequestHeader("token") String tokenHeader, @PathVariable("category_id") int category_id){

        HashMap<String, Object> result = new HashMap<>();

        UserTb user = userRepository.getUserTbByUserId(jwtTokenProvider.getUserId(tokenHeader));
        if(!jwtTokenProvider.validateToken(tokenHeader) || ObjectUtils.isEmpty(user)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        if(!user.getRole().equals("admin")){
            result.put("message", "not admin");
            result.put("resultCode","false");
            return result;
        }

        try{

            CategoryTb categoryTb = categoryRepository.getReferenceById(category_id);
            System.out.println(categoryTb.getCategory_name());
            categoryRepository.delete(categoryTb);
            result.put("resultCode", "true");
            return result;

        }catch (Exception e ){
            result.put("message", "db error");
            result.put("resultCode", "false");
            return result;
        }
    }

}
