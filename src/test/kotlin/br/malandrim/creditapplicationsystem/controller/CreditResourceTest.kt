package br.malandrim.creditapplicationsystem.controller

import br.malandrim.creditapplicationsystem.dto.CreditDto
import br.malandrim.creditapplicationsystem.dto.CustomerDto
import br.malandrim.creditapplicationsystem.dto.CustomerUpdateDto
import br.malandrim.creditapplicationsystem.entity.Credit
import br.malandrim.creditapplicationsystem.entity.Customer
import br.malandrim.creditapplicationsystem.enummeration.Status
import br.malandrim.creditapplicationsystem.repository.CreditRepository
import br.malandrim.creditapplicationsystem.repository.CustomerRepository
import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.Matcher
import org.hamcrest.Matchers
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
class CreditResourceTest {
    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var creditRepository: CreditRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    companion object {
        const val URL: String = "/api/v1/credits"
    }

    @BeforeEach
    fun setup() {
        creditRepository.deleteAll()
        customerRepository.deleteAll()
        customerRepository.save(builderCustomerDto().toEntity())
    }

    @AfterEach
    fun tearDown() {
        creditRepository.deleteAll()
        customerRepository.deleteAll()
    }

    @Test
    fun `should create a credit and return 201 status`() {
        //given
        val dayFirstOfInstallment: LocalDate = LocalDate.now().plusMonths(1)
        val customerDto: CreditDto = builderCreditDto(dayFirstOfInstallment = dayFirstOfInstallment)
        val valueAsString: String = objectMapper.writeValueAsString(customerDto)
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditValue").value("400.0"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfInstallment").value("5"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(Status.IN_PROGRESS.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.emailCustomer").value("paulo.malandrim@gmail.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.incomeCustomer").value("1000.0"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not save a credit with day of the first installment more than 3 months`() {
        //given
        val dayFirstOfInstallment: LocalDate = LocalDate.now().plusMonths(5)
        val customerDto: CreditDto = builderCreditDto(dayFirstOfInstallment = dayFirstOfInstallment)
        val valueAsString: String = objectMapper.writeValueAsString(customerDto)
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .content(valueAsString)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! Consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class br.malandrim.creditapplicationsystem.exception.BusinessException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not save a credit with day of the first installment in past`() {
        //given
        val dayFirstOfInstallment: LocalDate = LocalDate.now().minusMonths(2)
        val customerDto: CreditDto = builderCreditDto(dayFirstOfInstallment = dayFirstOfInstallment)
        val valueAsString: String = objectMapper.writeValueAsString(customerDto)
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .content(valueAsString)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! Consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.details.dayFirstOfInstallment").value("Date must be in future")
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class org.springframework.web.bind.MethodArgumentNotValidException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not save a credit with number of installments more than 48`() {
        //given
        val customerDto: CreditDto = builderCreditDto(numberOfInstallments = 60)
        val valueAsString: String = objectMapper.writeValueAsString(customerDto)
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .content(valueAsString)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! Consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.details.numberOfInstallments")
                    .value("Number of Installments must be up to 48")
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class org.springframework.web.bind.MethodArgumentNotValidException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should find all credits by customer id and return 200 status`() {
        //given
        val customerId: Long = 1L
        creditRepository.save(builderCreditDto(customerId = customerId).toEntity())
        creditRepository.save(
            builderCreditDto(
                customerId = customerId,
                creditValue = BigDecimal(500.0)
            ).toEntity()
        )
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.get(URL)
                .accept(MediaType.APPLICATION_JSON)
                .param("customerId", customerId.toString())
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.jsonPath("$[*].creditValue")
                    .value(Matchers.containsInAnyOrder(400.0, 500.0))
            )
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not find credits with invalid customer id and return 200 status`() {
        //given
        val rigthCustomerId: Long = 1L
        val wrongCustomerId: Long = 2L
        creditRepository.save(builderCreditDto(customerId = rigthCustomerId).toEntity())
        creditRepository.save(
            builderCreditDto(
                customerId = rigthCustomerId,
                creditValue = BigDecimal(500.0)
            ).toEntity()
        )
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.get(URL)
                .accept(MediaType.APPLICATION_JSON)
                .param("customerId", wrongCustomerId.toString())
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty)
            .andDo(MockMvcResultHandlers.print())
    }


//
//    @Test
//    fun `should not find customer with invalid id and return 400 status`() {
//        //given
//        val invalidId: Long = 2L
//        //when
//        //then
//        mockMvc.perform(
//            MockMvcRequestBuilders.get("$URL/$invalidId")
//                .accept(MediaType.APPLICATION_JSON)
//        )
//            .andExpect(MockMvcResultMatchers.status().isBadRequest)
//            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! Consult the documentation"))
//            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
//            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
//            .andExpect(
//                MockMvcResultMatchers.jsonPath("$.exception")
//                    .value("class br.malandrim.creditapplicationsystem.exception.BusinessException")
//            )
//            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
//            .andDo(MockMvcResultHandlers.print())
//    }
//
//    @Test
//    fun `should delete customer by id and return 204 status`() {
//        //given
//        val customer: Customer = customerRepository.save(builderCustomerDto().toEntity())
//        //when
//        //then
//        mockMvc.perform(
//            MockMvcRequestBuilders.delete("$URL/${customer.id}")
//                .accept(MediaType.APPLICATION_JSON)
//        )
//            .andExpect(MockMvcResultMatchers.status().isNoContent)
//            .andDo(MockMvcResultHandlers.print())
//    }
//
//    @Test
//    fun `should not delete customer by id and return 400 status`() {
//        //given
//        val invalidId: Long = Random().nextLong()
//        //when
//        //then
//        mockMvc.perform(
//            MockMvcRequestBuilders.delete("$URL/${invalidId}")
//                .accept(MediaType.APPLICATION_JSON)
//        )
//            .andExpect(MockMvcResultMatchers.status().isBadRequest)
//            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! Consult the documentation"))
//            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
//            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
//            .andExpect(
//                MockMvcResultMatchers.jsonPath("$.exception")
//                    .value("class br.malandrim.creditapplicationsystem.exception.BusinessException")
//            )
//            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
//            .andDo(MockMvcResultHandlers.print())
//    }
//
//    @Test
//    fun `should update a customer and return 200 status`() {
//        //given
//        val customer: Customer = customerRepository.save(builderCustomerDto().toEntity())
//        val customerUpdateDto: CustomerUpdateDto = builderCustomerUpdateDto()
//        val valueAsString: String = objectMapper.writeValueAsString(customerUpdateDto)
//        //when
//        //then
//        mockMvc.perform(
//            MockMvcRequestBuilders.patch("$URL?customerId=${customer.id}")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(valueAsString)
//        )
//            .andExpect(MockMvcResultMatchers.status().isOk)
//            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Paulo Roberto"))
//            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Malandrim"))
//            .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value("33920373898"))
//            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("paulo.malandrim@gmail.com"))
//            .andExpect(MockMvcResultMatchers.jsonPath("$.income").value("5000.0"))
//            .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value("45656"))
//            .andExpect(MockMvcResultMatchers.jsonPath("$.street").value("Rua Johann Ludwing"))
//            //.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
//            .andDo(MockMvcResultHandlers.print())
//    }
//
//    @Test
//    fun `should not update a customer with invalid id and return 400 status`() {
//        //given
//        val invalidId: Long = Random().nextLong()
//        val customerUpdateDto: CustomerUpdateDto = builderCustomerUpdateDto()
//        val valueAsString: String = objectMapper.writeValueAsString(customerUpdateDto)
//        //when
//        //then
//        mockMvc.perform(
//            MockMvcRequestBuilders.patch("$URL?customerId=$invalidId")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(valueAsString)
//        )
//            .andExpect(MockMvcResultMatchers.status().isBadRequest)
//            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! Consult the documentation"))
//            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
//            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
//            .andExpect(
//                MockMvcResultMatchers.jsonPath("$.exception")
//                    .value("class br.malandrim.creditapplicationsystem.exception.BusinessException")
//            )
//            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
//            .andDo(MockMvcResultHandlers.print())
//    }

    private fun builderCreditDto(
        creditValue: BigDecimal = BigDecimal.valueOf(400.0),
        dayFirstOfInstallment: LocalDate = LocalDate.now().plusMonths(1),
        numberOfInstallments: Int = 5,
        customerId: Long = 1L
    ) = CreditDto(
        creditValue = creditValue,
        dayFirstOfInstallment = dayFirstOfInstallment,
        numberOfInstallments = numberOfInstallments,
        customerId = customerId
    )

    private fun builderCustomerDto(
        firstName: String = "Paulo",
        lastName: String = "Malandrim",
        cpf: String = "33920373898",
        email: String = "paulo.malandrim@gmail.com",
        income: BigDecimal = BigDecimal.valueOf(1000.0),
        password: String = "1234",
        zipCode: String = "000000",
        street: String = "Rua do Meio",
    ) = CustomerDto(
        firstName = firstName,
        lastName = lastName,
        cpf = cpf,
        email = email,
        income = income,
        password = password,
        zipCode = zipCode,
        street = street
    )

    private fun builderCustomerUpdateDto(
        firstName: String = "Paulo Roberto",
        lastName: String = "Malandrim",
        income: BigDecimal = BigDecimal.valueOf(5000.0),
        zipCode: String = "45656",
        street: String = "Rua Johann Ludwing"
    ): CustomerUpdateDto = CustomerUpdateDto(
        firstName = firstName,
        lastName = lastName,
        income = income,
        zipCode = zipCode,
        street = street
    )
}
