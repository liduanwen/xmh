package com.xuyufengyy.xmh;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang3.builder.CompareToBuilder;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Min;

/**
 * Entity - 排序基类
 * 
 * @author Xu minghua 2017/07/21
 */
@Data
@MappedSuperclass
public abstract class OrderEntity extends BaseEntity implements Comparable<OrderEntity> {

	private static final long serialVersionUID = 5995013015967525827L;

	/** "排序"属性名称 */
	public static final String ORDER_PROPERTY_NAME = "order";

	/** 排序 */
	@Getter(onMethod_={
			@JsonProperty,
			@Min(0),
			@Column(name = "orders")
	})
	private Integer order;

	/**
	 * 实现compareTo方法
	 * 
	 * @param orderEntity
	 *            排序对象
	 * @return 比较结果
	 */
	public int compareTo(OrderEntity orderEntity) {
		return new CompareToBuilder().append(getOrder(), orderEntity.getOrder()).append(getId(), orderEntity.getId()).toComparison();
	}

}