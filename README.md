# ERD
![image](https://github.com/user-attachments/assets/e2b7f86e-d2dc-45ea-a63f-09bd1dad7d0e)


## 학습지 생성과, 학생의 문제 채점에 대한 다수의 Insert 쿼리 발생

### 문제

학습지 생성과, 학생의 문제 채점에 대해서 현재는 최대 50개의 문제에 대해서 insert를 진행했지만 
만약 학습지의 문제가 많아지면 insert 쿼리가 빈번하게 발생함

1. 동시요청이 많아 질 경우 DB 커넥션이 고갈될수도있음
2. DB와 네트워크 통신 비용이 증가함
3. DB의 서버가 부하될수있음 
4. 트랜잭션일 경우, 각 Insert에 대한 상태관리를 해야되므로 메모리 오버헤드 증가


### 해결

 - Batch Insert 적용

- DB 서버 부하 감소
- 네트워크 통신 비용 감소
- 단일 트랜잭션으로 처리되어 오버헤드 감소
- 커넥션 절약

ex) 예상 처리 시간     
단건 처리: 100ms * 100개 = 10초    
배치 처리: 200ms * 2회 = 0.4초


### 적용

batch insert vs 단일 insert 

비교테스트 (300개 문제 수 채점 결과) 

- 배치insert 적용 전 → 359ms
- 배치insert 적용 후 → 167 ms

### batch insert의 문제

JPA는 각 엔터티를 영속화할 때 Id가 필요함 그러므로 id 생성전략을 

`@GeneratedValue(strategy = GenerationType.*IDENTITY*)`으로 둘 경우 
데이터베이스를 갔다와야 id를 알수 있으므로 batch insert 불가능 

 SEQUENCE 전략을 사용하면 해결됨 ,

 DB의 sequence 객체로부터 값을 가져와서 애플리케이션 레벨에서 Id를 할당하여 batch insert 작업
`@GeneratedValue(strategy = GenerationType.*SEQUENCE***,** generator = "id_sequence")`

하지만 해당 프로젝트에서는 영속성 관리를 포기하고 `GenerationType.*IDENTITY` 으로 id를 생성 후*  

jdbc를 사용하여 batch insert로 해결

그럼 영속성을 포기해야되는건가?

→ 해당 프로젝트에서는 batch insert,update 후 바로 다시 조회 함 그럼 영속관리 확보







## 통계 분석에서 실시간 계산으로 인한 성능저하

### 문제

통계데이터에서, user와 problem이 많아질수록 계산식 증가로 인한 성능저하
- 학습지의 유저별 정답률 실행계획 분석

```bash
-> Group aggregate: count(0), sum((case when (ua.mark_result_type = 'PASS') then 1 else 0 end))  (cost=339 rows=8) (actual time=0.6..1.83 rows=6 loops=1)
    -> Nested loop inner join  (cost=172 rows=1675) (actual time=0.328..1.36 rows=1800 loops=1)
        -> Index scan on u using PRIMARY  (cost=1.05 rows=8) (actual time=0.229..0.233 rows=8 loops=1)
        -> Covering index lookup on ua using idx_user_answer_piece_user_mark (piece_id=5, user_id=u.id)  (cost=3.04 rows=209) (actual time=0.0374..0.122 rows=225 loops=8)

```

user에 대한 인덱스 스캔을 하고있지만, 학습지의 user에 대한 수가 늘어나면 기하급수적으로 증가함

결과적으로 user수만큼 다시 *n 번 루프를 돌게됨

- 학습지의 문제별 정답률 실행계획 분석

```bash
-> Table scan on <temporary>  (actual time=9.36..9.55 rows=300 loops=1)
    -> Aggregate using temporary table  (actual time=9.35..9.35 rows=300 loops=1)
        -> Nested loop inner join  (cost=413 rows=1005) (actual time=0.795..5.64 rows=1800 loops=1)
            -> Nested loop inner join  (cost=61.7 rows=300) (actual time=0.641..1.51 rows=300 loops=1)
                -> Table scan on uc  (cost=3.95 rows=37) (actual time=0.378..0.405 rows=37 loops=1)
                -> Index lookup on p using idx_problem_unit_code (unit_code=uc.unit_code)  (cost=0.772 rows=8.11) (actual time=0.0213..0.029 rows=8.11 loops=37)
            -> Index lookup on ua using idx_user_answer_piece_problem (piece_id=5, problem_id=p.id)  (cost=0.839 rows=3.35) (actual time=0.00932..0.0132 rows=6 loops=300)

```

problem과 user의 수가 많아지고있으면 임시테이블에 대한 메모리가 커짐,

두번의 중첩 루프를 돌게되는데 unitCode로 인한 중첩문과  problem과의 중첩문으로 1800개 행 임시테이블에 저장됨  → 현재 학습지의 문제를 푼 user가 6명이게되지만 user수와 problem 이 많아 질수록 메모리도 커짐

### 해결

실시간 계산이 아닌 통계테이블에 update와 insert 실행 

학습지 통계 테이블 /  학습지의 문제 통계테이블

**배치작업으로 주기적 계산 후 통계테이블에 update**
  
  장점 : 
  
  - 조회 시 성능 저하 감소
  - 최신데이터를 계속 가져와서 계산하므로 동시성이슈 없음
  
  단점 : 
  
  - 배치 작업을 비활성 시간에 실행하면 리소스 낭비
  - 데이터가 많을 시 배치작업 시간 늘어남 (chunk단위 분할배치로 해결)
  - 실시간 분석을 원하는 경우 적합하지않음

**userAnswer(채점) 시 비동기로 통계테이블에 update**
  
  장점: 
  
  - 조회 시 성능 저하 감소
  - 이벤트 기반이므로 비활성화 시간에는 리소스 사용이 없음
  
  단점:
  
  - 통계 조회보다 채점에 대한 insert가 더 많아지면 통계테이블에 insert의 수만큼 update 해줘야하므로 DB 부하발생
  - 통계테이블에 update시 동시성 이슈 발생 
  ( redis의 분산락으로 해결→  rock에 대한 이벤트를 받으려면 pub/sub 사용 → 하지만 구독자가 여러명 받게되면 다시 rock 경합상황 발생  → redisStream으로 순차적으로 소비자가 처리가능하게끔)



