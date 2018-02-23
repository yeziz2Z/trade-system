package com.liuk.trade.goods.service;

import com.liuk.trade.common.protocol.goods.*;

/**
 * Created by kl on 2018/2/22.
 */
public interface IGoodsService {
    QueryGoodsRes queryGoods(QueryGoodsReq queryGoodsReq);

    ReduceGoodsNumberRes reduceGoodsNumber(ReduceGoodsNumberReq reduceGoodsNumberReq);

    AddGoodsNumberRes addGoodsNumber(AddGoodsNumberReq addGoodsNumberReq);
}
