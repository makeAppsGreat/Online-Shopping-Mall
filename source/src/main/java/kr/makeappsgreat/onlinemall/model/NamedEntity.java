package kr.makeappsgreat.onlinemall.model;

import lombok.Getter;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@MappedSuperclass
@Getter
public class NamedEntity extends BaseEntity {

    @NotNull @NotBlank
    protected String name;

    @Override
    public String toString() {
        if (name != null) return String.format("(%s) %s", this.getClass().getSimpleName(), name);
        else return String.format("(%s) [%d]", this.getClass().getSimpleName(), id);
    }
}
