package br.malandrim.creditapplicationsystem.dto

import br.malandrim.creditapplicationsystem.entity.Credit
import br.malandrim.creditapplicationsystem.entity.Customer
import jakarta.validation.constraints.Future
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.LocalDate

class CreditDto(
    @field:NotNull(message = "Invalid creditValue") val creditValue: BigDecimal,
    @field:Future(message = "Date must be in future") val dayFirstOfInstallment: LocalDate,
    @field:Max(value = 48, message = "numberOfInstallment is up to 48") val numberOfInstallment: Int,
    @field:NotNull(message = "Invalid customerId") val customerId: Long
) {

    fun toEntity(): Credit = Credit(
        creditValue = this.creditValue,
        dayFirstInstallment = this.dayFirstOfInstallment,
        numberOfInstallments = this.numberOfInstallment,
        customer = Customer(id = this.customerId)
    )
}
