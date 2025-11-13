package com.example.devSns.task;

import com.example.devSns.member.Member;
import com.example.devSns.member.MemberRepository;
import com.example.devSns.task.dto.TaskRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void testSaveAndFindTask() {
        // given
        Member member = new Member("tester", "1234", "테스터");
        memberRepository.saveAndFlush(member);

        TaskRequest request = new TaskRequest(
                "할 일 테스트", "설명입니다", LocalDate.now().plusDays(1), 1, Task.Status.TODO,member.getId()
        );

        // when
        Task task = new Task(request, member);
        taskRepository.saveAndFlush(task);

        // then
        Task found = taskRepository.findById(task.getId()).orElseThrow();
        assertThat(found.getTitle()).isEqualTo("할 일 테스트");
        assertThat(found.getMember().getUsername()).isEqualTo("tester");
    }
}
