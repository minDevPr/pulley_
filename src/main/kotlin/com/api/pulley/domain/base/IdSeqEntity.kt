package com.api.pulley.domain.base

import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.SequenceGenerator

@MappedSuperclass
abstract class IdSeqEntity {
    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "id_sequence"
    )
    @SequenceGenerator(
        name = "id_sequence",
        sequenceName = "id_seq",
        allocationSize = 50,
        initialValue = 1
    )
    var id: Long? = null
        protected set

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as IdEntity
        return id != null && other.id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}