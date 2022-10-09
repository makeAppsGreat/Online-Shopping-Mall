package kr.makeappsgreat.onlinemall.product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Manufacturer> findByName(String name);
}
