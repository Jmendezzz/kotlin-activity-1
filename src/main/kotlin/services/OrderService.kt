package services

import models.Order
import models.Product

interface OrderService {
    fun createOrder(customerId: Number): Order
    fun addItemToOrder(order: Order, product: Product, quantity: Int)
    fun removeItemFromOrder(order: Order, productId: Long)
    fun calculateSubtotal(order: Order, menu: List<Product>): Double
    fun calculateTotalWithDiscount(order: Order, menu: List<Product>): Double
    fun showOrder(order: Order, menu: List<Product>)
}
