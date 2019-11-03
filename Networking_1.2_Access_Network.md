### 1.2 Access Network

#### Intro 

네트워크 edge에는 user, host, end system 이라고 불리는 device들이 있음

이런 장비들을 인터넷에 연결 시켜주는 것이 access network 

access network를 제공하는 회사들이 있는데, SKT, KT, LG와 같은 통신 회사 혹은 케이블 회사 등이 이에 해당함

------

<br/>

### 1) Characteristics of Access network

1. bandwidth : access network를 특징 짓는 중요한 요소

   = transmission rate : 단위 시간 당 실어 나를 수 있는 bit 수를 의미, Mbps, Gbps 등이 단위

2. Shared Or Dedicated 
   * shared : 여러 사용자가 공유
   * dedicated : 단일 사용
   * EX) 똑같은 100Mbps의 bandwidth를 지닌 network여도, shared이면 내가 사용할 수 있는 양이 적고, dedicated라면 전체를 내가 단독으로 사용할 수 있음

------

<br/>

#### 2) Type 1 : Digital Subscriber Line (DSL)

1. 통신 회사에서 제공하는 access network

<img width="400" alt="스크린샷 2019-11-03 오후 11 21 05" src="https://user-images.githubusercontent.com/45492242/68086681-954bda00-fe91-11e9-8f61-12a83319e316.png">

2. 우리집의 컴퓨터는 통신 회사의 **central office**에 연결

   이 라인을 옆집과 공유 하지 않음 : dedicated

3. 가정 내에 **DSL Modem**이 있음

   PC는 DSL Modem에 연결하고, 전화기는 Modem에 연결된 splitter와 연결함

4. 통신 회사의 cetral office에는 **DSLAM** 이라는 multiplexer가 있음

   voice : telephone network로 연결

   data : ISP로 연결

5. 우리는 인터넷으로부터 data를 upload하기도 하고, download 하기도 함

   일반적으로 download의 양이 훨씬 크기 때문에, upstream보다 downstream에 훨씬 큰 bandwidth를 배정

------

<br/>

#### 3) Type 2 : Cable Network = Hybrid Fiber Coax (HFC)

1. 케이블 회사에서 제공하는 access network

<img width="403" alt="스크린샷 2019-11-03 오후 11 21 20" src="https://user-images.githubusercontent.com/45492242/68086747-55d1bd80-fe92-11e9-87d3-471dfabccc40.png">

2. 우리집의 컴퓨터는 케이블 회사의 **cable headend**에 연결

   다른 가정 (보통은 같은 아파트끼리) 과 라인을 공유 : shared

3. 가정 내에 **Cable Modem**이 있음

   PC는 Cable Modem에 연결하고, TV는 Modem에 연결된 splitter와 연결함

4. 케이블 회사의 cable headend에는 **CMTS**라는 multiplexer가 있음

   data : ISP로 연결

5. 위의 그림은 간단하지만, cable headend에는 CMTS가 여러 개 존재

   이 CMTS 들을 다시 multiplexing >> **hierarchical structure** 

   이 부분은 bandwidth가 훨씬 큰 **fiber**로 연결

   여러 가구를 연결해서 하나의 CMTS로 넣을 때는 **coax cable ** 로 연결 

6. HFC도 DSL처럼 제공하는 bandwidth가 upstream보다 downstream이 훨씬 크고, 전반적으로 DSL보다 큰 bandwidth를 사용함
   * 주의 : 그렇다고 해서 HFC가 DSL보다 훨씬 큰 bandwidth를 "한 가구"에게 배정하는 것은 아님. HFC는 shared이고, DSL은 dedicated이기 때문!

------

<br/>

#### 3) Type 3 : Home Network 

1. 가정 내에서 우리가 컴퓨터 한 대만 사용 하는 것은 아님

   IoT device와 스마트폰 등 다양한 장비가 공존

   즉, 집안 내부에서도 network를 구성

2. **Home network** 중앙에는 **router**가 있음

   집 안의 end system들을 router가 묶어서 위에서 언급한 type1, type2의 방식을 따라 전화 / 케이블 회사에 연결

------

<br/>

#### 4) Type 4 : Ethernet

1. 대학교나 회사는 end system이 가정보다 훨씬 많음

   하나의 router만으로는 효과적인 network를 구성할 수 없음

2. 대학교나 회사에는 **ethernet switch**가 많이 존재

   wifi point는 ethernet switch에 직접 연결

3. 이 많은 ethernet switch들은 대학교나 회사 전체를 연결해주는 **router**에 연결
4. Home Network와 다르게, 전화 / 케이블 회사가 ISP에 연결 시켜주는 것이 아니라, 대학교나 회사, 그 기관을 대표하는 router가 자체적으로 지닌 dedicated line으로 ISP 에 직접 연결

------

<br/>

#### 5) Type 5 : Wireless Access Networks

1. wifi, cellular 등도 access network의 일종, 이런 것들을 wireless access network라고 함

2. Wifi : device가 직접 wifi point에 연결되고, wifi point는 ehternet switch에 연결되고, ethernet switch는 router에 연결

   Home network라면, router는 modem에, 기관이라면 바로 ISP에 연결

   Wide-area wireless netwokr라고도 함

3. Cellular Network  : 3G나 LTE 등
4. 공통점 
   * wireless 
   * shared
5. 차이점
   * Wifi : 주로 실내에서 사용, bandwidth가 굉장히 큼
   * Cellular : 외부에서 사용, bandwidth가 상대적으로 작음

------

<br/>

#### 6) 정리

<img width="657" alt="스크린샷 2019-11-03 오후 11 54 02" src="https://user-images.githubusercontent.com/45492242/68087037-5c156900-fe95-11e9-9206-a6551965f4db.png">

