package kr.makeappsgreat.onlinemall.order;

import kr.makeappsgreat.onlinemall.config.ApplicationConfig;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void contextLoads() {

    }

    @Nested
    class Create {

        private ModelMapper modelMapper = new ApplicationConfig().modelMapper();

        @Test
        void save() {
            // Given
            Order order = createOrder();
            long count = orderRepository.count();
            LocalDateTime start = LocalDateTime.now();

            // When
            Order savedOrder = orderRepository.save(order);

            // Then
            assertThat(savedOrder).isNotNull();
            assertThat(orderRepository.count() - count).isEqualTo(1L);
            assertThat(savedOrder.getOrderDate()).isAfter(start);
        }

        private Order createOrder() {
            Map<String, Object> order = new HashMap<>();
            Map<String, Object> destination = new HashMap<>();

            destination.put("zipcode", "42700");
            destination.put("address", "대구광역시 달서구");
            order.put("grandTotal", 29_800);
            order.put("status", OrderStatus.PAYMENT_DONE);
            order.put("receiver", "김가연");
            order.put("contact", "010-1234-5678");
            order.put("destination", destination);

            return modelMapper.map(order, Order.class);
        }
    }
}