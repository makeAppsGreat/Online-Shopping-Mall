package kr.makeappsgreat.onlinemall.order.transaction;

import lombok.Getter;

import javax.persistence.Entity;

/**
 * Refer to <a href="https://developer.pay.naver.com/docs/v2/api">네이버페이 개발자센터</a>
 */
@Entity
@Getter
public class Card extends TransactionDetail {

}
