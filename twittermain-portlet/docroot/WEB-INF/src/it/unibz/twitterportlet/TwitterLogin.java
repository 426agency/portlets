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
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;

/**
 * <a href="JSPPortlet.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class TwitterLogin extends GenericPortlet {

	public void init() throws PortletException {
		editJSP = getInitParameter("edit-jsp");
		helpJSP = getInitParameter("help-jsp");
		viewJSP = getInitParameter("view-jsp");
                errorJSP = getInitParameter("error-jsp");
                successJSP = getInitParameter("success-jsp");
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
            Logger.getLogger(TwitterLogin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SystemException ex) {
            Logger.getLogger(TwitterLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
          if(u==null||u.getEmailAddress().contains("guest")){
                    renderResponse.getWriter().write("Please login into LifeRay first!");
          }

else{
                if(!TwitterComponent.hasaccount(u.getUserId())){
                try {
                    t = new TwitterFactory().getInstance();
t.setOAuthConsumer("Kgrg9GlpGU8yoza6u1KqQQ", "bZUrgRsSWu9JiXMsE9mFFT5pcosZzPv4vKca7nhZsE");
                    rt=t.getOAuthRequestToken();
                    message = "Please visit <a href=" + rt.getAuthorizationURL() + "> Twitter</a>";
                    renderResponse.getWriter().write(message);
                    include(editJSP, renderRequest, renderResponse);
                } catch (TwitterException ex) {
                    Logger.getLogger(TwitterLogin.class.getName()).log(Level.SEVERE, null, ex);
                }
                }
 else{
                try {
                    //System.out.println("has account");
                    renderRequest.setAttribute("username", TwitterComponent.getTwitter(u.getUserId()).getScreenName());
                } catch (TwitterException ex) {
                    Logger.getLogger(TwitterLogin.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalStateException ex) {
                    Logger.getLogger(TwitterLogin.class.getName()).log(Level.SEVERE, null, ex);
                }
include(successJSP, renderRequest, renderResponse);
   // renderResponse.getWriter().write(message);


}
}

	}

	public void processAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws IOException, PortletException {

            if(actionRequest.getParameter("logout")!=null)
            {

                User u =null;
        try {
             u = PortalUtil.getUser(actionRequest);

                TwitterComponent.logout(u.getUserId());
            } catch (Exception ex) {
                Logger.getLogger(TwitterLogin.class.getName()).log(Level.SEVERE, null, ex);
            }
            }

            if(actionRequest.getParameter("twitterpin")!=null)
            {
            try {
User u =null;
             u = PortalUtil.getUser(actionRequest);

                TwitterComponent.login(t,actionRequest.getParameter("twitterpin"),rt,u.getUserId());
            } catch (Exception ex) {
                Logger.getLogger(TwitterLogin.class.getName()).log(Level.SEVERE, null, ex);
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
        protected String errorJSP;
        protected String successJSP;
	private static Log _log = LogFactoryUtil.getLog(TwitterLogin.class);

}
