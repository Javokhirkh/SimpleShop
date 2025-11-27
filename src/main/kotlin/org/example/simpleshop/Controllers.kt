package org.example.simpleshop

import org.example.simpleshop.dtoes.*
import org.example.simpleshop.services.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1/categories")
class CategoryController(
    private val categoryService: CategoryService
) {
    @PostMapping
    fun createCategory(@Valid @RequestBody dto: CategoryRequestDto): ResponseEntity<String> {
        categoryService.createCategory(dto)
        return ResponseEntity.status(HttpStatus.CREATED).body("Category created successfully")
    }

    @GetMapping("/{id}")
    fun getCategoryById(@PathVariable id: Long): ResponseEntity<CategoryFullResponse> {
        return ResponseEntity.ok(categoryService.getCategoryById(id))
    }

    @GetMapping
    fun getAllCategories(pageable: Pageable): ResponseEntity<Page<CategoryShortResponse>> {
        return ResponseEntity.ok(categoryService.getAllCategoriesPaginated(pageable))
    }

    @GetMapping("/ordered")
    fun getAllCategoriesByOrder(pageable: Pageable): ResponseEntity<Page<CategoryShortResponse>> {
        return ResponseEntity.ok(categoryService.getAllCategoriesByOrderPaginated(pageable))
    }

    @PutMapping("/{id}")
    fun updateCategory(
        @PathVariable id: Long,
        @Valid @RequestBody dto: CategoryUpdateRequestDto
    ): ResponseEntity<String> {
        categoryService.updateCategory(id, dto)
        return ResponseEntity.ok("Category updated successfully")
    }

    @DeleteMapping("/{id}")
    fun deleteCategory(@PathVariable id: Long): ResponseEntity<String> {
        categoryService.deleteCategory(id)
        return ResponseEntity.ok("Category deleted successfully")
    }
}

@RestController
@RequestMapping("/api/v1/products")
class ProductController(
    private val productService: ProductService
) {
    @PostMapping
    fun createProduct(@Valid @RequestBody dto: ProductRequest): ResponseEntity<String> {
        productService.createProduct(dto)
        return ResponseEntity.status(HttpStatus.CREATED).body("Product created successfully")
    }

    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: Long): ResponseEntity<ProductFullResponse> {
        return ResponseEntity.ok(productService.getProductById(id))
    }

    @GetMapping
    fun getAllProducts(pageable: Pageable): ResponseEntity<Page<ProductFullResponse>> {
        return ResponseEntity.ok(productService.getAllProductsPaginated(pageable))
    }

    @GetMapping("/category/{categoryId}")
    fun getProductsByCategory(
        @PathVariable categoryId: Long,
        pageable: Pageable
    ): ResponseEntity<Page<ProductFullResponse>> {
        return ResponseEntity.ok(productService.getProductsByCategoryPaginated(categoryId, pageable))
    }

    @PutMapping("/{id}")
    fun updateProduct(
        @PathVariable id: Long,
        @Valid @RequestBody dto: ProductUpdateRequest
    ): ResponseEntity<String> {
        productService.updateProduct(id, dto)
        return ResponseEntity.ok("Product updated successfully")
    }

    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: Long): ResponseEntity<String> {
        productService.deleteProduct(id)
        return ResponseEntity.ok("Product deleted successfully")
    }
}

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService
) {
    @PostMapping
    fun createUser(@Valid @RequestBody dto: UserRequestDto): ResponseEntity<String> {
        userService.createUser(dto)
        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully")
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<UserFullInformation> {
        return ResponseEntity.ok(userService.getUserById(id))
    }

    @GetMapping
    fun getAllUsers(pageable: Pageable): ResponseEntity<Page<UserShortResponse>> {
        return ResponseEntity.ok(userService.getAllUsers(pageable))
    }

    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable id: Long,
        @Valid @RequestBody dto: UserUpdateRequest
    ): ResponseEntity<String> {
        userService.updateUser(id, dto)
        return ResponseEntity.ok("User updated successfully")
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<String> {
        userService.deleteUser(id)
        return ResponseEntity.ok("User deleted successfully")
    }
}

@RestController
@RequestMapping("/api/v1/transactions")
class TransactionController(
    private val transactionService: TransactionService
) {
    @PostMapping
    fun createTransaction(@Valid @RequestBody dto: TransactionRequestDto): ResponseEntity<String> {
        transactionService.createTransaction(dto)
        return ResponseEntity.status(HttpStatus.CREATED).body("Transaction created successfully")
    }

    @GetMapping("/{id}")
    fun getTransactionById(@PathVariable id: Long): ResponseEntity<TransactionFullResponse> {
        return ResponseEntity.ok(transactionService.getTransactionById(id))
    }

    @GetMapping
    fun getAllTransactions(pageable: Pageable): ResponseEntity<Page<TransactionShortResponse>> {
        return ResponseEntity.ok(transactionService.getAllTransactionsPaginated(pageable))
    }

    @GetMapping("/user/{userId}")
    fun getTransactionsByUserId(
        @PathVariable userId: Long,
        pageable: Pageable
    ): ResponseEntity<Page<TransactionShortResponse>> {
        return ResponseEntity.ok(transactionService.getTransactionsByUserIdPaginated(userId, pageable))
    }

    @GetMapping("/date-range")
    fun getTransactionsByDateRange(
        @RequestParam startDate: LocalDate,
        @RequestParam endDate: LocalDate,
        pageable: Pageable
    ): ResponseEntity<Page<TransactionShortResponse>> {
        return ResponseEntity.ok(
            transactionService.getTransactionsByDateRangePaginated(startDate, endDate, pageable)
        )
    }

    @DeleteMapping("/{id}")
    fun deleteTransaction(@PathVariable id: Long): ResponseEntity<String> {
        transactionService.deleteTransaction(id)
        return ResponseEntity.ok("Transaction deleted successfully")
    }
}

@RestController
@RequestMapping("/api/v1/user-payments")
class UserPaymentTransactionController(
    private val userPaymentTransactionService: UserPaymentTransactionService
) {
    @PostMapping
    fun createPaymentTransaction(
        @Valid @RequestBody dto: UserPaymentTransactionRequestDto
    ): ResponseEntity<String> {
        userPaymentTransactionService.createPaymentTransaction(dto)
        return ResponseEntity.status(HttpStatus.CREATED).body("Payment transaction created successfully")
    }

    @GetMapping("/{id}")
    fun getPaymentTransactionById(@PathVariable id: Long): ResponseEntity<UserPaymentTransactionResponseFullInformation> {
        return ResponseEntity.ok(userPaymentTransactionService.getPaymentTransactionById(id))
    }

    @GetMapping
    fun getAllPaymentTransactions(pageable: Pageable): ResponseEntity<Page<UserPaymentTransactionShortResponse>> {
        return ResponseEntity.ok(userPaymentTransactionService.getAllPaymentTransactionsPaginated(pageable))
    }

    @GetMapping("/user/{userId}")
    fun getPaymentTransactionsByUserId(
        @PathVariable userId: Long,
        pageable: Pageable
    ): ResponseEntity<Page<UserPaymentTransactionShortResponse>> {
        return ResponseEntity.ok(userPaymentTransactionService.getPaymentTransactionsByUserIdPaginated(userId, pageable))
    }

    @GetMapping("/date")
    fun getPaymentTransactionsByDate(
        @RequestParam date: LocalDate,
        pageable: Pageable
    ): ResponseEntity<Page<UserPaymentTransactionShortResponse>> {
        return ResponseEntity.ok(userPaymentTransactionService.getPaymentTransactionsByDatePaginated(date, pageable))
    }

    @GetMapping("/date-range")
    fun getPaymentTransactionsByDateRange(
        @RequestParam startDate: LocalDate,
        @RequestParam endDate: LocalDate,
        pageable: Pageable
    ): ResponseEntity<Page<UserPaymentTransactionShortResponse>> {
        return ResponseEntity.ok(
            userPaymentTransactionService.getPaymentTransactionsByDateRangePaginated(startDate, endDate, pageable)
        )
    }

    @GetMapping("/total/{userId}")
    fun getTotalPaymentByUserId(@PathVariable userId: Long): ResponseEntity<Map<String, Any>> {
        val total = userPaymentTransactionService.getTotalPaymentByUserId(userId)
        return ResponseEntity.ok(mapOf("userId" to userId, "totalPayment" to total))
    }

    @DeleteMapping("/{id}")
    fun deletePaymentTransaction(@PathVariable id: Long): ResponseEntity<String> {
        userPaymentTransactionService.deletePaymentTransaction(id)
        return ResponseEntity.ok("Payment transaction deleted successfully")
    }
}