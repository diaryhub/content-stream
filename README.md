# Content Stream

OTT 플랫폼 백엔드 API 서버입니다. Netflix와 같은 구독형 콘텐츠 서비스의 핵심 기능을 구현했습니다.

---

## 기술 스택

| 분류 | 기술 |
|------|------|
| Language | Java 21 |
| Framework | Spring Boot 4.0.5 |
| Build | Gradle 9.4.1 (Kotlin DSL) |
| Authentication | Spring Security 7, JWT (JJWT 0.12.6) |
| Database | PostgreSQL + Flyway |
| Cache | Redis |
| Message Queue | Apache Kafka |
| Container | Docker, Docker Compose |
| CI/CD | GitHub Actions |
| API Docs | Swagger (springdoc-openapi) |

---

## 아키텍처

```
┌──────────────────────────────────────────────────┐
│                  Docker Network                  │
│                                                  │
│  ┌──────────────┐      ┌───────────────────────┐ │
│  │  content-    │      │       ServerDB        │ │
│  │  stream:8082 │─────▶│  PostgreSQL (mydb)   │ │
│  │              │      │  Redis (game_redis)   │ │
│  └──────┬───────┘      └───────────────────────┘ │
│         │                                        │
│         │         ┌──────────────────────┐       │
│         └───────▶│  Kafka + Zookeeper   │       │
│                   └──────────────────────┘       │
└──────────────────────────────────────────────────┘
```

### 이벤트 흐름

```
[시청 기록]
POST /api/watch-history
    → WatchHistoryEventProducer
    → Kafka (watch.history, 파티션 3개)
    → WatchHistoryEventConsumer
    → DB upsert (이어보기 위치 저장)

[구독 만료]
@Scheduled 매일 자정
    → 만료 구독 처리
    → SubscriptionEventProducer
    → Kafka (subscription.expired, 파티션 1개)
    → SubscriptionExpiredConsumer
    → 알림 DB 저장
```

---

## 기술 선택 이유

**Kafka — 시청 이벤트 비동기 처리**
> 사용자가 영상 시청 중 발생하는 진행 위치 저장 요청은 고빈도 이벤트입니다. DB에 직접 write하면 동시 접속자 증가 시 병목이 발생하므로 Kafka를 통해 비동기 처리하여 DB 부하를 분산했습니다.

**Redis — RefreshToken 저장**
> RefreshToken은 만료 시 자동 삭제가 필요합니다. Redis의 TTL 기능을 활용해 별도의 만료 처리 로직 없이 자동 삭제되도록 설계했습니다.

**Flyway — DB 마이그레이션 관리**
> `ddl-auto=validate` 설정으로 JPA가 스키마를 자동 변경하지 않도록 하고, Flyway로 버전 관리된 SQL 스크립트를 통해 스키마 변경 이력을 추적합니다.

**Feature-based 패키지 구조**
> `user`, `subscription`, `content` 등 도메인 단위로 패키지를 구성했습니다. 레이어 기반 구조보다 도메인 응집도가 높아 기능 단위 파악과 확장이 용이합니다.

---

## 도메인 구조

```
User (회원)
 ├── Subscription (구독) → Plan (FREE / BASIC / PREMIUM)
 ├── WatchHistory (시청기록) → Content, Episode
 ├── Wishlist (찜) → Content
 ├── Review (평점) → Content
 └── Notification (알림)

Content (콘텐츠)
 └── Episode (에피소드, SERIES만 해당)
```

---

## API 목록

### 인증
| Method | URI | 설명 | 인증 |
|--------|-----|------|------|
| POST | /api/auth/signup | 회원가입 | 불필요 |
| POST | /api/auth/login | 로그인 (JWT 발급) | 불필요 |
| POST | /api/auth/refresh | 토큰 재발급 | 불필요 |

### 구독
| Method | URI | 설명 | 인증 |
|--------|-----|------|------|
| GET | /api/plans | 플랜 목록 조회 | 불필요 |
| POST | /api/subscriptions | 구독 신청 | 필요 |
| GET | /api/subscriptions/me | 내 구독 조회 | 필요 |
| DELETE | /api/subscriptions | 구독 취소 | 필요 |

### 콘텐츠
| Method | URI | 설명 | 인증 |
|--------|-----|------|------|
| GET | /api/contents | 콘텐츠 목록 (장르/타입 필터) | 필요 |
| GET | /api/contents/{id} | 콘텐츠 상세 + 에피소드 | 필요 |

### 시청 기록
| Method | URI | 설명 | 인증 |
|--------|-----|------|------|
| POST | /api/watch-history | 시청 위치 저장 (Kafka) | 필요 |
| GET | /api/watch-history | 내 시청 기록 전체 | 필요 |
| GET | /api/watch-history/{contentId} | 이어보기 위치 조회 | 필요 |

### 찜 / 평점
| Method | URI | 설명 | 인증 |
|--------|-----|------|------|
| POST | /api/wishlist/{contentId} | 찜 토글 (추가/취소) | 필요 |
| GET | /api/wishlist | 내 찜 목록 | 필요 |
| POST | /api/reviews/{contentId} | 평점 등록/수정 | 필요 |
| GET | /api/reviews/{contentId} | 콘텐츠 평점 목록 | 필요 |

### 알림
| Method | URI | 설명 | 인증 |
|--------|-----|------|------|
| GET | /api/notifications | 내 알림 목록 | 필요 |
| PATCH | /api/notifications/{id}/read | 알림 읽음 처리 | 필요 |

---

## 로컬 실행 방법

### 사전 요구사항
- Docker, Docker Compose
- [ServerDB](https://github.com/diaryhub) 프로젝트 실행 필요 (PostgreSQL, Redis)

### 환경변수 설정
프로젝트 루트에 `.env` 파일 생성:
```env
DB_NAME=content
DB_USERNAME=postgres
DB_PASSWORD=your_password
JWT_SECRET=your-secret-key-must-be-at-least-256-bits-long
```

### 실행
```bash
# ServerDB 먼저 실행 (PostgreSQL, Redis)
cd ../ServerDB && docker compose up -d

# content-stream 실행
cd ../content-stream && docker compose up -d
```

### API 문서
서버 실행 후 접속: `http://localhost:8082/swagger-ui/index.html`

---

## DB 마이그레이션

Flyway를 통해 자동 적용됩니다.

| 버전 | 내용 |
|------|------|
| V1 | users 테이블 |
| V2 | plans, subscriptions 테이블 + 초기 플랜 데이터 |
| V3 | contents, episodes 테이블 + 샘플 데이터 |
| V4 | watch_history 테이블 |
| V5 | wishlists, reviews 테이블 |
| V6 | notifications 테이블 |

---

## CI/CD

`main` 브랜치 push 시 GitHub Actions 자동 실행:

```
push → 빌드 (Gradle bootJar)
     → Docker 이미지 빌드
     → Docker Hub 푸시 (dockdiary/content-stream)
```
