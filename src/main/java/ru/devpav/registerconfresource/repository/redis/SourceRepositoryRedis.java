package ru.devpav.registerconfresource.repository.redis;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.devpav.registerconfresource.domain.WebSource;

import java.util.List;

@Repository
@Transactional
public class SourceRepositoryRedis extends RepositoryRedis<WebSource> {

    public SourceRepositoryRedis() {
        super("tbx_source");
    }

    public WebSource save(WebSource webSource) {
        return this.save(webSource, webSource.getHost());
    }

    public List<WebSource> saveAll(List<WebSource> pages) {
        return super.saveAll(pages, WebSource::getHost);
    }

}
