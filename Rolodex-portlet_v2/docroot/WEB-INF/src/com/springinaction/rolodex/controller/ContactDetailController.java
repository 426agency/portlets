package com.springinaction.rolodex.controller;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.portlet.ModelAndView;

import com.springinaction.rolodex.domain.Contact;
import com.springinaction.rolodex.service.RolodexService;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;


@Controller
@RequestMapping("VIEW")
public class ContactDetailController {

  @RenderMapping(params = "action=contactDetail")
  protected ModelAndView handleRenderRequestInternal(RenderRequest request,
      RenderResponse response) throws Exception {

    int id = Integer.parseInt(request.getParameter("contactId"));
    Contact contact = rolodexService.getContact(id);
    
    return new ModelAndView("contactDetail", "contact", contact);
  }
  
  private RolodexService rolodexService;
   @Resource (name="rolodexService")
    @Required
  public void setRolodexService(RolodexService rolodexService) {
    this.rolodexService = rolodexService;
  }
}
