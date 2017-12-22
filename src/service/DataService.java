package service;

import model.QuakesEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

//Created by Gotcha on 2017/12/11.
public class DataService {
    private Configuration config = null;
    private SessionFactory sessionFactory = null;
    private Session session = null;
    private Transaction tx = null;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

    public void init() {
        config = new Configuration().configure("./hibernate.cfg.xml");
        sessionFactory = config.buildSessionFactory();
        session = sessionFactory.openSession();
    }

    public void update() {
        UpdateService updateService = new UpdateService(session);
        updateService.update();
    }

    public QuakesEntity getById(int id) {
        QuakesEntity earthquake = session.get(QuakesEntity.class, id);
        return earthquake;
    }

//    public List<QuakesEntity> getByAreaId(int areaId) {
//        Query query = session.createQuery("from QuakesEntity where areaId=?");
//        query.setParameter(0, areaId);
//        return query.list();
//    }

    public List<QuakesEntity> getByUTCDate(String dataString) {
        Query query = session.createQuery("from QuakesEntity where utcDate=?1");
        query.setParameter(1, dataString);
        return query.list();
    }

    public List<QuakesEntity> getByUTCDateRange(String fromDateString, String toDateString) {
        Query query = session.createQuery("from QuakesEntity where utcDate between ?1 and ?2");
        query.setParameter(1, fromDateString);
        query.setParameter(2, toDateString);
        return query.list();
    }

    public List<QuakesEntity> getByUTCDate(Date date) {
        return getByUTCDate(format.format(date));
    }


    public List<QuakesEntity> getByUTCDateRange(Date fromDate, Date toDate) {
        return getByUTCDateRange(format.format(fromDate), format.format(toDate));
    }

    public List<QuakesEntity> getByMagnitude(double magnitude) {
        Query query = session.createQuery("from QuakesEntity where magnitude=?1");
        query.setParameter(1, magnitude);
        return query.list();
    }

    public List<QuakesEntity> getByMagnitudeRange(double fromMagnitude, double toMagnitude) {
        Query query = session.createQuery("from QuakesEntity where magnitude between ?1 and ?2");
        query.setParameter(1, fromMagnitude);
        query.setParameter(2, toMagnitude);
        return query.list();
    }

    public List<QuakesEntity> getByRegion(String region) {
        Query query = session.createQuery("from QuakesEntity where region=?1");
        query.setParameter(1, region);
        return query.list();
    }

    public List<QuakesEntity> getAll() {
        Query query = session.createQuery("from QuakesEntity");
        return query.list();
    }

    public void close() {
        session.close();
    }

    public static void main(String[] args) {
        DataService dataService = new DataService();
        Date from = null;
        Date to = null;
        try {
            to = dataService.format.parse("2017-10-14 13:07:58.3");
            from = dataService.format.parse("2017-10-14 11:20:26.0");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dataService.init();
        System.out.println(dataService.getAll().size());
        System.out.println(dataService.getByMagnitudeRange(8, 9).size());
        System.out.println(dataService.getByUTCDateRange(from, to).size());
        dataService.update();
        System.out.println(dataService.getAll().size());
        dataService.close();
    }
}
