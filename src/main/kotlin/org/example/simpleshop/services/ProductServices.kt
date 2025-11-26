package org.example.simpleshop.services

import org.example.simpleshop.Category
import org.example.simpleshop.CategoryNotFoundException
import org.example.simpleshop.CategoryRepository
import org.example.simpleshop.Product
import org.example.simpleshop.ProductNotFoundException
import org.example.simpleshop.ProductRepository
import org.example.simpleshop.dtoes.CategoryResponseDto
import org.example.simpleshop.dtoes.ProductRequestDto
import org.example.simpleshop.dtoes.ProductResponseDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

interface ProductService {
    fun createProduct(dto: ProductRequestDto): ProductResponseDto
    fun getProductById(id: Long): ProductResponseDto
    fun getAllProducts(): List<ProductResponseDto>
    fun getAllProductsPaginated(pageable: Pageable): Page<ProductResponseDto>
    fun getProductsByName(name: String): List<ProductResponseDto>
    fun getProductsByCategory(categoryId: Long): List<ProductResponseDto>
    fun getProductsByCategoryPaginated(categoryId: Long, pageable: Pageable): Page<ProductResponseDto>
    fun getAvailableProducts(): List<ProductResponseDto>
    fun updateProduct(id: Long, dto: ProductRequestDto): ProductResponseDto
    fun deleteProduct(id: Long): Boolean
}

@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository,
) : ProductService {

    override fun createProduct(dto: ProductRequestDto): ProductResponseDto {
        val category = categoryRepository.findByIdAndDeletedFalse(dto.categoryId)
            ?: throw CategoryNotFoundException()
        return productRepository.save(dto.toEntity(category)).toProductResponse()
    }

    override fun getProductById(id: Long): ProductResponseDto =
        productRepository.findByIdAndDeletedFalse(id)
            ?.toProductResponse()
            ?: throw ProductNotFoundException()

    override fun getAllProducts(): List<ProductResponseDto> =
        productRepository.findAllNotDeleted().map { it.toProductResponse() }

    override fun getAllProductsPaginated(pageable: Pageable): Page<ProductResponseDto> =
        productRepository.findAllNotDeleted(pageable).map { it.toProductResponse() }

    override fun getProductsByName(name: String): List<ProductResponseDto> =
        productRepository.findByNameAndDeletedFalse(name).map { it.toProductResponse() }

    override fun getProductsByCategory(categoryId: Long): List<ProductResponseDto> =
        productRepository.findByCategoryIdAndDeletedFalse(categoryId).map { it.toProductResponse() }

    override fun getProductsByCategoryPaginated(categoryId: Long, pageable: Pageable): Page<ProductResponseDto> =
        productRepository.findByCategoryIdAndDeletedFalse(categoryId, pageable).map { it.toProductResponse() }

    override fun getAvailableProducts(): List<ProductResponseDto> =
        productRepository.findByCountGreaterThanAndDeletedFalse(0).map { it.toProductResponse() }

    override fun updateProduct(id: Long, dto: ProductRequestDto): ProductResponseDto {
        val product = productRepository.findByIdAndDeletedFalse(id)
            ?: throw ProductNotFoundException()
        val category = categoryRepository.findByIdAndDeletedFalse(dto.categoryId)
            ?: throw CategoryNotFoundException()

        product.name = dto.name
        product.count = dto.count
        product.category = category

        return productRepository.save(product).toProductResponse()
    }

    override fun deleteProduct(id: Long): Boolean =
        productRepository.trash(id) != null

    private fun ProductRequestDto.toEntity(category: Category) = Product(
        name = this.name,
        count = this.count,
        category = category,
    )

    private fun Product.toProductResponse() = ProductResponseDto(
        id = requireNotNull(this.id) { "Product id cannot be null" },
        name = this.name,
        count = this.count,
        category = this.category.toCategoryResponse(),
        createdDate = this.createdDate,
        modifiedDate = this.modifiedDate,
        createdBy = this.createdBy,
        lastModifiedBy = this.lastModifiedBy,
    )

    private fun Category.toCategoryResponse() = CategoryResponseDto(
        id = requireNotNull(this.id) { "Category id cannot be null" },
        name = this.name,
        order = this.order,
        description = this.description,
        createdDate = this.createdDate,
        modifiedDate = this.modifiedDate,
        createdBy = this.createdBy,
        lastModifiedBy = this.lastModifiedBy,
    )
}
