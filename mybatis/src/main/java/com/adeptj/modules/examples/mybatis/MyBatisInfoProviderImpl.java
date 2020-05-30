package com.adeptj.modules.examples.mybatis;

import com.adeptj.modules.data.mybatis.MyBatisInfoProvider;
import org.osgi.service.component.annotations.Component;

import java.util.Set;

@Component
public class MyBatisInfoProviderImpl implements MyBatisInfoProvider {

    @Override
    public Set<Class<?>> getMappers() {
        return Set.of(UserAnnotationMapper.class);
    }
}
