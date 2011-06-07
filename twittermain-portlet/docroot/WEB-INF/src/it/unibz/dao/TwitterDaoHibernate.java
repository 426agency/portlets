package it.unibz.dao;

import it.unibz.types.Login;
import it.unibz.types.Tweets;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import twitter4j.Status;
import twitter4j.auth.AccessToken;

/**
 * Implementation of the TwitterDao Interface.
 * Method explanation to be found in the Interface
 */
public class TwitterDaoHibernate extends HibernateDaoSupport implements TwitterDao {

    public TwitterDaoHibernate() {
    }

    @Override
    public AccessToken loadAccessToken(long useId) {
        List ret = null;
        ret = getHibernateTemplate().find("from " + Login.class.getName() + " where BASEID = " + useId + "");
        if (ret.size() > 0) {
            return new AccessToken(((Login) ret.get(0)).getaccesskey(), ((Login) ret.get(0)).getaccesstoken());
        } else {
            return null;
        }
    }

    private List findAllTweets(String user) {
        return getHibernateTemplate().find("from " + Tweets.class.getName() + " where ownername like \'" + user + "\'");
    }
    Session session = null;

    @Override
    public int saveTweets(List<Status> tweets, String user, int pagination) {
        List userstweets = findAllTweets(user);
        if (userstweets == null) {
            userstweets = new ArrayList();
        }
        int count = 0;
//remove already stored
        boolean tofind = true;
        Status st = null;
        for (int i = tweets.size() - 1; i >= 0; i--) {
            st = tweets.get(i);
            tofind = true;
            for (int j = 0; j < userstweets.size(); j++) {
                if (((Tweets) userstweets.get(j)).getbaseid() == st.getId()) {
                    tofind = false;
                }
            }
            if (tofind) {
                //REMOVE AS A STACK
                count++;
                Tweets t = findmin(user, pagination);
                if (t != null) {
                    getHibernateTemplate().delete(t);
                    getHibernateTemplate().flush();
                }
                getHibernateTemplate().save(new Tweets(st.getUser().getName(), st.getText(), st.getId(), user));
                getHibernateTemplate().flush();
            }
        }
        return count;
    }

    @Override
    public void storeAccesstoken(String token, String tokenSecret, long userId) {
        getHibernateTemplate().saveOrUpdate(new Login(token, tokenSecret, userId));
    }

    @Override
    public void logout(long userId) {
        List ret = getHibernateTemplate().find("from " + Login.class.getName() + " where BASEID = " + userId);

        if (ret != null) {
            getHibernateTemplate().delete((Login) ret.get(0));
        }
    }

    /**
     * Method returns the lowest Tweet entry ID for specific user,
     * only if tweet list size exceeds pagination
     * @param user To fetch Tweets for
     * @param pagination Limit amount
     * @return Tweet with lowest Id, else null
     */
    private Tweets findmin(String user, int pagination) {
        List ret = getHibernateTemplate().find("from " + Tweets.class.getName()
                + " t where t.id=(SELECT MIN(t2.id) from " + Tweets.class.getName() + " t2 where t2.ownername like \'" + user + "\')"
                + " and " + pagination + "<=(select count(t3.id) from " + Tweets.class.getName() + " t3 where t3.ownername like \'" + user + "\')"
                + " and t.ownername like \'" + user + "\'");
        return ret.size() > 0 ? (Tweets) ret.get(0) : null;
    }
}
