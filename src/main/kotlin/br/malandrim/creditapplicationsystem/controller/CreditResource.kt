package br.malandrim.creditapplicationsystem.controller

import br.malandrim.creditapplicationsystem.dto.CreditDto
import br.malandrim.creditapplicationsystem.repository.CreditRepository
import br.malandrim.creditapplicationsystem.service.impl.CreditService
import org.springframework.web.bind.annotation.Mapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
//    fun findCreditById(){
//
//    }


}