package br.malandrim.creditapplicationsystem.controller

import br.malandrim.creditapplicationsystem.dto.CustomerDto
import br.malandrim.creditapplicationsystem.service.impl.CustomerService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/customers")
class CustomerResource(
    private val customerService: CustomerService
) {

    @PostMapping
    fun saveCustomer(@RequestBody customerDto: CustomerDto): String{
        val savedCustomer = customerService.save(customerDto.toEntity())
        return "Customer ${savedCustomer.email} saved!"
    }
}