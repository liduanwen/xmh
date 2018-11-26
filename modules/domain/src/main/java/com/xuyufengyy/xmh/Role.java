package com.xuyufengyy.xmh;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entity - 角色
 *
 * @author Xu minghua 2017/07/19
 */
@Entity
@Table(name = "xmh_role")
public class Role extends BaseEntity {

	private static final long serialVersionUID = -9113938914548894780L;

	/** 名称 */
	@Getter(onMethod_={
			@NotEmpty,
			@Length(max = 200),
			@Column(nullable = false)
	})
	@Setter
	private String name;

	/** 是否内置 */
	@Getter(onMethod_={@Column(nullable = false, updatable = false)})
	@Setter
	private Boolean isSystem;

	/** 描述 */
	@Getter(onMethod_={@Length(max = 200)})
	@Setter
	private String description;

	/** 管理员 */
	@Getter(onMethod_={@ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)})
	@Setter
	private List<Admin> admins = new ArrayList<Admin>();

	/** 权限 */
	@Getter(onMethod_={
			@NotEmpty,
			@ManyToMany(fetch = FetchType.EAGER),
			@JoinTable(name = "xmh_role_authority")
	})
	@Setter
	private List<Authority> authorities = new ArrayList<Authority>();
}