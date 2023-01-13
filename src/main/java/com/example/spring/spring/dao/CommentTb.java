package com.example.spring.spring.dao;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "COMMENTS")
public class CommentTb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "community_id")
    private long community_id;

    @Column(name = "comment")
    private String comment;

    @Column(name = "date")
    private String date;

    @Column(name = "parent")
    private int parent;



    public long getCommunity_id() {
        return community_id;
    }

    public void setCommunity_id(long community_id) {
        this.community_id = community_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }


}
