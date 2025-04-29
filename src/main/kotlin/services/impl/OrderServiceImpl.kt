package services.impl

import models.Order
import models.OrderItem
import models.Product
import services.OrderService

class OrderServiceImpl : OrderService {
    private var nextOrderId = 1
    private val orders = mutableListOf<Order>()

    override fun createOrder(customerId: Number): Order {
        val newOrder = Order(
            id = nextOrderId++,
            customerId = customerId,
            orderItems = mutableListOf(),
            totalAmount = 0.0
        )
        orders.add(newOrder)
        return newOrder
    }

    override fun addItemToOrder(orderId: Number, product: Product, quantity: Int) {
        val order = findOrder(orderId) ?: return
        val existingItem = order.orderItems.find { it.productId == product.id.toLong() }
        if (existingItem != null) {
            existingItem.quantity += quantity
        } else {
            order.orderItems.add(OrderItem(productId = product.id.toLong(), quantity = quantity))
        }
    }

    override fun removeItemFromOrder(orderId: Number, productId: Long) {
        val order = findOrder(orderId) ?: return
        order.orderItems.removeIf { it.productId == productId }
    }

    override fun calculateSubtotal(orderId: Number, menu: List<Product>): Double {
        val order = findOrder(orderId) ?: return 0.0
        return order.orderItems.sumOf { item ->
            val product = menu.find { it.id.toLong() == item.productId }
            (product?.price ?: 0.0) * item.quantity
        }
    }

    override fun calculateTotalWithDiscount(orderId: Number, menu: List<Product>): Double {
        val subtotal = calculateSubtotal(orderId, menu)
        return if (subtotal > 100_000) subtotal * 0.90 else subtotal
    }

    override fun showOrder(orderId: Number, menu: List<Product>) {
        val order = findOrder(orderId)
        if (order == null || order.orderItems.isEmpty()) {
            println("El pedido está vacío.")
            return
        }

        println("Resumen del Pedido:")
        order.orderItems.forEach { item ->
            val product = menu.find { it.id.toLong() == item.productId }
            if (product != null) {
                println("- ${product.name} x ${item.quantity} = ${product.price * item.quantity} COP")
            }
        }
        println("Subtotal: ${calculateSubtotal(orderId, menu)} COP")
        println("Total con descuento (si aplica): ${calculateTotalWithDiscount(orderId, menu)} COP")
    }

    override fun getAllOrders(): List<Order> {
        return orders
    }

    private fun findOrder(orderId: Number): Order? {
        return orders.find { it.id.toInt() == orderId.toInt() }
    }
}
