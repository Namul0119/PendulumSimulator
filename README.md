# PendulumSimulator

Java로 구현한 인터랙티브 **진자 시뮬레이터 + 분석 대시보드** 프로젝트입니다.  
간단한 물리 공식으로 진자의 운동을 시뮬레이션하며, 실시간 **각속도 그래프**, **에너지 변화**, **속도 상태바**, **미니 그래프**, **다크/클래식 테마 전환**까지 시각적으로 구성했습니다.

## 주요 기능

### 1. 물리 시뮬레이션
- 진자의 실시간 움직임 (중력, 감쇠, 줄 길이, 초기 각도 등 반영)
- 마우스로 초기 각도 설정 가능

### 2. 실시간 분석 대시보드
- **각속도 그래프 (rad/s)**
- **운동/위치/총 에너지 변화 시각화**
- **속도 상태 색상바**
- **미니 총 에너지 그래프**
- 에너지 보존율, 주기/진동수 실시간 계산

### 3. 사용자 인터랙션
- 줄 길이 / 기준점 위치 / 애니메이션 속도 슬라이더
- 그래프 색상 직접 선택
- CSV 저장 버튼 (시뮬레이션 로그)

### 4. 테마 전환 지원
- 다크 모드 / 클래식 / 의료 / 고대비 / 기본 테마 순환 전환

## 실행 화면
시연 영상
https://github.com/user-attachments/assets/69c8b7d2-ce02-4ef9-a8e8-f871973b838c
![screen1](https://github.com/user-attachments/assets/46488ff3-70f6-4da4-b70f-68decca229e6)
![Graph2](https://github.com/user-attachments/assets/effef70d-e8d2-48dd-9a0e-c598a4e52bfb)
![Graph1](https://github.com/user-attachments/assets/a997c6cc-c4d2-44ab-88d8-cd371fe02b63)
![Theme1](https://github.com/user-attachments/assets/9527f662-6103-4e52-959f-003d3958dd53)
![Theme2](https://github.com/user-attachments/assets/2cb2d1bb-61ac-4b5d-8550-1d49c7f2925a)
![ButtonUI](https://github.com/user-attachments/assets/b47e1127-3654-4df6-9836-5a8653521ada)

## 실행 방법

1. Java 11 이상이 설치된 환경에서 Eclipse 또는 IntelliJ로 실행
2. `PendulumWithDashboard.java` 실행
3. 시뮬레이터가 GUI로 뜨며 상호작용 가능

## 사용 기술

- Java Swing GUI
- 실시간 그래픽 렌더링 (Graphics 객체)
- 타이머 기반 애니메이션
- 기본 물리 수식 구현
- CSV 파일 출력

## 파일 구성

src/
└── exercise01/
└── PendulumWithDashboard.java // 메인 GUI 및 시뮬레이션 실행
README.md

## 제작자

- Namul
- Software Engineering 전공  
- GitHub: [github.com/Namu10119](https://github.com/Namu10119)

---
