package it.unibz.dao;

import it.unibz.types.Login;
import it.unibz.types.Tweets;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import twitter4j.auth.AccessToken;


public class TwitterDaoHibernate extends HibernateDaoSupport implements TwitterDao {
  private static final String BASE_CONTACTS_QUERY =
      "from " + Tweets.class.getName() + " c ";
  

  
  public TwitterDaoHibernate() {}

  public AccessToken loadAccessToken(long useId){
        Transaction t = null;
 //     session =sessionFactory.openSession();
       // System.out.println("load access for user"+useId);
Session session = SessionFactoryUtil.getInstance().getCurrentSession();
t=session.beginTransaction();
 List ret = null;
    try {
      ret = session.createQuery("from "+Login.class.getName()+" where BASEID = "+useId+"").list();
t.commit();
    }
    catch(HibernateException he){
    }
//System.out.println("ret size"+ret.size());
    if(ret.size()>0)
      return new AccessToken(((Login)ret.get(0)).getaccesskey(), ((Login)ret.get(0)).getaccesstoken());
    else
        return null;
   // String token = // load from a persistent store
    //String tokenSecret = // load from a persistent store
    //return new AccessToken("241950624-tbJdWcp4Bbl7HZ3YaaS4TGB1vDQ7kM2T1VtDrTDa","4d6CbEqw0aZQVDGam9es7xACDfqbrQqy1teXe0Fec");
  }

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


    public void storeAccesstoken(String token, String tokenSecret, long userId) {
         Transaction t = null;
 //     session =sessionFactory.openSession();
Session session = SessionFactoryUtil.getInstance().getCurrentSession();
t=session.beginTransaction();
session.save(new Login(token,tokenSecret,userId));
t.commit();
    }

    public void logout(long userId) {
        Transaction t = null;
        //System.out.println(userId);

 //     session =sessionFactory.openSession();
session = SessionFactoryUtil.getInstance().getCurrentSession();
t=session.beginTransaction();
         Query query = session.createQuery("DELETE from "+Login.class.getName()+
                 " as t where t.baseid="+userId);
         query.executeUpdate();
         session.flush();
t.commit();
    }
}
