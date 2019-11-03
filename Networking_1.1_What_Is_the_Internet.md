### 1.1 Intro

#### 1. What Is the Internet? - Internet의 구성 요소

**1) Host = End System**

* 인터넷 덩어리의 가장자리
  * 서버, PC, laptop 등

* 사용자 communication application을 실행 하고 있어서 **host**라고 부름
* 네트워크의 가장 자리에 위치하고 있어서 **end system**이라고 부름

**2)Router**

* 네트워크 중앙에 보면, router 혹은 switch라고 부르는 특수한 컴퓨터들이 존재
* router : 사용자의 메세지가 destination을 찾아가게 해 주는 컴퓨터
  * 메세지를 다음 목적지까지 던져주는 역할
  * 실제로, source로부터 destination까지 가기 위해서는 여러 router를 거쳐감

**3) Communication Links**

* router와 router, 혹은 router와 host끼리 연결 시켜주는 물리적 실존 회로



<br/>

**4) Network of Networks** : 인터넷을 부르는 또 다른 이름

* router 와 switch는 flat하게 연결된 것이 아님. 

<img width="418" alt="스크린샷 2019-11-03 오후 11 09 47" src="https://user-images.githubusercontent.com/45492242/68086406-0ccc3a00-fe8f-11e9-9fc8-ca290b30ff2c.png">

​	위의 그림처럼, 덩어리끼리 모여 있음

	>> 그래서 인터넷을 네트워크들의 네트워크라고 부름

**5) Internet Protocol**

* 인터넷을 통해, 메세지를 보내고 받는 것을 제어하는 모든 일련의 규칙들
* 표준화가 매우 중요한 >> 그래야 컴퓨터 간의 통신이 가능
* IETF : Internet Engineering Task Force - Internet protocol 표준화 기관
* RFC : IETF에서 발표하는 표준안들

<br/>

#### 2. What Is a Protocol? - Protocol은 무엇을 정의하는가

***Protocols define <u>formant, order of messages sent and received</u> among network entities, and <u>actions taken</u> on message transmission, receipt.***



