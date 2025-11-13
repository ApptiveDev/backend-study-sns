package com.example.devSns.like;

import com.example.devSns.comment.Comment;
import com.example.devSns.comment.CommentRepository;
import com.example.devSns.member.Member;
import com.example.devSns.member.MemberRepository;
import com.example.devSns.task.Task;
import com.example.devSns.task.TaskRepository;
import com.example.devSns.task.dto.TaskRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class LikeRepositoryTest {

    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TaskRepository taskRepository;

    @Test
    void testLikeComment() {
        // given
        Member member = new Member("tester", "1234", "테스터");
        memberRepository.saveAndFlush(member);

        TaskRequest request = new TaskRequest("좋아요 테스트", "설명", LocalDate.now(), 1, Task.Status.TODO,member.getId());
        Task task = new Task(request, member);
        taskRepository.saveAndFlush(task);

        Comment comment = new Comment("테스트 댓글", "테스터", task, member);
        commentRepository.saveAndFlush(comment);

        Like like = new Like(member, comment);
        likeRepository.saveAndFlush(like);

        // when
        Like found = likeRepository.findById(like.getId()).orElseThrow();

        // then
        assertThat(found.getMember().getUsername()).isEqualTo("tester");
        assertThat(found.getComment().getContent()).isEqualTo("테스트 댓글");
    }
}
