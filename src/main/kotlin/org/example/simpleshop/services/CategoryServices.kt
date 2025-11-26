package org.example.simpleshop.services

import org.example.simpleshop.Category
import org.example.simpleshop.CategoryNotFoundException
import org.example.simpleshop.CategoryRepository
import org.example.simpleshop.dtoes.CategoryRequestDto
import org.example.simpleshop.dtoes.CategoryResponseDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

interface CategoryService {
    fun createCategory(dto: CategoryRequestDto): CategoryResponseDto
    fun getCategoryById(id: Long): CategoryResponseDto
    fun getAllCategoriesPaginated(pageable: Pageable): Page<CategoryResponseDto>
    fun getAllCategoriesByOrder(): List<CategoryResponseDto>
    fun getCategoryByName(name: String): CategoryResponseDto?
    fun updateCategory(id: Long, dto: CategoryRequestDto): CategoryResponseDto
    fun deleteCategory(id: Long): Boolean
}

@Service
class CategoryServiceImpl(
    private val categoryRepository: CategoryRepository,
) : CategoryService {

    override fun createCategory(dto: CategoryRequestDto): CategoryResponseDto =
        categoryRepository.save(dto.toEntity()).toCategoryResponse()

    override fun getCategoryById(id: Long): CategoryResponseDto =
        categoryRepository.findByIdAndDeletedFalse(id)
            ?.toCategoryResponse()
            ?: throw CategoryNotFoundException()


    override fun getAllCategoriesPaginated(pageable: Pageable): Page<CategoryResponseDto> =
        categoryRepository.findAllNotDeleted(pageable).map { it.toCategoryResponse() }

    override fun getAllCategoriesByOrder(): List<CategoryResponseDto> =
        categoryRepository.findAllByDeletedFalseOrderByOrder().map { it.toCategoryResponse() }

    override fun getCategoryByName(name: String): CategoryResponseDto? =
        categoryRepository.findByNameAndDeletedFalse(name)?.toCategoryResponse()

    override fun updateCategory(id: Long, dto: CategoryRequestDto): CategoryResponseDto {
        val category = categoryRepository.findByIdAndDeletedFalse(id)
            ?: throw CategoryNotFoundException()

        category.name = dto.name
        category.order = dto.order
        category.description = dto.description

        return categoryRepository.save(category).toCategoryResponse()
    }

    override fun deleteCategory(id: Long): Boolean =
        categoryRepository.trash(id) != null

    private fun CategoryRequestDto.toEntity() = Category(
        name = this.name,
        order = this.order,
        description = this.description,
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