package kr.makeappsgreat.onlinemall.model;

import kr.makeappsgreat.onlinemall.common.constraints.EditProfileGroup;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;

@Embeddable
@Getter @Setter
public class Address {

    @NotNull(groups = {Default.class, EditProfileGroup.class})
    @Pattern(regexp = "\\d{5}", message = "{message.invalid-pattern}", groups = {Default.class, EditProfileGroup.class})
    private String zipcode;
    @NotBlank(groups = {Default.class, EditProfileGroup.class})
    private String address;
    private String address2;
}
