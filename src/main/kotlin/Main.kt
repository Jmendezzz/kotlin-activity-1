import models.Customer
import services.impl.CustomerServiceImpl
import services.impl.OrderServiceImpl
import services.impl.ProductServiceImpl

fun main(args: Array<String>) {
    println("Bienvenido a la tienda de productos! 🛒")

    val customerService = CustomerServiceImpl()
    val orderService = OrderServiceImpl()
    val productService = ProductServiceImpl()

    var continueRegisteringCustomers = true

    while (continueRegisteringCustomers) {
        val customer = registerCustomer(customerService)

        if (customer != null) {
            val order = orderService.createOrder(customer.id)

            println("🛒 Cliente registrado: ${customer.name}. Ahora puede hacer su pedido.")

            var continueOrder = true

            while (continueOrder) {
                showMainMenu()
                when (readOption()) {
                    1 -> showProducts(productService)
                    2 -> filterProductsByPrice(productService)
                    3 -> addProductToOrder(order.id, productService, orderService)
                    4 -> orderService.showOrder(order.id, productService.getProducts())
                    5 -> removeProductFromOrder(order.id, orderService)
                    6 -> {
                        val subtotal = orderService.calculateSubtotal(order.id, productService.getProducts())
                        val total = orderService.calculateTotalWithDiscount(order.id, productService.getProducts())
                        println("Subtotal: $subtotal COP")
                        println("Total con descuento (si aplica): $total COP")
                    }
                    7 -> {
                        println("✅ Pedido finalizado para ${customer.name}")
                        continueOrder = false
                    }
                    else -> println("⚠️ Opción inválida.")
                }
            }
        }

        println("\n¿Deseas registrar otro cliente? (s/n)")
        continueRegisteringCustomers = readLine()?.lowercase() == "s"
    }

    generateGeneralReport(orderService, productService)
}

fun showMainMenu() {
    println("\n--- Selecciona una opción ---")
    println("1. Ver Menú")
    println("2. Filtrar Productos por Precio")
    println("3. Agregar Producto al Pedido")
    println("4. Ver Pedido")
    println("5. Eliminar Producto del Pedido")
    println("6. Calcular Total del Pedido")
    println("7. Finalizar Pedido")
}

fun readOption(): Int {
    print("Ingresa tu opción: ")
    return try {
        readLine()?.toInt() ?: -1
    } catch (e: Exception) {
        -1
    }
}

fun registerCustomer(customerService: CustomerServiceImpl): Customer? {
    println("\n--- Registro de Cliente ---")

    print("Nombre: ")
    val name = readLine() ?: ""

    print("Cédula (NID): ")
    val nid = readLine() ?: ""

    print("Teléfono: ")
    val phone = readLine() ?: ""

    val newCustomer = Customer(
        name = name,
        nid = nid,
        phone = phone
    )

    val wasRegistered = customerService.addCustomer(newCustomer)

    return if (wasRegistered) {
        println("✅ Cliente registrado exitosamente con ID: ${newCustomer.id}")
        newCustomer
    } else {
        println("⚠️ Ya existe un cliente con esa cédula. Registro fallido.")
        null
    }
}

fun showProducts(productService: ProductServiceImpl) {
    println("\n--- Menú de Productos ---")
    productService.getProducts().forEach {
        println("${it.id}. ${it.name} - ${it.description} - ${it.price} COP")
    }
}

fun filterProductsByPrice(productService: ProductServiceImpl) {
    print("Precio mínimo: ")
    val minPrice = readLine()?.toDoubleOrNull() ?: 0.0

    print("Precio máximo: ")
    val maxPrice = readLine()?.toDoubleOrNull() ?: Double.MAX_VALUE

    val filtered = productService.filterProductsByPrice(minPrice, maxPrice)

    if (filtered.isEmpty()) {
        println("No hay productos en ese rango de precios.")
    } else {
        println("\nProductos encontrados:")
        filtered.forEach {
            println("${it.id}. ${it.name} - ${it.price} COP")
        }
    }
}

fun addProductToOrder(orderId: Number, productService: ProductServiceImpl, orderService: OrderServiceImpl) {
    print("ID del producto que deseas agregar: ")
    val productId = readLine()?.toIntOrNull()

    val product = productService.getProducts().find { it.id.toLong() == productId?.toLong() }

    if (product != null) {
        print("Cantidad: ")
        val quantity = readLine()?.toIntOrNull() ?: 1
        orderService.addItemToOrder(orderId, product, quantity)
        println("Producto agregado exitosamente.")
    } else {
        println("⚠️ Producto no encontrado.")
    }
}

fun removeProductFromOrder(orderId: Number, orderService: OrderServiceImpl) {
    print("ID del producto que deseas eliminar: ")
    val productId = readLine()?.toLongOrNull()

    if (productId != null) {
        orderService.removeItemFromOrder(orderId, productId)
        println("Producto eliminado si existía.")
    } else {
        println("⚠️ ID inválido.")
    }
}

fun generateGeneralReport(orderService: OrderServiceImpl, productService: ProductServiceImpl) {
    println("\n--- Reporte General de Pedidos ---")
    val orders = orderService.getAllOrders()
    if (orders.isEmpty()) {
        println("No se registraron pedidos.")
        return
    }

    orders.forEachIndexed { index, order ->
        println("\nPedido #${index + 1}")
        println("Cliente ID: ${order.customerId}")
        println("Número de ítems: ${order.orderItems.sumOf { it.quantity }}")
        val totalPaid = orderService.calculateTotalWithDiscount(order.id, productService.getProducts())
        println("Total pagado: $totalPaid COP")
    }
}
