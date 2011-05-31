package it.unibz.service;

import java.util.List;

import it.unibz.types.Tweets;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public interface TwitterService {
  public int saveTweets(List<Status> tweets,String user,int pagination);

  public void storeAccesstoken(String token, String tokenSecret, long userId);

    public AccessToken loadAccessToken(long useId);

    public void login(Twitter t,String parameter,RequestToken rt,long userId) throws TwitterException;

    public void logout(long userId);

     public Twitter getTwitter(long userid);

}
