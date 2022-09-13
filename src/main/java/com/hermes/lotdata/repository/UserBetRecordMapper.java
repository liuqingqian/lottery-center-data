package com.hermes.lotdata.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hermes.lotdata.entity.UserBetRecordEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * Created by liuqingqian on 2022/9/9.
 */
@Mapper
@Repository
public interface UserBetRecordMapper extends BaseMapper<UserBetRecordEntity> {
}
