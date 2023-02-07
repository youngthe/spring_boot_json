package com.example.spring.controller;

import com.example.spring.dao.CommentTb;
import com.example.spring.dao.CommunityTb;
import com.example.spring.dao.LikeTb;
import com.example.spring.dao.UserTb;
import com.example.spring.repository.CommentRepository;
import com.example.spring.repository.CommunityRepository;
import com.example.spring.repository.LikeRepository;
import com.example.spring.repository.UserRepository;
import com.example.spring.utils.JwtTokenProvider;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

@RestController
//@Controller
public class CommunityContoller {

    @Autowired
    private CommunityRepository communityRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private LikeRepository likeRepository;


    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @ApiOperation(value = "게시판", notes = "게시글을 보여주는 게시판 페이지")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nowpage", value = "현재 페이지 번호", required = true),
            @ApiImplicitParam(name = "count", value = "보여줄 게시글 갯수", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode, community")
    })
    @RequestMapping(value = "/community", method = RequestMethod.GET)
    public HashMap community(@RequestBody HashMap<String, Object> data, @RequestHeader("token") String tokenHeader) {

        HashMap<String, Object> result = new HashMap<>();
        int nowpage = Integer.parseInt(data.get("nowpage").toString());
        int countpage = Integer.parseInt(data.get("count").toString());

        int start = countpage*nowpage;
        int end = countpage*(nowpage+1) - 1;

        log.info("start : {}", start);
        log.info("end : {}", end);

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        try{
            List<CommunityTb> communityTbList  =  communityRepository.getCommunity();
            int communityList_maxsize = communityTbList.size();
            log.info("maxSize : {} ", communityList_maxsize);
            if(end>=communityList_maxsize) end = communityList_maxsize-1;
            List<CommunityTb> communityTbList2 = new ArrayList<>();

            for(int i=start;i<=end;i++){
                communityTbList2.add(communityTbList.get(i));
            }

            result.put("community",communityTbList2);
            result.put("resultCode", "true");
            return result;
        }catch (Exception e){
            log.info("{}", e);
            log.info("/community error");
            result.put("resultCode", "false");
            return result;
        }
    }
    
    @ApiOperation(value = "게시글 쓰기", notes = "게시글 쓰는 페이지")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "제목", required = true),
            @ApiImplicitParam(name = "content", value = "본문", required = true),
            @ApiImplicitParam(name = "upload", value = "이미지"),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value = "/community", method = RequestMethod.POST)
    public HashMap community_save(@RequestHeader("token") String tokenHeader,
                                  @RequestParam(value = "upload", required = false) MultipartFile file, @RequestParam("title") String title, @RequestParam("content") String content) throws IOException{

        HashMap<String, Object> result = new HashMap<>();
        String filename = null;

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        if(file != null){
            System.out.println("test");
            long timestamp = new Timestamp(System.currentTimeMillis()).getTime();
            String[] filetotal = file.getOriginalFilename().split("\\.");
            System.out.println(file.getOriginalFilename());
            System.out.println(filetotal[1]);
            filename = timestamp + "." + filetotal[filetotal.length-1];
            String fullPath="C:\\project\\spring_boot_json\\src\\main\\resources\\static\\imgs\\" + filename;
            log.info("파일 저장 {}", fullPath);
            file.transferTo(new File(fullPath));

        }


        log.info("jwt : {} ", tokenHeader);
        log.info("title {} ", title);
        log.info("content {} ", content);

        try{
            UserTb user = userRepository.getUserTbByUserId(jwtTokenProvider.getUserId(tokenHeader));
            LocalDate now = LocalDate.now();
            CommunityTb communityTb = new CommunityTb();
            communityTb.setFile_name(filename);
            communityTb.setTitle(title);
            communityTb.setContent(content);
            communityTb.setUser_id(user.getUser_id());
            communityTb.setDate(now);
            communityRepository.save(communityTb);

            result.put("resultCode", "true");
            return result;
        }catch (Exception e){
            log.info("/community/write error");
            result.put("resultCode", "false");
            return result;
        }

    }

    @ApiOperation(value = "게시글 상세 페이지", notes = "제목 뿐만 아니라 본문이 있는 페이지")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value = "/community/{community_id}", method = RequestMethod.GET)
    public HashMap community_detail(@PathVariable("community_id") int community_id , @RequestHeader("token") String tokenHeader){

        HashMap<String, Object> result = new HashMap<>();

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        try{

            CommunityTb community = communityRepository.getCommunityById(community_id);
            List<CommentTb> commentTbList = commentRepository.getCommentList(community_id);

            communityRepository.Increase_like(community);
            result.put("community", community);
            result.put("comment", commentTbList);
            result.put("resultCode", "true");
            return result;

        }catch(Exception e){
            System.out.println("db error");
            System.out.println(e);
            result.put("message", "page null");
            result.put("resultCode", "false");
            return result;
        }
    }


    @ApiOperation(value = "게시글 삭제", notes = "게시글 번호로 게시글 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode, community, comment")
    })
    @RequestMapping(value= "/community/{community_id}", method = RequestMethod.DELETE)
    public HashMap community_delete(@PathVariable("community_id") int community_id, @RequestHeader("token") String tokenHeader){

        HashMap<String, Object> result = new HashMap<>();

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }
        try{
            UserTb user = userRepository.getUserTbByUserId(jwtTokenProvider.getUserId(tokenHeader));

            CommunityTb community = communityRepository.getCommunityById(community_id);

            int community_writer = community.getUser_id();

            if(user.getUser_id() == community_writer){

                communityRepository.deleteById(community_id);
                commentRepository.deleteByCommunityId(community_id);
                result.put("resultCode", "true");

            } else {
                log.info("unauthorized");
                result.put("message","unauthorized");
                result.put("resultCode", "false");
            }
        }catch(Exception e){
            result.put("message", "don't exist community");
            result.put("resultCode", "false");
        }

        return result;
    }
    
    @ApiOperation(value = "게시글 수정", notes = "게시글 번호로 게시글 수정")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "community_id", value = "게시글 id", required = true),
            @ApiImplicitParam(name = "title", value = "변경할 제목", required = true),
            @ApiImplicitParam(name = "content", value = "변경할 본문", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })

    @RequestMapping(value = "/community/{community_id}", method = RequestMethod.PATCH)
    public HashMap community_modify(@PathVariable ("community_id") int community_id,@RequestBody HashMap<String, Object> data, @RequestHeader("token") String tokenHeader) throws IOException {

        HashMap<String, Object> result = new HashMap<>();
        String title = data.get("title").toString();
        String content = data.get("content").toString();

        log.info("title : {}", title);
        log.info("content : {}", content);

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }
        try{
            CommunityTb communityTb = communityRepository.getCommunityById(community_id);
            int user_id = jwtTokenProvider.getUserId(tokenHeader);

            if(user_id == communityTb.getUser_id()){
                communityTb.setTitle(title);
                communityTb.setContent(content);
                communityRepository.updateCommunity(communityTb);
                result.put("resultCode", "true");

            }else{
                result.put("message", "unauthorized");
                result.put("resultCode", "false");
            }

        }catch (Exception e){
            result.put("message", "don't exist community");
            result.put("resultCode", "false");
        }


        return result;
    }

    @ApiOperation(value = "게시글 좋아요", notes = "게시글 좋아요 버튼")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value="/community/{community_id}", method = RequestMethod.PUT)
    public HashMap heart_push(@PathVariable ("community_id") int community_id, @RequestHeader("token") String tokenHeader){

        HashMap<String, Object> result = new HashMap<>();

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        log.info("community_id : {}", community_id);

        LikeTb likeTb = new LikeTb();
        likeTb.setCommunity_id(community_id);
        likeTb.setUser_id(jwtTokenProvider.getUserId(tokenHeader));

        if(likeRepository.LikeCheck(likeTb)){

            likeRepository.save(likeTb);
            result.put("message", "pushed");
            result.put("resultCode", "true");

        }else{
            LikeTb heart = likeRepository.getLike(likeTb);
            likeRepository.delete(heart);
            result.put("message", "unpushed");
            result.put("resultCode", "true");
        }
        return result;

    }
}
