package com.yxkong.gateway;

import com.googlecode.aviator.AviatorEvaluator;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: yxkong
 * @Date: 2021/6/28 5:56 下午
 * @version: 1.0
 */
public class RuleTest {

    @Test
    public void grayRule(){
        Map<String,Object> map = new HashMap<>();
        final Date date = new Date();
        String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS").format(date);
        map.put("mobileTail","9");
        map.put("userId",901);
        map.put("registerTime",dateStr);
        map.put("mobile","15600000269");
        map.put("sex",1);
        map.put("age",28);
        String expression = "(string.startsWith('9,0',mobileTail) && userId>=901 && registerTime>'2021-06-25 00:00:00')";
        final Object o = AviatorEvaluator.execute(expression, map);
        System.out.println(o);

    }

}