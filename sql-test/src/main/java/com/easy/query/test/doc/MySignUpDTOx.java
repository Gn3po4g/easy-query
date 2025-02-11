package com.easy.query.test.doc;


import com.easy.query.core.annotation.Navigate;
import com.easy.query.core.annotation.NavigateFlat;
import com.easy.query.core.enums.RelationTypeEnum;
import com.easy.query.core.expression.parser.core.available.MappingPath;
import com.easy.query.test.doc.proxy.MySignUpProxy;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;

/**
 * this file automatically generated by easy-query struct dto mapping
 * 当前文件是easy-query自动生成的 结构化dto 映射
 * {@link MySignUp }
 *
 * @author xuejiaming
 * @easy-query-dto schema: normal
 */
@Data
@FieldNameConstants
public class MySignUpDTOx {


    private String id;
    private LocalDateTime time;
    private String content;
    private String comUserName;

    private static final MappingPath COM_USER_PATH = MySignUpProxy.TABLE.comUser();

    @NavigateFlat(pathAlias = "COM_USER_PATH",prefix = true)
    private String gw;
    @NavigateFlat(pathAlias = "COM_USER_PATH",prefix = true)
    private String comId;
    @NavigateFlat(pathAlias = "COM_USER_PATH",prefix = true)
    private String userId;



}
