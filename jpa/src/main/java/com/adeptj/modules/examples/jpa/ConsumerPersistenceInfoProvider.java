package com.adeptj.modules.examples.jpa;

import com.adeptj.modules.data.jpa.PersistenceInfoProvider;
import org.osgi.service.component.annotations.Component;

@Component
public class ConsumerPersistenceInfoProvider implements PersistenceInfoProvider {

    @Override
    public String getPersistenceUnitName() {
        return "AdeptJ_PU_MySQL";
    }

}
