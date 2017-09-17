package com.example.springkotlin

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.notFound
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

fun createUrl(id: Long): URI = ServletUriComponentsBuilder
        .fromCurrentRequest().path("/{id}")
        .buildAndExpand(id).toUri()

data class CreateCustomerRequest(val name: String, val email: String)

data class UpdateCustomerRequest(val id: Long, val name: String, val email: String)

data class CustomerResponse(val id: Long, val name: String, val email: String)

@Entity data class Customer(val name: String,
                            val email: String,
                            @Id @GeneratedValue(strategy = GenerationType.AUTO)
                            val id: Long = 0)
@Repository
interface CustomerRepository : JpaRepository<Customer, Long>

@RestController
class HomeController(val customerRepository: CustomerRepository) {
    @GetMapping
    fun index() = customerRepository.findAll().map { (name, email, id) -> CustomerResponse(id, name, email) }

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): ResponseEntity<CustomerResponse>? {
        val customer = customerRepository.findOne(id) ?: return notFound().build()
        val response = CustomerResponse(customer.id, customer.name, customer.email)
        return ResponseEntity.ok(response)
    }

    @PostMapping
    fun create(@RequestBody request: CreateCustomerRequest): ResponseEntity<CustomerResponse>? {
        val customer = Customer(request.name, request.email)
        val saved = customerRepository.save(customer)
        return ResponseEntity.created(createUrl(saved.id)).body(CustomerResponse(saved.id, saved.name, saved.email))
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody request: UpdateCustomerRequest): ResponseEntity<Void> {
        val customer = customerRepository.findOne(id) ?: return notFound().build()
        val updatedCustomer = customer.copy(name = request.name, email = request.email)
        customerRepository.save(updatedCustomer)
        return ResponseEntity.ok(null)
    }
}
