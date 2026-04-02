# 수집형 RPG 가챠(Gacha) 서버 API - 트랜잭션 정합성 및 동시성 분석

대규모 동시 접속(신규 픽업 배너 오픈) 시 발생하는 동시성 문제를 분석하고 원인을 파악하는 과정에 집중한 수집형 RPG 가챠 서버 API입니다. 게임의 핵심 비즈니스 로직(재화 차감 및 아이템 지급)에서 요구되는 엄격한 트랜잭션 정합성과 데이터 무결성 보장, 그리고 자동화된 CI/CD 배포 파이프라인 구축을 목표로 설계되었습니다.

## 🌐 Demo
* **런처 클라이언트:** https://demolauncher.vercel.app
* **API 서버 (Swagger):** https://demolauncher.duckdns.org/swagger

> 관련 프로젝트: [BackOffice](https://github.com/diaryhub/BackOffice) · [WebLauncher](https://github.com/diaryhub/demo_launcher)

## 🛠 Tech Stack
* **Backend:** C# .NET 10.0, Entity Framework Core
* **Database & Cache:** PostgreSQL, Redis
* **Infra & CI/CD:** AWS EC2 (Ubuntu), Docker, GitHub Actions, nginx, Let's Encrypt
* **Frontend:** React + TypeScript + Vite (AI 코딩 도구 활용)
* **Test & Metrics:** k6 (Load Testing), Postman
* **Security:** JWT (JSON Web Token)

---

## 🚀 핵심 아키텍처 및 비즈니스 로직

### 1. 서버 권위(Server-Authoritative) 검증
* **클라이언트 변조 방어:** 게임 클라이언트의 입력값(재화 소모량 등)을 신뢰하지 않고, 서버 DB에 적재된 배너 마스터 데이터의 정가를 기준으로 재화를 차감합니다.
* **JWT 기반 인증:** HTTP 헤더의 서명된 토큰에서 유저 식별자(UserId)를 추출하여 타인 명의 도용(Spoofing) 공격을 원천 차단합니다.

### 2. ACID 트랜잭션 기반 정합성 보장
* 재화 차감(UPDATE), 인벤토리 아이템 지급(INSERT), 가챠 로그 기록(INSERT) 작업을 단일 트랜잭션으로 묶어 처리합니다. 동시 다발적인 요청 속에서도 데드락이나 재화 복사 버그가 발생하지 않도록 데이터 무결성을 확보했습니다.

### 3. Redis Cache-Aside 패턴 적용
* 가챠 확률 메타데이터의 정적 특성을 활용하여 Redis 인메모리 캐싱을 적용했습니다. 식별자(BannerId) 기반으로 캐시 키를 격리하여 RDBMS의 Read 부하(Select Query)를 최소화했습니다.

---

## ⚙️ 인프라 및 CI/CD 파이프라인
* **Docker 컨테이너화:** PostgreSQL, Redis 및 .NET API 서버를 Docker 컨테이너로 패키징하여 로컬과 운영 환경의 일관성을 확보했습니다.
* **CI 자동화(GitHub Actions):** `master` 브랜치 push 시 GitHub Actions Workflow가 트리거되어 빌드 및 단위 테스트를 자동으로 수행하고, 검증된 이미지를 Docker Hub에 푸시합니다.
* **CD 운영 배포(수동 트리거):** CI 파이프라인을 통과한 이미지를 기반으로, AWS EC2(Ubuntu) 인스턴스에 SSH 접속하여 `docker pull` 및 `docker compose up`으로 운영 환경에 배포합니다. push와 동시에 운영 반영되는 것을 방지하기 위해 운영 배포는 의도적으로 수동 트리거로 분리했습니다.
* **nginx 리버스 프록시 + HTTPS:** nginx를 리버스 프록시로 구성하고 Let's Encrypt SSL 인증서를 적용하여 HTTPS 통신을 확보했습니다. 프론트엔드(Vercel, HTTPS)와 백엔드 간 Mixed Content 문제를 해결했습니다.
* **환경변수 기반 설정 분리:** 민감 정보(DB 접속 정보, JWT 키 등)를 `appsettings.json`에서 제거하고 Docker 환경변수로 주입하여 보안을 강화했습니다.

## 🖥️ 프론트엔드 클라이언트
백엔드 API의 실제 동작을 시연하기 위한 게임 런처 클라이언트를 React + TypeScript로 구성했습니다. UI 개발에는 AI 코딩 도구(Claude)를 활용하여 생산성을 높였으며, Vercel을 통해 배포했습니다. 포트폴리오의 핵심은 서버 사이드 설계이며, 프론트엔드는 API 연동 및 실제 서비스 흐름을 확인하기 위한 데모 클라이언트입니다.

---

## 📊 부하 테스트 및 트러블슈팅 (Troubleshooting)
오픈소스 부하 테스트 도구 **k6**를 활용하여 가상 유저(VU) 50명이 30초간 가챠 API를 반복 호출하는 스트레스 테스트를 3단계로 진행하며 병목 원인을 분석했습니다.

* **Phase 1. 테스트 설계 오류 — 단일 계정 락 경합**
  * **현상:** 단일 계정으로 50명이 동시 요청 시 TPS 16.7, 최대 응답 지연 19.36초 기록.
  * **원인:** PostgreSQL이 동일 유저 행(Row)에 배타적 잠금(Exclusive Lock)을 걸어 50개의 요청이 순차 처리됨. 이는 실제 서비스 환경(다수의 유저가 각자 요청)과 다른 테스트 설계 문제였음.
  * **조치:** 가상 유저별로 고유 계정과 토큰을 발급받아 분산 접속하도록 테스트 스크립트 구조 변경.

* **Phase 2. 실측 환경에서의 병목 분석 — 스레드 블로킹**
  * **현상:** 다중 유저 분산 접속(실제 환경에 가까운 조건) 시 TPS 22.7, 최대 응답 지연 8.28초 기록.
  * **원인:** EF Core의 SQL 동기적 콘솔 로깅이 매 요청마다 스레드를 블로킹하는 것이 주된 병목으로 파악됨.

* **Phase 3. 로깅 레벨 조정 및 최종 지표 도출**
  * **조치:** `appsettings.json`에서 EF Core SQL 콘솔 출력 비활성화.
  * **결과 (Phase 2 대비):**
    * **TPS:** 22.7 → **402.6**
    * **평균 응답 지연:** 2,830ms → **23.61ms**
    * **에러율:** 0% (트랜잭션 정합성 유지)
  * **회고:** 로깅 설정 하나가 큰 수치 변화를 만든 만큼, 운영 환경에서 불필요한 동기 I/O를 제거하는 것이 중요함을 체감. 근본적인 DB 쓰기 병목 해소는 향후 과제로 남아 있음.

---

## 💡 한계점 및 향후 고도화 방향 (Future Works)
* **한계점:** 현재 아키텍처는 가챠 요청 시 RDBMS에 직접 쓰기(Direct Write)를 수행합니다. 실제 게임 런칭 수준의 대규모 트래픽 발생 시 커넥션 풀 고갈 및 디스크 I/O 병목이 재발할 수 있습니다.
* **해결 방안:** Redis Stream을 활용한 비동기 메시지 큐 구조 도입을 계획하고 있습니다. 클라이언트 요청은 메모리에 빠르게 적재 후 응답하고, 백그라운드 워커가 큐를 소비하여 일괄 DB에 저장(Batch Insert)하는 Write-Behind 패턴으로 쓰기 부하를 통제하고자 합니다.
