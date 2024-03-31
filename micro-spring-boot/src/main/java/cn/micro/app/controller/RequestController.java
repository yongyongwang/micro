package cn.micro.app.controller;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/micro/app")
public class RequestController {

    @GetMapping("/test1")
    public Map<String, String> test1() {
        Map<String, String> map = new HashMap<>();
        map.put("username", "administrator");
        map.put("password", "administrator");
        map.put("zh", "中文");
        return map;
    }

    @GetMapping("/exception")
    public Map<String, String> exception() {
        Map<String, String> map = new HashMap<>();
        map.put("username", "administrator");
        map.put("password", "administrator");
        map.put("zh", "中文");
        int i=9/0;
        return map;
    }

    @PostMapping("/json")
    public Map<String, String> exception(@RequestBody Map<String, Object> body) {
        Map<String, String> map = new HashMap<>();
        map.put("username", "administrator");
        map.put("password", "administrator");
        map.put("zh", "中文");
        return map;
    }

}
