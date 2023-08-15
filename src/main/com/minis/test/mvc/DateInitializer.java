package com.minis.test.mvc;


import java.util.Date;

import com.minis.web.bind.support.WebBindingInitializer;
import com.minis.web.bind.WebDataBinder;

public class DateInitializer implements WebBindingInitializer {
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(Date.class,"yyyy-MM-dd", false));
    }
}
