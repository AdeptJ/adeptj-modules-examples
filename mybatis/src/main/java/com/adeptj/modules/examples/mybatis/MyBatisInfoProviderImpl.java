package com.adeptj.modules.examples.mybatis;

import com.adeptj.modules.data.mybatis.MyBatisInfoProvider;
import org.osgi.service.component.annotations.Component;

import java.util.List;

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

    @Override
    public List<Class<?>> getMappers() {
        return List.of(UserAnnotationMapper.class);
    }
}
