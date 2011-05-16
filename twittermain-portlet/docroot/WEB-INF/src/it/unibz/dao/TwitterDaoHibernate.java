package it.unibz.dao;

import it.unibz.types.Tweets;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


public class TwitterDaoHibernate extends HibernateDaoSupport implements TwitterDao {
  private static final String BASE_CONTACTS_QUERY =
      "from " + Tweets.class.getName() + " c ";
  
  private static final String PUBLIC_CONTACTS_QUERY = 
      BASE_CONTACTS_QUERY + 
      "where c.ownerName is null";
  
  private static final String USER_CONTACTS_QUERY =
      BASE_CONTACTS_QUERY + " where c.ownerName = ?";
  
  private static final String SEARCH_PUBLIC_CONTACTS_QUERY =
      BASE_CONTACTS_QUERY +
      " where c.ownerName is null " +
      " and c.lastName LIKE ?";

  private static final String SEARCH_USER_CONTACTS_QUERY =
      BASE_CONTACTS_QUERY +
      " where (c.ownerName is null or c.ownerName = ?) " +
      " and c.lastName = ?";
  
  public TwitterDaoHibernate() {}

  private List findAllTweets(String user) {
      List ret = null;
    try {
      ret = session.createQuery("from "+Tweets.class.getName()+" where OWNERNAME like \'"+user+"\'").list();
      }
    catch(HibernateException he){
    }
      return ret;
  }

  Session  session=null;

    public int saveTweets(List<Tweets> tweets,String user) {
Transaction t = null;
 //     session =sessionFactory.openSession();
session = SessionFactoryUtil.getInstance().getCurrentSession();
t=session.beginTransaction();
        //Retrieve all stored Tweets
List userstweets = findAllTweets(user);
//System.out.println(userstweets.size());
if(userstweets==null)
    userstweets= new ArrayList();
int count=0;
//remove already stored
boolean tofind=true;
for(int i=0;i<tweets.size();i++){
    tofind=true;
    for(int j=0;j<userstweets.size();j++){
  //      System.out.println(((Tweets)userstweets.get(j)).getbaseid()+"+"+tweets.get(i).getbaseid());
        if(((Tweets)userstweets.get(j)).getbaseid()==tweets.get(i).getbaseid())
            tofind=false;
    }
    if(tofind){
        //REMOVE AS A STACK
        count++;
    //    System.out.println("nice insertion");
         Query query = session.createQuery("DELETE from "+Tweets.class.getName()+
                 " as t where t.id=(SELECT MIN(id) from "+Tweets.class.getName()+" where OWNERNAME like \'"+user+"\')"
                 + " and 20<=(select count(id) from "+Tweets.class.getName()+" where OWNERNAME like \'"+user+"\')"
                 + " and OWNERNAME like \'"+user+"\'");
         query.executeUpdate();
         session.save(tweets.get(i));
    }
}
session.flush();
t.commit();
        //getHibernateTemplate().saveOrUpdate(contact);
        return count;
    }

}
