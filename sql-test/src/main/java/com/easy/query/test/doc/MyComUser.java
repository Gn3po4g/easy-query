package com.easy.query.test.doc;

import com.easy.query.core.annotation.Column;
import com.easy.query.core.annotation.EntityProxy;
import com.easy.query.core.annotation.Table;
import com.easy.query.core.proxy.ProxyEntityAvailable;
import com.easy.query.test.doc.proxy.MyComUserProxy;
import lombok.Data;

/**
 * create time 2024/10/14 11:06
 * 文件说明
 *
 * @author xuejiaming
 */
@Data
@Table("my_com_user")
@EntityProxy
public class MyComUser implements ProxyEntityAvailable<MyComUser , MyComUserProxy> {
    @Column(primaryKey = true)
    private String id;
    private String comId;
    private String userId;
    private String gw;
}
