package com.adeptj.modules.examples.jpa;

import com.adeptj.modules.data.jpa.JpaRepository;
import com.adeptj.modules.data.jpa.core.AbstractJpaRepository;
import com.adeptj.modules.examples.jpa.entity.User;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

@Component(service = {JpaRepository.class, UserRepository.class}, immediate = true)
public class UserRepository extends AbstractJpaRepository<User, Long> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}
