package com.nathan.ex.service;

import com.nathan.ex.service.dto.BookSaveReq;

public interface BookService {

    void save(BookSaveReq req);

    boolean exist(Long id);
}
