package com.xuyufengyy.xmh;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entity - 景区
 *
 * @author Xu minghua 2018/05/24
 */
@Entity
@Table(name = "xmh_scenic_spot")
public class ScenicSpot extends BaseEntity{

    private static final long serialVersionUID = -6013243125939941350L;

    /** 名称 */
    @Getter(onMethod_={
            @JsonProperty,
            @NotEmpty(groups = BaseEntity.Save.class),
            @Length(min = 2, max = 20),
            @Column(nullable = false, updatable = false, unique = true, length = 100)
    })
    @Setter
    private String name;

    /** 展示图片 */
    @Getter(onMethod_={@JsonProperty}) @Setter
    private String image;

    /** 描述 */
    @Getter(onMethod_={
            @JsonProperty,
            @NotEmpty,
            @Column(nullable = false)
    })
    @Setter
    private String description;

    /** 简介 */
    @Getter(onMethod_={
            @NotEmpty,
            @Lob,
            @Column(nullable = false)
    })
    @Setter
    private String introduction;

    /** 推荐 */
    @Getter @Setter
    private Boolean isRecommend;

    /** 地区 */
    @Getter(onMethod_={
            @NotNull,
            @ManyToOne(fetch = FetchType.LAZY),
            @JoinColumn(name = "area_id", nullable = false, updatable = false)
    })
    @Setter
    private Area area;

    /** 产品 */
    @Getter(onMethod_={
            @JsonProperty,
            @OneToMany(mappedBy = "scenicSpot", fetch = FetchType.LAZY)
    })
    @Setter
    private List<Product> products = new ArrayList<Product>();
}
