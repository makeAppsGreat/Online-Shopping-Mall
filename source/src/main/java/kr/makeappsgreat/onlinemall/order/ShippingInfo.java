package kr.makeappsgreat.onlinemall.order;

import kr.makeappsgreat.onlinemall.model.Address;
import kr.makeappsgreat.onlinemall.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Getter
public class ShippingInfo extends BaseEntity {

    @OneToOne
    @NotNull
    @Setter
    private Order order;

    @NotNull @NotBlank
    private String receiver;

    @NotNull @NotBlank
    private String contact;

    private String contact2;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "zipcode", column = @Column(name = "dst_zipcode", nullable = false)),
            @AttributeOverride(name = "address", column = @Column(name = "dst_address", nullable = false)),
            @AttributeOverride(name = "address2", column = @Column(name = "dst_address2"))
    })
    @Valid
    private Address destination;

    @NotNull @NotBlank
    private String shippingCompany;
    @NotNull @NotBlank
    private String trackingNo;
    private String memo;
}
