package it.unibz.service;

import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * Interface for the Service.
 * Contains all the methods needed for Service/DB interaction
 */
public interface TwitterService {

    /**
     * Method inserts x pagination new tweets for a certain user in the DB
     * @param tweets New Tweets to investigate before insertion
     * @param user To user the Tweets belong to
     * @param pagination Amount of stored Tweets
     * @return Number of new Tweets
     */
    public int saveTweets(List<Status> tweets, String user, int pagination);

    /**
     * Loads the Access from the DB
     * @param useId ID of user to load data for
     * @return new AccessToken object if found, else null
     */
    public AccessToken loadAccessToken(long useId);

    /**

     * Method stores the OAuth access pair in the DB
     * @param t Twitter Object to work with
     * @param parameter System object
     * @param rt RequestToken for Authentication
     * @param userId Liferay user ID
     * @throws TwitterException
     */
    public void login(Twitter t, String parameter, RequestToken rt, long userId) throws TwitterException;

    /**
     * Method removes Access entry for user
     * @param userId User to remove data for
     */
    public void logout(long userId);

    /**
     * Returns current Twitter service object for user
     * @param userid User to get Twitterobject for
     * @return Twitterobject if exists, else null
     */
    public Twitter getTwitter(long userid);
}
