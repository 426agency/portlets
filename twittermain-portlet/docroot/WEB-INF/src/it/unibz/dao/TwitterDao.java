package it.unibz.dao;

import it.unibz.types.Tweets;
import java.util.List;
import twitter4j.auth.AccessToken;


public interface TwitterDao {
 
  public int saveTweets(List<Tweets> tweets,String user);

  public void storeAccesstoken(String token, String tokenSecret, long userId);

    public AccessToken loadAccessToken(long useId);

    public void logout(long userId);
}