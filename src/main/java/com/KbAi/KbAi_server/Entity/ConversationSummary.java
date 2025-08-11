package com.KbAi.KbAi_server.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationSummary extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "summary_id")
    private Long id;

    // 요약 메세지
    @Column(length = 200, nullable = false)
    private String summaryMessage;

    // 사용자
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    // 카테고리
    @Enumerated(EnumType.STRING)
    private Category category;

    // 키워드
    @Enumerated(EnumType.STRING)
    private Keyword keyword;

}
