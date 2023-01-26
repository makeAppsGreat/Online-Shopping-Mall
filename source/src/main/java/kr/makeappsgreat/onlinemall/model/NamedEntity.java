package kr.makeappsgreat.onlinemall.model;

import lombok.Getter;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;

@MappedSuperclass
@Getter
public class NamedEntity extends BaseEntity {

    @NotBlank
    protected String name;

    @Override
    public String toString() {
        return String.format("(%s) %s", this.getClass().getSimpleName(), name);
    }
}
