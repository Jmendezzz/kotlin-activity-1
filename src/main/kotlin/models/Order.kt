package models

data class Order(
    val id: Number,
    val customerId: Number,
    var orderItems: MutableList<OrderItem> = mutableListOf(),
    val totalAmount: Double,
) {
}