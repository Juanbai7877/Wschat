package org.websocketchat.websocketchat.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.websocketchat.websocketchat.pojo.Result;

/**
 * @author ALL
 * @date 2024/5/19
 * @Description
 */
@RestController
@RequestMapping("/Hello")
public class HelloController {
    @GetMapping("/Hello")
    public String test(){
        return "hello,world";
    }
}
