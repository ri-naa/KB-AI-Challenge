package com.KbAi.KbAi_server.AdminBoard.Service;

import com.KbAi.KbAi_server.AdminBoard.Dto.CategoryDistriDto;
import com.KbAi.KbAi_server.AdminBoard.Dto.KeywordCountDto;
import com.KbAi.KbAi_server.AdminBoard.Dto.MsgSummaryDto;
import com.KbAi.KbAi_server.AdminBoard.Dto.Period;
import com.KbAi.KbAi_server.Entity.Category;
import com.KbAi.KbAi_server.Entity.ConversationSummary;
import com.KbAi.KbAi_server.Entity.Keyword;
import com.KbAi.KbAi_server.Repository.SummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final SummaryRepository summaryRepository;
    private static final ZoneId ZONE = ZoneId.of("Asia/Seoul");

    // 카테고리, 기간 별 키워드 누적 구하기
    public List<KeywordCountDto> getKeywordCounts(Category category, Period period) {
        DateRange r = rangeOf(period);
        return mapToDto(summaryRepository.countByKeyword(category, r.start, r.end));
    }

    private record DateRange(LocalDateTime start, LocalDateTime end) {}

    private DateRange rangeOf(Period p) {
        LocalDate today = LocalDate.now(ZONE);

        return switch (p) {
            case WEEK -> {
                LocalDate s = today.minusDays(6);
                yield new DateRange(s.atStartOfDay(), today.plusDays(1).atStartOfDay());
            }
            case MONTH -> {
                LocalDate first = today.withDayOfMonth(1);
                LocalDate firstNext = first.plusMonths(1);
                yield new DateRange(first.atStartOfDay(), firstNext.atStartOfDay());
            }
            case YEAR -> {
                LocalDate s = today.minusDays(364);
                yield new DateRange(s.atStartOfDay(), today.plusDays(1).atStartOfDay());
            }
        };
    }

    private List<KeywordCountDto> mapToDto(List<SummaryRepository.KeywordCount> rows) {
        return rows.stream()
                .map(p -> new KeywordCountDto(
                        p.getKeyword(),
                        p.getKeyword().getDescription(),
                        p.getCnt()))
                .toList();
    }


    // 전체 카테고리 별 비율 구하기
    public List<CategoryDistriDto> getCategoryDistribution(Period period) {
        DateRange dateRange = rangeOf(period);

        var rows = summaryRepository.countByCategory(dateRange.start, dateRange.end);

        long total = Math.max(
                rows.stream().mapToLong(SummaryRepository.CategoryCount::getCnt).sum(),
                1
        );

        return rows.stream()
                .sorted(Comparator.comparingLong(SummaryRepository.CategoryCount::getCnt).reversed())
                .map(p -> new CategoryDistriDto(
                        period,
                        p.getCategory(),
                        p.getCategory().getDescription(),
                        Math.round(p.getCnt() * 10000.0 / total) / 100.0
                ))
                .toList();
    }


    // 키워드 별 메세지 요약 최근 3개
    public MsgSummaryDto getRecent3Summaries(Keyword keyword) {
        var list = summaryRepository
                .findTop3ByKeywordOrderByCreatedTimeDesc(keyword)
                .stream()
                .map(ConversationSummary::getSummaryMessage)
                .toList();

        return new MsgSummaryDto(
                keyword,
                keyword.getDescription(),
                list
        );
    }

}