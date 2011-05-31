package com.springinaction.rolodex.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.validation.BindException;
import org.springframework.web.portlet.ModelAndView;

import com.springinaction.rolodex.service.RolodexService;
import javax.annotation.Resource;
import javax.portlet.PortletRequest;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
@Controller
@RequestMapping("VIEW")
public class SearchContactsController  {
  
     @ModelAttribute("searchCommand")
  protected SearchCommand getSearchCommand(PortletRequest request) throws Exception {
    SearchCommand command = new SearchCommand() ;

    return command;
  }


  protected void handleAction(
      ActionRequest request, 
      ActionResponse response, 
      Object command, 
      BindException bindException) 
      throws Exception {
  }

  @RenderMapping(params = "action=searchContacts")
  protected ModelAndView handleRender(@ModelAttribute(value="searchCommand")SearchCommand command,
      RenderRequest request, 
      RenderResponse response) 
      throws Exception {
    
    String userName = 
        ControllerUtil.getUserName(request);
    
    SearchCommand searchCommand = 
        command;
    
    List contacts = rolodexService.
        searchContacts(userName, 
            searchCommand);
    
    Map model = new HashMap();
    model.put("contacts", contacts);
    model.put("actionName", "searchContacts");
    model.put("pageSize", 
        request.getPreferences().getValue(
            "pageSize", "5"));

    return new ModelAndView(
        "contactList", model);  
  }
  
  private RolodexService rolodexService;
     @Resource (name="rolodexService")
    @Required
     public void setRolodexService(
      RolodexService rolodexService) {
    this.rolodexService = rolodexService;
  }
}
