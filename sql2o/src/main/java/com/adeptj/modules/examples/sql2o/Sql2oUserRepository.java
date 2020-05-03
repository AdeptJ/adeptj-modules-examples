package com.adeptj.modules.examples.sql2o;

import com.adeptj.modules.data.sql2o.Sql2oRepository;
import com.adeptj.modules.data.sql2o.core.AbstractSql2oRepository;
import org.osgi.service.component.annotations.Component;

@Component(service = {Sql2oUserRepository.class, Sql2oRepository.class})
public class Sql2oUserRepository extends AbstractSql2oRepository<User, Long> {

}
