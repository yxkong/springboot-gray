package com.yxkong.test;

import com.yxkong.common.dto.LoginInfo;
import com.yxkong.common.utils.JsonUtils;
import com.yxkong.common.utils.JWTUtil;
import org.junit.Test;

/**
 * @Author: yxkong
 * @Date: 2021/6/28 11:42 上午
 * @version: 1.0
 */
public class JWTTest {

    @Test
    public void create(){
        try {
            LoginInfo loginInfo = LoginInfo.builder().userId(1L).mobile("156").nickName("yxkong").build();

            final String jwtToken = JWTUtil.createJWT(loginInfo);
            System.out.println(jwtToken);

            final LoginInfo info = JWTUtil.getLoginInfo(jwtToken);
            System.out.println(JsonUtils.toJson(info));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createAES(){
        try {
            LoginInfo loginInfo = LoginInfo.builder().userId(1L).mobile("156").nickName("yxkong").build();

            final String jwtToken = JWTUtil.createAESToken(loginInfo);
            System.out.println(jwtToken);

            final LoginInfo info = JWTUtil.getAESLoginInfo(jwtToken);
            System.out.println(JsonUtils.toJson(info));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deAES(){
        //try {
        //    String token = "EA644043DA92A95BF51F96327C909C8F2F8E339015A498EE89C40A64EF4FB9B1D03B910682E4FD16A3809ADA6865A5C4B8187D06CE130A8E0D66EF093A215A073608BE3480ACE88DA9B9DA8AD90AAE6C2EBA3F3A4A26FBAEF3631C1571E27BE09911670E65FBCBF700BD5FD569462108F3BDFF6B623A74B96971188EB1FB6DFDDC05D7511837A6B75DCD019C5C91AA57CF80212B1C40E8C430F8B49F236388B965A78D4FAB05FB0CD6691A749E4180F772F26C5AAEB2602F1AD16CD4F06C7849E9D566D433A5316764712D64DB366F8F80386EF1F1854CE8E5995B87346D719D4DB5D92260E04E30C26AEF93593FE2EB62FBDE85DF8C3F383CB348969367CC3443A4FE525EFFA997177F200F8DF5BA2F3F5C6F18F190905FFDC1A55E69CBDF9D066F06BB47E625C719E13914456337C23D24951C0DC7E7C2731FF6205C3D7AC41E8F06687FED0DA598253E16F260F42E29BE0F7EC9BFE6BAC06FFBE86E485AAC814D693A9F9212ABE82C12478B3C0FA352F138BCEB0A864C140A21063DA19AF73158DEB3DE87F623559416158B716B9BCF8D31AF37598F2F77B8F0F73C54250EABD6236266A5C2B1F519F96B28347BB6";
        //    final LoginInfo info = JWTUtil.getAESLoginInfo(token);
        //    System.out.println(JsonUtils.toJson(info));
        //} catch (Exception e) {
        //    e.printStackTrace();
        //}
    }

}