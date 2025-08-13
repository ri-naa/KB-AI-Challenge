# KB HUG AI - 직원 힐링 AI 챗봇

## 프로젝트 개요

KB HUG AI는 직원들의 스트레스와 고민을 해소하기 위한 AI 상담 챗봇 시스템입니다. 사용자가 선택한 카테고리에 따라 맞춤형 AI 캐릭터와 대화하며, 대화 내용을 바탕으로 관련 리소스를 추천해주는 서비스입니다.


### 주요 기능

1. **대화 기록 관리**
   - 사용자 별 상담 대화 요약 저장 (`ConversationSummary` 엔티티)
   - 카테고리·키워드 매핑 저장 및 조회
   - 기간별 데이터 필터링

2. **카테고리·기간별 키워드 누적 통계 조회**
   - 특정 카테고리와 기간(`WEEK`, `MONTH`, `YEAR`)에 해당하는 키워드별 메시지 건수 집계
   - 키워드별 한국어 설명(`keywordDesc`) 포함하여 반환
     
3. **전체 카테고리 분포 조회**
    - 지정한 기간 동안의 카테고리별 메시지 비율 계산
    - 전체 합 대비 비율(%) 계산
    - 결과를 카테고리 한국어 설명(`description`)과 함께 반환
4. **키워드별 최근 대화 요약 조회**
    - 특정 키워드에 해당하는 최근 요약 메시지 최신순으로 반환
    - 키워드 한국어 설명(`keywordDesc`)과 함께 반환

## 설치 및 실행

### 1. 환경 변수 설정
프로젝트를 실행하기 위해 application.yml 파일을 작성해야 합니다.
KbAi_server/src/main/resources/ 에 application.yml을 생성하고, 아래 코드를 복사하여 붙여넣습니다.
마스킹된 부분은 실행 환경에 맞게 넣어줍니다.
```yaml
spring:
  mvc:
    pathmatch:
      matching-strategy: ant_path_matcher

  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://{ ENDPOINT }:3306/{ DB_NAME }?serverTimezone=Asia/Seoul&characterEncoding=UTF-8
    username: { USERNAME }
    password: { PASSWORD }

  jpa:
    generate-ddl: true
    hibernate:
      ddl-auto: update
    database: mysql
    database-platform: org.hibernate.dialect.MySQLDialect

jwt:
  secret: { SECRET_KEY }

```


### 2. 프로젝트 빌드
```bash
./gradlew build
```

### 3. JAR 실행
```bash
java -jar build/libs/kbai-0.0.1-SNAPSHOT.jar
```

## 기술 스택 및 시스템 아키텍처

### 🛠️ 기술 스택

| 분류 | 기술 | 버전 | 용도 |
|------|------|------|------|
| **Backend** | Spring Boot | - | RESTful API 서버 (외부 구현) |
| **Database** | MySQL | - | 데이터 저장 및 관리 |
| **AI/ML** | OpenAI GPT-3.5 Turbo | - | 자연어 처리 및 대화 생성 |
| **Web Scraping** | BeautifulSoup4 | - | 네이버 블로그 크롤링 |
| **HTTP Client** | Requests | 2.31.0 | 외부 API 호출 |
| **Environment** | python-dotenv | 1.0.0 | 환경 변수 관리 |
| **Frontend** | HTML5/CSS3/JavaScript | - | 사용자 인터페이스 |
| **Storage** | localStorage | - | 클라이언트 사이드 상태 관리 |

### 시스템 아키텍처

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │   Backend       │    │   External      │
│   (Browser)     │    │   (Spring Boot) │    │   Services      │
├─────────────────┤    ├─────────────────┤    ├─────────────────┤
│ • HTML5/CSS3    │◄──►│ • Spring Server │◄──►│ • OpenAI API    │
│ • JavaScript    │    │ • RESTful API   │    │ • GPT-3.5 Turbo │
│ • localStorage  │    │ • Business Logic│    │                 │
│ • Real-time UI  │    │ • MySQL DB      │    │ • Naver Search  │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

### 주요 엔드포인트


#### 1) 카테고리·기간별 키워드 누적 통계
- **GET** `/admin/keywords`
- **Query**
  - `category` : `ORGANIZATION`, `PERSONAL`, `CUSTOMER`
  - `period`: `WEEK`, `MONTH`, `YEAR`

#### 2) 카테고리 분포
- **GET** `/admin/totalBoard`
- **Query**
  - `period`: `WEEK`, `MONTH`, `YEAR`
 
#### 3) 특정 키워드의 최근 대화 요약
- **GET** `/admin/summaries`
- **Query**
  - `keyword`: `SUPERVISOR`, `ANXIETY`, `RUDENESS`, ...
