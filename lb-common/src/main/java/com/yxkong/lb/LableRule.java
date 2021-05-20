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
public class LableRule  extends AbstractLoadBalancerRule {
    private static Logger log = LoggerFactory.getLogger(LableRule.class);
    public static final String META_DATA_KEY_VERSION = "version";
    public static final String META_DATA_KEY_WEIGHT = "weight";
    private AtomicInteger nextServerCyclicCounter;

    public LableRule() {
        nextServerCyclicCounter = new AtomicInteger(0);
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
        if(isGray()){
            server = chooseServer(lb,Boolean.TRUE);
            if(!Objects.isNull(server)){
                return server;
            }
        }
        //如果灰度找不到就随机从非灰里找
        return chooseServer(lb,Boolean.FALSE);
    }

    /**
     * 判断是否是灰度环境
     * @return
     */
    private Boolean isGray(){
        Pair<String,String> mateInfo=getMateInfo();
        if(!StringUtils.isEmpty(mateInfo.getKey()) && ! StringUtils.isEmpty(mateInfo.getValue()) && GrayHolder.LABEL_VAL.equals(mateInfo.getValue())){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    private Pair<String,String> getMateInfo(){
        //拿到上下文中的版本
        String versionStr = StringUtils.isEmpty(GrayHolder.getVersion()) ?
                ApplicationContextHolder.getBean(EurekaMetadata.class).getVersion(): GrayHolder.getVersion() ;
        //拿到对应的label标签
        String labelStr = StringUtils.isEmpty(GrayHolder.getLable()) ?
                ApplicationContextHolder.getBean(EurekaMetadata.class).getLabel(): GrayHolder.getLable() ;
        log.info("拿到上下文中的version是：{} label是：{}",versionStr,labelStr);
        return new Pair<>(versionStr,labelStr);
    }
    private List<Server> getGrayServers(List<Server> reachableServers){
        Pair<String,String> mateInfo = getMateInfo();
        return reachableServers.stream().filter(server -> {
            Map<String, String> metadata = ((DiscoveryEnabledServer) server).getInstanceInfo().getMetadata();
            if(mateInfo.getKey().equalsIgnoreCase(metadata.get(GrayHolder.VERSION_KEY))&& mateInfo.getValue().equalsIgnoreCase(metadata.get(GrayHolder.LABEL_KEY))){
                return Boolean.TRUE;
            }
            return false;
        }).collect(Collectors.toList());
    }
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
            // 如果在检索之前size =1 直接返回
            if (allServers.size() == 1) {
                return allServers.get(0);
            }
            //灰度环境重新计算allServers 和serverCount
            if(isGray){
                allServers = getGrayServers(allServers);
                //获取不到就直接返回null
                if(Objects.isNull(allServers)|| allServers.size()==0){
                    return null;
                }
                if (allServers.size() == 1) {
                    return allServers.get(0);
                }
                serverCount = allServers.size();
            }

            //轮训算法，是在单机里维护了一个AtomicInteger，不断自增取模server的数量
            int nextServerIndex = incrementAndGetModulo(serverCount);
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

    /**
     * Inspired by the implementation of {@link AtomicInteger#incrementAndGet()}.
     *
     * @param modulo The modulo to bound the value of the counter.
     * @return The next value.
     */
    private int incrementAndGetModulo(int modulo) {
        for (;;) {
            int current = nextServerCyclicCounter.get();
            int next = (current + 1) % modulo;
            if (nextServerCyclicCounter.compareAndSet(current, next))
                return next;
        }
    }

    @Override
    public Server choose(Object key) {
        return choose(getLoadBalancer(), key);
    }

}