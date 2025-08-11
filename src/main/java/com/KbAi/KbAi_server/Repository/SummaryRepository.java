package com.KbAi.KbAi_server.Repository;

import com.KbAi.KbAi_server.Entity.Category;
import com.KbAi.KbAi_server.Entity.ConversationSummary;
import com.KbAi.KbAi_server.Entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SummaryRepository extends JpaRepository<ConversationSummary, Long> {
    @Query("""
      select s.keyword as keyword, count(s) as cnt
      from ConversationSummary s
      where s.category = :category
        and s.createdTime >= :start and s.createdTime < :end
      group by s.keyword
      order by cnt desc
    """)
    List<KeywordCount> countByKeyword(
            @Param("category") Category category,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    interface KeywordCount {
        Keyword getKeyword();
        long getCnt();
    }


    @Query("""
       select s.category as category, count(s) as cnt
       from ConversationSummary s
       where s.createdTime >= :start and s.createdTime < :end
       group by s.category
    """)
    List<CategoryCount> countByCategory(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    interface CategoryCount {
        Category getCategory();
        long getCnt();
    }
}
