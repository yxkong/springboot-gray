package com.yxkong.common.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.yxkong.common.exception.ParamsRuntimeExeception;
import com.yxkong.common.enums.ResultStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.Objects;

/**
 * api接口返回数据包装体
 *
 * @Author: yxk
 * @Date: 2021/4/5 8:28 下午
 * @version: 1.0
 */
@Schema(name = "公共包装体")
public class ResultBean<T> implements Serializable {
	public static final String STATUS = "status";
	public static final String MESSAGE = "message";
	public static final String TIMESTAMP = "timestamp";
	private static final long serialVersionUID = -7602280271453240278L;
	@Schema(name = "返回信息")
	private String message ;
	@Schema(name = "返回状态，1 成功，0 失败，")
	private String status ;
	@Schema(name = "返回数据体")
	private T data;
	/**
	 * 请求返回时间
	 */
	@Schema(name = "返回时间戳")
	private Long timestamp ;

	public ResultBean() {
	}

	private ResultBean(Builder builder) {
		this.message = builder.message;
		this.status = builder.status;
		this.data = (T)builder.data;
		this.timestamp = builder.timestamp;
	}

	@JsonIgnore
	public boolean isSucc(){
		if (Objects.nonNull(status) && ResultStatusEnum.SUCCESS.getStatus().equals(status)){
			return true;
		}
		return false;
	}
	public String getMessage() {
		return message;
	}

	public String getStatus() {
		return status;
	}

	public T getData() {
		return data;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	@Override
	public String toString() {
		return "ResultBean{" +
				"message='" + message + '\'' +
				", status='" + status + '\'' +
				", data=" + data +
				", timestamp=" + timestamp +
				'}';
	}

	public static  class Builder<T>{
		private String message = ResultStatusEnum.SUCCESS.getMessage();
		private String status = ResultStatusEnum.SUCCESS.getStatus();
		private T data;
		/**
		 * 请求返回时间
		 */
		private Long timestamp = System.currentTimeMillis();

		public Builder() {
		}

		public ResultBean build() {
			if(Objects.isNull(status)){
				throw new ParamsRuntimeExeception("status must be exist");
			}
			return new ResultBean(this);
		}
		public Builder statusEnum(ResultStatusEnum statusEnum){
			this.init(statusEnum);
			return this;
		}
		public Builder fail(T data){
			this.init(ResultStatusEnum.ERROR);
			this.data = data;
			return this;
		}
		public Builder success(T data){
			this.init(ResultStatusEnum.SUCCESS);
			this.data = data;
			return this;
		}
		public Builder success(){
			this.init(ResultStatusEnum.SUCCESS);
			return this;
		}
		private void init(ResultStatusEnum statusEnum){
			this.status = statusEnum.getStatus();
			this.message = statusEnum.getMessage();
		}

		public Builder message(String message){
			this.message = message;
			return this;
		}
		public Builder status(String status){
			this.status = status;
			return this;
		}
		public Builder data(T data){
			this.data = data;
			return this;
		}
	}

}
