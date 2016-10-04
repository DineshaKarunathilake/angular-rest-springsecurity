package net.dontdrinkandroot.example.angularrestspringsecurity.dao.newmeasuremententry;

import net.dontdrinkandroot.example.angularrestspringsecurity.dao.JpaDao;
import net.dontdrinkandroot.example.angularrestspringsecurity.dao.newmeasuremententry.NewMeasurementEntryDao;
import net.dontdrinkandroot.example.angularrestspringsecurity.entity.NewMeasurementEntry;
import net.dontdrinkandroot.example.angularrestspringsecurity.entity.NewMeasurementEntry;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by DineshaK on 3/10/2016.
 */
public class JpaNewMeasurementEntryDao  extends JpaDao<NewMeasurementEntry, Long> implements NewMeasurementEntryDao
{
    public JpaNewMeasurementEntryDao()
    {
        super(NewMeasurementEntry.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewMeasurementEntry> findAll()
    {
        final CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
        final CriteriaQuery<NewMeasurementEntry> criteriaQuery = builder.createQuery(NewMeasurementEntry.class);

        Root<NewMeasurementEntry> root = criteriaQuery.from(NewMeasurementEntry.class);
        criteriaQuery.orderBy(builder.desc(root.get("date")));

        TypedQuery<NewMeasurementEntry> typedQuery = this.getEntityManager().createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }
}
