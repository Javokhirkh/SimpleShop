package org.example.simpleshop.services

import org.example.simpleshop.CategoryMapper
import org.example.simpleshop.CategoryNotFoundException
import org.example.simpleshop.CategoryRepository
import org.example.simpleshop.ProductMapper
import org.example.simpleshop.ProductNotFoundException
import org.example.simpleshop.ProductRepository
import org.example.simpleshop.dtoes.ProductRequest
import org.example.simpleshop.dtoes.ProductFullResponse
import org.example.simpleshop.dtoes.ProductUpdateRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

interface ProductService {
    fun createProduct(dto: ProductRequest)
    fun getProductById(id: Long): ProductFullResponse
    fun getAllProductsPaginated(pageable: Pageable): Page<ProductFullResponse>
    fun getProductsByCategoryPaginated(categoryId: Long, pageable: Pageable): Page<ProductFullResponse>
    fun updateProduct(id: Long, dto: ProductUpdateRequest)
    fun deleteProduct(id: Long)
}

@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository,
    private val productMapper: ProductMapper,
    private val categoryMapper: CategoryMapper
) : ProductService {
    override fun createProduct(dto: ProductRequest) {
        val category = categoryRepository.findByIdAndDeletedFalse(dto.categoryId)
            ?: throw IllegalArgumentException("Category with id ${dto.categoryId} not found")
        val product=productMapper.toEntity(dto,category)
        productRepository.save(product)
    }

    override fun getProductById(id: Long): ProductFullResponse {
        val product=productRepository.findByIdAndDeletedFalse(id)
            ?:throw ProductNotFoundException()
        val category=categoryRepository.findByIdAndDeletedFalse(id)
            ?:throw CategoryNotFoundException()
        return productMapper.toFullResponse(product,categoryMapper.toFullResponse(category))
    }

    override fun getAllProductsPaginated(pageable: Pageable): Page<ProductFullResponse> {
        val products=productRepository.findAllNotDeleted(pageable)
        return products.map { product ->
            val category=categoryRepository.findByIdAndDeletedFalse(product.category.id!!)
                ?:throw CategoryNotFoundException()
            productMapper.toFullResponse(product,categoryMapper.toFullResponse(category))
        }
    }

    override fun getProductsByCategoryPaginated(
        categoryId: Long,
        pageable: Pageable
    ): Page<ProductFullResponse> {
        val category=categoryRepository.findByIdAndDeletedFalse(categoryId)
            ?:throw CategoryNotFoundException()
        val products=productRepository.findAllByCategoryAndDeletedFalse(category,pageable)
        return products.map { product ->
            productMapper.toFullResponse(product,categoryMapper.toFullResponse(category))
        }
    }

    override fun updateProduct(id: Long, dto: ProductUpdateRequest) {
        val existingProduct=productRepository.findByIdAndDeletedFalse(id)
            ?:throw ProductNotFoundException()
        dto.categoryId?.let {
            val category=categoryRepository.findByIdAndDeletedFalse(it)
                ?:throw CategoryNotFoundException()
            existingProduct.category=category
        }
        dto.let {
            existingProduct.name=it.name?:existingProduct.name
            existingProduct.count=it.count?:existingProduct.count
        }
        productRepository.save(existingProduct)
    }

    override fun deleteProduct(id: Long) {
        val product=productRepository.findByIdAndDeletedFalse(id)
            ?:throw ProductNotFoundException()
        productRepository.trash(id)
    }

}
