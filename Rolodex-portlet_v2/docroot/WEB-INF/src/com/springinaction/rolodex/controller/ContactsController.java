package com.springinaction.rolodex.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.portlet.ModelAndView;

import com.springinaction.rolodex.service.RolodexService;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

@Controller
@RequestMapping("VIEW")
public class ContactsController 
     {    

    @RenderMapping
  protected 
      ModelAndView handleRenderRequestInternal(
          RenderRequest request, 
          RenderResponse response) 
          throws Exception {    

    String userName = 
        ControllerUtil.getUserName(request);
    List contacts = 
        rolodexService.getContacts(userName);

    Map model = new HashMap();    
    model.put("contacts", contacts);
    model.put("actionName", "contactList");
    model.put("pageSize", 
        request.getPreferences().getValue(
            "pageSize", 
            PreferencesCommand.DEFAULT_PAGE_SIZE));

    return new ModelAndView("contactList", model);  
  }
  
  private RolodexService rolodexService;
    @Resource (name="rolodexService")
    @Required
  public void setRolodexService(
      RolodexService rolodexService) {
    this.rolodexService = rolodexService;
  }
}
