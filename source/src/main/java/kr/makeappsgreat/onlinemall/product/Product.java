package kr.makeappsgreat.onlinemall.product;

import kr.makeappsgreat.onlinemall.model.NamedEntity;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
public class Product extends NamedEntity {

    @PositiveOrZero
    private int price;

    @ManyToOne
    @NotNull
    private Manufacturer manufacturer;

    @ManyToOne
    @NotNull
    private Category category;

    @OneToMany
    @JoinTable(inverseJoinColumns = @JoinColumn(name = "option_id"))
    private Set<Product> options;

    /* @OneToMany
    private Set<Product> relatedProducts = new HashSet<>(); // 관련상품 */

    private String imageLink;
    private String simpleDetail;
    private String detail;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime registeredDate;
}
