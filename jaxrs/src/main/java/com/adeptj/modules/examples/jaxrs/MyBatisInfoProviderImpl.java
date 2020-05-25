package com.adeptj.modules.examples.jaxrs;

import com.adeptj.modules.data.mybatis.MyBatisInfoProvider;
import org.osgi.service.component.annotations.Component;

@Component
public class MyBatisInfoProviderImpl implements MyBatisInfoProvider {

    @Override
    public String getMyBatisConfig() {
        return MyBatisInfoProvider.DEFAULT_MYBATIS_CONFIG;
    }

    @Override
    public String getEnvironmentId() {
        return "development";
    }
}
