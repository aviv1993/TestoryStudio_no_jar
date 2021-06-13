package com.testorystudio.testorystudio;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RESTController {

    @GetMapping("/")
    public String index() {
        return "index.html";
    }
}
