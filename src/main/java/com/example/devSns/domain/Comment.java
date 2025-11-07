package com.example.devSns.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Table(name = "comments")
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false) // 댓글 내용 (필수!)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    // 대댓글 (부모)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    // 대댓글 (자식)
    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    // --- 생성자 ---
    public Comment(Post post, Comment parent, String content) {
        this.post = post;
        this.parent = parent;
        this.content = content;
    }

    // --- 업데이트 메서드 ---
    public void update(String content) {
        this.content = content;
    }
}
// 데이터 모델

// Commnet (comment_id, post_id )
// 연관관계 매핑

// 외래키 기본키 개념
// 자동차랑 , 바퀴

// 자동차 바퀴 외래키
// 자동차 (자동차 ID , 이름, 년식, 번호판, 수리 여부, 가격)
// 1 그랜저 , 21년,  999 , true, 10000
// 2 스타렉스 , 24년


// 바퀴에 외래키
// 바퀴( 바퀴 id, 자동차 id(fk), 바퀴이름, 색깔 , 수리여부  )
// 1 , 1, 1번 바퀴, 노란색 , false
// 2 , 1, 2번 바퀴, 빨간색 , true
// 3 , 2, 3번 바퀴, 초록색 , false
// 4 , 1, 4번 바퀴, 노란색 , true

// 테이블은 양뱡향 참조
// 객체는 단방향 참조

// 테이블에는 join inner join
// select * from wheel w join car c Using(car_id)
//                                  on(w.car_id == c.car_id )
// 두개가 같은 의미
// 객체는 단방향 참조야
// 댓글(id, post)
// 1, post


// 정규화 1정규화만 DB의 컬럼이 원자값을 가져야 함.

// 댓글은 게시글 밑에 달리지, 댓글 또 댓글 이 달려
//  List<Comment> commentList = new ArrayList<>();
//  Post post; 객체
//--------- 이 중간단게 JPA
// 테이블 실제로 저장되는 정보는 객체 단위 x PK를 아이디의 형태로 ID
// 댓글 (내용, 작성자 )
