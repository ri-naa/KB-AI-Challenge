package com.KbAi.KbAi_server.AdminBoard.Service;

import com.KbAi.KbAi_server.AdminBoard.Dto.KeywordCountDto;
import com.KbAi.KbAi_server.AdminBoard.Dto.Period;
import com.KbAi.KbAi_server.Entity.Category;
import com.KbAi.KbAi_server.Repository.SummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final SummaryRepository summaryRepository;
    private static final ZoneId ZONE = ZoneId.of("Asia/Seoul");

    public List<KeywordCountDto> getKeywordCounts(Category category, Period period) {
        DateRange r = rangeOf(period);
        return mapToDto(summaryRepository.countByKeyword(category, r.start, r.end));
    }

    private record DateRange(LocalDateTime start, LocalDateTime end) {}

    private DateRange rangeOf(Period p) {
        LocalDate today = LocalDate.now(ZONE);

        return switch (p) {
            case WEEK -> {
                LocalDate s = today.minusDays(6); // 오늘 포함 최근 7일
                yield new DateRange(s.atStartOfDay(), today.plusDays(1).atStartOfDay());
            }
            case MONTH -> {
                LocalDate first = today.withDayOfMonth(1);
                LocalDate firstNext = first.plusMonths(1);
                yield new DateRange(first.atStartOfDay(), firstNext.atStartOfDay());
            }
            case YEAR -> { // 최근 1년
                LocalDate s = today.minusDays(364);
                yield new DateRange(s.atStartOfDay(), today.plusDays(1).atStartOfDay());
            }
        };
    }

    private List<KeywordCountDto> mapToDto(List<SummaryRepository.KeywordCountProjection> rows) {
        return rows.stream()
                .map(p -> new KeywordCountDto(
                        p.getKeyword(),
                        p.getKeyword().getDescription(),
                        p.getCnt()))
                .toList();
    }

}