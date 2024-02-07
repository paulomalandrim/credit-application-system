package br.malandrim.creditapplicationsystem.service.impl

import br.malandrim.creditapplicationsystem.entity.Customer
import br.malandrim.creditapplicationsystem.exception.BusinessException
import br.malandrim.creditapplicationsystem.repository.CustomerRepository
import br.malandrim.creditapplicationsystem.service.ICustomerService
import org.springframework.stereotype.Service

@Service
class CustomerService(
    private val customerRepository: CustomerRepository
): ICustomerService{
    override fun save(customer: Customer): Customer = this.customerRepository.save(customer)

    override fun findById(id: Long): Customer = this.customerRepository.findById(id).orElseThrow {
        throw BusinessException("Id $id not found")
    }

    override fun delete(id: Long){
        val customer = findById(id)
        customerRepository.delete(customer)
    }


}