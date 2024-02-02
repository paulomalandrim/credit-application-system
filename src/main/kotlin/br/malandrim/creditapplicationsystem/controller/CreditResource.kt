package br.malandrim.creditapplicationsystem.controller

import br.malandrim.creditapplicationsystem.dto.CreditDto
import br.malandrim.creditapplicationsystem.dto.CreditView
import br.malandrim.creditapplicationsystem.dto.CreditViewList
import br.malandrim.creditapplicationsystem.entity.Credit
import br.malandrim.creditapplicationsystem.entity.Customer
import br.malandrim.creditapplicationsystem.repository.CreditRepository
import br.malandrim.creditapplicationsystem.service.impl.CreditService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.stream.Collector
import java.util.stream.Collectors

@RestController
@RequestMapping("/api/v1/credit")
class CreditResource(
    private val creditService: CreditService
) {

    @PostMapping
    fun saveCredit(@RequestBody creditDto: CreditDto): ResponseEntity<String>{
        val credit = creditService.save(creditDto.toEntity())
        return ResponseEntity.status(HttpStatus.CREATED)
            .body("Credit ${credit.creditCode} - Customer ${credit.customer?.firstName} saved!")
    }

    @GetMapping
    fun findAllByCustomerId(@RequestParam(value = "customerId") customerId: Long):
            ResponseEntity<List<CreditViewList>> {

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
        return ResponseEntity.status(HttpStatus.OK)
            .body(mutableCreditViewList.toList())
    }

    @GetMapping("/{creditCode}")
    fun findByCreditCode(@RequestParam(value = "customerId") customerId: Long,
                         @PathVariable creditCode: UUID): ResponseEntity<CreditView> {
        val credit: Credit = creditService.findByCreditCode(creditCode, customerId)
        val creditView = CreditView(credit)
        return ResponseEntity.status(HttpStatus.OK)
            .body(creditView)
    }


}