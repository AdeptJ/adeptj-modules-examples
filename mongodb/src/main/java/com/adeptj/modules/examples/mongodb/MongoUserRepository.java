package com.adeptj.modules.examples.mongodb;

import com.adeptj.modules.data.mongodb.api.AbstractMongoRepository;
import com.adeptj.modules.data.mongodb.api.DocumentInfo;
import com.adeptj.modules.data.mongodb.api.MongoRepository;
import com.adeptj.modules.data.mongodb.api.Repository;
import org.osgi.service.component.annotations.Component;

@Repository("UserRepository")
@DocumentInfo(database_name = "AdeptJ", collection_name = "users")
@Component(service = {MongoUserRepository.class, MongoRepository.class})
public class MongoUserRepository extends AbstractMongoRepository<User> {

    public MongoUserRepository() {
        super(User.class);
    }
}
