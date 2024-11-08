package com.api.pulley.domain.base

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class IdAuditEntity : IdEntity() {
    @CreatedDate
    @Column(updatable = false)
    var createdAt: Long? = null
        protected set

    @LastModifiedDate
    var lastModifiedAt: Long? = null
        protected set
}
