package services.impl

import models.Order
import models.OrderItem
import models.Product
import services.OrderService

class OrderServiceImpl : OrderService {
    private var nextOrderId = 1

    override fun createOrder(customerId: Number): Order {
        return Order(
            id = nextOrderId++,
            customerId = customerId,
            orderItems = mutableListOf(),
            totalAmount = 0.0
        )
    }

    override fun addItemToOrder(order: Order, product: Product, quantity: Int) {
        val existingItem = order.orderItems.find { it.productId == product.id.toLong() }
        if (existingItem != null) {
            existingItem.quantity += quantity
        } else {
            order.orderItems.add(OrderItem(productId = product.id.toLong(), quantity = quantity))
        }
    }


    override fun removeItemFromOrder(order: Order, productId: Long) {
        val mutableItems = order.orderItems.toMutableList()
        mutableItems.removeIf { it.productId == productId }
        orderItemsSetter(order, mutableItems)
    }

    override fun calculateSubtotal(order: Order, menu: List<Product>): Double {
        return order.orderItems.sumOf { item ->
            val product = menu.find { it.id.toLong() == item.productId }
            (product?.price ?: 0.0) * item.quantity
        }
    }

    override fun calculateTotalWithDiscount(order: Order, menu: List<Product>): Double {
        val subtotal = calculateSubtotal(order, menu)
        return if (subtotal > 100_000) subtotal * 0.90 else subtotal
    }

    override fun showOrder(order: Order, menu: List<Product>) {
        if (order.orderItems.isEmpty()) {
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
        println("Subtotal: ${calculateSubtotal(order, menu)} COP")
        println("Total con descuento (si aplica): ${calculateTotalWithDiscount(order, menu)} COP")
    }

    private fun orderItemsSetter(order: Order, newItems: List<OrderItem>) {
        val orderClass = order.javaClass
        val field = orderClass.getDeclaredField("orderItems")
        field.isAccessible = true
        field.set(order, newItems)
    }
}
