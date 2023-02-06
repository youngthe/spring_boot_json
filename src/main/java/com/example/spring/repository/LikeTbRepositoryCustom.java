package com.example.spring.repository;

import com.example.spring.dao.LikeTb;

public interface LikeTbRepositoryCustom {
    public boolean deleteByCommunityIdAndWriterId(LikeTb likeTb);

    public LikeTb getLike(LikeTb likeTb);

    public boolean LikeCheck(LikeTb likeTb);

}
