package ru.devpav.registerconfresource.resources;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.devpav.registerconfresource.domain.Page;
import ru.devpav.registerconfresource.service.PageResourceService;

import java.util.Collection;

;

@RestController
@RequestMapping("/api/pages")
public class PageController {

    private final PageResourceService pageResourceService;

    public PageController(PageResourceService pageResourceService) {
        this.pageResourceService = pageResourceService;
    }

    @GetMapping
    public ResponseEntity<Collection<Page>> findByHost(@RequestParam("host") String host) {
        return ResponseEntity.ok(pageResourceService.findByHost(host));
    }

}
