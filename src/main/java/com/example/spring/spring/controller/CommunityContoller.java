package com.example.spring.spring.controller;

import com.example.spring.spring.dao.CommentTb;
import com.example.spring.spring.dao.CommunityTb;
import com.example.spring.spring.dao.LikeTb;
import com.example.spring.spring.dao.UserTb;
import com.example.spring.spring.repository.*;
import com.example.spring.spring.utils.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
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
    CommunityRepository communityRepository;
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    LikeRepository likeRepository;


    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @RequestMapping(value = "/community", method = RequestMethod.GET)
    public HashMap community(@RequestBody HashMap<String, Object> data, @RequestHeader("jwt") String tokenHeader) throws JSONException {

        HashMap<String, Object> result = new HashMap<>();
        int nowpage = Integer.parseInt(data.get("nowpage").toString());
        int countpage = Integer.parseInt(data.get("countpage").toString());

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
                    System.out.println(temp);
                    communityArray.put(temp);
            }
            System.out.println(communityArray);
            result.put("list",communityArray.toString());
            result.put("resultCode", "true");
            return result;
        }catch (Exception e){
            log.info("{}", e);
            log.info("/community error");
            result.put("resultCode", "false");
            return result;
        }
    }

    @RequestMapping(value = "/community/write", method = RequestMethod.POST)
    public HashMap community_save(@RequestHeader("jwt") String tokenHeader,
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


    @RequestMapping(value = "/community/detail", method = RequestMethod.GET)
    public HashMap community_detail(@RequestBody HashMap<String, Object> data, @RequestHeader("jwt") String tokenHeader){

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



    @RequestMapping(value= "/community/delete", method = RequestMethod.GET)
    public HashMap community_delete(@RequestBody HashMap<String, Object> data, @RequestHeader("jwt") String tokenHeader){

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
                log.info("don't have authority");
                result.put("message","don't have authority");
                result.put("resultCode", "false");
            }
        }catch(Exception e){
            result.put("message", "don't exist community");
            result.put("resultCode", "false");
        }

        return result;
    }

    @RequestMapping(value = "/community/modify", method = RequestMethod.POST)
    public HashMap community_modify(@RequestBody HashMap<String, Object> data, @RequestHeader("jwt") String tokenHeader) throws IOException {

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
                result.put("message", "don't have authority");
                result.put("resultCode", "false");
            }

        }catch (Exception e){
            result.put("message", "don't exist community");
            result.put("resultCode", "false");
        }


        return result;
    }

    @RequestMapping(value="/community/likes", method = RequestMethod.GET)
    public HashMap heart_push(@RequestBody HashMap<String, Object> data, @RequestHeader("jwt") String tokenHeader){

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

    @RequestMapping(value = "/site")
    public String site (){

        return "/community/community_write";
    }

}
