package com.adeptj.modules.examples.mongodb;

import com.adeptj.modules.data.mongodb.DocumentInfo;
import com.adeptj.modules.data.mongodb.MongoRepository;
import com.adeptj.modules.data.mongodb.core.AbstractMongoRepository;
import org.osgi.service.component.annotations.Component;

import java.util.ArrayList;
import java.util.List;

@DocumentInfo(database_name = "AdeptJ", collection_name = "users")
@Component(service = {MongoUserRepository.class, MongoRepository.class})
public class MongoUserRepository extends AbstractMongoRepository<User> {

    public MongoUserRepository() {
        super(User.class);
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        this.mongoCollection.find(User.class).forEach(users::add);
        return users;
    }
}
