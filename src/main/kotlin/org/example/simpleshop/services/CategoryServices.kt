package org.example.simpleshop.services

import org.example.simpleshop.CategoryMapper
import org.example.simpleshop.CategoryNotFoundException
import org.example.simpleshop.CategoryRepository
import org.example.simpleshop.InvalidCategoryNameException
import org.example.simpleshop.dtoes.CategoryRequestDto
import org.example.simpleshop.dtoes.CategoryFullResponse
import org.example.simpleshop.dtoes.CategoryShortResponse
import org.example.simpleshop.dtoes.CategoryUpdateRequestDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

interface CategoryService {
    fun createCategory(dto: CategoryRequestDto)
    fun getCategoryById(id: Long): CategoryFullResponse
    fun getAllCategoriesPaginated(pageable: Pageable): Page<CategoryShortResponse>
    fun getAllCategoriesByOrderPaginated(pageable:Pageable): Page<CategoryShortResponse>
    fun updateCategory(id: Long, dto: CategoryUpdateRequestDto)
    fun deleteCategory(id: Long)
}

@Service
class CategoryServiceImpl(
    private val categoryRepository: CategoryRepository,
    private val mapper : CategoryMapper
) : CategoryService {
    override fun createCategory(dto: CategoryRequestDto){
        dto.run {
            categoryRepository.existsByName(name)?.let {
                throw InvalidCategoryNameException(name)
            } ?: run {
                val categoryEntity=mapper.toEntity(dto)
                categoryRepository.save(categoryEntity)
            }
        }
    }

    override fun getCategoryById(id: Long): CategoryFullResponse {
        val category=categoryRepository.findByIdAndDeletedFalse(id)
            ?:throw CategoryNotFoundException()
        return mapper.toFullResponse(category)
    }

    override fun getAllCategoriesPaginated(pageable: Pageable): Page<CategoryShortResponse> {
        val categories=categoryRepository.findAllNotDeleted(pageable)
        return categories.map { mapper.toShortResponse(it) }
    }

    override fun getAllCategoriesByOrderPaginated(pageable: Pageable): Page<CategoryShortResponse> {
        val categories=categoryRepository.findAllByOrderByNameAsc(pageable)
        return categories.map { mapper.toShortResponse(it) }
    }

    override fun updateCategory(id: Long, dto: CategoryUpdateRequestDto) {
        val existingCategory=categoryRepository.findByIdAndDeletedFalse(id)
            ?:throw CategoryNotFoundException()
        dto.run {
            name?.let{
                categoryRepository.existsByNameAndNotEqualId(it,id)
            }
            description= description ?: this.description

        }
        categoryRepository.save(existingCategory)
    }

    override fun deleteCategory(id: Long) {
        val category=categoryRepository.findByIdAndDeletedFalse(id)
            ?:throw CategoryNotFoundException()
        categoryRepository.trash(id)
    }


}