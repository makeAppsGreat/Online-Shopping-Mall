package kr.makeappsgreat.onlinemall.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;

@MappedSuperclass
@NoArgsConstructor @SuperBuilder
@Getter @Setter(AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class NamedEntity {

    @Id @GeneratedValue
    private Long id;
    @NotBlank
    private String name;
}
