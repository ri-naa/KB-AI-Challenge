package com.KbAi.KbAi_server.AdminBoard.Dto;

import com.KbAi.KbAi_server.Entity.Keyword;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KeywordCountDto {
    private Keyword keyword;
    private String description;
    private long count;
}