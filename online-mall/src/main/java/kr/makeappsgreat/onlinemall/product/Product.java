package kr.makeappsgreat.onlinemall.product;

import kr.makeappsgreat.onlinemall.model.NamedEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@SuperBuilder
@Getter @Setter
public class Product extends NamedEntity {

    private int price;
    @Builder.Default
    private LocalDateTime registeredDate = LocalDateTime.now();

    @ManyToOne
    private Manufacturer manufacturer;
    @ManyToOne
    private Category category;
    @OneToMany
    @Builder.Default
    private Set<Product> options =  new HashSet<>();
    @OneToMany
    @Builder.Default
    private Set<Product> relatedProducts = new HashSet<>();

    private String imageLink;
    private String simpleDetail;
    private String detail;

    @Override
    public String toString() {
        return getName();
    }
}
