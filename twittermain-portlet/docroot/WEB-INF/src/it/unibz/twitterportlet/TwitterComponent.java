/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unibz.twitterportlet;

import it.unibz.dao.TwitterDaoHibernate;
import it.unibz.types.Tweets;
import java.util.List;
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
        TwitterDaoHibernate dao = new TwitterDaoHibernate();

        if(cb==null){
    cb = new ConfigurationBuilder();
    accessToken = dao.loadAccessToken(userid);

cb.setDebugEnabled(true)
  .setOAuthConsumerKey("Kgrg9GlpGU8yoza6u1KqQQ")
  .setOAuthConsumerSecret("bZUrgRsSWu9JiXMsE9mFFT5pcosZzPv4vKca7nhZsE")
  .setOAuthAccessToken(accessToken.getToken())
  .setOAuthAccessTokenSecret(accessToken.getTokenSecret());
tf = new TwitterFactory(cb.build());
twitter = tf.getInstance();

        }
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
        TwitterDaoHibernate dao = new TwitterDaoHibernate();

        if(dao.loadAccessToken(userId)==null)
            return false;
        else
            return true;
    }

    static void login(Twitter t,String parameter,RequestToken rt,long userId) throws TwitterException {
       
        AccessToken a = t.getOAuthAccessToken(rt, parameter);
        TwitterDaoHibernate d = new TwitterDaoHibernate();

    d.storeAccesstoken(a.getToken(),a.getTokenSecret(),userId);
    }

    

    static void logout(long userId) {
        TwitterDaoHibernate dao = new TwitterDaoHibernate();
        dao.logout(userId);
    }
}
