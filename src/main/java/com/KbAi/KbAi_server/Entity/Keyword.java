package com.KbAi.KbAi_server.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Keyword {
    // 조직업무
    SUPERVISOR("상사", Category.ORGANIZATION),
    VACATION("휴가", Category.ORGANIZATION),
    WORKLOAD("업무량", Category.ORGANIZATION),
    LACK_TRAINING("교육 부족", Category.ORGANIZATION),
    PROMOTION("승진", Category.ORGANIZATION),
    PERFORMANCE_PRESSURE("성과 압박", Category.ORGANIZATION),
    OVERTIME_WORK("야근", Category.ORGANIZATION),

    // 개인 번아웃
    LOSS_MOTIVATION("의욕 상실", Category.PERSONAL),
    ANXIETY("불안", Category.PERSONAL),
    LETHARGY("무기력", Category.PERSONAL),
    FINANCIAL_PRESSURE("경제적 압박", Category.PERSONAL),
    FATIGUE("피로 누적", Category.PERSONAL),
    JOB_CHANGE("이직 고민", Category.PERSONAL),
    LACK_SLEEP("수면 부족", Category.PERSONAL),

    // 고객
    RUDENESS("폭언/무례", Category.CUSTOMER),
    DEMANDS("과도한 요구", Category.CUSTOMER),
    DIFFERENCE("언어/문화 차이", Category.CUSTOMER),
    SUSPICION("불신/의심", Category.CUSTOMER),
    REPEATED_COMPLAINTS("반복 민원", Category.CUSTOMER),
    WAITING_CONGESTION("대기 혼잡", Category.CUSTOMER),
    VALUES_CONFLICT("가치관 충돌", Category.CUSTOMER);


    private final String description;
    private final Category category;
}
