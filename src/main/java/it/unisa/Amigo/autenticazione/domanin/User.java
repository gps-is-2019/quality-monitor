package it.unisa.Amigo.autenticazione.domanin;

import it.unisa.Amigo.gruppo.domain.Persona;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Questa classe modella l'oggetto user.
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
public  class User implements Serializable {

    private final static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @NonNull
    private String email;

    @NonNull
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Role> roles = new HashSet<>();


    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Persona persona;

    public void addRole(Role r){
        roles.add(r);
    }
    public int getId(){return this.id;}




}
