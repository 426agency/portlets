package it.unibz.controller.follower;

import com.liferay.portal.model.User;
import it.unibz.controller.ControllerUtil;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.validation.BindException;
import org.springframework.web.portlet.ModelAndView;

import it.unibz.service.TwitterService;
import javax.annotation.Resource;
import javax.portlet.PortletURL;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Class represents the render and actionhandler for the Follower Portlet
 */
@RequestMapping("view")
@Controller
public class FollowerController {

    protected void handleAction(
            ActionRequest request,
            ActionResponse response,
            Object command,
            BindException bindException)
            throws Exception {
    }

    private boolean testNullOrEmpty(String sz) {
        if (sz != null && !sz.equals("")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Handles and render requests.
     * If it comes after an action, the model is appropriately filled
     * with information messages if needed.
     * Main task is to prepare the message containing the list of
     * followers to output.
     * @param request
     * @param response
     * @return Model to show
     * @throws Exception
     */
    @RenderMapping
    ModelAndView handleRenderRequestInternal(
            RenderRequest request,
            RenderResponse response)
            throws Exception {
        Map model = new HashMap();
        User u = ControllerUtil.getUser(request);
        String msg = request.getParameter("msg");
        if (!testNullOrEmpty(msg)) {
            model.put("msg", msg);
        }
        String message = "";
        if (u == null || u.getEmailAddress().contains("guest") || twitterService.loadAccessToken(u.getUserId()) == null) {
            model.put("errormsg", "error");
        } else {

            try {
                // Status status = twitter.updateStatus(args[1]);
                message += "<table>";
                Twitter twit = twitterService.getTwitter(u.getUserId());
                long[] arrayLngIDs = twit.getFriendsIDs(-1).getIDs();
                PortletURL action = response.createActionURL();
                twitter4j.User twitteruser = null;
                for (int i = 0; i < arrayLngIDs.length; i++) {
                    twitteruser = twit.showUser(arrayLngIDs[i]);
                    action = response.createActionURL();
                    action.setParameter("followerID", String.valueOf(arrayLngIDs[i]));
                    message += "<tr><td>" + twitteruser.getScreenName() + "</td><td><a href=\"" + action.toString()
                            + "\"><img src=\"/twitterfollower-portlet/images/trash.gif\" border=\"0\""
                            + " title=\"Unfollow\"></a></td></tr>";
                }
                message += "</table>";
            } catch (TwitterException ex) {
                message = "";
            }
        }


        model.put("toprint", message);
        return new ModelAndView(
                "followers", model);
    }

    /**
     * Method handles action Requests
     * It handles the follow and unfollow actions
     * and then sends render impulse
     * @param request
     * @param response
     * @throws Exception
     */
    @ActionMapping
    protected void handleActionRequestInternal(ActionRequest request, ActionResponse response)
            throws Exception {

        User u = ControllerUtil.getUser(request);
        if (request.getParameter("followerID") != null) {
            twitterService.getTwitter(u.getUserId()).destroyFriendship(Integer.parseInt(request.getParameter("followerID")));
        }

        if (request.getParameter("followername") != null) {
            String followerName = request.getParameter("followername");
            try {
                twitterService.getTwitter(u.getUserId()).createFriendship(followerName);
            } catch (TwitterException ex) {
                response.setRenderParameter("msg", "Unable to follow user: " + followerName);
            }

        }

    }
    private TwitterService twitterService;

    @Resource(name = "twitterService")
    @Required
    public void setTwitterService(
            TwitterService twitterService) {
        this.twitterService = twitterService;
    }
}
