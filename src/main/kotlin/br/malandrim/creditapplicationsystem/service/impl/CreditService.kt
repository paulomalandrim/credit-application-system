package br.malandrim.creditapplicationsystem.service.impl

import br.malandrim.creditapplicationsystem.entity.Credit
import br.malandrim.creditapplicationsystem.exception.BusinessException
import br.malandrim.creditapplicationsystem.repository.CreditRepository
import br.malandrim.creditapplicationsystem.service.ICreditService
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Service
class CreditService(
    private val creditRepository: CreditRepository,
    private val customerService: CustomerService
): ICreditService {
    override fun save(credit: Credit): Credit {
        val localDateTime = LocalDate.now().plusMonths(3)

        if (credit.dayFirstInstallment > localDateTime)
            throw BusinessException("Day of first installment must be within 3 month")

        credit.apply {
            customer = customerService.findById(credit.customer?.id!!)
        }
        return this.creditRepository.save(credit)
        //
    }

    override fun findAllByCustomer(customerId: Long): List<Credit> =
        creditRepository.findAllByCustomer(customerId)

    override fun findByCreditCode(creditCode: UUID, customerId: Long): Credit {
        val credit: Credit = creditRepository.findByCreditCode(creditCode) ?:
                   throw BusinessException("CreditCode $creditCode not found")
        return if (credit.customer?.id == customerId) credit else throw IllegalArgumentException("Contact admin")

    }

}