package nnero.netty.im.server.db;

import lombok.extern.slf4j.Slf4j;
import nnero.netty.im.Single;
import nnero.netty.im.server.entity.Group;
import nnero.netty.im.server.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import javax.persistence.Query;
import java.util.List;

/**
 * Author: NNERO
 * Time : 下午2:55 19-2-20
 */
@Slf4j
@Single
public class DataBase {

    private SessionFactory sessionFactory;

    public DataBase(){
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.
            log.error("",e);
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    public <T> void save(T entity){
        if(entity == null){
            log.debug("entity is null. Not save");
            return;
        }
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(entity);
        transaction.commit();
        session.close();
    }

    public Group getGroup(int groupId){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query query = session.createQuery("from Group where group_id=?1");
            query.setParameter(1,groupId);
            List result = ((org.hibernate.query.Query) query).list();
            transaction.commit();
            if(result != null && result.size() >= 1){
                return (Group) result.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("",e);
            transaction.rollback();
            return null;
        } finally {
            session.close();
        }
    }

    public User getUser(String username, String password){
        if(username == null || password == null){
            log.debug("params is null");
            return null;
        }
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {

            Query query = session.createQuery("from User where username=?1 and password=?2");
            query.setParameter(1,username);
            query.setParameter(2,password);
            List result = ((org.hibernate.query.Query) query).list();
            transaction.commit();
            if(result != null && result.size() >= 1){
                return (User) result.get(0);
            } else {
                log.error("error: username->"+username+" password->"+password);
                return null;
            }
        } catch (Exception e) {
            log.error("",e);
            transaction.rollback();
            return null;
        } finally {
            session.close();
        }
    }
}
