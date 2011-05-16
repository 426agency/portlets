/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unibz.twitterportlet;

import it.unibz.dao.SessionFactoryUtil;
import it.unibz.dao.TwitterDao;
import it.unibz.dao.TwitterDaoHibernate;
import it.unibz.types.Login;
import it.unibz.types.Tweets;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author user
 */
public class TwitterComponent {
private static AccessToken accessToken=null;
    private static ConfigurationBuilder cb=null;
    private static TwitterFactory tf=null;
    private static Twitter twitter=null;

    private static void init(long userid){
        if(cb==null){
    cb = new ConfigurationBuilder();
    accessToken = loadAccessToken(userid);

cb.setDebugEnabled(true)
  .setOAuthConsumerKey("Kgrg9GlpGU8yoza6u1KqQQ")
  .setOAuthConsumerSecret("bZUrgRsSWu9JiXMsE9mFFT5pcosZzPv4vKca7nhZsE")
  .setOAuthAccessToken(accessToken.getToken())
  .setOAuthAccessTokenSecret(accessToken.getTokenSecret());
tf = new TwitterFactory(cb.build());
twitter = tf.getInstance();

        }
    }

    private static AccessToken loadAccessToken(long useId){
        Transaction t = null;
 //     session =sessionFactory.openSession();
        System.out.println("load access for user"+useId);
Session session = SessionFactoryUtil.getInstance().getCurrentSession();
t=session.beginTransaction();
 List ret = null;
    try {
      ret = session.createQuery("from "+Login.class.getName()+" where BASEID = "+useId+"").list();
t.commit();
    }
    catch(HibernateException he){
    }
System.out.println("ret size"+ret.size());
    if(ret.size()>0)
      return new AccessToken(((Login)ret.get(0)).getaccesskey(), ((Login)ret.get(0)).getaccesstoken());
    else
        return null;
   // String token = // load from a persistent store
    //String tokenSecret = // load from a persistent store
    //return new AccessToken("241950624-tbJdWcp4Bbl7HZ3YaaS4TGB1vDQ7kM2T1VtDrTDa","4d6CbEqw0aZQVDGam9es7xACDfqbrQqy1teXe0Fec");
  }

    public static Twitter getTwitter(long userid){
        init(userid);
        return twitter;
    }

    static int getModified(List<Tweets> tweets,String user) {

TwitterDaoHibernate d = new TwitterDaoHibernate();
return d.saveTweets(tweets,user);
    }

    static boolean hasaccount(long userId) {
        if(loadAccessToken(userId)==null)
            return false;
        else
            return true;
    }

    static void login(Twitter t,String parameter,RequestToken rt,long userId) throws TwitterException {
       
        AccessToken a = t.getOAuthAccessToken(rt, parameter);
    storeAccesstoken(a.getToken(),a.getTokenSecret(),userId);
    }

    private static void storeAccesstoken(String token, String tokenSecret, long userId) {
         Transaction t = null;
 //     session =sessionFactory.openSession();
Session session = SessionFactoryUtil.getInstance().getCurrentSession();
t=session.beginTransaction();
session.save(new Login(token,tokenSecret,userId));
t.commit();
    }
}
