package kr.makeappsgreat.onlinemall.product;

import kr.makeappsgreat.onlinemall.model.NamedEntity;

import javax.persistence.Entity;

@Entity
public class Manufacturer extends NamedEntity {

    public static Manufacturer of(Long id) {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setId(id);

        return manufacturer;
    }

    public static Manufacturer of(String name) {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setName(name);

        return manufacturer;
    }
}
