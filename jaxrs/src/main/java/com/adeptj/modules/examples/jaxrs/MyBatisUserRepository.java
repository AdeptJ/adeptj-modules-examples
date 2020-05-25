package com.adeptj.modules.examples.jaxrs;

import com.adeptj.modules.data.mybatis.MyBatisRepository;
import com.adeptj.modules.data.mybatis.core.AbstractMyBatisRepository;
import com.adeptj.modules.examples.jpa.entity.User;
import org.osgi.service.component.annotations.Component;

@Component(service = {MyBatisUserRepository.class, MyBatisRepository.class})
public class MyBatisUserRepository extends AbstractMyBatisRepository<User, Long> {
}
