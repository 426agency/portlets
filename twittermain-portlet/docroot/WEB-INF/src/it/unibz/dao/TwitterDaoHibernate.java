package it.unibz.dao;

import it.unibz.types.Login;
import it.unibz.types.Tweets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import twitter4j.Status;
import twitter4j.auth.AccessToken;


public class TwitterDaoHibernate extends HibernateDaoSupport implements TwitterDao {
  private static final String BASE_CONTACTS_QUERY =
      "from " + Tweets.class.getName() + " c ";
  

  
  public TwitterDaoHibernate() {}

  public AccessToken loadAccessToken(long useId){
     List ret = null;
      ret = getHibernateTemplate().find("from "+Login.class.getName()+" where BASEID = "+useId+"");
//        Transaction t = null;
// //     session =sessionFactory.openSession();
//       // System.out.println("load access for user"+useId);
//Session session = SessionFactoryUtil.getInstance().getCurrentSession();
//t=session.beginTransaction();
//
//    try {
//      ret = session.createQuery("from "+Login.class.getName()+" where BASEID = "+useId+"").list();
//t.commit();
//    }
//    catch(HibernateException he){
//    }
System.out.println("ret size"+ret.size());
    if(ret.size()>0)
      return new AccessToken(((Login)ret.get(0)).getaccesskey(), ((Login)ret.get(0)).getaccesstoken());
    else
        return null;
   // String token = // load from a persistent store
    //String tokenSecret = // load from a persistent store
    //return new AccessToken("241950624-tbJdWcp4Bbl7HZ3YaaS4TGB1vDQ7kM2T1VtDrTDa","4d6CbEqw0aZQVDGam9es7xACDfqbrQqy1teXe0Fec");
  }

  private List findAllTweets(String user) {
     return getHibernateTemplate().find("from "+Tweets.class.getName()+" where ownername like \'"+user+"\'");
//      List ret = null;
//    try {
//      ret = session.createQuery("from "+Tweets.class.getName()+" where OWNERNAME like \'"+user+"\'").list();
//      }
//    catch(HibernateException he){
//    }
//      return ret;
  }

  Session  session=null;

    @Override
    public int saveTweets(List<Status> tweets,String user,int pagination) {
//Transaction t = null;
 //     session =sessionFactory.openSession();
//session = SessionFactoryUtil.getInstance().getCurrentSession();
//t=session.beginTransaction();
        //Retrieve all stored Tweets
List userstweets = findAllTweets(user);
//System.out.println(userstweets.size());
if(userstweets==null)
    userstweets= new ArrayList();
int count=0;
//remove already stored
boolean tofind=true;
Status st=null;
for(int i=tweets.size()-1;i>=0;i--){
    st=tweets.get(i);
    tofind=true;
    for(int j=0;j<userstweets.size();j++){
  //      System.out.println(((Tweets)userstweets.get(j)).getbaseid()+"+"+tweets.get(i).getbaseid());
        if(((Tweets)userstweets.get(j)).getbaseid()==st.getId())
            tofind=false;
    }
    if(tofind){
        //REMOVE AS A STACK
        count++;
        //System.out.println("inserting);
        Tweets t = findmin(user,pagination);
        if(t!=null){
            getHibernateTemplate().delete(t);
//         Query query = session.createQuery("DELETE from "+Tweets.class.getName()+
//                 " as t where t.id=(SELECT MIN(id) from "+Tweets.class.getName()+" where OWNERNAME like \'"+user+"\')"
//                 + " and 20<=(select count(id) from "+Tweets.class.getName()+" where OWNERNAME like \'"+user+"\')"
//                 + " and OWNERNAME like \'"+us/getHibernateTemplate().flush();
//         session.save(tweets.get(i));
            getHibernateTemplate().flush();
        }
        getHibernateTemplate().save(new Tweets(st.getUser().getName(),st.getText(),st.getId(),user));
  getHibernateTemplate().flush();
    }
}
//session.flush();
//t.commit();
        //getHibernateTemplate().saveOrUpdate(contact);
        return count;
    }


    @Override
    public void storeAccesstoken(String token, String tokenSecret, long userId) {
        getHibernateTemplate().saveOrUpdate(new Login(token,tokenSecret,userId));
//         Transaction t = null;
// //     session =sessionFactory.openSession();
//Session session = SessionFactoryUtil.getInstance().getCurrentSession();
//t=session.beginTransaction();
//session.save(new Login(token,tokenSecret,userId));
//t.commit();
    }

    public void logout(long userId) {
        //Transaction t = null;
        //System.out.println(userId);
        List ret = getHibernateTemplate().find("from "+Login.class.getName()+" where BASEID = "+userId);

        if(ret!=null)
            getHibernateTemplate().delete((Login)ret.get(0));
 //     session =sessionFactory.openSession();
//session = SessionFactoryUtil.getInstance().getCurrentSession();
//t=session.beginTransaction();
//         Query query = session.createQuery("DELETE from "+Login.class.getName()+
//                 " as t where t.baseid="+userId);
//         query.executeUpdate();
//         session.flush();
//t.commit();
    }

    private Tweets findmin(String user,int pagination) {
        List ret = getHibernateTemplate().find("from "+Tweets.class.getName()+
                 " t where t.id=(SELECT MIN(t2.id) from "+Tweets.class.getName()+" t2 where t2.ownername like \'"+user+"\')"
                 + " and "+pagination+"<=(select count(t3.id) from "+Tweets.class.getName()+" t3 where t3.ownername like \'"+user+"\')"
                 + " and t.ownername like \'"+user+"\'");
        return ret.size()>0?(Tweets)ret.get(0):null;
    }
}
