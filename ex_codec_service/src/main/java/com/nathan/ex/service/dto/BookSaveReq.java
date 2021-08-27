package com.nathan.ex.service.dto;

import com.nathan.ex.service.validator.group.SaveCheck;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.groups.Default;

@Data
public class BookSaveReq extends CommonReq {

    @NotBlank(message = "名称不能为空", groups = {SaveCheck.class, Default.class})
    private String name;

}
