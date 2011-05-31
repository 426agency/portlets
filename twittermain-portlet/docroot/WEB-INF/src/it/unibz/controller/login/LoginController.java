package it.unibz.controller.login;
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
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;

@RequestMapping("view")
@Controller
public class LoginController  {
  


  protected void handleAction(
      ActionRequest request, 
      ActionResponse response, 
      Object command, 
      BindException bindException) 
      throws Exception {
  }

          private RequestToken rt=null;
        private Twitter t=null;

@RenderMapping
 ModelAndView handleRenderRequestInternal(
          RenderRequest request,
          RenderResponse response)
          throws Exception {
      Map model = new HashMap();
       User u = ControllerUtil.getUser(request);

       String message="";
      if(u==null||u.getEmailAddress().contains("guest")){
              model.put("errormsg", "notallowed");

     }
 else{

           if(twitterService.loadAccessToken(u.getUserId())==null){
                try {
                    t = new TwitterFactory().getInstance();
t.setOAuthConsumer("Kgrg9GlpGU8yoza6u1KqQQ", "bZUrgRsSWu9JiXMsE9mFFT5pcosZzPv4vKca7nhZsE");
                    rt=t.getOAuthRequestToken();
                    message = rt.getAuthorizationURL();
                    //renderResponse.getWriter().write(message);
                    //include(editJSP, renderRequest, renderResponse);
                } catch (TwitterException ex) {
                    //Logger.getLogger(TwitterLogin.class.getName()).log(Level.SEVERE, null, ex);
                }
                }
 else{
 model.put("username", twitterService.getTwitter(u.getUserId()).getScreenName());
 }
      }
       
       model.put("toprint", message);
    return new ModelAndView(
        "login", model);
  }
  
 @ActionMapping
  protected void handleActionRequestInternal(ActionRequest request, ActionResponse response)
      throws Exception {
    
     if(request.getParameter("logout")!=null)
            {

                User u = ControllerUtil.getUser(request);
                twitterService.logout(u.getUserId());
                //TwitterComponent.logout();

            }

            if(request.getParameter("twitterpin")!=null)
            {
            try {
                User u = ControllerUtil.getUser(request);

                twitterService.login(t,request.getParameter("twitterpin"),rt,u.getUserId());
            } catch (Exception ex) {
                //Logger.getLogger(TwitterLogin.class.getName()).log(Level.SEVERE, null, ex);
            }
            
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
