package it.unibz.controller;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;
import java.security.Principal;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.portlet.PortletRequest;


public class ControllerUtil {

    public static User getUser(PortletRequest request){
        try {
            return PortalUtil.getUser(request);
        } catch (Exception ex) {
            Logger.getLogger(ControllerUtil.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

  public static String getUserName(
      PortletRequest request) {
    Principal userPrincipal = 
      request.getUserPrincipal();

    return (userPrincipal == null) ? 
           null : 
           userPrincipal.getName();
  }
}
