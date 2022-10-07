package kr.makeappsgreat.onlinemall.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@RequiredArgsConstructor @SuperBuilder
@Getter @Setter
@EqualsAndHashCode(of = "id")
public class NamedEntity {

    @Id @GeneratedValue
    private Long id;
    private String name;
}
