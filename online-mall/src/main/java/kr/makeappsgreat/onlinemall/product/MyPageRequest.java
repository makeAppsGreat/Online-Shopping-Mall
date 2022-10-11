package kr.makeappsgreat.onlinemall.product;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter @Setter
public class MyPageRequest {

    /** @TODO : Consider to move package to common. */

    private String keyword;
    @Min(1)
    private int page = 1;
    private int sort;
}
