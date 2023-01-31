package kr.makeappsgreat.onlinemall.order;

import kr.makeappsgreat.onlinemall.config.ApplicationConfig;
import kr.makeappsgreat.onlinemall.order.transaction.Card;
import kr.makeappsgreat.onlinemall.order.transaction.Transaction;
import kr.makeappsgreat.onlinemall.order.transaction.TransactionDetailRepository;
import kr.makeappsgreat.onlinemall.order.transaction.TransactionRepository;
import kr.makeappsgreat.onlinemall.product.Product;
import kr.makeappsgreat.onlinemall.product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class OrderRepositoryTest {

    @Autowired private OrderRepository orderRepository;
    @Autowired private TransactionRepository transactionRepository;
    @Autowired private TransactionDetailRepository<Card> transactionDetailRepository;
    @Autowired private OrderDetailRepository orderDetailRepository;
    @Autowired private ProductRepository productRepository;

    private List<Product> products;
    private int deliveryFee = 3_000;

    @BeforeEach
    void beforeEach() {
        products = new ArrayList<>();
        productRepository.findByNameContainingIgnoreCase("프리머스", Pageable.unpaged())
                .stream().sorted(Comparator.comparingInt(Product::getPrice))
                .iterator().forEachRemaining(products::add);
        assertThat(products).hasSizeGreaterThan(1);
    }

    @Test
    void contextLoads() {

    }

    @Nested
    class Create {

        private ModelMapper modelMapper = new ApplicationConfig().modelMapper();
        private Transaction transaction;
        private Card transactionDetail;
        private OrderDetail orderDetail;
        private OrderDetail orderDetail2;

        @Test
        void save() {
            // Given
            Order order = createOrder();
            long count = orderRepository.count();
            LocalDateTime start = LocalDateTime.now();

            // When
            Order savedOrder = orderRepository.save(order);
            transactionDetailRepository.save(transactionDetail);
            transactionRepository.save(transaction);
            orderDetailRepository.saveAll(savedOrder.getItems());


            transactionRepository.flush();
            transactionDetailRepository.flush();
            orderDetailRepository.flush();

            // Then
            assertThat(savedOrder).isNotNull();
            assertThat(orderRepository.count() - count).isEqualTo(1L);
            assertThat(savedOrder.getOrderDate()).isAfter(start);
        }

        private Order createOrder() {
            Map<String, Object> orderMap = new HashMap<>();
            Map<String, Object> destinationMap = new HashMap<>();
            Map<String, Object> transactionMap = new HashMap<>();
            Map<String, Object> transactionDetailMap = new HashMap<>();
            Map<String, Object> orderDetailMap = new HashMap<>();
            Map<String, Object> orderDetail2Map = new HashMap<>();
            int subTotal;
            int grandTotal;

            orderDetailMap.put("product", products.get(0));
            orderDetailMap.put("quantity", 1);
            orderDetailMap.put("total", products.get(0).getPrice());

            orderDetail2Map.put("product", products.get(1));
            orderDetail2Map.put("quantity", 1);
            orderDetail2Map.put("total", products.get(1).getPrice());

            orderDetail = modelMapper.map(orderDetailMap, OrderDetail.class);
            orderDetail2 = modelMapper.map(orderDetail2Map, OrderDetail.class);
            orderDetail.addOption(orderDetail2);
            subTotal = orderDetail.getTotal() + orderDetail2.getTotal();
            grandTotal = subTotal + deliveryFee;

            //transactionDetailMap.put("transaction", transaction);
            transactionDetailMap.put("amount", grandTotal);
            transactionDetailMap.put("tax", grandTotal);
            transactionDetailMap.put("serviceFee", 0);
            transactionDetailMap.put("vat", grandTotal / 1.1);
            transactionDetailMap.put("transactionDate", LocalDateTime.now());
            transactionDetailMap.put("admissionTypeCode", "01");
            transactionDetailMap.put("admissionDate", LocalDateTime.now().format(Card.ADMISSION_DATE_FORMATTER));
            transactionDetailMap.put("admissionState", "SUCCESS");
            transactionDetailMap.put("cardCorpCode", "CB");
            transactionDetailMap.put("cardNo", "9425-****-****-****");
            transactionDetailMap.put("cardAuthNo", "12345678");
            transactionDetailMap.put("cardInstCount", "0");

            //transactionMap.put("order", orderMap);
            transactionMap.put("orderDesc", products.get(0).getName());
            //transactionMap.put("transactionDetail", transactionDetail);
            transactionMap.put("subTotal", subTotal);
            transactionMap.put("deliveryFee", deliveryFee);

            destinationMap.put("zipcode", "42700");
            destinationMap.put("address", "대구광역시 달서구");

            transaction = modelMapper.map(transactionMap, Transaction.class);
            transactionDetail = modelMapper.map(transactionDetailMap, Card.class);
            transaction.setTransactionDetail(transactionDetail);


            orderMap.put("items", List.of(orderDetail, orderDetail2));
            orderMap.put("transactions", List.of(transaction));
            orderMap.put("grandTotal", grandTotal);
            orderMap.put("status", OrderStatus.PAYMENT_DONE);
            orderMap.put("receiver", "김가연");
            orderMap.put("contact", "010-1234-5678");
            orderMap.put("destination", destinationMap);
            orderMap.put("orderDate", LocalDateTime.now());

            Order order = modelMapper.map(orderMap, Order.class);
            transaction.setOrder(order);
            orderDetail.setOrder(order);


            return order;
        }
    }
}