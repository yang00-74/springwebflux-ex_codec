package com.nathan.ex.service.validator;

import com.nathan.ex.service.BookService;
import com.nathan.ex.service.dto.BookSaveReq;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 1. @UniqueFaq 标注在方法入参对象，而不是对象属性上时，需要在方法所在的类上添加 @Validated 注解
 * 2. 如果方法实际由接口声明，则  @UniqueFaq 和 @Validated 注解都应该在接口上使用，否则会报错
 */
@Slf4j
public class UniqueBookValidator implements ConstraintValidator<UniqueBook, BookSaveReq> {

    @Resource
    BookService bookService;

    @Override
    public boolean isValid(BookSaveReq req, ConstraintValidatorContext context) {
        return !bookService.exist(req.getId());
    }

}
