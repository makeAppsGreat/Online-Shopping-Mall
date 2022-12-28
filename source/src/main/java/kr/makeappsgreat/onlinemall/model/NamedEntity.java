package kr.makeappsgreat.onlinemall.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;

@MappedSuperclass
@Getter
@EqualsAndHashCode(of = "id")
public class NamedEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    @NotBlank
    protected String name;
}
