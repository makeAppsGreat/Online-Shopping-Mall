package kr.makeappsgreat.onlinemall.order;

import kr.makeappsgreat.onlinemall.model.BaseEntity;
import kr.makeappsgreat.onlinemall.product.Product;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@MappedSuperclass
@Getter
public class Item<T extends Item> extends BaseEntity {

    @OneToOne
    @NotNull
    protected Product product;

    @Positive
    protected int quantity;

    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    protected List<T> options;

    @ManyToOne(fetch = FetchType.LAZY)
    protected T parent;

    public void addOption(T item) {
        if (options == null) options = new ArrayList<>();

        options.add(item);
        item.parent = this;
    }

    public <S extends ItemRequest> void setOptions(List<S> options)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        for (S request : options) {
            T item = (T) this.getClass().getDeclaredConstructor().newInstance();
            item.product = Product.of(request.getProductId());
            item.quantity = request.getQuantity();

            this.addOption(item);
        }
    }

    public void include(T item) {
        if (item == null || item.product == null) {
            throw new NullPointerException("Unexpected usage : Item or Product in item is null.");
        } else if (!item.product.equals(this.product)) throw new RuntimeException("Unexpected usage : Product is not equal.");

        List<T> srcOptions = item.options;
        if (srcOptions != null) {
            if (this.options == null || this.options.isEmpty()) {
                for (T source : srcOptions) this.addOption(source);
            } else {
                for (T source : srcOptions) {
                    boolean flag = false;

                    for (T option : this.options) {
                        if (source.product.equals(option.product)) {
                            option.quantity += source.quantity;

                            flag = true;
                            break;
                        }
                    }

                    if (!flag) this.addOption(source);
                }
            }
        }
        quantity += item.getQuantity();
    }
}
