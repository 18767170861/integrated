package org.wmc.integrated.cors;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class CorsController {

    @CrossOrigin(origins = "http://localhost:63342", methods = {RequestMethod.GET, POST, PUT, DELETE}, maxAge = 60L)
    @RequestMapping(value = "/test/cors", method = {OPTIONS, GET})
    @GetMapping("/test/cors")
    public Object testCors(HttpServletResponse response) {
        // HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN
        response.addHeader("Access-Control-Allow-Origin", "http://localhost:63342");
        return "hello cors";
    }
}
