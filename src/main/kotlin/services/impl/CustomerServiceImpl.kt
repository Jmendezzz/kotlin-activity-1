package services.impl

import models.Customer
import services.CustomerService

class CustomerServiceImpl : CustomerService {
    private val customers = mutableListOf<Customer>()
    private var nextId = 1

    override fun addCustomer(customer: Customer): Boolean {
        if (customers.any { it.nid == customer.nid }) {
            return false
        }
        customer.id = nextId++
        customers.add(customer)
        return true
    }
}
