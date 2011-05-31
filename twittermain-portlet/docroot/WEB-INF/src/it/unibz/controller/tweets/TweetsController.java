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

@RequestMapping("view")
@Controller
public class TweetsController
     {    

    @RenderMapping
  protected 
      ModelAndView handleRenderRequestInternal(
          RenderRequest request, 
          RenderResponse response) 
          throws Exception {    
Map model = new HashMap();
    User u = ControllerUtil.getUser(request);
    List<Status> statuses = new ArrayList<Status>();
    int num=0;
     if(u==null||u.getEmailAddress().contains("guest")||twitterService.loadAccessToken(u.getUserId())==null){
              //include(errorJSP,renderRequest,renderResponse);
         model.put("toprint", "error");
         //System.out.println("cannot get user"+u==null?"isnull":u.getEmailAddress());
     }
 else{
    

            try {
                Twitter twit = twitterService.getTwitter(u.getUserId());
                Paging p = new Paging(1, Integer.parseInt(request.getPreferences().getValue(
            "pageSize",
            PreferencesCommand.DEFAULT_PAGE_SIZE)));
                statuses = twit.getHomeTimeline(p);
                
                num= twitterService.saveTweets(statuses,twit.getScreenName(),Integer.parseInt(request.getPreferences().getValue(
            "pageSize", 
            PreferencesCommand.DEFAULT_PAGE_SIZE)));
                //renderRequest.setAttribute("numtweets", num);
                //message+="you have "+num+" new tweets";
            } catch (TwitterException ex) {
                //Logger.getLogger(TwitterMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
      
    model.put("stats", statuses);
    //model.put("actionName", "tweetList");
    model.put("numtweets", num);
    model.put("pageSize", 
        request.getPreferences().getValue(
            "pageSize", 
            PreferencesCommand.DEFAULT_PAGE_SIZE));

    return new ModelAndView("tweetList", model);

  }

    @ActionMapping
  protected void handleActionRequestInternal(ActionRequest request, ActionResponse response)
      throws Exception {
    if(request.getParameter("tweettext")!=null&&!request.getParameter("tweettext").isEmpty())
            {

               User u = ControllerUtil.getUser(request);

                twitterService.getTwitter(u.getUserId()).updateStatus(request.getParameter("tweettext"));
        }
    }

  private TwitterService twitterService;
    @Resource (name="twitterService")
    @Required
  public void setTwitterService(
      TwitterService twitterService) {
    this.twitterService = twitterService;
  }
}
