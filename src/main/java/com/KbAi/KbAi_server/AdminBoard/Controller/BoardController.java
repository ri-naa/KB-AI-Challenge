package com.KbAi.KbAi_server.AdminBoard.Controller;

import com.KbAi.KbAi_server.AdminBoard.Dto.MsgSummaryDto;
import com.KbAi.KbAi_server.AdminBoard.Dto.Period;
import com.KbAi.KbAi_server.AdminBoard.Service.BoardService;
import com.KbAi.KbAi_server.Entity.Category;
import com.KbAi.KbAi_server.Entity.Keyword;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/keywords")
    public Map<String, Object> getCategoryResult(
            @RequestParam Category category,
            @RequestParam Period period
    ) {
        var items = boardService.getKeywordCounts(category, period);

        return Map.of(
                "category", category.name(),
                "period", period.name(),
                "items", items
        );
    }

    @GetMapping("/totalBoard")
    public Map<String, Object> getCategoryDistribution() {
        var items = boardService.getCategoryDistribution();
        return Map.of("items", items);
    }


    @GetMapping("/summaries")
    public MsgSummaryDto recentSummaries(
            @RequestParam Keyword keyword
    ) {
        return boardService.getRecent3Summaries(keyword);
    }
}