package services

import models.Order
import models.Product

interface OrderService {
    fun createOrder(customerId: Number): Order
    fun addItemToOrder(orderId: Number, product: Product, quantity: Int)
    fun removeItemFromOrder(orderId: Number, productId: Long)
    fun calculateSubtotal(orderId: Number, menu: List<Product>): Double
    fun calculateTotalWithDiscount(orderId: Number, menu: List<Product>): Double
    fun showOrder(orderId: Number, menu: List<Product>)
    fun getAllOrders(): List<Order>
}
