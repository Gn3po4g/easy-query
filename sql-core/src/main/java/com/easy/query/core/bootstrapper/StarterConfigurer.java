package com.easy.query.core.bootstrapper;

import com.easy.query.core.inject.ServiceCollection;

/**
 * create time 2023/5/15 12:56
 * 文件说明
 *
 * @author xuejiaming
 */
public interface StarterConfigurer {
    void configure(ServiceCollection services);
}
