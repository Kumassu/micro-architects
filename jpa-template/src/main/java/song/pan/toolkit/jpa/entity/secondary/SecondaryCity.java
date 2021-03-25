package song.pan.toolkit.jpa.entity.secondary;

import javax.persistence.*;

/**
 * @author Song Pan
 * @version 1.0.0
 */
@Entity
@Table(name = "secondary_city", catalog = "toolkit", schema = "toolkit")
public class SecondaryCity {


    @Id
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

}
