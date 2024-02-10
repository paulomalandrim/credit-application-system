package br.malandrim.creditapplicationsystem.repository

import br.malandrim.creditapplicationsystem.entity.Credit
import br.malandrim.creditapplicationsystem.entity.Customer
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface CustomerRepository: JpaRepository<Customer, Long>