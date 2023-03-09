package com.example.spring.repository;

import com.example.spring.dao.CommentLikeTb;

import java.util.List;

public interface CommentlikeRepositoryCustom {
    public boolean CommentLikeCheckByAll(CommentLikeTb commentLikeTb);

    public CommentLikeTb getCommentLike(CommentLikeTb commentLikeTb);

    public List<CommentLikeTb> getCommentLikeListByCommentLikeTb(CommentLikeTb commentLikeTb);

    public int getCommentLike_total(int comment_id);


}
