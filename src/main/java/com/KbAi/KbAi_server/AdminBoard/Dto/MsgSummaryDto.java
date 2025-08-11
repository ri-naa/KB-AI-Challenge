package com.KbAi.KbAi_server.AdminBoard.Dto;

import com.KbAi.KbAi_server.Entity.Keyword;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MsgSummaryDto {
    private Keyword keyword;
    private String keywordDesc; // 키워드 한국어 설명
    private List<String> summaryList;
}
