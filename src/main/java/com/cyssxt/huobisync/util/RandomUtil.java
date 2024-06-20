package com.cyssxt.huobisync.util;

import com.bigo.project.bigo.marketsituation.domain.RandomConfig;
import com.cyssxt.huobisync.view.RandomView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;

@Slf4j
@Component
public class RandomUtil {
    private static BigDecimal DAY_OPEN = null;


    public RandomView random(BigDecimal open, long timer, int upRate, RandomConfig randomConfig){
        double[] doubles = new double[4];
        Random random = new Random();
        DecimalFormat df = new DecimalFormat("#.########");
        if(DAY_OPEN==null || isDayStart(timer)){
            DAY_OPEN = open;
        }
        double up = randomConfig.getUpdateRate().doubleValue();
        for(int i=0;i<4;i++) {
            Double get_double = Double.parseDouble(df.format(random.nextDouble()));
            doubles[i] = get_double * DAY_OPEN.doubleValue() * up * upRate / randomConfig.getRandomRate();
        }
        int flag = random.nextInt(100);
        int type = 1;
        double decline = randomConfig.getDecline();
        int declineRate = (int) ((1-decline)*100);
        if(flag>declineRate || (flag>(declineRate+10) && upRate>=240)){
            type = -1;
        }
        double value = doubles[2]*type;
        BigDecimal close = open.add(BigDecimal.valueOf(Double.parseDouble(df.format(value))*randomConfig.getBarRate()));
        double lowDeg = doubles[1]*randomConfig.getLowDeg();
        BigDecimal low;
        if(open.compareTo(close)>0){
            low = close.subtract(BigDecimal.valueOf(lowDeg));
        }else{
            low = open.subtract(BigDecimal.valueOf(lowDeg));
        }
        log.info("close====={}",close);
        Arrays.sort(doubles);
        RandomView randomView = new RandomView();
        randomView.setClose(close);
        randomView.setHigh(open.add(BigDecimal.valueOf(doubles[3]*randomConfig.getLeadWire())));
        randomView.setLow(low);
        return randomView;
    }

    public static boolean isDayStart(long timer){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timer*1000);
        calendar.set(Calendar.HOUR,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTimeInMillis()/1000==timer;
    }
    public static void main(String[] args) {
//        System.out.println(JSON.toJSONString(random(4)));
    }
}

