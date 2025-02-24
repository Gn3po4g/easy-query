package com.easy.query.processor.helper;

import com.easy.query.core.util.EasyBase64Util;
import com.easy.query.core.util.EasyStringUtil;
import com.easy.query.processor.FieldRenderVal;

import java.nio.charset.StandardCharsets;

/**
 * create time 2023/11/8 16:46
 * 文件说明
 *
 * @author xuejiaming
 */
public class AptCreatorHelper {
    public static String createProxy(AptFileCompiler aptFileCompiler, AptValueObjectInfo aptValueObjectInfo) {

        String selectorContent = renderSelectorUI(aptFileCompiler);
        FieldRenderVal fieldRenderVal = renderStaticFieldCommentUI(aptFileCompiler);
        String propertyContent = renderPropertyUI(aptFileCompiler, aptValueObjectInfo);
        String valueObjectContent = renderValueObjectUI(aptFileCompiler, aptValueObjectInfo);
        String proxyTemplate = AptConstant.PROXY_TEMPLATE
                .replace("@{package}", aptFileCompiler.getPackageName())
                .replace("@{imports}", String.join("\n", aptFileCompiler.getImports()))
                .replace("@{entityClass}", aptFileCompiler.getEntityClassName())
                .replace("@{entityClassProxy}", aptFileCompiler.getEntityClassProxyName())
                .replace("@{fieldContent}", propertyContent)
                .replace("@{valueObjectContext}", valueObjectContent)
                .replace("@{selectorContext}", selectorContent)
                .replace("@{fieldStaticContext}", fieldRenderVal.staticField.toString());
//                .replace("@{fieldCommentContext}", fieldRenderVal.fieldComment.toString());
        return proxyTemplate;
    }

    private static String renderPropertyUI(AptFileCompiler aptFileCompiler, AptValueObjectInfo aptValueObjectInfo) {
        StringBuilder filedContent = new StringBuilder();
        for (AptPropertyInfo property : aptValueObjectInfo.getProperties()) {
            if (property.isValueObject()) {

                String fieldString = AptConstant.FIELD_VALUE_OBJECT_TEMPLATE
                        .replace("@{entityClass}", property.getEntityName())
                        .replace("@{comment}", property.getComment())
                        .replace("@{propertyType}", property.getPropertyType())
                        .replace("@{property}", property.getPropertyName())
                        .replace("@{proxyProperty}", property.getProxyPropertyName());
                filedContent.append(fieldString);
            } else {
                if (property.isIncludeProperty() && property.getNavigateProxyName() != null) {
                    if (property.isIncludeManyProperty()) {
                        String fieldString = AptConstant.FIELD_NAVIGATES_TEMPLATE
                                .replace("@{entityClassProxy}", aptFileCompiler.getEntityClassProxyName())
                                .replace("@{propertyProxy}", property.getNavigateProxyName())
                                .replace("@{comment}", property.getComment())
                                .replace("@{propertyType}", property.getPropertyType())
                                .replace("@{property}", property.getPropertyName())
                                .replace("@{proxyProperty}", property.getProxyPropertyName());
                        filedContent.append(fieldString);
                    } else {
                        String fieldString = AptConstant.FIELD_NAVIGATE_TEMPLATE
                                .replace("@{propertyProxy}", property.getNavigateProxyName())
                                .replace("@{comment}", property.getComment())
                                .replace("@{property}", property.getPropertyName())
                                .replace("@{proxyProperty}", property.getProxyPropertyName());
                        filedContent.append(fieldString);
                    }
                } else {
                    if (property.isAnyType()) {
                        String fieldString = AptConstant.ANY_FIELD_TEMPLATE
                                .replace("@{entityClassProxy}", aptFileCompiler.getEntityClassProxyName())
                                .replace("@{comment}", property.getComment())
                                .replace("@{propertyType}", property.getPropertyType())
                                .replace("@{propertyTypeClass}", property.getPropertyTypeClass())
                                .replace("@{property}", property.getPropertyName())
                                .replace("@{proxyProperty}", property.getProxyPropertyName())
                                .replace("@{SQLColumn}", property.getSqlColumn())
                                .replace("@{sqlColumnMethod}", property.getSqlColumnMethod());
                        filedContent.append(fieldString);
                    } else {
                        String fieldString = AptConstant.FIELD_TEMPLATE
                                .replace("@{entityClassProxy}", aptFileCompiler.getEntityClassProxyName())
                                .replace("@{comment}", property.getComment())
//                                 .replace("@{propertyType}", property.getPropertyType())
//                             .replace("@{propertyTypeClass}", property.getPropertyTypeClass())
                                .replace("@{property}", property.getPropertyName())
                                .replace("@{proxyProperty}", property.getProxyPropertyName())
                                .replace("@{SQLColumn}", property.getSqlColumn())
                                .replace("@{sqlColumnMethod}", property.getSqlColumnMethod());
                        filedContent.append(fieldString);
                    }
                }
            }
        }
        return filedContent.toString();
    }

    private static String renderSelectorUI(AptFileCompiler aptFileCompiler) {
        String fieldSelectorContent = renderSelectorPropertyUI(aptFileCompiler);
        AptSelectorInfo selectorInfo = aptFileCompiler.getSelectorInfo();
        return AptConstant.PROXY_SELECTOR_TEMPLATE
                .replace("@{entityClass}", aptFileCompiler.getEntityClassName())
                .replace("@{selectorName}", selectorInfo.getName())
                .replace("@{entityClassProxy}", aptFileCompiler.getEntityClassProxyName())
                .replace("@{fieldSelectorContent}", fieldSelectorContent);
    }


    private static FieldRenderVal renderStaticFieldCommentUI(AptFileCompiler aptFileCompiler) {
//        boolean ignoreComment = !aptFileCompiler.isTableEntity();
        FieldRenderVal fieldRenderVal = new FieldRenderVal();
        AptSelectorInfo selectorInfo = aptFileCompiler.getSelectorInfo();
//        StringBuilder fieldCase = new StringBuilder();
        for (AptSelectPropertyInfo property : selectorInfo.getProperties()) {
//            if (!ignoreComment) {
//                String comment = EasyStringUtil.trimOuterWhitespaceOptimized(EasyStringUtil.startWithRemove(property.getEntityComment(), "*"));
//                String fieldString = AptConstant.FIELD_COMMENT_TEMPLATE
//                        .replace("@{property}", property.getPropertyName())
//                        .replace("@{comment}", new String(EasyBase64Util.encode(comment.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8));
//                fieldCase.append(fieldString);
//            }
            String staticFiled = AptConstant.FIELD_STATIC_TEMPLATE
                    .replace("@{property}", property.getPropertyName());
            fieldRenderVal.staticField.append(staticFiled);
        }
//        if(!ignoreComment){
//            String fieldCommentMethod = AptConstant.FIELD_COMMENT_METHOD
//                    .replace("@{caseContent}", fieldCase.toString());
//            fieldRenderVal.fieldComment.append(fieldCommentMethod);
//        }
        return fieldRenderVal;
    }

    private static String renderSelectorPropertyUI(AptFileCompiler aptFileCompiler) {
        AptSelectorInfo selectorInfo = aptFileCompiler.getSelectorInfo();
        StringBuilder filedContent = new StringBuilder();
        for (AptSelectPropertyInfo property : selectorInfo.getProperties()) {

            String fieldString = AptConstant.FIELD_SELECTOR_PROPERTY_TEMPLATE
                    .replace("@{selectorName}", selectorInfo.getName())
                    .replace("@{comment}", property.getComment())
//                         .replace("@{property}", property.getPropertyName())
                    .replace("@{proxyProperty}", property.getProxyPropertyName());
            filedContent.append(fieldString);

        }
        return filedContent.toString();
    }

    private static String renderValueObjectUI(AptFileCompiler aptFileCompiler, AptValueObjectInfo aptValueObjectInfo) {
        StringBuilder valueObjectContentBuilder = new StringBuilder();
        for (AptValueObjectInfo valueObject : aptValueObjectInfo.getChildren()) {
            String propertyContent = renderPropertyUI(aptFileCompiler, valueObject);
            String vc = renderValueObjectUI(aptFileCompiler, valueObject);
            String valueObjectContent = AptConstant.FIELD_VALUE_OBJECT_CLASS_TEMPLATE
                    .replace("@{entityClass}", valueObject.getEntityName())
                    .replace("@{mainEntityClassProxy}", aptFileCompiler.getEntityClassProxyName())
                    .replace("@{fieldContent}", propertyContent)
                    .replace("@{valueObjectContext}", vc);
            valueObjectContentBuilder.append(valueObjectContent);
            valueObjectContentBuilder.append("\n");
        }
        return valueObjectContentBuilder.toString();
    }
}
