package be.vdab.artikels.domain;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity @Table(name = "artikels")
public class Artikel {
    @Id
    private long id;
    private String naam;
    private BigDecimal prijs;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "artikelGroepId")
    private ArtikelGroep artikelGroep;

    public long getId() {
        return id;
    }

    public String getNaam() {
        return naam;
    }

    public BigDecimal getPrijs() {
        return prijs;
    }

    public ArtikelGroep getArtikelGroep() {
        return artikelGroep;
    }
}
