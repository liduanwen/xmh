package com.xuyufengyy.xmh;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.groups.Default;
import java.io.Serializable;
import java.util.Date;

/**
 * Entity-基类
 *
 * @author Xu minghua 2017/07/19
 */
@Data
@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, creatorVisibility = Visibility.NONE)
@EntityListeners(EntityListener.class)
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

	private static final long serialVersionUID = -3064263777867978791L;

	/** "ID"属性名称 */
	public static final String ID_PROPERTY_NAME = "id";

	/** "创建日期"属性名称 */
	public static final String CREATE_DATE_PROPERTY_NAME = "createDate";

	/** "修改日期"属性名称 */
	public static final String MODIFY_DATE_PROPERTY_NAME = "modifyDate";

	/**
	 * 保存验证组
	 */
	public interface Save extends Default {

	}

	/**
	 * 更新验证组
	 */
	public interface Update extends Default {

	}

	/** ID */
	@Getter(onMethod_={@Id,@Column(name="id",nullable=false),@GeneratedValue(strategy= GenerationType.IDENTITY),@JsonProperty})
	private Long id;

	/** 创建日期 */
	@Getter(onMethod_={@Column(nullable = false, updatable = false),@JsonProperty,@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")})
	private Date createDate;

	/** 修改日期 */
	@Getter(onMethod_={@Column(nullable = false),@JsonProperty})
	private Date modifyDate;

	/**
	 * 重写equals方法
	 *
	 * @param obj
	 *            对象
	 * @return 是否相等
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (!BaseEntity.class.isAssignableFrom(obj.getClass())) {
			return false;
		}
		BaseEntity other = (BaseEntity) obj;
		return getId() != null ? getId().equals(other.getId()) : false;
	}

	/**
	 * 重写hashCode方法
	 *
	 * @return hashCode
	 */
	@Override
	public int hashCode() {
		int hashCode = 17;
		hashCode += null == getId() ? 0 : getId().hashCode() * 31;
		return hashCode;
	}

}