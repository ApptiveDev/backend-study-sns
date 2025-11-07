package com.example.devSns.domain; // domain 패키지에 속한다

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity // 이 클래스가 데이터베이스 테이블과 매핑
@Getter // getId() 등 getter 메서드 자동 생성
//@Setter 이거 위험함! 캡슐화 측면에서 주의할 것. - 멘토 조언
@NoArgsConstructor
public class Post { // 별도 이름 지정 안하면 클래스 명 소문자형태가 테이블명으로 사용됨

    @Id // 이 필드가 Primary Key임을 나타냄
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 값 자동 생성 방식 지정
    // 즉 새 게시글 저장할 때 마다 DB가 알아서 id를 1,2,3 순으로 자동 부여함
    private Long id;

    // 내용은 길이가 길 수 있으므로 DB에 TEXT로 지정(멘토 조언)
    @Column(columnDefinition = "TEXT")
    private String title; // 게시글 제목

    @Column(columnDefinition = "TEXT") // 멘토 조언
    private String content; // 게시글 내용

    @OneToMany(mappedBy ="post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    // 생성자
    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // 안전한 업데이트 메서드
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}