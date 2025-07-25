package com.easy.query.test.mysql8.dto;


import com.easy.query.core.annotation.Navigate;
import com.easy.query.core.annotation.NavigateFlat;
import com.easy.query.core.enums.RelationTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * this file automatically generated by easy-query struct dto mapping
 * 当前文件是easy-query自动生成的 结构化dto 映射
 * {@link com.easy.query.test.mysql8.entity.M8Role }
 *
 * @author xuejiaming
 * @easy-query-dto schema: normal
 */
@Data
public class M8RoleDTO2 {


    private String id;
    private String name;
    private LocalDateTime createTime;
    @NavigateFlat(pathAlias = "menus.menuOwner.id")
    private List<String> menuOwnerIds;

//    @Data
//    public static class InternalOwner {
//        private String id;
//        private String name;
//    }


}
