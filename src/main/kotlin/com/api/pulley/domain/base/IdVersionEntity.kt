package com.api.pulley.domain.base

import jakarta.persistence.MappedSuperclass
import jakarta.persistence.Version

@MappedSuperclass
abstract class IdVersionEntity : IdEntity() {
    @Version
    var version: Long? = null
        protected set
}
