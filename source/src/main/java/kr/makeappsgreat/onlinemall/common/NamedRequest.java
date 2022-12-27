package kr.makeappsgreat.onlinemall.common;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class NamedRequest {

    @NotBlank
    private String name;
}
