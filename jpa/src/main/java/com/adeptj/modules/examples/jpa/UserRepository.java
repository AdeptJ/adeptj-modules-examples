package com.adeptj.modules.examples.jpa;

import com.adeptj.modules.data.jpa.JpaRepository;
import com.adeptj.modules.data.jpa.JpaUnitName;
import com.adeptj.modules.data.jpa.core.AbstractJpaRepository;
import com.adeptj.modules.examples.jpa.entity.User;
import org.osgi.service.component.annotations.Component;

@JpaUnitName(name = "AdeptJ_PU_MySQL")
@Component(service = {JpaRepository.class, UserRepository.class}, immediate = true)
public class UserRepository extends AbstractJpaRepository<User, Long> {

}
