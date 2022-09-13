package com.hermes.lotdata.domain;

import com.beicai.common.DateTimeUtil;
import com.hermes.lotdata.application.web.vo.LotteryRecordRefreshVO;
import com.hermes.lotdata.domain.dto.LotRecordDTO;
import com.hermes.lotdata.entity.LotteryRecordEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by liuqingqian on 2022/9/13.
 */
@Component
public class LotteryRecordRefreshDomain {

    @Autowired
    private DownloadLotDomain downloadLotDomain;

    @Autowired
    private FileOptDomain fileOptDomain;

    @Autowired
    private LotTableParserDomain lotTableParserDomain;

    @Autowired
    private LotteryRecordDomain lotteryRecordDomain;

    public LotteryRecordRefreshVO refreshTask(String code, String date) {

        String url = LotteryConfig.RESULT_85_URL;
        Map<String, Object> params = new HashMap<>();
        params.put("lotCode", code);
        params.put("startDate", date);
        String rawContent = downloadLotDomain.getHtmlRawContent(url, params);
        boolean writeLotFile = fileOptDomain.writeLotFile(rawContent, code, date);
        String rawFileContent = fileOptDomain.readLotFile(code, date);

        LotteryRecordRefreshVO lotteryRecordRefreshVO = new LotteryRecordRefreshVO();

        List<LotRecordDTO> lotRecordList = lotTableParserDomain.parseLotTable(code, rawFileContent);
        //检查数据库，只补齐需要插入的数据
        List<LotteryRecordEntity> dbRecordEntities = lotteryRecordDomain.queryFileListByDate(code, date);
        Map<String, LotteryRecordEntity> dbRecordEntityMap = dbRecordEntities.stream()
                .collect(Collectors.toMap(LotteryRecordEntity::getPeriodNumber, item -> item, (k1, k2) -> k2));

        List<LotteryRecordEntity> lotteryRecordEntities = lotRecordList.stream()
                //DB中没有的记录才插入；
                .filter(lotRecordDTO -> !dbRecordEntityMap.keySet().contains(lotRecordDTO.getPeriodNumber()))
                .map(lotteryRecordDomain::toLotteryRecordEntity)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        boolean batchInsert = lotteryRecordDomain.batchInsert(lotteryRecordEntities);
        lotteryRecordRefreshVO.setCode(code);
        lotteryRecordRefreshVO.setDate(date);
        lotteryRecordRefreshVO.setWriteLotFile(writeLotFile);
        lotteryRecordRefreshVO.setLotFileRecordCount(lotRecordList.size());
        lotteryRecordRefreshVO.setBatchInsertCount(lotteryRecordEntities.size());
        lotteryRecordRefreshVO.setBatchInsert(batchInsert);
        lotteryRecordRefreshVO.setSyncTime(DateTimeUtil.nowStr());

        return lotteryRecordRefreshVO;
    }
}
