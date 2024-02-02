package br.malandrim.creditapplicationsystem.controller

import br.malandrim.creditapplicationsystem.dto.CreditDto
import br.malandrim.creditapplicationsystem.dto.CreditViewList
import br.malandrim.creditapplicationsystem.entity.Credit
import br.malandrim.creditapplicationsystem.entity.Customer
import br.malandrim.creditapplicationsystem.repository.CreditRepository
import br.malandrim.creditapplicationsystem.service.impl.CreditService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.Mapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.stream.Collector
import java.util.stream.Collectors

@RestController
@RequestMapping("/api/v1/credit")
class CreditResource(
    private val creditService: CreditService
) {

    @PostMapping
    fun saveCredit(@RequestBody creditDto: CreditDto): String{
        val credit = creditService.save(creditDto.toEntity())
        return "Credit ${credit.creditCode} - Customer ${credit.customer?.firstName} saved!"
    }

    @GetMapping
    fun findAllByCustomerId(@RequestParam(value = "customerId") customerId: Long):
            List<CreditViewList> {

       // Solução dada no curso
//        return creditService.findAllByCustomer(customerId).stream().map {
//            credit: Credit -> CreditViewList(credit)
//        }.collect(Collectors.toList())

        // Minha Solução
        val credits = creditService.findAllByCustomer(customerId)
        val mutableCreditViewList: MutableList<CreditViewList> = mutableListOf()
        for (c in credits){
                mutableCreditViewList.add(CreditViewList(c))
        }
        return mutableCreditViewList.toList()
    }


}