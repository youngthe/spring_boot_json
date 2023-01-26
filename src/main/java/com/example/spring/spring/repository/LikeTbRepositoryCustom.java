package com.example.spring.spring.repository;

import com.example.spring.spring.dao.LikeTb;

public interface LikeTbRepositoryCustom {
    public boolean deleteByCommunityIdAndWriterId(LikeTb likeTb);

    public LikeTb getLike(LikeTb likeTb);

    public boolean LikeCheck(LikeTb likeTb);

}
