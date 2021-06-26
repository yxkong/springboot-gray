package com.yxkong.common.plugin.context;

import com.yxkong.lb.holder.GrayHolder;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 如有需要自己扩展（此处只做了灰度）
 * @Author: yxkong
 * @Date: 2021/6/26 6:03 下午
 * @version: 1.0
 */
@Data
@Builder
public class SecurityDTO implements Serializable {
    private String label;
    private String version;
    private String weight;
}