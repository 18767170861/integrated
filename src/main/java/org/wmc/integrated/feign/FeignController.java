package org.wmc.integrated.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/feign")
public class FeignController {

    @GetMapping("/demo1")
    public String getDemo1(@RequestParam String name, @RequestParam String age) {
        log.info(name);
        return "success:" + name;
    }

    @GetMapping("/demo2")
    public String getDemo1(@RequestParam String name) {
        log.info(name);
        return "success:" + name;
    }

    @PostMapping("/demo4")
    public String demo4(@RequestBody String name) {
        log.info(name);
        return "success:" + name;
    }

}