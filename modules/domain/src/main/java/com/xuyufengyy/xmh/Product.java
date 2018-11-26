package com.xuyufengyy.xmh;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Entity - 产品
 *
 * @author Xu minghua 2018/05/24
 */
@Entity
@Table(name = "xmh_product")
public class Product extends BaseEntity {

    private static final long serialVersionUID = -7031524785508624966L;

    /** 名称 */
    @Getter(onMethod_={
            @JsonProperty,
            @NotEmpty(groups = Save.class),
            @Length(min = 2, max = 20),
            @Column(nullable = false, unique = true, length = 100)
    })
    @Setter
	private String name;

    /** 标题 */
    @Getter(onMethod_={
            @JsonProperty,
            @Column(nullable = false)
    })
    @Setter
    private String title;

	/** 展示图片 */
    @Getter(onMethod_={@JsonProperty}) @Setter
	private String image;

    /** 原价 */
    @Getter(onMethod_={
            @NotNull(groups = Save.class),
            @Column(nullable = false, precision = 21, scale = 2)
    })
    @Setter
    private BigDecimal originalPrice = new BigDecimal(0.00);

    /** 优惠价 */
    @Getter(onMethod_={
            @NotNull(groups = Save.class),
            @Column(nullable = false, precision = 21, scale = 2)
    })
    @Setter
    private BigDecimal preferentialPrice = new BigDecimal(0.00);

    /** 文本 */
    @Getter(onMethod_={@Lob}) @Setter
    private String uploadText;

    /** 录音 */
    @Getter(onMethod_={@JsonProperty}) @Setter
    private String uploadTape;

    /** 收听量 */
    @Getter(onMethod_={@JsonProperty}) @Setter
    private Integer listenIn = 0;

    /** 作者 */
    @Getter(onMethod_={
            @JsonProperty,
            @NotEmpty,
            @Column(nullable = false)
    })
    @Setter
    private String author;

    /** 景区 */
    @Getter(onMethod_={
            @NotNull,
            @ManyToOne(fetch = FetchType.LAZY)
    })
    @Setter
    private ScenicSpot scenicSpot;
}