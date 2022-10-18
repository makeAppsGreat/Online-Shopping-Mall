package kr.makeappsgreat.onlinemall.product;

import kr.makeappsgreat.onlinemall.model.NamedEntity;

import javax.persistence.Entity;

@Entity
public class Category extends NamedEntity {

    /** @TODO : Add order */

    public static Category of(Long id) {
        Category category = new Category();
        category.setId(id);

        return category;
    }
}
