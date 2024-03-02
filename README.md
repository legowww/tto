# 서론
리팩토링 과정은 두 개의 챕터로 구성했습니다.

1. 도메인을 중심으로 실행 가능한 어플리케이션 단위로 분리
2. (나갈시간 도메인) 추상화와 의존성 역전을 중심으로 멀티모듈 구조 설계


# 도메인 중심으로 실행 가능한 어플리케이션 분리

## 도메인 설명

기존 '나갈시간' 프로젝트는 유저와 나갈시간 두 개의 도메인으로 구성된 프로젝트입니다. 

1️⃣ 유저 도메인
- 도메인 객체
    - 사용자
    - 인증 토큰
- 비즈니스 로직
    - 회원가입: 사용자 정보를 기반으로 새로운 계정을 생성합니다.
    - 로그인: 사용자 계정 정보를 인증하여 로그인을 처리합니다.
    - 경로 즐겨찾기: 사용자가 자주 이용하는 경로를 저장하고 관리합니다.

2️⃣ 나갈시간 도메인
- 도메인 객체
    - 교통수단 (하위 항목으로 버스, 지하철, 도보)
    - 경로
- 비즈니스 로직
    - 경로 검색: 출발지부터 도착지까지의 경로를 검색합니다 (외부 API 사용)
    - 버스도착시간 검색: 가장 먼저 도착하는 버스의 시간을 검색합니다 (외부 API 사용)
    - 지하철도착시간 검색: 가장 빠른 지하철 도착 시간을 검색합니다 (외부 API 사용)
    - 나갈시간 계산: 외부 API의 결과를 토대로 각 대중교통 수단 별 보정 시간 연산을 수행하고, 버스의 경우 동일 버스 최대 N개까지 검색합니다.

## 문제점

**[불필요한 의존성]**
- 나갈시간 도메인의 경우 Data Access 계층이 필요없는 도메인입니다. 해당 도메인은 마치 함수처럼 경로값을 입력으로 받고, 출력으로 나갈시간을 반환해주는 역할만을 수행합니다. 즉, 나갈시간 도메인 입장에서는 User 영속화에 사용되는 `spring-data-jpa` 같은 의존성이 필요하지 않습니다. 하지만 기존 프로젝트처럼 두 도메인이 하나로 묶인 상황에서는 불필요한 의존성을 가지는 문제점이 발생합니다. 

- 두 도메인은 서로 다른 관심사를 가집니다. 관심사를 분리하여 코드를 작성하게 되면, 독립된 기능에 집중할 수 있기 때문에 코드를 파악하는데 수월하며 기능을 변경하거나 추가할 때도 그 부분만 교체하면 되기 때문에 훨씬 간단하게 문제를 해결할 수 있습니다.

결론적으로, 코드 응집도와 의존성 관리 측면에서 독립적으로 실행 가능한 어플리케이션으로 분리하기로 결정했습니다.

# (나갈시간 도메인) 추상화와 의존성역전을 중심으로 멀티모듈 구조 설계

## 추상화
기존의 프로젝트는 도메인이 현재 사용하고 있는 외부 API에 강하게 의존하는 방식으로 코드를 작성했습니다. 이 경우 인프라(외부 API)가 변경됐을 때 비지니스 로직도 변경해야 되는 문제가 발생합니다. 프로젝트를 개발하던 당시에는 `외부 API마다 요청값과 응답값이 상이하기때문에 어쩔 수 없는 부분 아닌가?` 라는 생각으로 개발을 했던 기억이 있습니다.

하지만 이번 리팩토링을 수행하며 가장 중점적으로 생각했던 부분은 `외부API를 변경하거나 추가하더라도 기존 코드를 유지할 수 있는 설계`였습니다. 이를 실현하기 위해서는 도메인과 인프라에 강한 의존성을 제거해야했습니다. 객체지향원칙관점으로 바라봤을 때, 추상화를 통해 사물의 본질만을 가진 객체를 활용하면 도메인과 인프라의 의존성을 낮출 수 있다는 생각이 들었습니다. 

버스의 도착 시간을 검색하는 다양한 API가 존재했지만, 모든 API가 공통적으로 사용하는 값은 '버스 정류장 ID'와 '노선 ID'입니다. 이 값은 국가에서 설정한 고유 번호로, 해당 API의 핵심이라고 볼 수 있습니다.
이 외의 파라미터들은 세부 사항으로 볼 수 있으며, 이들은 필수적이지 않다고 판단했습니다. 따라서, 불필요한 세부 사항들을 제거하고, API의 본질적인 부분만을 남기는 방식으로 추상화를 진행하였습니다.
이와 같은 추상화 방식은 경로 검색 API와 지하철도착시간 검색 API에 대해서도 동일하게 적용했습니다. 추상화는 자바의 interface를 사용해 나타냈습니다. 

```java
//버스도착시간 검색기 추상화
public interface BusArrivalTimeSearcher {
    List<LocalDateTime> execute(String bstopId, String routeId); // 버스정류장ID과 노선ID
}

//지하철도착시간 검색기 추상화
public interface SubwayArrivalTimeService {
    List<LocalDateTime> execute(String stationId, String wayCode); // 역ID, 상행,하행 코드
}

//경로 검색기 추상화
public interface RouteSearcher {
    List<Route> execute(LocationCoordinate locationCoordinate); //출발지와 목적지의 위경도를 가지는 객체 LocationCoordinate
}
```

## 의존성 역전
어플리케이션 실행, 도메인, 경로 검색, 버스도착시간 검색, 지하철도착시간 검색을 수행하는 각각의 모듈 5개를 생성했습니다. 멀티모듈을 사용한 이유는 앞서 말했던 **관심사의 분리**와 **의존성 관리** 측면과 동일합니다.

- settings.gradle
```java
rootProject.name = 'tto'
include 'tto-api'                //서비스 실행에 필요한 모듈을 선택하고 연결하며, 실행가능한 어플리케이션이 존재하는 모듈
include 'tto-domain'             //나갈시간 계산만을 담당하는 도메인 모듈
include 'api-modules:route-api'  //경로검색 기능만을 수행하는 모듈
include 'api-modules:bus-api'    //버스검색 기능만을 수행하는 모듈
include 'api-modules:subway-api' //지하철검색 기능만을 수행하는 모듈
```


## tto-domain
해당 모듈은 POJO를 지향하며, 클린 아키텍처 속 도메인처럼 항상 고수준의 의존성을 가지도록 설계했습니다. 그러므로 도메인은 외부 인프라에 대해 모릅니다. 빈으로 등록하기 위한 컨텍스트와 JSON 라이브러리만 정도만 추가했습니다. 도메인을 고수준의 의존성을 가지도록 설계한 이유는 후에 서술하겠지만 의존성 역전 원칙을 적용하여 인프라에 의존적이지 않은 도메인을 중심으로 프로젝트를 구성했기 때문입니다.
```java
dependencies {
    implementation 'org.springframework:spring-context'
    implementation 'com.fasterxml.jackson.core:jackson-databind:'
}
```

POJO로 구성했고, 추상화를 통해  
비지니스 로직 테스트를 진행하는 과정에서 역할을 수행하는 `외부API Mock` 객체를 ----- 예시 첨부하면 좋을듯 

## api-modules
다양한 기술로 외부 API와 HTTP 통신하도록 상황 설정을 했습니다. 통신 기술로는 RestClient 와 OpenFeign 을 사용했습니다. 각 모듈의 의존성 설정입니다.

```java
//api-modules:bus-api 
dependencies {
	implementation 'org.springframework.cloud:spring-cloud-starter-openfeign' //OpenFeign
}

//api-modules:subway-api
dependencies {
    implementation 'org.springframework.cloud:spring-cloud-starter-openfeign' //OpenFeign
    implementation group: 'com.googlecode.json-simple', name: 'json-simple', version: '1.1.1'
}

//api-modules:route-api
dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-web' // RestClient
}

```

`tto-api` 모듈 입장에서는 버스도착시간 검색을 알기 위해 `api-modules:bus-api` 모듈에 등록된 스프링빈을 사용할 뿐이며, 어떤 외부 API를 사용하고 어떠한 HTTP통신 모듈을 사용하는지는 모르는 구조입니다.
