package com.nathan.ex.service.impl;

import com.nathan.ex.service.BookService;
import com.nathan.ex.service.dto.BookSaveReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class BookServiceImpl implements BookService {

    @Override
    public void save(BookSaveReq req) {
        log.info("save book:{}", req);
    }

    @Override
    public boolean exist(Long id) {
        return Objects.nonNull(id);
    }
}
