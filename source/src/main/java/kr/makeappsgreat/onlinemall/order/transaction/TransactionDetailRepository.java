package kr.makeappsgreat.onlinemall.order.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionDetailRepository<T extends TransactionDetail> extends JpaRepository<T, Long> {
}
