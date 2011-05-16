package it.unibz.dao;

import it.unibz.types.Tweets;
import java.util.List;


public interface TwitterDao {
 
  public int saveTweets(List<Tweets> tweets,String user);
    
}