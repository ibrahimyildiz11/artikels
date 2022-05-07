package be.vdab.artikels.repositories;

import be.vdab.artikels.domain.Artikel;
import be.vdab.artikels.domain.ArtikelGroep;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest(showSql = false)
@Sql({"/insertArtikelGroepen.sql", "/insertArtikels.sql"})
class ArtikelRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static final String ARTIKELS = "artikels";
    private final ArtikelRepository repository;

    ArtikelRepositoryTest(ArtikelRepository repository) {
        this.repository = repository;
    }

    @Test
    void findByPrijsBetween() {
        var van = BigDecimal.TEN;
        var tot = BigDecimal.valueOf(20);
        assertThat(repository.findByPrijsBetween(van,tot))
                .hasSize(countRowsInTableWhere(ARTIKELS, "prijs between 10 and 20"))
                .extracting(Artikel::getPrijs)
                .allSatisfy(prijs -> assertThat(prijs).isBetween(van,tot));
    }

    @Test
    void findHoogstePrijs() {
        assertThat(repository.findHoogstePrijs())
                .isEqualByComparingTo(
                        jdbcTemplate.queryForObject("select max(prijs) from artikels", BigDecimal.class));
    }
    @Test
    void findByArtikelGroepNaam() {
        var groep = "groep2";
        assertThat(repository.findByArtikelGroepNaam(groep))
                .hasSize(countRowsInTableWhere(ARTIKELS,
                        "artikelGroepId = (select id from artikelgroepen where naam = 'groep2')"))
                .first()
                .extracting(Artikel::getArtikelGroep)
                .extracting(ArtikelGroep::getNaam)
                .isEqualTo(groep); }
}