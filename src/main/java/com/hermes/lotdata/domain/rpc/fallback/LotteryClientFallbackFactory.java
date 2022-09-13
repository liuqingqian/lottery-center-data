package com.hermes.lotdata.domain.rpc.fallback;

import com.hermes.lotdata.domain.rpc.LotteryClient;
import com.hermes.lotdata.domain.rpc.criteria.LotteryLastCriteria;
import com.hermes.lotdata.domain.rpc.criteria.LotteryOptCriteria;
import com.hermes.lotdata.domain.rpc.response.LotteryLastResponse;
import com.hermes.lotdata.domain.rpc.response.LotteryOptResponse;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created by liuqingqian on 2022/9/8.
 */
@Slf4j
@Component
public class LotteryClientFallbackFactory implements FallbackFactory<LotteryClient> {
    @Override
    public LotteryClient create(Throwable throwable) {
        return new LotteryClient() {

            @Override
            public LotteryOptResponse lotteryOpt(LotteryOptCriteria criteria) {
                log.error("rpc LotteryClient lotteryOpt error:{}", throwable.getMessage());
                return null;
            }

            @Override
            public LotteryLastResponse lotteryLast(LotteryLastCriteria criteria) {
                log.error("rpc LotteryClient lotteryLast error:{}", throwable.getMessage());
                return null;
            }
        };
    }
}
