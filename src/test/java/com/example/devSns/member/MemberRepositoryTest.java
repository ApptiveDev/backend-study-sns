package com.example.devSns.member;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void testSaveAndFindMember() {
        Member member = new Member("tester", "1234", "테스터");
        memberRepository.save(member);
        memberRepository.flush();


        Optional<Member> found = memberRepository.findByUsername("tester");
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("tester");
    }
}
