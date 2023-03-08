package com.example.spring.domain;

import com.example.spring.dao.TestContent;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

public class QCommunityTbWithoutContent extends EntityPathBase<com.example.spring.dao.TestContent> {

        public static final QCommunityTbWithoutContent CommunityTb = new QCommunityTbWithoutContent("Community");
        //    public final StringPath id = createString("id");
        public final NumberPath<Integer> community_id = createNumber("community_id", Integer.class);
        public final StringPath title = createString("title");
        public final NumberPath<Integer> user_id = createNumber("user_id", Integer.class);
        public final StringPath date = createString("date");
        public final StringPath category = createString("category");
        public final NumberPath<Integer> hits = createNumber("hits", Integer.class);
        public final NumberPath<Double> get_coin =  createNumber("get_coin", double.class);
        public final BooleanPath highlight = createBoolean("highlight");

        public QCommunityTbWithoutContent(String variable) {

            super(TestContent.class, forVariable(variable));

        }


}
