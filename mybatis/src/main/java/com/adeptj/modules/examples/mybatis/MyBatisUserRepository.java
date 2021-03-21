package com.adeptj.modules.examples.mybatis;

import com.adeptj.modules.data.mybatis.api.AbstractMyBatisRepository;
import com.adeptj.modules.data.mybatis.api.MyBatisRepository;
import com.adeptj.modules.examples.mybatis.domain.User;
import org.osgi.service.component.annotations.Component;

@Component(service = {MyBatisUserRepository.class, MyBatisRepository.class})
public class MyBatisUserRepository extends AbstractMyBatisRepository<User, Long> {
}
