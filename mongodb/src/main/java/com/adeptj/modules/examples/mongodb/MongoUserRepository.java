package com.adeptj.modules.examples.mongodb;

import com.adeptj.modules.data.mongodb.DocumentInfo;
import com.adeptj.modules.data.mongodb.MongoRepository;
import com.adeptj.modules.data.mongodb.core.AbstractMongoRepository;
import org.osgi.service.component.annotations.Component;

@DocumentInfo(database_name = "AdeptJ", collection_name = "users")
@Component(service = {MongoUserRepository.class, MongoRepository.class})
public class MongoUserRepository extends AbstractMongoRepository<User> {

    @Override
    public Class<User> getDocumentClass() {
        return User.class;
    }
}
