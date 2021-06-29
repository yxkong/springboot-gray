package com.yxkong.service.controller;

import com.yxkong.common.dto.LoginInfo;
import com.yxkong.common.dto.ResultBean;
import com.yxkong.common.enums.ResultStatusEnum;
import com.yxkong.common.utils.JWTUtil;
import com.yxkong.service.controller.dto.LoginDto;
import com.yxkong.service.controller.vo.LoginVO;
import com.yxkong.service.entity.user.Account;
import com.yxkong.service.mapper.user.AccountMapper;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

/**
 * @Author: yxkong
 * @Date: 2021/6/27 5:01 下午
 * @version: 1.0
 */
@RestController
@Tag(name = "用户",description="用户相关接口")
@RequestMapping("/account")
public class AccountController {

    @Resource
    private AccountMapper accountMapper;
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @Operation(summary = "登录接口",tags = "用户")
    @ApiResponses({@ApiResponse(code = 1, message = "1:成功;其它状态失败")})
    @Parameters({@Parameter(in = ParameterIn.DEFAULT, name = "loginVO", description = "用户登录body", required = true)})
    public ResultBean<LoginDto> login(@RequestBody LoginVO loginVO){
        Account account = accountMapper.findByMobile(loginVO.getMobile());
        if (Objects.nonNull(account)){
            LoginInfo loginInfo = LoginInfo.builder().userId(account.getId())
                    .mobile(account.getMobile())
                    .loginSource(loginVO.getSource())
                    .registerTime(account.getCreateTime())
                    .nickName(account.getNickName())
                    .loginTime(new Date()).build();
            final String token = JWTUtil.createAESToken(loginInfo);
            LoginDto dto = LoginDto.builder().token(token).build();
            return new ResultBean.Builder<LoginDto>().success(dto).build();
        } else{
            return new ResultBean.Builder<LoginDto>().statusEnum(ResultStatusEnum.NO_DATA).build();
        }
    }
    @RequestMapping(value = "verify",method = RequestMethod.POST)
    @Operation(summary = "验证接口",method = "POST",tags = "用户")
    @ApiResponses({@ApiResponse(code = 1, message = "1:成功;其它状态失败")})
    @Parameters({@Parameter(in = ParameterIn.HEADER, name = "token", description = "用户登录token", required = true,style = ParameterStyle.DEFAULT)})
    public ResultBean<Boolean> verify(@RequestHeader(value = "token") String token){
        try {
            final boolean verify = JWTUtil.verifyAES(token);
            return new ResultBean.Builder<Boolean>().success(verify).build();
        } catch (Exception e) {
            e.printStackTrace();
            return getResultBean(e);
        }
    }
    @RequestMapping(value = "getLoginInfo",method = RequestMethod.POST)
    @Operation(summary = "获取jwt信息",tags = "用户")
    @ApiResponses({@ApiResponse(code = 1, message = "1:成功;其它状态失败")})
    @Parameters({@Parameter(in = ParameterIn.HEADER, name = "token", description = "用户登录token", required = true,style = ParameterStyle.DEFAULT)})
    public ResultBean<LoginInfo> getLoginInfo(@RequestHeader(value = "token") String token){
        try {
            final LoginInfo loginInfo = JWTUtil.getAESLoginInfo(token);
            return new ResultBean.Builder<LoginInfo>().success(loginInfo).build();
        } catch (Exception e) {
            e.printStackTrace();
            return getResultBean(e);
        }
    }

    private ResultBean getResultBean(Exception e) {
        return new ResultBean.Builder().fail(e.getMessage()).build();
    }
}