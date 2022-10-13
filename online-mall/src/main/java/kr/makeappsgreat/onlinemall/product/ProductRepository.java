package kr.makeappsgreat.onlinemall.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByManufacturer(Manufacturer manufacturer, Pageable pageable);
    Page<Product> findByCategory(Category category, Pageable pageable);
    Page<Product> findByManufacturerAndCategory(Manufacturer manufacturer, Category category, Pageable pageable);

    Page<Product> findByNameContaining(String name, Pageable pageable);
    Page<Product> findByNameContainingOrSimpleDetailContaining(String name, String simpleDetail, Pageable pageable);
}
