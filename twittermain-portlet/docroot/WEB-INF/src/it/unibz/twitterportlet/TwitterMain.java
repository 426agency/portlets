/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unibz.twitterportlet;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import it.unibz.types.Tweets;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 * <a href="JSPPortlet.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class TwitterMain extends GenericPortlet {

	public void init() throws PortletException {
		editJSP = getInitParameter("edit-jsp");
		helpJSP = getInitParameter("help-jsp");
		viewJSP = getInitParameter("view-jsp");
	}

        private RequestToken rt=null;
        private Twitter t=null;
         
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

	public void doView(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {
            String message="";
            User u =null;
        try {
             u = PortalUtil.getUser(renderRequest);
        } catch (PortalException ex) {
            Logger.getLogger(TwitterMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SystemException ex) {
            Logger.getLogger(TwitterMain.class.getName()).log(Level.SEVERE, null, ex);
        }
          if(u==null||u.getEmailAddress().contains("guest")){
              renderResponse.getWriter().write("NOTALLOWED");
          }

else{
                if(!TwitterComponent.hasaccount(u.getUserId())){
                try {
                    System.out.println("has no account");
                    t = new TwitterFactory().getInstance();
t.setOAuthConsumer("Kgrg9GlpGU8yoza6u1KqQQ", "bZUrgRsSWu9JiXMsE9mFFT5pcosZzPv4vKca7nhZsE");
                    rt=t.getOAuthRequestToken();
                    message = "Please visit <a href=" + rt.getAuthorizationURL() + ">visit Twitter</a>";
                    renderResponse.getWriter().write(message);
                    include(editJSP, renderRequest, renderResponse);
                } catch (TwitterException ex) {
                    Logger.getLogger(TwitterMain.class.getName()).log(Level.SEVERE, null, ex);
                }
                }
 else{

System.out.println("has account");
            // Status status = twitter.updateStatus(args[1]);
            List<Status> statuses;
            try {
                statuses = TwitterComponent.getTwitter(u.getUserId()).getHomeTimeline();
                Tweets t = null;
                List<Tweets> tweets=new ArrayList<Tweets>();
                for (Status status : statuses) {
                     t= new Tweets(status.getUser().getName(),status.getText(),status.getId(),TwitterComponent.getTwitter(u.getUserId()).getScreenName());
        message+="<p><b>"+ t.getAuthor() + "</b>:" + t.getText() +"</p>";
        tweets.add(t);
    }
                int num= TwitterComponent.getModified(tweets,TwitterComponent.getTwitter(u.getUserId()).getScreenName());
                renderRequest.setAttribute("numtweets", num);
                //message+="you have "+num+" new tweets";
            } catch (TwitterException ex) {
                Logger.getLogger(TwitterMain.class.getName()).log(Level.SEVERE, null, ex);
            }
include(viewJSP, renderRequest, renderResponse);
    renderResponse.getWriter().write(message);


}
}

	}

	public void processAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws IOException, PortletException {

            if(actionRequest.getParameter("tweettext")!=null)
            {

                User u =null;
        try {
             u = PortalUtil.getUser(actionRequest);

                TwitterComponent.getTwitter(u.getUserId()).updateStatus(actionRequest.getParameter("tweettext"));
            } catch (Exception ex) {
                Logger.getLogger(TwitterMain.class.getName()).log(Level.SEVERE, null, ex);
            }
            }

            if(actionRequest.getParameter("twitterpin")!=null)
            {
            try {
User u =null;
             u = PortalUtil.getUser(actionRequest);

                TwitterComponent.login(t,actionRequest.getParameter("twitterpin"),rt,u.getUserId());
            } catch (Exception ex) {
                Logger.getLogger(TwitterMain.class.getName()).log(Level.SEVERE, null, ex);
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
	private static Log _log = LogFactoryUtil.getLog(TwitterMain.class);

}
