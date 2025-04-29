package services

import models.Product

interface ProductService {
    fun getProducts(): List<Product>
    fun filterProductsByPrice(minPrice: Double, maxPrice: Double): List<Product>
}