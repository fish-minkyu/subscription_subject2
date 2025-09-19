## Subject2_Subscription 프로젝트

### 구현 기능

- JWT 로그인 기능 구현 완료.
- 프로젝트 관리 API 구현 완료.
    - 요금제에 따른 프로젝트 생성 개수 제한 validation 구현 완료.

  | **기능** | **메서드** | **엔드포인트** | **설명** |
      | --- | --- | --- | --- |
  | **프로젝트 생성** | POST | /projects | 프로젝트 생성 (중복 불가) |
  | **프로젝트 상세 조회** | GET | /projects/:id | 프로젝트 상세 + 진행 중 태스크 개수 |
  | **프로젝트 수정** | PATCH | /projects/:id | 마감일, 상태, 담당자 변경 |
  | **프로젝트 삭제** | DELETE | /projects/:id | 삭제 권한 없을시 삭제 불가 |

  |  | **베이직플랜** | **프로플랜** | **A** | **B** | **C** | **D** |
      | --- | --- | --- | --- | --- | --- | --- |
  | **프로젝트 생성** | 최대 1개 | 최대 5개 | O | O | O | O |
  | **프로젝트 수정** |  |  | O | O | X | X |
  | **프로젝트 삭제** |  |  | O | O | O | X |
  | **프로젝트 조회** |  |  | O | X | X | O |
- 관리자가 일반 유저의 역할 및 권한 정책 조정 기능 구현 완료.

### 사용 기술

- Java 21
- Spring Boot 3.5.5
- Spring Security
- jjwt
- SQLite
- Querydsl

### 설치 및 실행방법

- git clone https://github.com/fish-minkyu/subscription_subject2.git

### API 명세서 링크 (Postman)
https://subject1images.postman.co/workspace/Subject1_Images-Workspace~c69748bb-1997-44ae-8afd-c39a5a82815b/collection/28378931-e8c138f8-aa44-4cb9-a685-421337d405e7?action=share&source=copy-link&creator=28378931

### Postman 문서
[postman_document](subject2_subscription_eominkyu.json)