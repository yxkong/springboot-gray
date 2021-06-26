package com.yxkong.common.plugin.context;

import com.yxkong.lb.holder.GrayHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @Author: yxkong
 * @Date: 2021/6/26 6:20 下午
 * @version: 1.0
 */
public class SecurityContextHolder {
    private static SecurityContextHolderStrategy strategy;
    static {
        initialize();
    }

    public static void clearContext() {
        strategy.clearContext();
    }
    public static SecurityContext getContext() {
        return strategy.getContext();
    }
    public static Object getData() {
        return strategy.getContext().getContext();
    }

    public static HttpServletRequest getRequest() {
        if (Objects.nonNull(getData()) && HttpServletRequest.class.isInstance(getData())){
            return (HttpServletRequest) getData();
        }
        return null;
    }

    private static void initialize() {
        try {
            strategy = (new ThreadLocalSecurityContextHolderStrategy());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 设置context
     * @param data 数据实体
     */
    public static void setData(Object data) {
        strategy.setContext(new SecurityContextImpl(data));
    }

    public static SecurityDTO getSecurityDTO(){
        if (Objects.nonNull(getData()) && SecurityDTO.class.isInstance(getData())){
            return (SecurityDTO)getData();
        }
        return null;
    }
    public static void initSecurityDTO(String lable,String version){
        SecurityDTO dto = SecurityDTO.builder()
                .label(lable)
                .version(version)
                .build();
        strategy.setContext(new SecurityContextImpl(dto));
    }

    public static void setRequest(HttpServletRequest request){
       initSecurityDTO(request.getHeader(GrayHolder.LABEL_KEY),request.getHeader(GrayHolder.VERSION_KEY));

    }


}