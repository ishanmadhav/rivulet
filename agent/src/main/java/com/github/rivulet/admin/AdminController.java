package com.github.rivulet.admin;

import com.github.rivulet.metastore.MetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private MetadataService metastore;

    @GetMapping("")
    public String flushDB() {
        metastore.flushDB();
        return "Flushed";
    }
}
