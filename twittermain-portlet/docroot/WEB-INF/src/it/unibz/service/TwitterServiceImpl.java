package it.unibz.service;

import it.unibz.types.Tweets;
import java.util.List;


import it.unibz.dao.TwitterDao;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterServiceImpl implements TwitterService {
  
  public TwitterServiceImpl() {}
  
  private AccessToken accessToken=null;
    private ConfigurationBuilder cb=null;
    private TwitterFactory tf=null;
    private Twitter twitter=null;

    private void init(long userid){

        if(cb==null){
    cb = new ConfigurationBuilder();
    accessToken = twitterDao.loadAccessToken(userid);

cb.setDebugEnabled(true)
  .setOAuthConsumerKey("Kgrg9GlpGU8yoza6u1KqQQ")
  .setOAuthConsumerSecret("bZUrgRsSWu9JiXMsE9mFFT5pcosZzPv4vKca7nhZsE")
  .setOAuthAccessToken(accessToken.getToken())
  .setOAuthAccessTokenSecret(accessToken.getTokenSecret());
tf = new TwitterFactory(cb.build());
twitter = tf.getInstance();

        }
    }
   
   public Twitter getTwitter(long userid){
        init(userid);
        return twitter;
    }
  private TwitterDao twitterDao;

    public void setTwitterDao(TwitterDao twitterDao) {
    this.twitterDao = twitterDao;
  }

    @Override
    public int saveTweets(List<Status> tweets, String user,int pagination) {
        return twitterDao.saveTweets(tweets, user,pagination);
    }

    @Override
    public void storeAccesstoken(String token, String tokenSecret, long userId) {
        twitterDao.storeAccesstoken(token, tokenSecret, userId);
    }

    @Override
    public AccessToken loadAccessToken(long useId) {
        return twitterDao.loadAccessToken(useId);
    }

    @Override
    public void logout(long userId) {
        twitterDao.logout(userId);
    }

    @Override
    public void login(Twitter t,String parameter,RequestToken rt,long userId) throws TwitterException {

        AccessToken a = t.getOAuthAccessToken(rt, parameter);
        twitterDao.storeAccesstoken(a.getToken(),a.getTokenSecret(),userId);
    }
}
