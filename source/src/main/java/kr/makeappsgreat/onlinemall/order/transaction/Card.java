package kr.makeappsgreat.onlinemall.order.transaction;

import lombok.Getter;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.format.DateTimeFormatter;

/**
 * Refer to <a href="https://developer.pay.naver.com/docs/v2/api">네이버페이 개발자센터</a>
 */
@Entity
@Getter
public class Card extends TransactionDetail {

    public static DateTimeFormatter ADMISSION_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @NotNull @NotBlank
    private String admissionTypeCode; // 01 : 원결제 승인건, 03 : 전체취소 건, 04: 부분취소 건
    @NotNull @NotBlank
    private String admissionDate; // 결제/취소 일시(YYYYMMDDHH24MMSS)
    @NotNull @NotBlank
    private String admissionState; // SUCCESS : 완료, FAIL : 실패
    @NotNull @NotBlank
    private String cardCorpCode; // 카드사
    @NotNull @NotBlank
    private String cardNo; // 일부 마스킹 된 신용카드 번호
    @NotNull @NotBlank
    private String cardAuthNo; // 카드승인번호
    @PositiveOrZero
    private int cardInstCount; // 할부 개월 수 (일시불은 0)
}
