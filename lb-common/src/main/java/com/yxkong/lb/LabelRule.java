package com.yxkong.lb;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import com.yxkong.common.utils.ApplicationContextHolder;
import com.yxkong.lb.configuration.EurekaMetadata;
import com.yxkong.lb.holder.GrayHolder;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 标签规则，参考 RoundRobinRule 轮训规则
 * @Author: yxkong
 * @Date: 2021/5/17 3:11 下午
 * @version: 1.0
 */
public class LabelRule  extends AbstractLoadBalancerRule {
    private static Logger log = LoggerFactory.getLogger(LabelRule.class);
    public static final String META_DATA_KEY_VERSION = "version";
    public static final String META_DATA_KEY_WEIGHT = "weight";
    //普通server轮训器
    private AtomicInteger nextServerCyclicCounter;
    //灰度轮训器
    private AtomicInteger nextGrayServerCyclicCounter;

    public LabelRule() {
        nextServerCyclicCounter = new AtomicInteger(0);
        nextGrayServerCyclicCounter = new AtomicInteger(0);
    }

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {

    }

    public Server choose(ILoadBalancer lb, Object key) {
        if (lb == null) {
            log.warn("no load balancer");
            return null;
        }
        Server server = null;
        //是否灰度用户
        if(isGray()){
            log.info("用户是一个灰度用户");
            //选择灰度服务器
            server = chooseServer(lb,Boolean.TRUE);
            if(!Objects.isNull(server)){
                return server;
            }
            log.info("没有匹配到灰度服务器将从非灰度服务器中随机挑选一个");
        }
        //如果灰度找不到就随机从非灰里找
        return chooseServer(lb,Boolean.FALSE);
    }

    /**
     * 判断是否是灰度用户
     * @return
     */
    private Boolean isGray(){
        //有一个为空表示不是灰度用户
        if (Objects.isNull(GrayHolder.getVersion()) || Objects.isNull(GrayHolder.getLable())) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 获取灰度server
     * @param reachableServers
     * @return
     */
    private List<Server> getGrayServers(List<Server> reachableServers){
        return reachableServers.stream().filter(server -> {
            Map<String, String> metadata = ((DiscoveryEnabledServer) server).getInstanceInfo().getMetadata();

            /**
             * 到这里一定是灰度用户，上下文中一定有标签和版本
             * 并且上下文中传递的值和eureka中的元数据相同，则为灰度环境
             */
            if (GrayHolder.getVersion().equalsIgnoreCase(metadata.get(GrayHolder.VERSION_KEY))
                    && GrayHolder.getLable().equalsIgnoreCase(metadata.get(GrayHolder.LABEL_KEY))){
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }).collect(Collectors.toList());
    }

    /**
     * 排除灰度服务器
     * @param reachableServers
     * @return
     */
    private List<Server> getExcludeGrayServers(List<Server> reachableServers){
        return reachableServers.stream().filter(server -> {
            Map<String, String> metadata = ((DiscoveryEnabledServer) server).getInstanceInfo().getMetadata();
            //只要有一个是空，就认为是非灰服务器
            if (Objects.isNull(metadata.get(GrayHolder.VERSION_KEY))
                    || Objects.isNull(metadata.get(GrayHolder.LABEL_KEY))){
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }).collect(Collectors.toList());
    }

    /**
     * 选择服务器
     * @param lb
     * @param isGray 是否挑选灰度服务器  true 是，false 否
     * @return
     */
    private Server chooseServer(ILoadBalancer lb,Boolean isGray){
        Server server = null;
        int count = 0;
        while (server == null && count++ < 10) {
            //每次循环都会重新获取，lb里的正常都是缓存下来的信息，如果服务下线
            List<Server> reachableServers = lb.getReachableServers();
            List<Server> allServers = lb.getAllServers();
            int upCount = reachableServers.size();
            int serverCount = allServers.size();

            if ((upCount == 0) || (serverCount == 0)) {
                log.warn("No up servers available from load balancer: " + lb);
                return null;
            }
            if(!isGray){
                // 如果在检索之前size =1 直接返回
                if (allServers.size() == 1) {
                    return allServers.get(0);
                }
            }
            int nextServerIndex = 0;
            //灰度环境重新计算allServers 和serverCount
            AtomicInteger counter = null;
            if(isGray){
                allServers = getGrayServers(allServers);
                counter = nextGrayServerCyclicCounter;
            }else {
                allServers = getExcludeGrayServers(allServers);
                counter = nextServerCyclicCounter;

            }
            //获取不到就直接返回null
            if(Objects.isNull(allServers)|| allServers.size()==0){
                return null;
            }
            if (allServers.size() == 1) {
                return allServers.get(0);
            }
            serverCount = allServers.size();
            //轮训算法，是在单机里维护了一个AtomicInteger，不断自增取模server的数量
            nextServerIndex = incrementAndGetModulo(counter,serverCount);
            server = allServers.get(nextServerIndex);

            if (server == null) {
                /* Transient. */
                Thread.yield();
                continue;
            }

            if (server.isAlive() && (server.isReadyToServe())) {
                return (server);
            }
            // Next.
            server = null;
        }

        if (count >= 10) {
            log.warn("No available alive servers after 10 tries from load balancer: "
                    + lb);
        }
        return server;
    }
    private int incrementAndGetModulo(AtomicInteger counter,int modulo) {
        for (;;) {
            int current = counter.get();
            int next = (current + 1) % modulo;
            if (counter.compareAndSet(current, next)){
                return next;
            }
        }
    }

    @Override
    public Server choose(Object key) {
        return choose(getLoadBalancer(), key);
    }

}