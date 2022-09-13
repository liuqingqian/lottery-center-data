package com.hermes.lotdata.domain;

import com.google.common.collect.Lists;
import com.hermes.lotdata.infrastructure.enums.SidedEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by liuqingqian on 2022/9/13.
 */
@Component
public class LotteryHaoMaDomain {

    public List<String> convertToName(String lotteryHaoMa) {
        //haoMa=1,5,6 --> 单、双、大、小、豹
        if (StringUtils.isBlank(lotteryHaoMa)) {
            return Lists.newArrayList();
        }
        String[] diceArray = lotteryHaoMa.trim().split(",");
        if (Objects.isNull(diceArray) || diceArray.length != 3) {
            return Lists.newArrayList();
        }
        Integer dice1 = Integer.valueOf(diceArray[0].trim());
        Integer dice2 = Integer.valueOf(diceArray[1].trim());
        Integer dice3 = Integer.valueOf(diceArray[2].trim());

        return convertDice(dice1, dice2, dice3);
    }

    public List<Integer> convertToDices(String lotteryHaoMa) {
        if (StringUtils.isBlank(lotteryHaoMa)) {
            return Lists.newArrayList();
        }
        String[] diceArray = lotteryHaoMa.trim().split(",");
        if (Objects.isNull(diceArray) || diceArray.length != 3) {
            return Lists.newArrayList();
        }
        Integer dice1 = Integer.valueOf(diceArray[0].trim());
        Integer dice2 = Integer.valueOf(diceArray[1].trim());
        Integer dice3 = Integer.valueOf(diceArray[2].trim());
        return Arrays.asList(dice1, dice2, dice3);
    }


    //大小：三个开奖号码总和值11~17 为大；总和值4~10 为小；若三个号码相同、通吃[大]、[小]各注。
    //单双：三个开奖号码总和值5,7,9,11,13,15,17为单；总和值4,6,8,10,12,14,16为双；若三个号码相同、通吃[单]、[双]各注。
    public List<String> convertDice(Integer dice1, Integer dice2, Integer dice3) {
        if (Objects.isNull(dice1) || Objects.isNull(dice2) || Objects.isNull(dice3)) {
            return Lists.newArrayList();
        }
        if (Objects.equals(dice1, dice2) && Objects.equals(dice2, dice3)) {
            return Lists.newArrayList(SidedEnum.DUAL.getName());
        }
        Integer sum = dice1 + dice2 + dice3;
        String size = sum > 10 ? SidedEnum.BIG.getName() : SidedEnum.SMALL.getName();
        String singleDouble = sum % 2 == 0 ? SidedEnum.EVEN.getName() : SidedEnum.ODD.getName();
        return Lists.newArrayList(size, singleDouble);
    }

    public String convertSizeSidedName(String lotteryHaoMa) {
        List<String> sidedNames = convertToName(lotteryHaoMa);
        return getSizeSidedName(sidedNames);
    }

    public String convertSingleDoubleSidedName(String lotteryHaoMa) {
        List<String> sidedNames = convertToName(lotteryHaoMa);
        return getSingleDoubleSidedName(sidedNames);
    }

    public String getSizeSidedName(List<String> sidedNames) {
        if (CollectionUtils.isEmpty(sidedNames) || sidedNames.size() == 1) {
            return null;
        }
        if (sidedNames.contains(SidedEnum.BIG.getName())) {
            return SidedEnum.BIG.getName();
        } else if (sidedNames.contains(SidedEnum.SMALL.getName())) {
            return SidedEnum.SMALL.getName();
        } else {
            return null;
        }
    }

    public String getSingleDoubleSidedName(List<String> sidedNames) {
        if (CollectionUtils.isEmpty(sidedNames) || sidedNames.size() == 1) {
            return null;
        }
        if (sidedNames.contains(SidedEnum.ODD.getName())) {
            return SidedEnum.ODD.getName();
        } else if (sidedNames.contains(SidedEnum.EVEN.getName())) {
            return SidedEnum.EVEN.getName();
        } else {
            return null;
        }
    }
}
