package kr.makeappsgreat.onlinemall.product;

import kr.makeappsgreat.onlinemall.model.NamedEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@SuperBuilder
@Getter @Setter
public class Product extends NamedEntity {

    private int price;

    // private Manufacturer manufacturer;
    // private Category category;
    @OneToMany
    @JoinColumn(name = "product_id")
    private Set<Product> options;

    private String imageLink;
    private String simpleDetail;
    private String detail;

    // 상품 등록일
    // 관련상품
}
