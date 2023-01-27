package kr.makeappsgreat.onlinemall.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OrderStatus {

    // 입금대기, 결제완료, 상품준비중, 배송중, 배송완료, 취소요청, 취소완료
    PAYMENT_WAIT("order.status.payment-wait"),
    PAYMENT_DONE("order.status.payment-done"),
    READY("order.status.ready"),
    SHIPPING("order.status.shipping"),
    COMPLETE("order.status.complete"),
    CANCEL_WAIT("order.status.cancel-wait"),
    CANCEL_DONE("order.status.cancel-done");

    private final String code;
}
