package kr.makeappsgreat.onlinemall.common;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SimpleResult {

    private String request;
    private boolean result;
    private int code;
    private String name;
    private String message;
}
