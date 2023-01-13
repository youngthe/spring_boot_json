package com.example.spring.spring.repository;


import com.example.spring.spring.dao.CommentTb;

import javax.transaction.Transactional;
import java.util.List;

public interface CommentTbRepositoryCustom {

    public CommentTb getCommentByCommentId(int commentId);

    public List<CommentTb> getCommentList(int Community_id);
    public void deleteByCommunityId(int Community_id);

    public int getCommunityIdByCommentId(int CommentId);
    @Transactional
    void deleteComment(int comment_id);

    public List<CommentTb> getCommentByCommunityId(int community_id);

    public void deleteByParent(int parent_id);

}
