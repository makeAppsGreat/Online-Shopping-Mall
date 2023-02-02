package kr.makeappsgreat.onlinemall.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
@Getter
@EqualsAndHashCode(of = "id")
public class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected boolean canEqual(Object other) {
        // if (!(other instanceof NamedEntity)) return false;
        if (!this.getClass().isInstance(other)) return false;
        if (this.id == null || ((BaseEntity) other).id == null) return false;

        return true;
    }
}
