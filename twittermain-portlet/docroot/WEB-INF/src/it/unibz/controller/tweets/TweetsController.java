package it.unibz.controller.tweets;

import com.liferay.portal.model.User;
import it.unibz.controller.ControllerUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.portlet.ModelAndView;

import it.unibz.service.TwitterService;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Class represents the render and actionhandler for the TwitterTimeline Portlet
 */
@RequestMapping("view")
@Controller
public class TweetsController {

    /**
     * Handles and render requests.
     * If it comes after an action, the model is appropriately filled
     * with information messages if needed.
     * Its main task is to fetch new tweets and to send them to the service
     * which stores them and returns the number of new tweets.
     * It adds to the Model the list of statuses to output.
     * @param request
     * @param response
     * @return Model to show
     * @throws Exception
     */
    @RenderMapping
    protected ModelAndView handleRenderRequestInternal(
            RenderRequest request,
            RenderResponse response)
            throws Exception {
        Map model = new HashMap();
        User u = ControllerUtil.getUser(request);
        List<Status> statuses = new ArrayList<Status>();
        int num = 0;
        if (u == null || u.getEmailAddress().contains("guest") || twitterService.loadAccessToken(u.getUserId()) == null) {
            model.put("toprint", "error");
        } else {
            try {
                Twitter twit = twitterService.getTwitter(u.getUserId());
                Paging p = new Paging(1, Integer.parseInt(request.getPreferences().getValue(
                        "pageSize",
                        PreferencesCommand.DEFAULT_PAGE_SIZE)));
                statuses = twit.getHomeTimeline(p);

                num = twitterService.saveTweets(statuses, twit.getScreenName(), Integer.parseInt(request.getPreferences().getValue(
                        "pageSize",
                        PreferencesCommand.DEFAULT_PAGE_SIZE)));
            } catch (TwitterException ex) {
            }
        }

        model.put("stats", statuses);
        model.put("numtweets", num);
        model.put("pageSize",
                request.getPreferences().getValue(
                "pageSize",
                PreferencesCommand.DEFAULT_PAGE_SIZE));

        return new ModelAndView("tweetList", model);

    }

    /**
     * Method handles action Requests
     * It handles the tweet actions to post messages
     * and then sends render impulse
     * @param request
     * @param response
     * @throws Exception
     */
    @ActionMapping
    protected void handleActionRequestInternal(ActionRequest request, ActionResponse response)
            throws Exception {
        if (request.getParameter("tweettext") != null && !request.getParameter("tweettext").isEmpty()) {

            User u = ControllerUtil.getUser(request);

            twitterService.getTwitter(u.getUserId()).updateStatus(request.getParameter("tweettext"));
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
