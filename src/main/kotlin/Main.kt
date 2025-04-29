import models.Customer
import models.Order
import services.impl.CustomerServiceImpl

fun main(args: Array<String>) {
    println("Bienvenido a la tienda de productos! 🛒")

    val customerService = CustomerServiceImpl()
    val orders = mutableListOf<Order>()

    var continueRegisteringCustomers = true

    while (continueRegisteringCustomers) {
        val customer = registerCustomer(customerService)

        if (customer != null) {
            val order = Order(customer)
            println("🛒 Cliente registrado: ${customer.name}. Ahora puede hacer su pedido.")

            var continueOrder = true

            while (continueOrder) {
                showMainMenu()
                when (readOption()) {
                    1 -> println("📋 Ver menú de productos (implementación pendiente)")
                    2 -> println("💰 Filtrar productos por precio (implementación pendiente)")
                    3 -> println("➕ Agregar producto al pedido (implementación pendiente)")
                    4 -> println("🛒 Ver pedido actual (implementación pendiente)")
                    5 -> println("➖ Eliminar producto del pedido (implementación pendiente)")
                    6 -> println("💳 Calcular total del pedido (implementación pendiente)")
                    7 -> {
                        println("✅ Pedido finalizado para ${customer.name}")
                        orders.add(order)
                        continueOrder = false
                    }
                    else -> println("⚠️ Opción inválida")
                }
            }
        }

        println("\n¿Deseas registrar otro cliente? (s/n)")
        continueRegisteringCustomers = readLine()?.lowercase() == "s"
    }

    generateGeneralReport(orders)
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

fun generateGeneralReport(orders: List<Order>) {
    println("\n--- Reporte General de Pedidos ---")
    if (orders.isEmpty()) {
        println("No se registraron pedidos.")
        return
    }

    orders.forEachIndexed { index, order ->
        println("\nPedido #${index + 1}")
        println("Cliente: ${order.customer.name}")
        println("Número de ítems: ${order.getItemCount()}")
        println("Total pagado: ${order.calculateTotalWithDiscount()} COP")
    }
}
// Implementación de la clase Order y otros métodos pendientes