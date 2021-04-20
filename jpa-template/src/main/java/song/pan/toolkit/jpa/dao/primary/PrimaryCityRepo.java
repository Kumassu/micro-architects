package song.pan.toolkit.jpa.dao.primary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import song.pan.toolkit.jpa.entity.primary.PrimaryCity;

/**
 * @author Song Pan
 * @version 1.0.0
 */
@Repository
public interface PrimaryCityRepo extends JpaRepository<PrimaryCity, Long>, JpaSpecificationExecutor<PrimaryCity> {

}
