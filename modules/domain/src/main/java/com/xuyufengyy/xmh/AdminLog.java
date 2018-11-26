package com.xuyufengyy.xmh;

import lombok.Data;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Entity - 管理平台操作日志
 *
 * @author	Xu minghua 2017/07/25
 */
@Data
@Entity
@Table(name = "xmh_admin_log")
public class AdminLog extends BaseEntity {

	private static final long serialVersionUID = 1241238702498238405L;

	/** 操作员 */
	private String operator;

	/** ip */
	@Getter(onMethod_={@Column(nullable = false, updatable = false)})
	private String ip;

	/** 类的方法 */
	@Getter(onMethod_={@Column(nullable = false, updatable = false)})
	private String classMethod;

	/** 请求的URL */
	@Getter(onMethod_={@Column(nullable = false, updatable = false)})
	private String requestUrl;

	/** 参数 */
	@Getter(onMethod_={@Lob})
	private String parameter;
}