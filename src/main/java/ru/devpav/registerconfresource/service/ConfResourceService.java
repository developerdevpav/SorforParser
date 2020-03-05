package ru.devpav.registerconfresource.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.devpav.registerconfresource.domain.ConfResource;
import ru.devpav.registerconfresource.repository.redis.ConfResourceRepositoryRedis;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ConfResourceService {

    private final ConfResourceRepositoryRedis confResourceRepository;


    public ConfResourceService(ConfResourceRepositoryRedis confResourceRepository) {
        this.confResourceRepository = confResourceRepository;
    }

    @Transactional
    public ConfResource save(ConfResource confResource) {
        return confResourceRepository.save(confResource);
    }

    void delete(String host) {
        confResourceRepository.deleteById(host);
    }


    public Collection<ConfResource> findAll() {
        final Iterable<ConfResource> confs = confResourceRepository.findAll();

        return StreamSupport.stream(confs.spliterator(), false)
                .collect(Collectors.toList());
    }


    public ConfResource findById(String id) {
        return confResourceRepository.findById(id);
    }

    public void clean() {
        confResourceRepository.deleteAll();
    }

    @Transactional
    public ConfResource update(ConfResource confResource) {
        boolean existsById = confResourceRepository.existsById(confResource.getSource());

        if (!existsById) {
            throw new RuntimeException("Entity with id " + confResource.getSource() + " not found");
        }

        return confResourceRepository.save(confResource);
    }
}
