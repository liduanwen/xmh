package com.xuyufengyy.xmh;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.*;

/**
 * Entity - 管理员
 *
 * @author Xu minghua 2017/07/19
 */
@Entity
@Table(name = "xmh_admin")
public class Admin extends BaseEntity {

	private static final long serialVersionUID = 3953531372817318275L;

	/** 用户名 */
	@Getter(onMethod_={
			@NotEmpty(groups = Save.class),
			@Pattern(regexp = "^[0-9a-z_A-Z\\u4e00-\\u9fa5]+$"),
			@Length(min = 2, max = 20),
			@Column(nullable = false, updatable = false, unique = true, length = 100)
	})
	@Setter
	private String username;

	/** 密码 */
	@Getter(onMethod_={
			@NotEmpty(groups = Save.class),
			@Pattern(regexp = "^[^\\s&\"<>]+$"),
			@Length(min = 4, max = 200),
			@Column(nullable = false)
	})
	@Setter
	private String password;

	/** E-mail */
	@Getter(onMethod_={
			@NotEmpty,
			@Email,
			@Length(max = 200),
			@Column(nullable = false)
	})
	@Setter
	private String email;

	/** 姓名 */
	@Getter(onMethod_={@Length(max = 200)})
	@Setter
	private String name;

	/** 部门 */
	@Getter(onMethod_={@Length(max = 200)})
	@Setter
	private String department;

	/** 是否启用 */
	@Getter(onMethod_={@NotNull,@Column(nullable = false)})
	@Setter
	private Boolean isEnabled;

	/** 是否锁定 */
	@Getter(onMethod_={@Column(nullable = false)})
	@Setter
	private Boolean isLocked;

	/** 连续登录失败次数 */
	@Getter(onMethod_={@Column(nullable = false)})
	@Setter
	private Integer loginFailureCount;

	/** 锁定日期 */
	@Getter @Setter
	private Date lockedDate;

	/** 最后登录日期 */
	@Getter @Setter
	private Date loginDate;

	/** 最后登录IP */
	@Getter @Setter
	private String loginIp;

	/** 昵称 */
	@Getter(onMethod_={@NotEmpty,@Column(nullable = false)})
	@Setter
	private String nickname;

	/** 角色 */
	@Getter(onMethod_={
			@NotEmpty,
			@ManyToMany(fetch = FetchType.EAGER),
			@JoinTable(name = "xmh_admin_role")
	})
	@Setter
	private List<Role> roles = new ArrayList<Role>();
}