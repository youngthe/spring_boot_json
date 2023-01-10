package com.example.spring.spring.controller;

import com.example.spring.spring.dao.CommentTb;
import com.example.spring.spring.dao.CommunityTb;
import com.example.spring.spring.dao.HeartTb;
import com.example.spring.spring.dao.UserTb;
import com.example.spring.spring.repository.CommentRepository;
import com.example.spring.spring.repository.CommunityRepository;
import com.example.spring.spring.repository.UserRepository;
import com.example.spring.spring.repository.UserTbRepositoryCustom;
import com.example.spring.spring.utils.ScriptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class CommunityContoller {

    @Autowired
    CommunityRepository communityRepository;
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/community")
    public String community_view(HttpServletRequest request, HttpSession session, Model model){

        if(session.getAttribute("user") == null){
            return "redirect:/";
        }
        List<CommunityTb> communityTb;

        String search = request.getParameter("search");
        if(search == null){
            
            try{
                communityTb  =  communityRepository.getCommunity();
            }catch(Exception e){
                System.out.println("비었음");
                return "/community/community";
            }

//            System.out.println("try : " + communityTb.get(0).getUser().getName());
//            System.out.println("try : " + communityTb.get(1).getUser().getName());
//            System.out.println("try : " + communityTb.get(2).getUser().getName());

        }else{
            communityTb = communityRepository.getCommunityBySearch(search);
        }

        model.addAttribute("community_list", communityTb);
        return "/community/community";
    }


//    @RequestMapping(value = "/community/save")
//    public String community_save(HttpServletRequest request, HttpSession session){
//
//        if(session.getAttribute("user") == null){
//            return "redirect:/";
//        }
//        String title = request.getParameter("title");
//        String content = request.getParameter("content");
//        String account = (String) session.getAttribute("user");
//
//        LocalDate now = LocalDate.now();
//        CommunityTb communityTb = new CommunityTb();
//        communityTb.setTitle(title);
//        communityTb.setContent(content);
//        communityTb.setWriterPk(1);
//        communityTb.setDate(now);
//        communityRepository.save(communityTb);
//
//        return "redirect:/community";
//    }

    @RequestMapping(value = "/community/write")
    public String community_write(HttpSession session, HttpServletRequest request){


        if(session.getAttribute("user") == null){
            return "redirect:/";
        }

        if(request.getMethod().equals("GET")){
            return "community/community_write";
        }else if(request.getMethod().equals("POST")){

            String title = request.getParameter("title");
            String content = request.getParameter("content");
            String account = (String) session.getAttribute("user");

            UserTb user = userRepository.getUserTbByAccount(account);
            LocalDate now = LocalDate.now();
            CommunityTb communityTb = new CommunityTb();
            communityTb.setTitle(title);
            communityTb.setContent(content);
            communityTb.setUser(user);
            communityTb.setDate(now);
            communityRepository.save(communityTb);

            return "redirect:/community";

        }else{
            return "redirect:/community";
        }

    }

    @RequestMapping(value = "/community/detail/{community_id}")
    public String community_detail(@PathVariable int community_id, Model model, HttpSession session){

        System.out.println(community_id);
        String user = (String)session.getAttribute("user");
        System.out.println(user);
        try{
            CommunityTb communityTb = communityRepository.getCommunityById(community_id);
            List<CommentTb> commentTb = commentRepository.getCommentList(community_id);

            communityRepository.Increase_like(communityTb);

            model.addAttribute("community", communityTb);
            model.addAttribute("comments",commentTb);
            model.addAttribute("user", user);

        }catch(Exception e){
            System.out.println("db error");
            System.out.println(e);
        }



        return "/community/community_detail";
    }



    @RequestMapping(value= "/community/delete/{community_id}")
    public String community_delete(@PathVariable int community_id, HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {

        String account = (String) session.getAttribute("user");


        CommunityTb community = communityRepository.getCommunityById(community_id);

        String community_writer = community.getUser().getAccount();

        if(account.equals(community_writer)){
            communityRepository.deleteById(community_id);
            CommentTb comment = new CommentTb();
            comment.setCommunity_id(community_id);
            commentRepository.deleteByCommunityId(community_id);
            ScriptUtil.alert_location(response, "삭제되었습니다.", "/community");
         } else {
             ScriptUtil.alert_location(response, "삭제할 수 없습니다.", "/community/detail/"+community_id);
         }
        return "redirect:/community";
    }

    @RequestMapping(value = "/community/modify/{community_id}")
    public String communit_modify(@PathVariable int community_id, HttpServletResponse response,HttpServletRequest request, HttpSession session, Model model) throws IOException {


        if(request.getMethod().equals("GET")){
            String user = (String) session.getAttribute("user");
            CommunityTb communityTb = communityRepository.getCommunityById(community_id);
            if(user.equals(communityTb.getUser().getAccount())){

                model.addAttribute("community", communityTb);

            }else{
                ScriptUtil.alert_back(response, "수정할 수 있는 권한이 없습니다.");
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
                ScriptUtil.alert_back(response, "수정할 수 있는 권한이 없습니다.");
            }

            return "redirect:/community";
        }else{
            ScriptUtil.alert_back(response, "올바른 요청이 아닙니다");
            return "redirect:/community";
        }

    }

    @RequestMapping(value="/community/heart/{community_id}")
    public String heart_Increase(@PathVariable int community_id, HttpSession session){

        String user = (String) session.getAttribute("user");
        HeartTb heartTb = new HeartTb();
//        heartTb.getCommunity_id()

        return "redirect:/community";

    }

}
