package com.allinpay.bigdata.mapper;

import com.allinpay.bigdata.model.TChanlqyInfo;
import org.springframework.stereotype.Repository;


@Repository
public interface TChanlqyInfoMapper {

    int insert(TChanlqyInfo record);
}