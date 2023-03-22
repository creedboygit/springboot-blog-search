<br/>

# springboot-blog-search

<br/>

## 프로젝트 내용

### 블로그 검색 서비스
- 카카오, 네이버 Open API를 이용한 블로그 검색 서비스 - 백엔드 API 구현
- 인기 블로그 검색어 TOP 10 조회 서비스 - 백엔드 API 구현

<br/>

## 실행 방법

1. __[Executable jar Download](https://github.com/creedboygit/springboot-blog-search/releases/download/v1.0/blog-search-0.0.1-SNAPSHOT.jar "jar 파일 다운로드")__ - 실행 가능한 jar 파일 다운로드

2. `$ cd [다운로드 경로]`

3. `java -jar blog-search-01-0.0.1-SNAPSHOT.jar`

4. __[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html "localhost Swagger 경로")__ - localhost Swagger 접속

<br/>

## 구현 스펙

- JAVA 17
- Spring Boot 2.7.9
- Gradle
- H2 (in-memory DB)
- JPA
- Junit5
- Lombok

<br/>

## 외부 라이브러리

- Swagger : API 자동 명세 & 테스트
- WebClient : HTTP Client로 카카오, 네이버 Open API 연결 시 사용

<br/>

## localhost Swagger 경로

__[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html "Swagger 경로")__

<br/>

## Backend API 명세서 경로 (Postman Publish)

__[https://documenter.getpostman.com/view/21941243/2s93RL1Ge8](https://documenter.getpostman.com/view/21941243/2s93RL1Ge8 "API 명세서 보기")__

<br/>

## 구현 내용

### 1. 블로그 검색

- 카카오 검색 외에 새로운 검색 소스 추가가 용이하도록 확장성을 고려하여 구현
    - 추가되는 사이트를 확장 구현하기 쉽게 Interface 설계
- 검색 결과 Pagination 형태로 제공

### 2. 인기 검색어 목록

- <mark>JPA를 이용하여 사용자가 많이 검색한 순서대로 최대 10개의 검색 키워드 조회</mark>
- 검색어 + 검색된 횟수 조회

<br/>

## 기타 구현 내용

- RestControllerAdvice를 이용하여 Exception Handling 구현
- ResponseBodyAdvice를 이용하여 공통 Response 구현

<br/>

## 테스트

- Controller Level 테스트케이스 통과
- Service Level 테스트케이스 통과

<br/>

## Contact

scrom@kakao.com