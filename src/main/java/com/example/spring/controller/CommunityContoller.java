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
            JSONArray communityArray = new JSONArray();
            List<CommunityTb> communityTbList  =  communityRepository.getCommunity();
            int communityList_maxsize = communityTbList.size();
            log.info("maxSize : {} ", communityList_maxsize);
            if(end>=communityList_maxsize) end = communityList_maxsize-1;

            for(int i=start;i<=end;i++){
                    JSONObject temp = new JSONObject();
                    temp.put("title", communityTbList.get(i).getTitle());
                    temp.put("content", communityTbList.get(i).getContent());
                    temp.put("date", communityTbList.get(i).getDate());
                    temp.put("hits", communityTbList.get(i).getHits());
                    temp.put("user_id", communityTbList.get(i).getUser_id());
                    communityArray.put(temp);
            }
            System.out.println(communityArray);
            result.put("community",communityArray.toString());
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
    @RequestMapping(value = "/community/write", method = RequestMethod.POST)
    public HashMap community_save(@RequestHeader("token") String tokenHeader,
                                  @RequestParam(value = "file", required = false) MultipartFile file, @RequestBody HashMap<String, Object> data) throws IOException{

        HashMap<String, Object> result = new HashMap<>();
        String filename = null;
        String title = data.get("title").toString();
        String content = data.get("content").toString();

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
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "제목", required = true),
            @ApiImplicitParam(name = "content", value = "본문", required = true),
            @ApiImplicitParam(name = "upload", value = "이미지"),

    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode, community, comment")
    })
    @RequestMapping(value = "/community/detail", method = RequestMethod.GET)
    public HashMap community_detail(@RequestBody HashMap<String, Object> data, @RequestHeader("token") String tokenHeader){

        int community_id = Integer.parseInt(data.get("community_id").toString());
        HashMap<String, Object> result = new HashMap<>();

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        try{

            JSONArray communityArray = new JSONArray();
            JSONObject temp1 = new JSONObject();
            JSONArray commentArray = new JSONArray();

            CommunityTb community = communityRepository.getCommunityById(community_id);
            List<CommentTb> commentTbList = commentRepository.getCommentList(community_id);

            temp1.put("content", community.getContent());
            temp1.put("title", community.getTitle());
            temp1.put("date", community.getDate());
            temp1.put("hits", community.getHits());
            temp1.put("user_id", community.getUser_id());
            communityArray.put(temp1);

            for(CommentTb comment : commentTbList){
                JSONObject temp2 = new JSONObject();
                temp2.put("id", comment.getComment_id());
                temp2.put("comment",comment.getComment());
                temp2.put("date", comment.getDate());
                temp2.put("parent", comment.getComment_id());
                commentArray.put(temp2);
            }

            communityRepository.Increase_like(community);
            result.put("community", communityArray.toString());
            result.put("comment", commentArray.toString());
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
    @ApiImplicitParams({
            @ApiImplicitParam(name = "community_id", value = "게시글 id", required = true),

    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode, community, comment")
    })
    @RequestMapping(value= "/community/delete", method = RequestMethod.GET)
    public HashMap community_delete(@RequestBody HashMap<String, Object> data, @RequestHeader("token") String tokenHeader){

        int community_id = Integer.parseInt(data.get("community_id").toString());

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

    @RequestMapping(value = "/community/modify", method = RequestMethod.POST)
    public HashMap community_modify(@RequestBody HashMap<String, Object> data, @RequestHeader("token") String tokenHeader) throws IOException {

        HashMap<String, Object> result =new HashMap<>();
        int community_id = Integer.parseInt(data.get("community_id").toString());
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
    @ApiImplicitParams({
            @ApiImplicitParam(name = "community_id", value = "게시글 id", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value="/community/likes", method = RequestMethod.GET)
    public HashMap heart_push(@RequestBody HashMap<String, Object> data, @RequestHeader("token") String tokenHeader){

        int community_id = Integer.parseInt(data.get("community_id").toString());
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
