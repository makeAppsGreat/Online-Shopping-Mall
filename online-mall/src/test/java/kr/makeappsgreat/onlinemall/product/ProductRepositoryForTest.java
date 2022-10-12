package kr.makeappsgreat.onlinemall.product;

import java.util.Optional;

/**
 * NOT A TEST CLASS
 * Repository for Spring Boot test
 */
public interface ProductRepositoryForTest extends ProductRepository {

    Optional<Product> findTopByOrderById();
}
