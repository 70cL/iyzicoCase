package com.iyzico.challenge.entity;

import lombok.*;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@MappedSuperclass
public abstract class AbstractEntityBase {

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate = new Date();
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    private Boolean deleteFlag = false;
    @Version
    private Integer version;

    @PreUpdate
    private void onUpdate(){
        setUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
    }
}
