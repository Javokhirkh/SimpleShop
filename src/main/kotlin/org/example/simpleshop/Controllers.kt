package org.example.simpleshop


import org.example.simpleshop.services.CategoryService
import org.example.simpleshop.services.ProductService
import org.example.simpleshop.services.TransactionService
import org.example.simpleshop.services.TransactionItemService
import org.example.simpleshop.services.UserPaymentTransactionService
import org.example.simpleshop.dtoes.CategoryRequestDto
import org.example.simpleshop.dtoes.CategoryResponseDto
import org.example.simpleshop.dtoes.ProductRequestDto
import org.example.simpleshop.dtoes.ProductResponseDto
import org.example.simpleshop.dtoes.TransactionRequestDto
import org.example.simpleshop.dtoes.TransactionResponseDto
import org.example.simpleshop.dtoes.TransactionItemRequestDto
import org.example.simpleshop.dtoes.TransactionItemResponseDto
import org.example.simpleshop.dtoes.UserPaymentTransactionRequestDto
import org.example.simpleshop.dtoes.UserPaymentTransactionResponseDto
import org.example.simpleshop.dtoes.UserRequestDto
import org.example.simpleshop.dtoes.UserResponseDto
import org.example.simpleshop.services.UserService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate


@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService,
) {

    @PostMapping
    fun createUser(@RequestBody dto: UserRequestDto): ResponseEntity<UserResponseDto> {
        val response = userService.createUser(dto)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<UserResponseDto> {
        val response = userService.getUserById(id)
        return ResponseEntity.ok(response)
    }

    @GetMapping
    fun getAllUsers(): ResponseEntity<Page<UserResponseDto>> {
        val response = userService.getAllUsers()
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable id: Long,
        @RequestBody dto: UserRequestDto,
    ): ResponseEntity<UserResponseDto> {
        val response = userService.updateUser(id, dto)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Unit> {
        userService.deleteUser(id)
        return ResponseEntity.noContent().build()
    }
}

@RestController
@RequestMapping("/api/v1/categories")
class CategoryController(
    private val categoryService: CategoryService,
) {

    @PostMapping
    fun createCategory(@RequestBody dto: CategoryRequestDto): ResponseEntity<CategoryResponseDto> {
        val response = categoryService.createCategory(dto)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping("/{id}")
    fun getCategoryById(@PathVariable id: Long): ResponseEntity<CategoryResponseDto> {
        val response = categoryService.getCategoryById(id)
        return ResponseEntity.ok(response)
    }


    @GetMapping("/paginated")
    fun getAllCategories(pageable: Pageable): ResponseEntity<Page<CategoryResponseDto>> {
        val response = categoryService.getAllCategoriesPaginated(pageable)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/ordered")
    fun getAllCategoriesByOrder(): ResponseEntity<List<CategoryResponseDto>> {
        val response = categoryService.getAllCategoriesByOrder()
        return ResponseEntity.ok(response)
    }

    @GetMapping("/by-name")
    fun getCategoryByName(@RequestParam name: String): ResponseEntity<CategoryResponseDto?> {
        val response = categoryService.getCategoryByName(name)
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{id}")
    fun updateCategory(
        @PathVariable id: Long,
        @RequestBody dto: CategoryRequestDto,
    ): ResponseEntity<CategoryResponseDto> {
        val response = categoryService.updateCategory(id, dto)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{id}")
    fun deleteCategory(@PathVariable id: Long): ResponseEntity<Boolean> {
        val result = categoryService.deleteCategory(id)
        return ResponseEntity.ok(result)
    }
}

@RestController
@RequestMapping("/api/v1/products")
class ProductController(
    private val productService: ProductService,
) {

    @PostMapping
    fun createProduct(@RequestBody dto: ProductRequestDto): ResponseEntity<ProductResponseDto> {
        val response = productService.createProduct(dto)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: Long): ResponseEntity<ProductResponseDto> {
        val response = productService.getProductById(id)
        return ResponseEntity.ok(response)
    }

    @GetMapping
    fun getAllProducts(): ResponseEntity<List<ProductResponseDto>> {
        val response = productService.getAllProducts()
        return ResponseEntity.ok(response)
    }

    @GetMapping("/paginated")
    fun getAllProductsPaginated(pageable: Pageable): ResponseEntity<Page<ProductResponseDto>> {
        val response = productService.getAllProductsPaginated(pageable)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/by-name")
    fun getProductsByName(@RequestParam name: String): ResponseEntity<List<ProductResponseDto>> {
        val response = productService.getProductsByName(name)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/by-category/{categoryId}")
    fun getProductsByCategory(@PathVariable categoryId: Long): ResponseEntity<List<ProductResponseDto>> {
        val response = productService.getProductsByCategory(categoryId)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/by-category/{categoryId}/paginated")
    fun getProductsByCategoryPaginated(
        @PathVariable categoryId: Long,
        pageable: Pageable,
    ): ResponseEntity<Page<ProductResponseDto>> {
        val response = productService.getProductsByCategoryPaginated(categoryId, pageable)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/available")
    fun getAvailableProducts(): ResponseEntity<List<ProductResponseDto>> {
        val response = productService.getAvailableProducts()
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{id}")
    fun updateProduct(
        @PathVariable id: Long,
        @RequestBody dto: ProductRequestDto,
    ): ResponseEntity<ProductResponseDto> {
        val response = productService.updateProduct(id, dto)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: Long): ResponseEntity<Boolean> {
        val result = productService.deleteProduct(id)
        return ResponseEntity.ok(result)
    }
}

@RestController
@RequestMapping("/api/v1/transactions")
class TransactionController(
    private val transactionService: TransactionService,
) {

    @PostMapping
    fun createTransaction(@RequestBody dto: TransactionRequestDto): ResponseEntity<TransactionResponseDto> {
        val response = transactionService.createTransaction(dto)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping("/{id}")
    fun getTransactionById(@PathVariable id: Long): ResponseEntity<TransactionResponseDto> {
        val response = transactionService.getTransactionById(id)
        return ResponseEntity.ok(response)
    }

    @GetMapping
    fun getAllTransactions(@RequestParam role: UserRole): ResponseEntity<List<TransactionResponseDto>> {
        val response = transactionService.getAllTransactions(role)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/paginated")
    fun getAllTransactionsPaginated(pageable: Pageable): ResponseEntity<Page<TransactionResponseDto>> {
        val response = transactionService.getAllTransactionsPaginated(pageable)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/by-user/{userId}")
    fun getTransactionsByUserId(@PathVariable userId: Long): ResponseEntity<List<TransactionResponseDto>> {
        val response = transactionService.getTransactionsByUserId(userId)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/by-user/{userId}/paginated")
    fun getTransactionsByUserIdPaginated(
        @PathVariable userId: Long,
        pageable: Pageable,
    ): ResponseEntity<Page<TransactionResponseDto>> {
        val response = transactionService.getTransactionsByUserIdPaginated(userId, pageable)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/by-date")
    fun getTransactionsByDate(@RequestParam date: LocalDate): ResponseEntity<List<TransactionResponseDto>> {
        val response = transactionService.getTransactionsByDate(date)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/by-date-range")
    fun getTransactionsByDateRange(
        @RequestParam startDate: LocalDate,
        @RequestParam endDate: LocalDate,
    ): ResponseEntity<List<TransactionResponseDto>> {
        val response = transactionService.getTransactionsByDateRange(startDate, endDate)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/by-date-range/paginated")
    fun getTransactionsByDateRangePaginated(
        @RequestParam startDate: LocalDate,
        @RequestParam endDate: LocalDate,
        pageable: Pageable,
    ): ResponseEntity<Page<TransactionResponseDto>> {
        val response = transactionService.getTransactionsByDateRangePaginated(startDate, endDate, pageable)
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{id}")
    fun updateTransaction(
        @PathVariable id: Long,
        @RequestBody dto: TransactionRequestDto,
    ): ResponseEntity<TransactionResponseDto> {
        val response = transactionService.updateTransaction(id, dto)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{id}")
    fun deleteTransaction(@PathVariable id: Long): ResponseEntity<Boolean> {
        val result = transactionService.deleteTransaction(id)
        return ResponseEntity.ok(result)
    }

}

@RestController
@RequestMapping("/api/v1/transaction-items")
class TransactionItemController(
    private val transactionItemService: TransactionItemService,
) {

    @PostMapping
    fun createTransactionItem(@RequestBody dto: TransactionItemRequestDto): ResponseEntity<TransactionItemResponseDto> {
        val response = transactionItemService.createTransactionItem(dto)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping("/{id}")
    fun getTransactionItemById(@PathVariable id: Long): ResponseEntity<TransactionItemResponseDto> {
        val response = transactionItemService.getTransactionItemById(id)
        return ResponseEntity.ok(response)
    }

    @GetMapping
    fun getAllTransactionItems(): ResponseEntity<List<TransactionItemResponseDto>> {
        val response = transactionItemService.getAllTransactionItems()
        return ResponseEntity.ok(response)
    }

    @GetMapping("/paginated")
    fun getAllTransactionItemsPaginated(pageable: Pageable): ResponseEntity<Page<TransactionItemResponseDto>> {
        val response = transactionItemService.getAllTransactionItemsPaginated(pageable)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/by-transaction/{transactionId}")
    fun getTransactionItemsByTransactionId(@PathVariable transactionId: Long): ResponseEntity<List<TransactionItemResponseDto>> {
        val response = transactionItemService.getTransactionItemsByTransactionId(transactionId)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/by-product/{productId}")
    fun getTransactionItemsByProductId(@PathVariable productId: Long): ResponseEntity<List<TransactionItemResponseDto>> {
        val response = transactionItemService.getTransactionItemsByProductId(productId)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/by-product/{productId}/paginated")
    fun getTransactionItemsByProductIdPaginated(
        @PathVariable productId: Long,
        pageable: Pageable,
    ): ResponseEntity<Page<TransactionItemResponseDto>> {
        val response = transactionItemService.getTransactionItemsByProductIdPaginated(productId, pageable)
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{id}")
    fun updateTransactionItem(
        @PathVariable id: Long,
        @RequestBody dto: TransactionItemRequestDto,
    ): ResponseEntity<TransactionItemResponseDto> {
        val response = transactionItemService.updateTransactionItem(id, dto)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{id}")
    fun deleteTransactionItem(@PathVariable id: Long): ResponseEntity<Boolean> {
        val result = transactionItemService.deleteTransactionItem(id)
        return ResponseEntity.ok(result)
    }
}

@RestController
@RequestMapping("/api/v1/payment-transactions")
class UserPaymentTransactionController(
    private val userPaymentTransactionService: UserPaymentTransactionService,
) {

    @PostMapping
    fun createPaymentTransaction(@RequestBody dto: UserPaymentTransactionRequestDto): ResponseEntity<UserPaymentTransactionResponseDto> {
        val response = userPaymentTransactionService.createPaymentTransaction(dto)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping("/{id}")
    fun getPaymentTransactionById(@PathVariable id: Long): ResponseEntity<UserPaymentTransactionResponseDto> {
        val response = userPaymentTransactionService.getPaymentTransactionById(id)
        return ResponseEntity.ok(response)
    }

    @GetMapping
    fun getAllPaymentTransactions(): ResponseEntity<List<UserPaymentTransactionResponseDto>> {
        val response = userPaymentTransactionService.getAllPaymentTransactions()
        return ResponseEntity.ok(response)
    }

    @GetMapping("/paginated")
    fun getAllPaymentTransactionsPaginated(pageable: Pageable): ResponseEntity<Page<UserPaymentTransactionResponseDto>> {
        val response = userPaymentTransactionService.getAllPaymentTransactionsPaginated(pageable)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/by-user/{userId}")
    fun getPaymentTransactionsByUserId(@PathVariable userId: Long): ResponseEntity<List<UserPaymentTransactionResponseDto>> {
        val response = userPaymentTransactionService.getPaymentTransactionsByUserId(userId)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/by-user/{userId}/paginated")
    fun getPaymentTransactionsByUserIdPaginated(
        @PathVariable userId: Long,
        pageable: Pageable,
    ): ResponseEntity<Page<UserPaymentTransactionResponseDto>> {
        val response = userPaymentTransactionService.getPaymentTransactionsByUserIdPaginated(userId, pageable)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/by-date")
    fun getPaymentTransactionsByDate(@RequestParam date: LocalDate): ResponseEntity<List<UserPaymentTransactionResponseDto>> {
        val response = userPaymentTransactionService.getPaymentTransactionsByDate(date)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/by-date-range")
    fun getPaymentTransactionsByDateRange(
        @RequestParam startDate: LocalDate,
        @RequestParam endDate: LocalDate,
    ): ResponseEntity<List<UserPaymentTransactionResponseDto>> {
        val response = userPaymentTransactionService.getPaymentTransactionsByDateRange(startDate, endDate)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/by-date-range/paginated")
    fun getPaymentTransactionsByDateRangePaginated(
        @RequestParam startDate: LocalDate,
        @RequestParam endDate: LocalDate,
        pageable: Pageable,
    ): ResponseEntity<Page<UserPaymentTransactionResponseDto>> {
        val response = userPaymentTransactionService.getPaymentTransactionsByDateRangePaginated(startDate, endDate, pageable)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/total-by-user/{userId}")
    fun getTotalPaymentByUserId(@PathVariable userId: Long): ResponseEntity<java.math.BigDecimal> {
        val response = userPaymentTransactionService.getTotalPaymentByUserId(userId)
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{id}")
    fun updatePaymentTransaction(
        @PathVariable id: Long,
        @RequestBody dto: UserPaymentTransactionRequestDto,
    ): ResponseEntity<UserPaymentTransactionResponseDto> {
        val response = userPaymentTransactionService.updatePaymentTransaction(id, dto)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{id}")
    fun deletePaymentTransaction(@PathVariable id: Long): ResponseEntity<Boolean> {
        val result = userPaymentTransactionService.deletePaymentTransaction(id)
        return ResponseEntity.ok(result)
    }
}