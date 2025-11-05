//DB에 저장·조회·삭제 등을 대신 처리

package com.example.devSns.task;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> { }