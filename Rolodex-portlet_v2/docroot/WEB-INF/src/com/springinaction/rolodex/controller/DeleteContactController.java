package com.springinaction.rolodex.controller;

import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.portlet.ModelAndView;

import com.springinaction.rolodex.service.RolodexService;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;


@Controller
@RequestMapping("VIEW")
public class DeleteContactController  {
 @ActionMapping(params = "action=deleteContact")
  protected void handleActionRequestInternal(ActionRequest request, ActionResponse response)
      throws Exception {
    
    int contactId = Integer.parseInt(request.getParameter("contactId"));
    rolodexService.deleteContact(contactId);
  }
 @RenderMapping(params = "action=deleteContact")
  protected ModelAndView handleRenderRequestInternal(RenderRequest request,
      RenderResponse response) throws Exception {
    
    String userName = ControllerUtil.getUserName(request);
    
    List contacts = rolodexService.getContacts(userName);
    
    return new ModelAndView("contactList", "contacts", contacts);  

  }
  
  private RolodexService rolodexService;
   @Resource (name="rolodexService")
    @Required
  public void setRolodexService(RolodexService rolodexService) {
    this.rolodexService = rolodexService;
  }
}
