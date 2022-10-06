package kr.makeappsgreat.onlinemall.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter @Setter
@RequiredArgsConstructor @SuperBuilder
public class NamedEntity {

    @Id @GeneratedValue
    private Long id;
    private String name;
}
