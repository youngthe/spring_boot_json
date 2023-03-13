package com.example.spring.repository;


import com.example.spring.dao.AskingTb;

import java.util.List;

public interface AskingTbRepositoryCustom {

    public AskingTb getAskingTbByAskingId(int asking_id);

    public List<AskingTb> getAskingListByUserId(int user_id);

    public List<AskingTb> getAskingListByInputOutput(boolean input_output);

}
