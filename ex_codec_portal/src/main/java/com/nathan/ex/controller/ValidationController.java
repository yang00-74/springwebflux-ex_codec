package com.nathan.ex.controller;

import com.nathan.ex.dto.ApiResult;
import com.nathan.ex.service.BookService;
import com.nathan.ex.service.dto.BookSaveReq;
import com.nathan.ex.service.validator.UniqueBook;
import com.nathan.ex.service.validator.group.SaveCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class ValidationController {

    @Autowired
    private BookService bookService;

    @PostMapping(value = "/book")
    public ApiResult save(@RequestBody @UniqueBook @Validated(value = {SaveCheck.class}) BookSaveReq req) {
        bookService.save(req);
        return ApiResult.success();
    }
}
