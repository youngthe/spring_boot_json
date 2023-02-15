package com.example.spring.dto;

import com.example.spring.dao.CommentTb;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CommentWriterDto {

    private int comment_id;

    private int community_id;

    private String comment;

    private String date;

    private int parent;

    private int user_id;

    private String name;

    private boolean like;

    private int like_total;

    private boolean add_reply = false;

    private String add_comment = "";

    private boolean edit = false;

    private String edit_comment;
    List<RecommentDto> reply;


    public CommentWriterDto(CommentTb comment, String name, boolean like, int like_total, List<RecommentDto> reply) {
        this.comment_id = comment.getComment_id();
        this.community_id = (int) comment.getCommunity_id();
        this.comment = comment.getComment();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        this.date = format.format(comment.getDate());
        this.parent = comment.getParent();
        this.user_id = comment.getUser_id();
        this.name = name;
        this.like = like;
        this.like_total = like_total;
        this.reply = reply;
        this.edit_comment = comment.getComment();
    }
}
