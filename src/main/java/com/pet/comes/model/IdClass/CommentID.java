package com.pet.comes.model.IdClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data // equals, hashCode 구현
@NoArgsConstructor // 기본 생성자 필요함.
@AllArgsConstructor
public class CommentID implements Serializable { // 식별자 클래스는 public이고 Serializable을 상속
    // Comment 테이블이 복합키를 가지고 있어서 만들어준 클래스
    private Long userId; // 식별자 클래스(CommentID)의 변수명과 엔티티(Comment)에서 사용되는 변수명이 동일
    private Long diaryId;
}
