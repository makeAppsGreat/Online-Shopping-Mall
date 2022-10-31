package kr.makeappsgreat.onlinemall.model;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

@Embeddable
@Builder
@Getter
public class Address {

    @NotBlank
    private String zipcode;
    @NotBlank
    private String address;
    private String address2;
}
