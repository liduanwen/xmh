package com.xuyufengyy.xmh;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entity - 权限分类
 *
 * @author Xu minghua 2017/07/21
 */
@Entity
@Table(name = "xmh_authority_category")
public class AuthorityCategory extends OrderEntity {

    private static final long serialVersionUID = 6548683235643716421L;

    /** 名称*/
    @Getter(onMethod_={
            @NotEmpty,
            @Column(nullable = false)
    })
    @Setter
    private String name;

    /** 权限*/
    @Getter(onMethod_={@OneToMany(mappedBy = "authorityCategory", fetch = FetchType.LAZY)})
    @Setter
    private List<Authority> authorities = new ArrayList<Authority>();
}
