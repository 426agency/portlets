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
import com.liferay.portal.util.PortalUtil;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
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

	public void init() throws PortletException {
		editJSP = getInitParameter("edit-jsp");
		helpJSP = getInitParameter("help-jsp");
		viewJSP = getInitParameter("follower-jsp");
                                errorJSP = getInitParameter("error-jsp");

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
            User u =getCurrentPortalUser(renderRequest);
          if(u==null||u.getEmailAddress().contains("guest")||!TwitterComponent.hasaccount(u.getUserId())){
              include(errorJSP,renderRequest,renderResponse);
          }

else{

        try {
            // Status status = twitter.updateStatus(args[1]);
            message+="<table>";
            Twitter twit=TwitterComponent.getTwitter(u.getUserId());
            long[] arrayLngIDs = twit.getFriendsIDs(-1).getIDs();
            PortletURL action = renderResponse.createActionURL();
            twitter4j.User twitteruser =null;
            for(int i=0;i<arrayLngIDs.length;i++){
                twitteruser = twit.showUser(arrayLngIDs[i]);
                action = renderResponse.createActionURL();
                action.setParameter("followerID",String.valueOf(arrayLngIDs[i]) );
    message+="<tr><td>"+twitteruser.getScreenName()+"</td><td><a href=\""+action.toString()+
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
        }
      


	public void processAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws IOException, PortletException {

                        User u =getCurrentPortalUser(actionRequest);
if(u==null||u.getEmailAddress().contains("guest")||!TwitterComponent.hasaccount(u.getUserId())){
          }
 else{
            if(actionRequest.getParameter("followerID")!=null)
            {
            try {

                TwitterComponent.getTwitter(u.getUserId()).destroyFriendship(Integer.parseInt(actionRequest.getParameter("followerID")));
            } catch (TwitterException ex) {
                Logger.getLogger(TwitterFollower.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
            if(actionRequest.getParameter("followername")!=null)
            {
                 String followerName = actionRequest.getParameter("followername");
                 try {
                    TwitterComponent.getTwitter(u.getUserId()).createFriendship(followerName);
            } catch (TwitterException ex) {
                actionResponse.setRenderParameter("msg", "Unable to follow user: "+followerName);

                Logger.getLogger(TwitterFollower.class.getName()).log(Level.SEVERE, null, ex);
            }
                 
            }}
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
        protected String errorJSP;

	private static Log _log = LogFactoryUtil.getLog(TwitterFollower.class);

    private User getCurrentPortalUser(PortletRequest renderRequest) {
        User ret=null;
        try {
             ret = PortalUtil.getUser(renderRequest);
        } catch (PortalException ex) {
            Logger.getLogger(TwitterMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SystemException ex) {
            Logger.getLogger(TwitterMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
        return ret;
        }
    }

}
