package song.pan.toolkit.jpa.entity.primary;

import javax.persistence.*;

/**
 * @author Song Pan
 * @version 1.0.0
 */
@Entity
@Table(name = "primary_city", catalog = "world", schema = "world")
public class PrimaryCity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "Name")
    private String name;

    @Column(name = "CountryCode")
    private String countryCode;

    @Column(name = "District")
    private String district;

    @Column(name = "Population")
    private String population;

    @Override
    public String toString() {
        return "PrimaryCity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", district='" + district + '\'' +
                ", population='" + population + '\'' +
                '}';
    }
}
