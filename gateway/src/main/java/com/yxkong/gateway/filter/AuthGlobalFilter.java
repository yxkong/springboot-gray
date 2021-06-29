package com.yxkong.gateway.filter;

import com.yxkong.common.constant.HeaderConstant;
import com.yxkong.common.dto.LoginInfo;
import com.yxkong.common.dto.ResultBean;
import com.yxkong.common.enums.OrderEnum;
import com.yxkong.common.utils.JsonUtils;
import com.yxkong.common.utils.WebUtil;
import com.yxkong.gateway.configuration.AuthProperties;
import com.yxkong.gateway.feignclient.ServiceFeignClient;
import com.yxkong.lb.holder.GrayHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 权限拦截
 * @Author: yxkong
 * @Date: 2021/5/12 9:35 上午
 * @version: 1.0
 */
@Component
@Slf4j
public class AuthGlobalFilter implements GlobalFilter, Ordered {
    byte[] tokenNull = "{\"status\":\"1008\",\"message\":\"token无效,请重新登录\"}".getBytes(StandardCharsets.UTF_8);
    byte[] tokenError = "{\"status\":\"0\",\"message\":\"鉴权异常，请稍后再试!\"}".getBytes(StandardCharsets.UTF_8);
    private static final String ALLOWED_HEADERS = "*";
    private static final String ALLOWED_METHODS = "*";
    private static final String ALLOWED_ORIGIN = "*";
    private static final String ALLOWED_Expose = "*";
    private static final String MAX_AGE = "18000L";
    @Autowired
    private AuthProperties authProperties;

    @Resource
    private ServiceFeignClient serviceFeignClient;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        if(!exclude(exchange)){
           //直接走转发逻辑
            return chain.filter(corsConfig(exchange).mutate()
                    .request(exchange.getRequest().mutate()
                            .header(HeaderConstant.REQUEST_FROM, HeaderConstant.REQUEST_FROM_SOURCE)
                            .build())
                    .build()
            );
        }
        String token = exchange.getRequest().getHeaders().getFirst(HeaderConstant.TOKEN);
        String requestIp = WebUtil.getIpAddr(exchange.getRequest());
        String loginInfo = getLoginInfo(token);
        if(StringUtils.isEmpty(loginInfo)){
            return authFail(exchange,Boolean.TRUE);
        }
        ServerHttpRequest request = exchange.getRequest().mutate()
                //将请求登录信息放入请求头
                .header(HeaderConstant.LOGIN_INFO, loginInfo)
                //将用户的真实ip放入到请求头
                .header(HeaderConstant.IP_HEADER_X_FORWARDED_FOR,requestIp)
                //标记从网关过去的
                .header(HeaderConstant.REQUEST_FROM, HeaderConstant.REQUEST_FROM_SOURCE)
                .build();

        return chain.filter(corsConfig(exchange).mutate().request(request).build());
    }

    /**
     * 根据token获取用户信息
     * 可以通过jwt，也可以通过redis，防止穿透到数据库
     * @param token
     * @return
     */
    private String getLoginInfo(String token) {
        String loginInfo = getUserInfo(token);
        log.info("用户信息{} ",loginInfo);
        if (Objects.isNull(loginInfo)){
            return null;
        }
        try{
            loginInfo = URLEncoder.encode(loginInfo,StandardCharsets.UTF_8.name());
        }catch (UnsupportedEncodingException e){

        }
        return loginInfo;
    }
    private String getUserInfo(String token){
        GrayHolder.initHystrixRequestContext(null,null);
        final ResultBean<LoginInfo> resultBean = serviceFeignClient.getLoginInfo(token);
        if (resultBean.isSucc() && Objects.nonNull(resultBean.getData())){
            return JsonUtils.toJson(resultBean.getData());
        }
        return null;
    }

    /**
     * 返回失败
     * @param exchange
     * @param type true 表示token为空，false，表示异常
     * @return
     */
    private Mono<Void> authFail(ServerWebExchange exchange,Boolean type) {
        ServerHttpResponse serverHttpResponse = exchange.getResponse();
        serverHttpResponse.setStatusCode(HttpStatus.OK);
        byte[] response = type ? tokenNull : tokenError;
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(response);
        return exchange.getResponse().writeWith(Flux.just(buffer));
    }

    /**
     * 权限校验
     * @param exchange
     * @return 返回false，放行，返回true 拦截
     */
    private Boolean exclude(ServerWebExchange exchange){
        String host = exchange.getRequest().getURI().toString();
        String url = exchange.getRequest().getURI().getPath();
        /**
         * 如果内网和外网用一个网关，可以根据内外网做鉴权
         * 如果资本雄厚，就内外网分开
         */

        //判断是否有例外
        if(authProperties.getHosts() != null
                &&authProperties.getHosts().stream().anyMatch(s -> s.equals(host))){
            return Boolean.FALSE;
        }
        if(authProperties.getUrls() != null
                && authProperties.getUrls().stream().anyMatch(s -> s.equals(url))){
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
    public ServerWebExchange corsConfig(ServerWebExchange exchange) {

        ServerHttpRequest request = exchange.getRequest();
        // 判断是否为跨域
        if (!CorsUtils.isCorsRequest(request)) {
            return exchange;
        }

        ServerHttpResponse response = exchange.getResponse();
        HttpHeaders headers = response.getHeaders();

        headers.remove("Access-Control-Allow-Origin");
        headers.remove("Access-Control-Allow-Methods");
        headers.remove("Access-Control-Max-Age");
        headers.remove("Access-Control-Allow-Headers");
        headers.remove("Access-Control-Expose-Headers");
        headers.remove("Access-Control-Allow-Credentials");

        headers.add("Access-Control-Allow-Origin", ALLOWED_ORIGIN);
        headers.add("Access-Control-Allow-Methods", ALLOWED_METHODS);
        headers.add("Access-Control-Max-Age", MAX_AGE);
        headers.add("Access-Control-Allow-Headers", ALLOWED_HEADERS);
        headers.add("Access-Control-Expose-Headers", ALLOWED_Expose);
        headers.add("Access-Control-Allow-Credentials", "true");

        return exchange;
    }
    @Override
    public int getOrder() {
        return OrderEnum.AUTH.getOrder();
    }
}