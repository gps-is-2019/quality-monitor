package it.unisa.Amigo.gruppo.domain;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Dipartimento implements Serializable {
    private final static long serialVersionUID = 41L;

    @Id
    private int id;

    @NonNull
    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set <ConsiglioDidattico> consiglioDidattico;

    @OneToOne(cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Supergruppo supergruppoGAQR;



}
