/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unibz.twitterportlet;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.Authorization;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * <a href="JSPPortlet.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class TwitterFollower extends GenericPortlet {

    private static AccessToken accessToken;
    private static ConfigurationBuilder cb;
    private static TwitterFactory tf;
    private static Twitter twitter;

	public void init() throws PortletException {
		editJSP = getInitParameter("edit-jsp");
		helpJSP = getInitParameter("help-jsp");
		viewJSP = getInitParameter("view-jsp");
                cb = new ConfigurationBuilder();
    accessToken = loadAccessToken(0);

cb.setDebugEnabled(true)
  .setOAuthConsumerKey("Kgrg9GlpGU8yoza6u1KqQQ")
  .setOAuthConsumerSecret("bZUrgRsSWu9JiXMsE9mFFT5pcosZzPv4vKca7nhZsE")
  .setOAuthAccessToken(accessToken.getToken())
  .setOAuthAccessTokenSecret(accessToken.getTokenSecret());
tf = new TwitterFactory(cb.build());
twitter = tf.getInstance();
	}

	public void doDispatch(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {

		String jspPage = renderRequest.getParameter("jspPage");

		if (jspPage != null) {
			include(jspPage, renderRequest, renderResponse);
		}
		else {
			super.doDispatch(renderRequest, renderResponse);
		}
	}

	public void doEdit(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {

		if (renderRequest.getPreferences() == null) {
			super.doEdit(renderRequest, renderResponse);
		}
		else {
			include(editJSP, renderRequest, renderResponse);
		}
	}

	public void doHelp(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {

		include(helpJSP, renderRequest, renderResponse);
	}

        private boolean testNullOrEmpty(String sz) {
        if(sz!=null && !sz.equals(""))
            return false;
        else
            return true;
    }
        
	public void doView(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {
String msg=renderRequest.getParameter("msg");
        if(!testNullOrEmpty(msg))
            renderRequest.setAttribute("msg",msg);
    String message="";

        try {
            // Status status = twitter.updateStatus(args[1]);
            message+="<table>";
            long[] arrayLngIDs = twitter.getFriendsIDs("Fbihack",-1).getIDs();
            PortletURL action = renderResponse.createActionURL();
            User u =null;
            for(int i=0;i<arrayLngIDs.length;i++){
                u = twitter.showUser(arrayLngIDs[i]);
                action = renderResponse.createActionURL();
                action.setParameter("followerID",String.valueOf(arrayLngIDs[i]) );
    message+="<tr><td>"+u.getScreenName()+"</td><td><a href=\""+action.toString()+
            "\"><img src=\"/twitterfollower-portlet/images/trash.gif\" border=\"0\""
            + " title=\"Unfollow\"></a></td></tr>";
            }
            message+="</table>";
        } catch (TwitterException ex) {
            message="";
            Logger.getLogger(TwitterFollower.class.getName()).log(Level.SEVERE, null, ex);
        }
    PortletURL au = renderResponse.createActionURL();
    renderRequest.setAttribute("actionURL", au.toString());
    renderResponse.getWriter().append(message);
        include(viewJSP, renderRequest, renderResponse);

    }
        //           "241950624-tbJdWcp4Bbl7HZ3YaaS4TGB1vDQ7kM2T1VtDrTDa","4d6CbEqw0aZQVDGam9es7xACDfqbrQqy1teXe0Fec");

        private static AccessToken loadAccessToken(int useId){
   // String token = // load from a persistent store
    //String tokenSecret = // load from a persistent store
    return new AccessToken("241950624-tbJdWcp4Bbl7HZ3YaaS4TGB1vDQ7kM2T1VtDrTDa","4d6CbEqw0aZQVDGam9es7xACDfqbrQqy1teXe0Fec");
  }



	public void processAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws IOException, PortletException {

            if(actionRequest.getParameter("followerID")!=null)
            {
            try {
                twitter.destroyFriendship(Integer.parseInt(actionRequest.getParameter("followerID")));
            } catch (TwitterException ex) {
                Logger.getLogger(TwitterFollower.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
            if(actionRequest.getParameter("followername")!=null)
            {
                 String followerName = actionRequest.getParameter("followername");
                 try {
                    twitter.createFriendship(followerName);
            } catch (TwitterException ex) {
                actionResponse.setRenderParameter("msg", "Unable to follow user: "+followerName);

                Logger.getLogger(TwitterFollower.class.getName()).log(Level.SEVERE, null, ex);
            }
                 
            }
}


	protected void include(
			String path, RenderRequest renderRequest,
			RenderResponse renderResponse)
		throws IOException, PortletException {

		PortletRequestDispatcher portletRequestDispatcher =
			getPortletContext().getRequestDispatcher(path);

		if (portletRequestDispatcher == null) {
			_log.error(path + " is not a valid include");
		}
		else {
			portletRequestDispatcher.include(renderRequest, renderResponse);
		}
	}

	protected String editJSP;
	protected String helpJSP;
	protected String viewJSP;

	private static Log _log = LogFactoryUtil.getLog(TwitterFollower.class);

}
