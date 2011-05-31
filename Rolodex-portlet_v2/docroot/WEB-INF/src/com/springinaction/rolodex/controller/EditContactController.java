package com.springinaction.rolodex.controller;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import org.springframework.web.portlet.bind.PortletRequestUtils;

import com.springinaction.rolodex.domain.Contact;
import com.springinaction.rolodex.service.RolodexService;
import javax.annotation.Resource;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

@Controller
@RequestMapping("VIEW")
public class EditContactController 
     {
  
 
 @ModelAttribute("contact")
  protected Contact getContact(
      PortletRequest request) throws Exception {
    
    int contactId = PortletRequestUtils.getIntParameter(
        request, "contactId", -1);
    
    Contact contact = 
        (contactId < 0) ? 
        new Contact() : 
        rolodexService.getContact(contactId);

    if(contact == null) {
      throw new 
          PortletException("Contact not found");
    }
    
    return contact;
  }
  
  
  @RenderMapping(params = "action=editContact")
  protected ModelAndView handleRenderRequestInternal(RenderRequest request,
      RenderResponse response) throws Exception {
/*
    int id = Integer.parseInt(request.getParameter("contactId"));
    Contact contact = rolodexService.getContact(id); */
    Contact contact  =getContact(request);
    return new ModelAndView("editContact", "contact", contact);
  }
  
 @ActionMapping(params = "action=editContact")
 protected void onSubmitAction(@ModelAttribute(value="contact")Contact command,
      ActionRequest request,
      ActionResponse response) 
      throws Exception {

    String userName = 
        ControllerUtil.getUserName(request);
    
    Contact contact = (Contact) command;
    rolodexService.addContact(contact, userName);
    
    response.setRenderParameter(
        "action", "contacts");
  }
  
  // injected
  private RolodexService rolodexService;
   @Resource (name="rolodexService")
    @Required
  public void setRolodexService(
      RolodexService rolodexService) {
    this.rolodexService = rolodexService;
  }

}
