package com.pet.comes.model.Entity;


import com.pet.comes.dto.Req.PinReqDto;
import com.pet.comes.model.IdClass.PinID;
import com.pet.comes.model.Timestamped;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
//@IdClass(PinID.class) // 식별자 클래스 매핑
@EntityListeners(AuditingEntityListener.class) // Auditing : 감시, 자동으로 시간을 매핑하여 DB 테이블에 넣어줌.
@Table(name = "pin")
public class Pin  implements Serializable  {


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long pinId;

    @ManyToOne(fetch = FetchType.LAZY) // OneToOne default FetchType = EAGER
    @JoinColumn(name = "user_id")
    private User user;

    private Long diaryId;

    @CreatedDate
    private LocalDateTime pinedAt;


    public Pin(User user, Long diaryId){
        this.setUser(user);
        this.diaryId = diaryId;
    }
    public void setUser(User user) {
        if(this.user !=null)
            this.user.getPins().remove(this);

        this.user = user;
        if(!user.getPins().contains(this))
            user.setPins(this);
    }
}
