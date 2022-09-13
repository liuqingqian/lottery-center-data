package com.hermes.lotdata.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hermes.lotdata.entity.UserBetTaskEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * Created by liuqingqian on 2022/9/9.
 */
@Mapper
@Repository
public interface UserBetTaskMapper extends BaseMapper<UserBetTaskEntity> {
}
