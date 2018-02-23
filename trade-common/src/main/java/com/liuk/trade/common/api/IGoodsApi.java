package com.liuk.trade.common.api;

import com.liuk.trade.common.protocol.goods.QueryGoodsReq;
import com.liuk.trade.common.protocol.goods.QueryGoodsRes;
import com.liuk.trade.common.protocol.goods.ReduceGoodsNumberReq;
import com.liuk.trade.common.protocol.goods.ReduceGoodsNumberRes;

/**
 * Created by kl on 2018/2/18.
 */
public interface IGoodsApi {
    QueryGoodsRes queryGoods(QueryGoodsReq queryGoodsReq);

    ReduceGoodsNumberRes reduceGoodsNumber(ReduceGoodsNumberReq reduceGoodsNumberReq);
}
