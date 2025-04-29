package services.impl

import models.Product
import services.ProductService

class ProductServiceImpl : ProductService {
    private val products: MutableList<Product> = mutableListOf()

    init {
        products.add(Product(1, "Hamburguesa", "Hamburguesa clásica con queso", 15000.0))
        products.add(Product(2, "Papas Fritas", "Papas crujientes medianas", 8000.0))
        products.add(Product(3, "Soda", "Refresco de 500ml", 4000.0))
        products.add(Product(4, "Ensalada", "Ensalada verde fresca", 12000.0))
        products.add(Product(5, "Pizza", "Pizza de pepperoni", 20000.0))
        products.add(Product(6, "Taco", "Taco de pollo", 10000.0))
        products.add(Product(7, "Sushi", "Sushi variado", 25000.0))
        products.add(Product(8, "Pasta", "Pasta al pesto", 18000.0))
        products.add(Product(9, "Sopa", "Sopa de verduras", 9000.0))
        products.add(Product(10, "Postre", "Postre de chocolate", 7000.0))
        products.add(Product(11, "Sandwich", "Sandwich de pollo", 12000.0))
    }

    override fun getProducts(): List<Product> {
        return products
    }

    override fun filterProductsByPrice(minPrice: Double, maxPrice: Double): List<Product> {
        return products.filter { it.price in minPrice..maxPrice }
    }
}
