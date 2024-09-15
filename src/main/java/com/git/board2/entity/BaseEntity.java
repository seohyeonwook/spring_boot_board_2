package com.git.board2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
// JPA 엔티티 클래스들의 공통 매핑 정보를 제공하는 슈퍼클래스로 사용
@EntityListeners(AuditingEntityListener.class)
// 엔티티의 라이프사이클 이벤트(생성, 수정 등) 시점에 콜백 메서드를
// 호출하도록 지정하는 어노테이션
// 생성 및 수정 날짜를 자동으로 관리
@Getter
public class BaseEntity { // 시간정보만 따로

    @CreationTimestamp // 생성되었을때 시간 만들어주는
    @Column(updatable = false) // 수정시에는 관여안하게끔
    private LocalDateTime createdTime;

    @UpdateTimestamp // 업데이트 되었을때 시간 만들어주는
    @Column(insertable = false) // 입력시에는 관여안하게끔
    private LocalDateTime updatedTime;
}
