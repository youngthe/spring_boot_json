package com.example.spring.spring.controller;

import com.example.spring.spring.dao.CommentTb;
import com.example.spring.spring.dao.CommunityTb;
import com.example.spring.spring.dao.HeartTb;
import com.example.spring.spring.dao.UserTb;
import com.example.spring.spring.repository.*;
import com.example.spring.spring.utils.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@RestController
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
    HeartRepository heartRepository;

    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @RequestMapping(value = "/community/")
    public HashMap community(@RequestBody HashMap<String, Object> data) throws JSONException {

        HashMap<String, Object> result = new HashMap<>();
        String jwt = data.get("jwt").toString();
        int nowpage = Integer.parseInt(data.get("nowpage").toString());
        int countpage = Integer.parseInt(data.get("countpage").toString());

        int start = countpage*nowpage;
        int end = countpage*(nowpage+1) - 1;

        log.info("start : {}", start);
        log.info("end : {}", end);

        if(!jwtTokenProvider.validateToken(jwt)){
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
                    temp.put("writer", communityTbList.get(i).getUser().getName());
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


    @RequestMapping(value = "/community/write/")
    public HashMap community_save(@RequestBody HashMap<String, Object> data){

        HashMap<String, Object> result = new HashMap<>();
        String jwt = data.get("jwt").toString();
        String title = data.get("title").toString();
        String content = data.get("content").toString();

        if(!jwtTokenProvider.validateToken(jwt)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        log.info("jwt : {} ", jwt);
        log.info("title {} ", title);
        log.info("content {} ", content);

        try{
            UserTb user = userRepository.getUserTbByAccount(jwtTokenProvider.getUserAccount(jwt));
            LocalDate now = LocalDate.now();
            CommunityTb communityTb = new CommunityTb();
            communityTb.setTitle(title);
            communityTb.setContent(content);
            communityTb.setUser(user);
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
    @RequestMapping(value = "/community/detail/")
    public HashMap community_detail(@RequestBody HashMap<String, Object> data){

        String jwt = data.get("jwt").toString();
        int community_id = Integer.parseInt(data.get("community_id").toString());
        HashMap<String, Object> result = new HashMap<>();

        if(!jwtTokenProvider.validateToken(jwt)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        try{

            JSONArray communityArray = new JSONArray();
            JSONObject temp1 = new JSONObject();
            JSONArray commentArray = new JSONArray();
            JSONObject temp2 = new JSONObject();

            CommunityTb community = communityRepository.getCommunityById(community_id);
            List<CommentTb> commentTbList = commentRepository.getCommentList(community_id);

            temp1.put("content", community.getContent());
            temp1.put("title", community.getTitle());
            temp1.put("date", community.getDate());
            temp1.put("hits", community.getHits());
            temp1.put("writer", community.getUser().getName());
            communityArray.put(temp1);

            for(CommentTb comment : commentTbList){
                temp2.put("comment",comment.getComment());
                temp2.put("date", comment.getDate());
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
            result.put("resultCode", "false");
            return result;
        }
    }



    @RequestMapping(value= "/community/delete/")
    public HashMap community_delete(@RequestBody HashMap<String, Object> data){


        String jwt = data.get("jwt").toString();
        int community_id = Integer.parseInt(data.get("community_id").toString());

        HashMap<String, Object> result = new HashMap<>();

        if(!jwtTokenProvider.validateToken(jwt)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        UserTb user = userRepository.getUserTbByAccount(jwtTokenProvider.getUserAccount(jwt));

        CommunityTb community = communityRepository.getCommunityById(community_id);

        String community_writer = community.getUser().getAccount();

        if(user.getAccount().equals(community_writer)){
            communityRepository.deleteById(community_id);

            //게시글과 관련된 comment 전부 삭제
            CommentTb commentOne = commentRepository.getCommentByCommentId(community.getId());
            List<CommentTb> commentTbList = commentRepository.getCommentList((int) commentOne.getCommunity_id());

            Collections.reverse(commentTbList);
            for(CommentTb comment : commentTbList){
                if(comment.getParent().getId() == commentOne.getId()){
                    System.out.println(comment.getComment());
                    commentRepository.deleteById(comment.getId());
                }
            }

            result.put("resultCode", "true");
            return result;
         } else {
            log.info("error");
            log.info("/community/delete/{community_id}");
            result.put("resultCode", "false");
            return result;
         }
    }

    @RequestMapping(value = "/community/modify/{community_id}")
    public String communit_modify(@PathVariable int community_id, HttpServletResponse response,HttpServletRequest request, HttpSession session, Model model) throws IOException {


        if(request.getMethod().equals("GET")){
            String user = (String) session.getAttribute("user");
            CommunityTb communityTb = communityRepository.getCommunityById(community_id);
            if(user.equals(communityTb.getUser().getAccount())){

                model.addAttribute("community", communityTb);

            }else{
            }
            return "community/community_modify";
        }else if(request.getMethod().equals("POST")){
            String user = (String) session.getAttribute("user");
            System.out.println("user : "+ user);
            CommunityTb communityTb = communityRepository.getCommunityById(community_id);

            if(user.equals(communityTb.getUser().getAccount())){
                String title = request.getParameter("title");
                String content = request.getParameter("content");
                System.out.println(title);
                System.out.println(content);
                communityTb.setTitle(title);
                communityTb.setContent(content);
                communityRepository.updateCommunity(communityTb);
            }else{
            }

            return "redirect:/community";
        }else{
            return "redirect:/community";
        }

    }

    @RequestMapping(value="/community/heart/push/")
    public HashMap heart_push(@RequestBody HashMap<String, Object> data){

        String jwt = data.get("jwt").toString();
        String community_id = data.get("community_id").toString();
        HashMap<String, Object> result = new HashMap<>();

        if(!jwtTokenProvider.validateToken(jwt)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        UserTb user = userRepository.getUserTbByAccount(jwtTokenProvider.getUserAccount(jwt));
        log.info("community_id : {}", community_id);

        try{
            HeartTb heartTb = new HeartTb();
            heartTb.setCommunity_id(community_id);
            heartTb.setWriter_id(user.getId());

            if(heartRepository.HeartCheck(heartTb)){
                heartRepository.save(heartTb);
                result.put("resultCode", "true");
            }else{
                result.put("message", "already");
                result.put("resultCode", "false");
            }
            return result;


        }catch (Exception e){
            result.put("resultCode", "false");
            return result;
        }

    }
    @RequestMapping(value="/community/heart/unpush/")
    public HashMap heart_unpush(@RequestBody HashMap<String, Object> data){

        String jwt = data.get("jwt").toString();
        String community_id = data.get("community_id").toString();
        HashMap<String, Object> result = new HashMap<>();

        if(!jwtTokenProvider.validateToken(jwt)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        UserTb user = userRepository.getUserTbByAccount(jwtTokenProvider.getUserAccount(jwt));
        log.info("community_id : {}", community_id);

        try{
            HeartTb heartTb = new HeartTb();
            heartTb.setCommunity_id(community_id);
            heartTb.setWriter_id(user.getId());
            heartRepository.deleteByCommunityIdAndWriterId(heartTb);
            result.put("resultCode", "true");
            return result;

        }catch (Exception e){
            result.put("resultCode", "false");
            return result;
        }

    }

}
