package br.malandrim.creditapplicationsystem.service

import br.malandrim.creditapplicationsystem.entity.Address
import br.malandrim.creditapplicationsystem.entity.Credit
import br.malandrim.creditapplicationsystem.entity.Customer
import br.malandrim.creditapplicationsystem.enummeration.Status
import br.malandrim.creditapplicationsystem.repository.CreditRepository
import br.malandrim.creditapplicationsystem.repository.CustomerRepository
import br.malandrim.creditapplicationsystem.service.impl.CreditService
import br.malandrim.creditapplicationsystem.service.impl.CustomerService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Month
import java.util.*
import kotlin.random.Random


//@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CreditServiceTest {

    @MockK
    lateinit var customerRepository: CustomerRepository
    @MockK
    lateinit var customerService: CustomerService
    @MockK
    lateinit var creditRepository: CreditRepository
    @InjectMockKs
    lateinit var creditService: CreditService

    @Test
    fun `should create credit`() {
        // given
        val fakeCustomer: Customer = buildCustomer()
        val fakeCredit: Credit = buildCredit(customer = fakeCustomer)
        every { customerService.findById(any()) } returns fakeCustomer
        every { creditRepository.save(any()) } returns fakeCredit
        //when
        val actual: Credit = creditService.save(fakeCredit)
        //then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(fakeCredit)
        verify(exactly = 1) { creditRepository.save(fakeCredit) }
    }

    @Test
    fun `should find all by customers`(){
        // given
        val fakeCustomerId: Long = Random.nextLong()
        val fakeCustomer: Customer = buildCustomer(id = fakeCustomerId)
        val fakeCredit1: Credit = buildCredit(customer = fakeCustomer)
        val fakeCredit2: Credit = buildCredit(customer = fakeCustomer)
        val fakeCreditList: List<Credit> = listOf(fakeCredit1, fakeCredit2)
        every { customerService.findById(any()) } returns fakeCustomer
        every { creditRepository.findAllByCustomer(any()) } returns fakeCreditList
        //when
        val actual: List<Credit> = creditService.findAllByCustomer(fakeCustomerId)
        //then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(fakeCreditList)
        verify(exactly = 1) { creditRepository.findAllByCustomer(fakeCustomerId) }
    }

    @Test
    fun `should find by credit code`(){
        // given
        val fakeCustomerId: Long = Random.nextLong()
        val fakeCustomer: Customer = buildCustomer(id = fakeCustomerId)
        val fakeCredit1: Credit = buildCredit(customer = fakeCustomer)
        val fakeCredit2: Credit = buildCredit(customer = fakeCustomer)
        val fakeCreditList: List<Credit> = listOf(fakeCredit1, fakeCredit2)
        every { customerService.findById(any()) } returns fakeCustomer
        every { creditRepository.findAllByCustomer(any()) } returns fakeCreditList
        //when
        val actual: List<Credit> = creditService.findAllByCustomer(fakeCustomerId)
        //then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(fakeCreditList)
        verify(exactly = 1) { creditRepository.findAllByCustomer(fakeCustomerId) }
    }



    fun buildCredit(
        creditValue: BigDecimal = BigDecimal.valueOf(500.0),
        dayFirstInstallment: LocalDate = LocalDate.of(2024, Month.MARCH, 20),
        numberOfInstallments: Int = 5,
        customer: Customer,
        id: Long? = 1L
    ) = Credit(
        creditValue = creditValue,
        dayFirstInstallment = dayFirstInstallment,
        numberOfInstallments = numberOfInstallments,
        customer = customer,
        id = id
    )

    fun buildCustomer(
        firstName: String = "Paulo",
        lastName: String = "Malandrim",
        cpf: String = "33920373898",
        email: String = "paulo.malandrim@gmail.com",
        password: String = "1234",
        zipCode: String = "123231",
        street: String = "Rua do meio",
        income: BigDecimal = BigDecimal(5000.0),
        id: Long = 1L
    ) = Customer(
        firstName = firstName,
        lastName = lastName,
        cpf = cpf,
        email = email,
        password = password,
        address = Address(
            zipCode = zipCode,
            street = street,
        ),
        income = income,
        id = id
    )

}