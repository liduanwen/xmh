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
 * Entity - 地区
 * 
 * @author 	Xu minghua 2018/05/21
 */
@Entity
@Table(name = "xmh_area")
public class Area extends OrderEntity {

	private static final long serialVersionUID = -2158109459123036967L;

	/** 树路径分隔符 */
	private static final String TREE_PATH_SEPARATOR = ",";

	/** 名称 */
	@Getter(onMethod_={
			@NotEmpty,
			@Length(max = 100),
			@Column(nullable = false, length = 100)
	})
	@Setter
	private String name;

	/** 全称 */
	@Getter(onMethod_={@Column(nullable = false, length = 500)})
	@Setter
	private String fullName;

	/** 树路径 */
	@Getter(onMethod_={@Column(nullable = false, updatable = false)})
	@Setter
	private String treePath;

	/** 上级地区 */
	@Getter(onMethod_={
			@ManyToOne(fetch = FetchType.LAZY),
			@JoinColumn(name = "parent")
	})
	@Setter
	private Area parent;

	/** 下级地区 */
	@Getter(onMethod_={
			@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE),
			@OrderBy("order asc")
	})
	@Setter
	private List<Area> children = new ArrayList<Area>();

	/**
	 * 持久化前处理
	 */
	@PrePersist
	public void prePersist() {
		Area parent = getParent();
		if (parent != null) {
			setFullName(parent.getFullName() + getName());
			setTreePath(parent.getTreePath() + parent.getId() + TREE_PATH_SEPARATOR);
		} else {
			setFullName(getName());
			setTreePath(TREE_PATH_SEPARATOR);
		}
	}

	/**
	 * 更新前处理
	 */
	@PreUpdate
	public void preUpdate() {
		Area parent = getParent();
		if (parent != null) {
			setFullName(parent.getFullName() + getName());
		} else {
			setFullName(getName());
		}
	}

	/**
	 * 重写toString方法
	 * 
	 * @return 全称
	 */
	@Override
	public String toString() {
		return getFullName();
	}

}