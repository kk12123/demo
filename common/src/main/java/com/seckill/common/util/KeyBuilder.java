package com.seckill.common.util;

/**
 * @author 18020727
 */
public class KeyBuilder {
    /**
     *
     * @param userNo
     * @param productNo
     * @return
     */
    public static String build(String userNo,String productNo){
        return userNo+"_"+productNo;
    }

    /**
     *
     * @param userNo
     * @param productNo
     * @return
     */
    public static String orderKeyBuilder(String userNo,String productNo){
        return userNo+"_"+productNo+"_"+"order";
    }
}
