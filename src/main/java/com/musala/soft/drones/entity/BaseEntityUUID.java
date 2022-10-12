package com.musala.soft.drones.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntityUUID implements Serializable {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    @JsonIgnore
    @Version
    @Column(name = "version_no")
    private Long versionNo;

    @JsonIgnore
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @JsonIgnore
    @LastModifiedDate
    @Column(name = "updated_at")
    private Date updatedAt;

    @JsonIgnore
    @Column(name = "is_active")
    private Boolean isActive = Boolean.TRUE;

    @PrePersist
    protected void onCreate() {
        updatedAt = createdAt = new Date();
        versionNo = 0L;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
        versionNo++;
    }
}
