package com.yxkong.api.task;

import com.yxkong.api.feignclient.DemoServiceFeignClient;
import com.yxkong.common.dto.ResultBean;
import com.yxkong.common.plugin.context.SecurityContextHolder;
import com.yxkong.common.plugin.context.SecurityDTO;
import com.yxkong.common.utils.JsonUtils;
import com.yxkong.lb.holder.GrayHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: yxkong
 * @Date: 2021/6/26 9:41 下午
 * @version: 1.0
 */
@Component
@Slf4j
public class ScheduleTask {

    @Autowired
    private DemoServiceFeignClient demoServiceFeginClient;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ThreadPoolExecutor executor;

    /**
     * 多线程灰度环境
     */
    //@Scheduled(cron = "0/30 * * * * ?")
    private void grayTasks() {
        log.info("grayTasks init");
        SecurityContextHolder.initSecurityDTO("gray","2.0");
        executor.submit(new Task());
        executor.submit(new Task1());
        try {
            //防止还未取到，就被清理了
            Thread.sleep(1000);
        } catch (Exception e){

        }
        SecurityContextHolder.clearContext();
    }

    /**
     * 多线程非灰环境，只能测试一个
     */
    //@Scheduled(cron = "0/30 * * * * ?")
    private void normalTasks() {
        log.info("normalTasks init");
        SecurityContextHolder.initSecurityDTO(null,null);
        executor.submit(new Task());
        executor.submit(new Task1());
        SecurityContextHolder.clearContext();
    }
    class Task implements Runnable{
        @Override
        public void run() {
            try {
                /**
                 * 1，从主线程获取标签，（可以是任意的一个地方）
                 * 2，初始化initHystrixRequestContext
                 */

                final SecurityDTO dto = SecurityContextHolder.getSecurityDTO();
                GrayHolder.initHystrixRequestContext(dto.getLabel(),dto.getVersion());
                ResultBean resultBean = demoServiceFeginClient.hello();
                log.info(JsonUtils.toJson(resultBean));
            }catch (Exception e){
                log.error("Task",e);
            }finally {
                GrayHolder.shutdownHystrixRequestContext();
            }

        }
    }
    class Task1 implements Runnable{
        @Override
        public void run() {
            try {
                /**
                 * 1，从主线程获取标签，（可以是任意的一个地方）
                 * 2，初始化initHystrixRequestContext
                 */
                final SecurityDTO dto = SecurityContextHolder.getSecurityDTO();
                GrayHolder.initHystrixRequestContext(dto.getLabel(),dto.getVersion());
                ResponseEntity<ResultBean> entity = restTemplate.getForEntity("http://demo-service/demo/hello", ResultBean.class);
                ResultBean resultBean = entity.getBody();
                log.warn(JsonUtils.toJson(resultBean));
            }catch (Exception e){
                log.error("Task1",e);
            }finally {
                GrayHolder.shutdownHystrixRequestContext();
            }

        }
    }
}
