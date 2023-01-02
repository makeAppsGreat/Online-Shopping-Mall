package kr.makeappsgreat.onlinemall.common;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SimpleResult {

    private final String request;
    private final boolean result;
    private final int code;
    private final String name;
    private final String message;
}
