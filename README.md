# PEEPPO(back-end)

핍포(PEEPPO)는 몰래보다(Peeping)와 주머니(Pocket)의 합성어로, 주머니를 몰래 살펴본다는 뜻을 가지고 있어요. 내 물건을 상대방의 물건과 물물교환할 수 있는 플랫폼입니다.

## 개발자

### [김지훈](https://github.com/K-IMjihun)
- `Rating` 게임 개발 / `Scheduling`
- 물품과 관련된 `API` 구현
- `S3`를 이용한 이미지 처리
- `Redis`를 활용한 캐싱 
- `Auction`, `Bid`, `Home`, `RequestList`, `ReceiveList` 등 전체적인 기능 보조 개발

### [이지원](https://github.com/jiooong)
- 프로젝트 부팀장
- 경매와 관련된 `API` 구현
- `Websocket`, `Stomp`, `Redis` 활용한 실시간 채팅 기능 개발
- `SSE`를 사용한 실시간 알림 기능 개발
- `Goods`, `Home`, `dips`, `RequestList`, `ReceiveList` 등 전체적인 기능 보조 개발
- `AWS Ec2` 배포 

### [이지원](https://github.com/stoow1)
- `Spring Security`를 이용한 `User` 회원 도메인 개발
- 입찰과 관련된 `API` 구현
- `SSE`를 사용한 실시간 알림 기능 개발
- `S3`를 이용한 이미지 처리
- `Auction`, `Goods`, `Home`, `dips`, `RequestList`, `ReceiveList` 등 전체적인 기능 보조 개발


## 주요 기능

### 교환하고 싶은 물건 등록 / 물물교환 리스트
![image](https://github.com/ffinal4/back-end/assets/102176567/83616510-ab9b-42ea-81d0-0f36d5b2de09)

### 교환 요청 / 교환 신청할 물건 선택
![image](https://github.com/ffinal4/back-end/assets/102176567/1f4b20b6-15e9-4fbe-b4d2-fa827f842fea)

### 나의 교환 상태 확인 / 채팅을 통한 교환
![image](https://github.com/ffinal4/back-end/assets/102176567/44c15c3c-b1ef-4885-bd40-baed6a9810f1)

### 게임을 통한 물건 레이팅
![image](https://github.com/ffinal4/back-end/assets/102176567/9294c278-054b-42ea-8692-f1e1fa331f99)

### 물물교환 경매 시스템 / 입찰
![image](https://github.com/ffinal4/back-end/assets/102176567/acd236d9-373a-4c21-8c29-d9f385b5a973)

## 왜 이 프로젝트를 만들었는가?

**시장조사 결과, 인기 중고거래 사이트에서 교환에 대한 검색 결과가 11만건 이상으로 수요와 공급은 증가하고 있지만, 수요와 공급을 동시에 충족시키는, 즉 물물교환 플랫폼은 아직 없습니다. 따라서 우리는 '핍포'를 서비스하여 사람들이 간편하고 편리하게 물물 교환을 할 수 있도록 하고자 합니다.**

### MZ세대 특징

- **집단보다는 개인의 행복과 만족을, 소유보다는 공유(공유 경제, 중고거래, 렌트 서비스 등)를 중시하는 소비 특징 - 한국 소비자원**
- **'가심비': 가격에 ‘마음 심(心)’을 더한 말로 가격 대비 마음의 만족을 추구하는 소 비 형태. 가격 대비 성능을 중시하는 가성비에서 파생되었으며, 가격 대비 심리적인 만족감을 중요시 함.**

## Architecture
![image](https://github.com/ffinal4/back-end/assets/102176567/43dd4fa2-9bb1-4ce6-86ef-8051060ff2db)

## ERD
![image](https://github.com/ffinal4/back-end/assets/102176567/21568fb1-4da8-4b60-ae47-033aca8649d2)

### Teck Stack

- s3 : 2.2.6
- jwt version: 0.11.5
- querydsl-jpa:5.0.0:jakarta
- stomp: 2.3.4
- mySQL: 8.0.28
- Redis : '3.1.2'
- gradle: 8.2.1
- springframework.boot: '3.1.2'
- security: 6.1.2
- java: 17

## 앞으로 진행될 사항들
- 클린 코드를 위한 코드 리팩터링
