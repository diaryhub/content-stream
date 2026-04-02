# Demo Launcher

수집형 RPG 게임 런처 데모 클라이언트입니다.
백엔드 API의 실제 동작을 시연하기 위한 목적으로 제작되었으며, UI는 AI 코딩 도구(Claude)를 활용하여 구성했습니다.

> 포트폴리오의 핵심은 서버 사이드 설계입니다 → [ServerAPI](https://github.com/diaryhub/ServerAPI)

## 데모

https://demolauncher.vercel.app

## 기능

- 로그인 / 회원가입 (JWT 인증)
- 가챠 배너 표시 (BackOffice에서 등록한 배너 실시간 반영)
- 공지사항 조회
- 서버 상태 / 게임 버전 정보 표시
- 토큰 만료 시 자동 로그아웃

## 기술 스택

React 19 · TypeScript · Vite · Tailwind CSS v4 · Vercel 배포

## 관련 프로젝트

- [ServerAPI](https://github.com/diaryhub/ServerAPI) — C# .NET 게임 서버 (핵심)
- [BackOffice](https://github.com/diaryhub/BackOffice) — Kotlin Spring Boot 관리자 서버
