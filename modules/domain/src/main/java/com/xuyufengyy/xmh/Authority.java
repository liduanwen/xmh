package com.xuyufengyy.xmh;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity - 权限
 *
 * @author Xu minghua 2017/07/19
 */
@Entity
@Table(name = "xmh_authority")
public class Authority extends BaseEntity {

    private static final long serialVersionUID = 6548683235643716421L;

    /** 名称*/
    @Getter(onMethod_={
            @NotEmpty,
            @Column(nullable = false)
    })
    @Setter
    private String name;

    /** 访问路径*/
    @Getter @Setter
    private String path;

    /** 权限*/
    @Getter(onMethod_={
            @NotEmpty,
            @Column(nullable = false)
    })
    @Setter
    private String authorityValue;

    /** 权限分类*/
    @Getter(onMethod_={
            @ManyToOne(fetch = FetchType.LAZY),
            @JoinColumn(name = "authority_category_id")
    })
    @Setter
    private AuthorityCategory authorityCategory;

    /** 角色*/
    @Getter(onMethod_={@ManyToMany(mappedBy = "authorities", fetch = FetchType.LAZY)})
    @Setter
    private List<Role> roles = new ArrayList<Role>();
}
