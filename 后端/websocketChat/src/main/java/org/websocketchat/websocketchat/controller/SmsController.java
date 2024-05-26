package org.websocketchat.websocketchat.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class SmsController {

    @RequestMapping("/send/{phone}")
    public void send (@PathVariable("phone") String phone) throws Exception {

    }
}