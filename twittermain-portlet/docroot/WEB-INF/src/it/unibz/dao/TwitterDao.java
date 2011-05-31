package it.unibz.dao;

import java.util.List;
import twitter4j.Status;
import twitter4j.auth.AccessToken;


public interface TwitterDao {
 
  public int saveTweets(List<Status> tweets,String user,int pagination);

  public void storeAccesstoken(String token, String tokenSecret, long userId);

    public AccessToken loadAccessToken(long useId);

    public void logout(long userId);
}