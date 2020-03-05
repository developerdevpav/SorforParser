package ru.devpav.registerconfresource.resources;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.devpav.registerconfresource.domain.ConfResource;
import ru.devpav.registerconfresource.service.ConfResourceService;

@RestController
@RequestMapping("/api/configs")
public class ConfResourceController {

    private final ConfResourceService confResourceService;


    public ConfResourceController(ConfResourceService confResourceService) {
        this.confResourceService = confResourceService;
    }


    @GetMapping("/{host}")
    public ResponseEntity<ConfResource> findById(@PathVariable String host) {
        return ResponseEntity.ok(confResourceService.findById(host));
    }

    @PutMapping
    public ResponseEntity<ConfResource> update(@RequestBody ConfResource confResource) {
        return ResponseEntity.ok(confResourceService.update(confResource));
    }

}
