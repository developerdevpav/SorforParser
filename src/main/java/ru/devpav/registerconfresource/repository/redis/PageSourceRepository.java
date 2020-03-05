package ru.devpav.registerconfresource.repository.redis;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.devpav.registerconfresource.domain.Page;

import java.util.Collection;

@Repository
@Transactional
public class PageSourceRepository extends RepositoryRedis<Page> {

    public PageSourceRepository() {
        super("tbx_page");
    }

    public Page save(Page page) {
        return this.save(page, page.getSource());
    }

    public void saveAll(Collection<Page> pages) {
        super.saveAll(pages, Page::getSource);
    }

}
