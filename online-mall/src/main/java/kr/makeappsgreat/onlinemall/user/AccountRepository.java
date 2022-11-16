package kr.makeappsgreat.onlinemall.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository<T extends Account> extends JpaRepository<T, Long> {

    Optional<T> findByUsername(String username);

    boolean existsByUsername(String username);
}
