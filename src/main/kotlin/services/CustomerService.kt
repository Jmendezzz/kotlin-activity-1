package services

import models.Customer

interface CustomerService {
    fun addCustomer(customer: Customer): Boolean
}