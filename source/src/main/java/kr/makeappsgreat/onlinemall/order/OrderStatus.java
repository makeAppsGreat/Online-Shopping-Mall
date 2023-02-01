package kr.makeappsgreat.onlinemall.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
@Getter
public enum OrderStatus {

    // 입금대기, 결제완료, 상품준비중, 배송중, 배송완료, 취소요청, 취소완료, 환불요청, 환불완료
    PAYMENT_WAIT("order.status.payment-wait"),
    PAYMENT_DONE("order.status.payment-done"),
    READY("order.status.ready"),
    SHIPPING("order.status.shipping"),
    COMPLETE("order.status.complete"),
    CANCEL_WAIT("order.status.cancel-wait"),
    CANCEL_DONE("order.status.cancel-done"),
    REFUND_WAIT("order.status.refund-wait"),
    REFUND_DONE("order.status.refund-done");

    private final String code;

    public boolean isChangeableStatus(OrderStatus status) {
        if (this.ordinal() > status.ordinal()) return false;

        switch (status) {
            case CANCEL_WAIT:
                if (!Set.of(PAYMENT_WAIT, PAYMENT_DONE).contains(this)) return false;
                break;
            case REFUND_WAIT:
                if (!Set.of(READY, SHIPPING, COMPLETE).contains(this)) return false;
                break;
        }


        return true;
    }
}
