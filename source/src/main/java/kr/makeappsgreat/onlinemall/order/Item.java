package kr.makeappsgreat.onlinemall.order;

import kr.makeappsgreat.onlinemall.model.BaseEntity;
import kr.makeappsgreat.onlinemall.product.Product;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@MappedSuperclass
@Getter
public class Item<T extends Item> extends BaseEntity {

    @OneToOne
    @NotNull
    protected Product product;

    @Positive
    @Setter
    protected int quantity;

    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
    protected List<T> options;

    @ManyToOne(fetch = FetchType.LAZY)
    protected T parent;

    public void addOption(T item) {
        if (options == null) options = new ArrayList<>();

        options.add(item);
        item.parent = this;
    }
}
