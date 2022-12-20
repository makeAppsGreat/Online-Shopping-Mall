package kr.makeappsgreat.onlinemall.product;

import kr.makeappsgreat.onlinemall.model.NamedEntity;

import javax.persistence.Entity;

@Entity
public class Category extends NamedEntity {

    /** @TODO : Add order */

    public static Category of(Long id) {
        Category category = new Category();
        category.id = id;

        return category;
    }

    public static Category of(String name) {
        Category category = new Category();
        category.name = name;

        return category;
    }
}
