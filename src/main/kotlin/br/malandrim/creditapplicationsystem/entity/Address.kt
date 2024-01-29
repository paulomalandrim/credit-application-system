package br.malandrim.creditapplicationsystem.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.Embedded

@Embeddable
data class Address (
    @Column(nullable = false)
    var zipCode: String = "",

    @Column(nullable = false)
    var street: String = ""
)
