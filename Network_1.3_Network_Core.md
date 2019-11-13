### 1.3 Network Core

#### 1. Intro

* network core : Router와 switch들이 복잡하게 연결

* Router & Switch의 목적 : source로부터 destination으로 user의 application 메세지를 전달

* 전달 방식 : circuite swithcing & packet switching

------

<br/>

#### 2. Circuit Switcing

1. 주로 예전 전화 네트워크에 사용됨

2. Call Set-up 과정을 거침 : user message를 보내기 전에 call이 있어야함

   * source로부터 destination까지 경로 선택
   * 경로 상의 자원을 예약 

3. Call Set-up 이후, source 쪽에서 user 메세지를 밀어 넣으며, 마치 Pipe처럼 destination으로 이동

4. **단점** : 네트워크 자원을 분할하는 방식이 필요

   분할하지 않으면, 한 user가 자원 사용 시, 다른 user들은 자원을 사용할 수 없음

   따라서 call이 들어오면 분할된 자원들 중 아직 사용되지 않은 자원을 할당하는 방식이 필요

5. **자원 분할 방식 : FDM vs TDM**

   1) Frequency Division Multiplexing (FDM)

   ​	링크에 가용한 frequency를 몇 개의 대역으로 나눔

   ​	사용자별로 다른 대역을 할당

   ​	각 사용자는 별도의 frequency를 할당 받아서 계속 그걸 잡고 사용할 수 있도록 함

   2) Time Division Multiplexing (TDM)

   ​	링크에 가용한 frequency 전체를 사용할 수 있게 함

   ​	시간을 잘라서 사용자들이 돌아가면서 사용할 수 있도록 함

   <img width="777" alt="스크린샷 2019-11-05 오전 11 37 39" src="https://user-images.githubusercontent.com/45492242/68174241-bb23cc80-ffc0-11e9-9ec9-7f5d83f486b2.png">

6. Circuit Switching은 전화 네트워크에서는 매우 적합

   But! 인터넷과 같은 data 네트워크에서는 매우 불리

   **이유** : 전화와 인터넷에서 발생하는 통신의 차이 때문. 전화는 call을 하고 나면 대화를 안 하고 있는 일이 없음. 끊기 전까지 continuous하게 voice를 전달. But! 컴퓨터 간의 통신은 intermittnet & bursty해서 request가 있으면 data 이동이 왕성하게 이뤄지다가, 한동안 아예 이동이 없음. Circuit Switching처럼 두 개의 communication point 사이의 네트워크 자원을 "예약" 해 버리면, 아무도 이 자원을 공유할 수 없음

   **즉, circuit switching 사용 시, 데이터의 이동이 없을 땐 자원이 낭비됨 = 효율성이 떨어짐**

   ***1960년 대에 packet switching 이라는 새로운 user data 전달 방식이 등장***

