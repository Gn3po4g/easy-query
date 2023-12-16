package com.easy.query.test.entity;

import com.alibaba.fastjson2.TypeReference;
import com.easy.query.core.basic.extension.complex.ComplexPropType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.lang.reflect.Type;
import java.util.List;

/**
 * create time 2023/5/23 22:36
 * 文件说明
 *
 * @author xuejiaming
 */
@Data
@EqualsAndHashCode
public class TopicTypeJsonValue implements ComplexPropType {
    private String name;
    private Integer age;

    @Override
    public Type complexType() {
        return myType(new TypeReference<List<TopicTypeJsonValue>>() {
        });
    }

    private <T> Type myType(TypeReference<T> typeReference) {
        return typeReference.getType();
    }
}
