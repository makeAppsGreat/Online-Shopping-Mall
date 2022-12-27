package kr.makeappsgreat.onlinemall.product;

import kr.makeappsgreat.onlinemall.model.NamedEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor @SuperBuilder
@Getter
public class Product extends NamedEntity {

    @Min(0)
    private int price;

    @ManyToOne
    @NotNull
    private Manufacturer manufacturer;

    @ManyToOne
    @NotNull
    private Category category;

    @OneToMany
    @Builder.Default
    private Set<Product> options = new HashSet<>();

    /* @OneToMany
    @Builder.Default
    private Set<Product> relatedProducts = new HashSet<>(); // 관련상품 */

    private String imageLink;
    private String simpleDetail;
    private String detail;

    @CreationTimestamp
    private LocalDateTime registeredDate;

    @Override
    public String toString() {
        return getName();
    }
}
