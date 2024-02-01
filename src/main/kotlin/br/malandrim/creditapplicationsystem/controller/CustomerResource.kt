package br.malandrim.creditapplicationsystem.controller

import br.malandrim.creditapplicationsystem.dto.CustomerDto
import br.malandrim.creditapplicationsystem.dto.CustomerUpdateDto
import br.malandrim.creditapplicationsystem.dto.CustomerView
import br.malandrim.creditapplicationsystem.service.impl.CustomerService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/customers")
class CustomerResource(
    private val customerService: CustomerService
) {

    @PostMapping
    fun saveCustomer(@RequestBody customerDto: CustomerDto): String {
        val savedCustomer = customerService.save(customerDto.toEntity())
        return "Customer ${savedCustomer.email} saved!"
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): CustomerView = CustomerView(customerService.findById(id))

    @DeleteMapping("{/id}")
    fun deleteById(@PathVariable id: Long) = customerService.delete(id)

    @PatchMapping
    fun updateCustomer(@RequestParam(value = "customId") id: Long,
                       @RequestBody customerUpdateDto: CustomerUpdateDto
    ): CustomerView{
        val customer = customerService.findById(id)
        val customerToUpdate = customerUpdateDto.toEntity(customer)
        val customerUpdated = this.customerService.save(customerToUpdate)
        return CustomerView(customerUpdated)
    }

}