package com.gxzx.testrabbitmq.web.service.impl;

import com.gxzx.testrabbitmq.web.entity.Market;
import com.gxzx.testrabbitmq.web.mapper.MarketMapper;
import com.gxzx.testrabbitmq.web.service.IMarketService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 行情配置表 服务实现类
 * </p>
 *
 * @author administrator
 * @since 2019-08-21
 */
@Service
public class MarketServiceImpl extends ServiceImpl<MarketMapper, Market> implements IMarketService {

}
