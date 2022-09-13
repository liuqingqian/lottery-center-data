package com.hermes.lotdata.domain;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beicai.common.DateTimeUtil;
import com.google.common.collect.Lists;
import com.hermes.lotdata.domain.dto.LotRecordDTO;
import com.hermes.lotdata.domain.rpc.response.LotteryLastResponse;
import com.hermes.lotdata.entity.LotteryRecordEntity;
import com.hermes.lotdata.infrastructure.enums.LotSingleDoubleEnum;
import com.hermes.lotdata.infrastructure.enums.LotSizeEnum;
import com.hermes.lotdata.infrastructure.enums.LotStatusEnum;
import com.hermes.lotdata.repository.LotteryRecordMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by liuqingqian on 2022/9/1.
 */
@Component
public class LotteryRecordDomain extends ServiceImpl<LotteryRecordMapper, LotteryRecordEntity> {


    @Autowired
    private LotteryHaoMaDomain lotteryHaoMaDomain;


    public int insert(LotteryRecordEntity lotteryRecordEntity) {

        if (Objects.isNull(lotteryRecordEntity)) {
            return 0;
        }
        return baseMapper.insert(lotteryRecordEntity);
    }

    public boolean batchInsert(List<LotteryRecordEntity> lotteryRecordEntities) {
        if (CollectionUtils.isEmpty(lotteryRecordEntities)) {
            return false;
        }
        return saveBatch(lotteryRecordEntities);
    }

    public LotteryRecordEntity query(String code, String periodNumber) {
        if (StringUtils.isBlank(code) || StringUtils.isBlank(periodNumber)) {
            return null;
        }
        LambdaQueryWrapper<LotteryRecordEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(LotteryRecordEntity::getCode, code);
        queryWrapper.eq(LotteryRecordEntity::getPeriodNumber, periodNumber);
        queryWrapper.last("LIMIT 1");

        return baseMapper.selectOne(queryWrapper);
    }

    public List<LotteryRecordEntity> queryListByPeriodNumber(String code, List<String> periodNumbers) {
        if (StringUtils.isBlank(code) || CollectionUtils.isEmpty(periodNumbers)) {
            return Lists.newArrayList();
        }

        LambdaQueryWrapper<LotteryRecordEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(LotteryRecordEntity::getCode, code);
        queryWrapper.in(LotteryRecordEntity::getPeriodNumber, periodNumbers);

        return baseMapper.selectList(queryWrapper);
    }

    public List<LotteryRecordEntity> queryListByDate(String code, String startDate) {
        if (StringUtils.isBlank(code) || StringUtils.isBlank(startDate)) {
            return Lists.newArrayList();
        }

        String start = DateTimeUtil.dateToMidnightDateTime(startDate);
        String end = DateTimeUtil.dateToMaxDateTime(startDate);
        LambdaQueryWrapper<LotteryRecordEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(LotteryRecordEntity::getCode, code);
        queryWrapper.ge(LotteryRecordEntity::getLotTime, start);
        queryWrapper.lt(LotteryRecordEntity::getLotTime, end);

        return baseMapper.selectList(queryWrapper);
    }

    public List<LotteryRecordEntity> queryFileListByDate(String code, String startDate) {
        //查询开始日期文件中对应的开奖集合：2022-01-07 00:00:00 -- 2022-01-06 00:01:00
        if (StringUtils.isBlank(code) || StringUtils.isBlank(startDate)) {
            return Lists.newArrayList();
        }

        String start = DateTimeUtil.dateToMidnightDateTime(startDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        String endDate = dateFormat.format(calendar.getTime());
        String end = DateTimeUtil.dateToMidnightDateTime(endDate);
        LambdaQueryWrapper<LotteryRecordEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(LotteryRecordEntity::getCode, code);
        queryWrapper.gt(LotteryRecordEntity::getLotTime, start);
        queryWrapper.le(LotteryRecordEntity::getLotTime, end);
        List<LotteryRecordEntity> lotteryRecordEntities = baseMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(lotteryRecordEntities)) {
            return Lists.newArrayList();
        }
        return lotteryRecordEntities;
    }

    public LotteryRecordEntity toLotteryRecordEntity(LotRecordDTO lotRecordDTO) {
        if (Objects.isNull(lotRecordDTO) ||
                StringUtils.isBlank(lotRecordDTO.getCode()) ||
                StringUtils.isBlank(lotRecordDTO.getPeriodNumber())) {
            return null;
        }

        LotteryRecordEntity recordEntity = new LotteryRecordEntity();
        BeanUtils.copyProperties(lotRecordDTO, recordEntity);
        //处理特殊字段：
        //大小@1:小@2:大@:3豹
        if (LotSizeEnum.SMALL.equalsDesc(lotRecordDTO.getSizeTxt())) {
            recordEntity.setSize(LotSizeEnum.SMALL.getCode());
        } else if (LotSizeEnum.BIG.equalsDesc(lotRecordDTO.getSizeTxt())) {
            recordEntity.setSize(LotSizeEnum.BIG.getCode());
        } else if (LotSizeEnum.DUAL.equalsDesc(lotRecordDTO.getSizeTxt())) {
            recordEntity.setSize(LotSizeEnum.DUAL.getCode());
        } else {
            recordEntity.setSize(null);
        }
        //单双@1:单@2:双@:3豹
        if (LotSingleDoubleEnum.ODD.equalsDesc(lotRecordDTO.getSingleDoubleTxt())) {
            recordEntity.setSingleDouble(LotSingleDoubleEnum.ODD.getCode());
        } else if (LotSingleDoubleEnum.EVEN.equalsDesc(lotRecordDTO.getSingleDoubleTxt())) {
            recordEntity.setSingleDouble(LotSingleDoubleEnum.EVEN.getCode());
        } else if (LotSingleDoubleEnum.DUAL.equalsDesc(lotRecordDTO.getSingleDoubleTxt())) {
            recordEntity.setSingleDouble(LotSingleDoubleEnum.DUAL.getCode());
        } else {
            recordEntity.setSingleDouble(null);
        }
        //豹@0:否@1:是
        recordEntity.setEquals(lotRecordDTO.getIsEquals() ? 1 : 0);
        return recordEntity;
    }


    public LotteryRecordEntity toLotteryRecordEntity(LotteryLastResponse.Last last) {

        if (Objects.isNull(last)) {
            return null;
        }
        String lotteryHaoMa = last.getHaoMa();
        List<Integer> dices = lotteryHaoMaDomain.convertToDices(lotteryHaoMa);
        if (CollectionUtils.isEmpty(dices)) {
            return null;
        }

        LotteryRecordEntity recordEntity = new LotteryRecordEntity();
        recordEntity.setCode(last.getLotCode());
        recordEntity.setPeriodNumber(last.getQiHao());
        //开奖时间
        String openTime = DateTimeUtil.queryByTimestamp(last.getOpenTime()).format(DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
        recordEntity.setLotTime(openTime);
        Integer dice1 = dices.get(0);
        Integer dice2 = dices.get(1);
        Integer dice3 = dices.get(2);
        recordEntity.setDice1(dice1);
        recordEntity.setDice2(dice2);
        recordEntity.setDice3(dice3);
        recordEntity.setSum(dice1 + dice2 + dice3);
        boolean isDual = Objects.equals(dice1, dice2) && Objects.equals(dice2, dice3);
        //豹@0:否@1:是
        recordEntity.setEquals(isDual ? 1 : 0);
        if (isDual) {
            recordEntity.setSize(LotSizeEnum.DUAL.getCode());
            recordEntity.setSingleDouble(LotSingleDoubleEnum.DUAL.getCode());
        } else {
            //大小
            String sizeSidedName = lotteryHaoMaDomain.convertSizeSidedName(lotteryHaoMa);
            LotSizeEnum lotSizeEnum = LotSizeEnum.fromDesc(sizeSidedName);
            recordEntity.setSize(lotSizeEnum.getCode());
            //单双
            String singleDoubleSidedName = lotteryHaoMaDomain.convertSingleDoubleSidedName(lotteryHaoMa);
            LotSingleDoubleEnum lotSingleDoubleEnum = LotSingleDoubleEnum.fromDesc(singleDoubleSidedName);
            recordEntity.setSingleDouble(lotSingleDoubleEnum.getCode());
        }
        recordEntity.setLotStatus(LotStatusEnum.HAS_LOT.getCode());
        return recordEntity;
    }

}
