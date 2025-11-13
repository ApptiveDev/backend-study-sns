package com.example.devSns.comment;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TaskRepository taskRepository;

    @Test
    void testSaveAndFindComment() {
        // given
        Member member = new Member("tester", "1234", "테스터");
        memberRepository.saveAndFlush(member);

        TaskRequest request = new TaskRequest("댓글 테스트", "설명", LocalDate.now(), 1, Task.Status.TODO,member.getId());
        Task task = new Task(request, member);
        taskRepository.saveAndFlush(task);

        Comment comment = new Comment("댓글 내용입니다", "테스터", task, member);
        commentRepository.saveAndFlush(comment);

        // when
        List<Comment> comments = commentRepository.findByTask(task);

        // then
        assertThat(comments).hasSize(1);
        assertThat(comments.get(0).getContent()).isEqualTo("댓글 내용입니다");
        assertThat(comments.get(0).getMember().getUsername()).isEqualTo("tester");
    }
}
