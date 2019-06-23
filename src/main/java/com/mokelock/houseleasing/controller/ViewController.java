package com.mokelock.houseleasing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/view")
public class ViewController {

    @RequestMapping(value = "index")
    public String index() {
        return "index";
    }

}
