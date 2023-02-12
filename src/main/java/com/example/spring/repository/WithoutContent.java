package com.example.spring.repository;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class WithoutContent {

        int community_id;
        String title;
        LocalDate date;
        int hits;
        int user_id;
        String type;
        boolean highlight;
        double get_coin;

        public WithoutContent() {
        }

        @QueryProjection
        public WithoutContent(int community_id, String title, LocalDate date, int hits, int user_id, String type, boolean highlight, double get_coin) {
                this.community_id = community_id;
                this.title = title;
                this.date = date;
                this.hits = hits;
                this.user_id = user_id;
                this.type = type;
                this.highlight = highlight;
                this.get_coin = get_coin;
        }
}
