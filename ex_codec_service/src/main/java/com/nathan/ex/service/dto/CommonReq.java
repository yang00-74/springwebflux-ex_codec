package com.nathan.ex.service.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

@Data
public class CommonReq {

    @NotNull(message = "id 参数为空，请检查", groups = {Default.class})
    private Long id;

}
