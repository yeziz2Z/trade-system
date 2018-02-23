package com.liuk.trade.common.util;

import java.util.UUID;

/**
 * Created by kl on 2018/2/19.
 */
public class IDGenerator {
    public static String generateUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
