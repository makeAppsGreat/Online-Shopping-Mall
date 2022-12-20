package kr.makeappsgreat.onlinemall.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;

@MappedSuperclass
@NoArgsConstructor @SuperBuilder
@Getter
@EqualsAndHashCode(of = "id")
public class NamedEntity {

    @Id @GeneratedValue
    protected Long id;
    @NotBlank
    protected String name;
}
