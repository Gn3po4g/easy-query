package com.easy.query.core.metadata;

import com.easy.query.core.basic.extension.complex.ComplexPropType;
import com.easy.query.core.basic.extension.conversion.ColumnValueSQLConverter;
import com.easy.query.core.basic.extension.conversion.ValueConverter;
import com.easy.query.core.basic.extension.encryption.EncryptionStrategy;
import com.easy.query.core.basic.extension.generated.GeneratedKeySQLColumnGenerator;
import com.easy.query.core.basic.extension.track.update.ValueUpdateAtomicTrack;
import com.easy.query.core.basic.jdbc.types.handler.JdbcTypeHandler;
import com.easy.query.core.expression.lambda.Property;
import com.easy.query.core.expression.lambda.PropertySetterCaller;
import com.easy.query.core.util.EasyClassUtil;

import java.beans.PropertyDescriptor;
import java.util.List;

/**
 * create time 2023/2/11 18:13
 * 解析对象后获得的列元信息
 *
 * @author xuejiaming
 */
public class ColumnMetadata {
    /**
     * 所属对象元信息
     */

    private final EntityMetadata entityMetadata;
    /**
     * 数据库列名
     */
    private final String name;


    /**
     * 属性信息
     */
    private final PropertyDescriptor property;
    /**
     * property.getPropertyType()默认会加锁synchronized所以这边增加一个冗余字段
     */
    private final Class<?> propertyType;
    /**
     * 属性名
     */
    private final String propertyName;

    /**
     * 是否是主键
     */
    private final  boolean primary;
    /**
     * 是否是数据库生成列 比如自增键
     */
    private final  boolean generatedKey;


//    private  boolean nullable=true;
    /**
     * 是否是乐观锁版本号
     */
    private final  boolean version;
    /**
     * 是否插入时忽略
     */
    private final  boolean insertIgnore;
    /**
     * 是否更新时忽略
     */
    private final  boolean updateIgnore ;
    /**
     * 如果更新时忽略当前列存在track的diff中是否也要更新
     */
    private final  boolean updateSetInTrackDiff ;

    /**
     * 加密策略
     */
    private final EncryptionStrategy encryptionStrategy;
    /**
     * 加密后查询是否支持like
     */
    private final  boolean supportQueryLike;
    /**
     * 是否是大列
     */
    private final  boolean large;
    /**
     * 是否自动查询结果
     */
    private final  boolean autoSelect;
    /**
     * 是否是基本类型 int long double 而不是Integer Long...
     */
    private final  boolean primitive;

    /**
     * 数据库和对象值转换器
     */
    private final ValueConverter<?, ?> valueConverter;
    /**
     * 对象数据库列转换器
     */
    private final ColumnValueSQLConverter columnValueSQLConverter;
    /**
     * 原子更新
     */
    private final ValueUpdateAtomicTrack<Object> valueUpdateAtomicTrack;
    /**
     * 数据库生成键生成器
     */
    private final GeneratedKeySQLColumnGenerator generatedSQLColumnGenerator;
    /**
     * 当前对象属性setter调用方法
     */
    private final PropertySetterCaller<Object> setterCaller;
    /**
     * 当前对象属性getter调用方法
     */
    private final Property<Object,?> getterCaller;
    /**
     * 当前属性对应的jdbc处理器
     */
    private final JdbcTypeHandler jdbcTypeHandler;
    private final ComplexPropType complexPropType;

    public ColumnMetadata(ColumnOption columnOption) {
        this.entityMetadata = columnOption.getEntityMetadata();
        this.name = columnOption.getName();
        this.property= columnOption.getProperty();
        this.propertyType = columnOption.getProperty().getPropertyType();
        this.propertyName = columnOption.getProperty().getName();
        this.primary = columnOption.isPrimary();
        this.generatedKey = columnOption.isGeneratedKey();
        this.version= columnOption.isVersion();
        this.insertIgnore= columnOption.isInsertIgnore();
        this.updateIgnore= columnOption.isUpdateIgnore();
        this.updateSetInTrackDiff= columnOption.isUpdateSetInTrackDiff();
        this.encryptionStrategy= columnOption.getEncryptionStrategy();
        this.supportQueryLike= columnOption.isSupportQueryLike();
        this.large= columnOption.isLarge();
        this.autoSelect= columnOption.isAutoSelect();
        this.valueConverter = columnOption.getValueConverter();
        this.columnValueSQLConverter = columnOption.getColumnValueSQLConverter();
        this.valueUpdateAtomicTrack = columnOption.getValueUpdateAtomicTrack();
        this.generatedSQLColumnGenerator = columnOption.getGeneratedKeySQLColumnGenerator();
        this.primitive = propertyType.isPrimitive();

        if(columnOption.getGetterCaller()==null){
            throw new IllegalArgumentException("not found "+ EasyClassUtil.getSimpleName(columnOption.getEntityMetadata().getEntityClass()) +"."+propertyName+" getter caller");
        }
        if(columnOption.getSetterCaller()==null){
            throw new IllegalArgumentException("not found "+ EasyClassUtil.getSimpleName(columnOption.getEntityMetadata().getEntityClass()) +"."+propertyName+" setter caller");
        }
        this.getterCaller = columnOption.getGetterCaller();
        this.setterCaller = columnOption.getSetterCaller();
        this.jdbcTypeHandler=columnOption.getJdbcTypeHandler();
        this.complexPropType =columnOption.getComplexPropType();
    }

    public EntityMetadata getEntityMetadata() {
        return entityMetadata;
    }

    public String getName() {
        return name;
    }

    public boolean isPrimary() {
        return primary;
    }


    public boolean isGeneratedKey() {
        return generatedKey;
    }


    public boolean isVersion() {
        return version;
    }


    public boolean isInsertIgnore() {
        return insertIgnore;
    }


    public boolean isUpdateIgnore() {
        return updateIgnore;
    }

    public boolean isUpdateSetInTrackDiff() {
        return updateSetInTrackDiff;
    }

    public PropertyDescriptor getProperty() {
        return property;
    }


    public boolean isEncryption() {
        return encryptionStrategy!=null;
    }

    public EncryptionStrategy getEncryptionStrategy() {
        return encryptionStrategy;
    }

    public boolean isSupportQueryLike() {
        return supportQueryLike;
    }


    public boolean isLarge() {
        return large;
    }

    public boolean isAutoSelect() {
        return autoSelect;
    }

    public ValueConverter<?, ?> getValueConverter() {
        return valueConverter;
    }

    public ValueUpdateAtomicTrack<Object> getValueUpdateAtomicTrack() {
        return valueUpdateAtomicTrack;
    }

    public Class<?> getPropertyType() {
        return propertyType;
    }

    public String getPropertyName() {
        return propertyName;
    }
    public boolean isPrimitive(){
        return primitive;
    }

    public PropertySetterCaller<Object> getSetterCaller() {
        return setterCaller;
    }

    public Property<Object, ?> getGetterCaller() {
        return getterCaller;
    }

    public JdbcTypeHandler getJdbcTypeHandler() {
        return jdbcTypeHandler;
    }

    public ColumnValueSQLConverter getColumnValueSQLConverter(){
        return columnValueSQLConverter;
    }

    public GeneratedKeySQLColumnGenerator getGeneratedSQLColumnGenerator() {
        return generatedSQLColumnGenerator;
    }

    /**
     * 表示当前属性的复杂类型
     * 如果当前属性为基本类型那么就是基本类型
     * 如果当前属性是{@link List<Object>} 这种泛型类型通过添加 {@link ComplexPropType}来表示为正确的真实类型
     * @return
     */
    public ComplexPropType getComplexPropType() {
        return complexPropType;
    }
}
