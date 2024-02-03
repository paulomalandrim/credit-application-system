package br.malandrim.creditapplicationsystem.dto

import br.malandrim.creditapplicationsystem.entity.Address
import br.malandrim.creditapplicationsystem.entity.Customer
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.br.CPF
import java.math.BigDecimal

data class CustomerDto(
    @field:NotEmpty(message = "Invalid firstName") val firstName: String,
    @field:NotEmpty(message = "Invalid lastName") val lastName: String,
    @field:CPF(message = "Invalid cpf") val cpf: String,
    @field:NotNull(message = "Invalid income") val income: BigDecimal,
    @field:Email(message = "Invalid email") val email: String,
    @field:NotEmpty(message = "Invalid password") val password: String,
    @field:NotEmpty(message = "Invalid zipcode") val zipCode: String,
    @field:NotEmpty(message = "Invalid street") val street: String
){
    fun toEntity(): Customer = Customer(
        firstName = this.firstName,
        lastName = this.lastName,
        cpf = this.cpf,
        income = this.income,
        email = this.email,
        password = this.password,
        address = Address(
            zipCode = this.zipCode,
            street = this.street
        )
    )
}