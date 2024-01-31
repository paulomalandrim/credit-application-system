package br.malandrim.creditapplicationsystem.service.impl

import br.malandrim.creditapplicationsystem.entity.Credit
import br.malandrim.creditapplicationsystem.repository.CreditRepository
import br.malandrim.creditapplicationsystem.service.ICreditService
import org.springframework.stereotype.Service
import java.util.*

@Service
class CreditService(
    private val creditRepository: CreditRepository,
    private val customerService: CustomerService
): ICreditService {
    override fun save(credit: Credit): Credit {
        credit.apply {
            customer = customerService.findById(credit.customer?.id!!)
        }
        return this.creditRepository.save(credit)
    }

    override fun findAllByCustomer(customerId: Long): List<Credit> =
        creditRepository.findAllByCustomer(customerId)

    override fun findByCreditCode(creditCode: UUID, customerId: Long): Credit {
        val credit: Credit = creditRepository.findByCreditCode(creditCode) ?:
                   throw RuntimeException("CreditCode $creditCode not found")
        return if (credit.customer?.id == customerId) credit else throw RuntimeException("Contact admin")

    }

}