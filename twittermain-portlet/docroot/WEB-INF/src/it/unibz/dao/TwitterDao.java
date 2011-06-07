package it.unibz.dao;

import java.util.List;
import twitter4j.Status;
import twitter4j.auth.AccessToken;

/**
 * Interface for the DAO. Contains all the methods needed for DB interaction
 */
public interface TwitterDao {
    /**
     * Method inserts x pagination new tweets for a certain user in the DB
     * @param tweets New Tweets to investigate before insertion
     * @param user To user the Tweets belong to
     * @param pagination Amount of stored Tweets
     * @return Number of new Tweets
     */
  public int saveTweets(List<Status> tweets,String user,int pagination);

  /**
   * Method stores the OAuth access pair in the DB
   * @param token
   * @param tokenSecret
   * @param userId The user for which data is stored
   */
  public void storeAccesstoken(String token, String tokenSecret, long userId);

  /**
   * Loads the Access from the DB
   * @param useId ID of user to load data for
   * @return new AccessToken object if found, else null
   */
    public AccessToken loadAccessToken(long useId);

    /**
     * Method removes Access entry for user
     * @param userId User to remove data for
     */
    public void logout(long userId);
}