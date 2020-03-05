package ru.devpav.registerconfresource.repository.redis;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.devpav.registerconfresource.domain.ConfResource;

import java.util.List;

@Repository
@Transactional
public class ConfResourceRepositoryRedis extends RepositoryRedis<ConfResource> {

    protected ConfResourceRepositoryRedis() {
        super("tbx_config");
    }

    public ConfResource save(ConfResource confResource) {
        return this.save(confResource, confResource.getSource());
    }

    public List<ConfResource> saveAll(List<ConfResource> pages) {
        return super.saveAll(pages, ConfResource::getSource);
    }

}
