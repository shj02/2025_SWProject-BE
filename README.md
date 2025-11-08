## 📄 몽글트립 백엔드

### \# 몽글트립 (MongleTrip) 백엔드 서버 ✈️

여행 계획의 모든 협업 과정(날짜 합의, 후보지 투표, 예산 정산 등)을 실시간으로 관리하는 모바일 애플리케이션의 백엔드 서버입니다. Spring Boot 3.x 기반의 안정적인 RESTful API를 제공합니다.

-----

### 1\. 프로젝트 개요 및 목표

  * **목표:** 친구/가족과의 여행 계획을 하나의 공간에서 **공동으로 생성하고 관리**할 수 있도록 합니다.
  * **핵심 기능:**
      * **협업 기능:** 날짜 합의, 장소 후보지 투표, 실시간 일정 공유
      * **재정 관리:** 공용/개인 지출 기록 및 자동 정산 계산
      * **커뮤니티:** 여행 후기 게시판 (CRUD 및 공감 기능)

-----

### 2\. 🛠️ 개발 환경 및 기술 스택

저희가 이 프로젝트를 개발하는 데 사용한 핵심 기술 스택입니다.

#### 백엔드 (Backend)

| 카테고리 | 기술 스택 | 버전 (예시) | 비고 |
| :--- | :--- | :--- | :--- |
| **언어** | Java | 17 (LTS) | Spring Boot 3.x의 필수 요구사항 |
| **프레임워크** | Spring Boot | 3.2.x (Stable) | 서버 핵심 프레임워크 |
| **데이터베이스** | MySQL | 8.0.x | 관계형 데이터 저장 |
| **ORM** | Spring Data JPA / Hibernate | - | 데이터베이스 접근 자동화 |
| **유틸리티** | Lombok | - | Getter/Setter/Builder 자동 생성 |
| **인증** | Spring Security | - | JWT 기반 사용자 인증 및 권한 관리 |

#### 개발 환경 (Development Environment)

  * **IDE:** IntelliJ IDEA
  * **빌드 도구:** Gradle
  * **버전 관리:** Git / GitHub

-----

### 3\. 🚀 프로젝트 실행 방법 (Local Run)

로컬에서 백엔드 서버를 실행하기 위한 단계입니다.

#### 3-1. 필수 요구사항

  * Java 17+ (JDK)
  * MySQL 8.0+ 서버 실행 중
  * MySQL에 **`mongletrip_db`** 데이터베이스 생성 완료

#### 3-2. 설정 파일 변경

`src/main/resources/application.yml` 파일을 열어 MySQL 연결 정보를 설정합니다.

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mongletrip_db?serverTimezone=Asia/Seoul&characterEncoding=UTF-8
    username: root
    password: 'hyeji2628@'
```

#### 3-3. 서버 실행

1.  IntelliJ IDEA에서 프로젝트를 엽니다.
2.  `MongleTripBackendApplication.java` 파일을 실행합니다.
3.  콘솔에 "Started MongleTripBackendApplication..." 메시지와 함께 \*\*Hibernate가 테이블을 생성하는 로그(DDL)\*\*가 보이면 성공입니다.

-----

### 4\. 🔑 주요 API 엔드포인트

(선택 사항: Postman 컬렉션 링크가 있다면 추가)

| 기능 분류 | 메서드 | API 경로 (URL) | 설명 |
| :--- | :--- | :--- | :--- |
| **인증** | `POST` | `/api/auth/naver` | 네이버 소셜 로그인 |
| **여행 생성** | `POST` | `/api/trips` | 새로운 여행 방 생성 |
| **날짜 합의** | `PUT` | `/api/trips/{tripId}/available-dates` | 가능한 날짜 목록 등록 |
| **후보지 투표** | `POST` | `/api/trips/candidates/{id}/vote` | 장소 후보지 투표/취소 |
| **예산 현황** | `GET` | `/api/trips/{tripId}/budget` | 예산 사용 현황 조회 |
