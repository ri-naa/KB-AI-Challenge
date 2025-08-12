package com.KbAi.KbAi_server.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Category {
    ORGANIZATION("조직업무"),
    PERSONAL("개인 번아웃"),
    CUSTOMER("고객");

    private final String description;
}
