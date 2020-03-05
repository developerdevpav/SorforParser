package ru.devpav.registerconfresource.resources;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.devpav.registerconfresource.domain.ConfResource;
import ru.devpav.registerconfresource.domain.WebSource;
import ru.devpav.registerconfresource.domain.WebSourceInformation;
import ru.devpav.registerconfresource.service.WebSourceService;

import java.util.Collection;
import java.util.Set;

@RestController
@RequestMapping("/api/sources")
public class WebSourceResource {

    private final WebSourceService webSourceService;

    public WebSourceResource(WebSourceService webSourceService) {
        this.webSourceService = webSourceService;
    }

    @GetMapping
    public ResponseEntity<Collection<WebSource>> findAll() {
        return ResponseEntity.ok(webSourceService.findAll());
    }

    @PostMapping
    public ResponseEntity<WebSource> save(@RequestBody WebSourceInformation webSourceInformation) {
        return ResponseEntity.ok(webSourceService.save(webSourceInformation));
    }

    @GetMapping("/filter")
    public ResponseEntity<WebSource> save(@RequestParam("url") String host) {
        return ResponseEntity.ok(webSourceService.findByHost(host));
    }

    @DeleteMapping
    public ResponseEntity<WebSource> delete(@RequestParam("url") String url) {
        webSourceService.delete(url);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/sitemaps")
    public ResponseEntity<Set<String>> getSitemaps(@RequestParam("url") String url) {
        return ResponseEntity.ok(webSourceService.getSitemap(url));
    }

    @GetMapping("/config")
    public ResponseEntity<ConfResource> getConfigResource(@RequestParam("url") String url) {
        ConfResource config = webSourceService.getConfig(url);

        if (config == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(config);
    }
}
