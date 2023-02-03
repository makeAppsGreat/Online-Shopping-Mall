package kr.makeappsgreat.onlinemall.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SimpleResult {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String request;
    private final boolean result;
    private final int code;
    private final String name;
    private final String message;
}
