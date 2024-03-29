package com.nathan.ex.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author nathan.yang
 * @date 2019/11/29
 */
@RestController
public class CoolController {

    @GetMapping(value = "/hello")
    public String hello(@RequestParam("acct") String acct) {
        return acct;
    }
}
