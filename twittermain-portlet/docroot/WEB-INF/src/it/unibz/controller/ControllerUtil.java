package it.unibz.controller;

import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;
import java.security.Principal;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.portlet.PortletRequest;

/**
 * Class contains methods globally used by the portlets controllers
 */
public class ControllerUtil {

    /**
     * Method returns the currently logged user if any
     * @param request portlet object containing needed data
     * @return the currently logged user, if guest, null
     */
    public static User getUser(PortletRequest request){
        try {
            return PortalUtil.getUser(request);
        } catch (Exception ex) {
            Logger.getLogger(ControllerUtil.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    /**
     * Returns the currently logged user's username
     * @param request portlet object containing needed data
     * @return the currently logged username, if guest, null
     */
  public static String getUserName(
      PortletRequest request) {
    Principal userPrincipal = 
      request.getUserPrincipal();

    return (userPrincipal == null) ? 
           null : 
           userPrincipal.getName();
  }
}
