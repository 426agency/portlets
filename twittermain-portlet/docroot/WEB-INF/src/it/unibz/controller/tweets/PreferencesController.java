package it.unibz.controller.tweets;

import it.unibz.controller.*;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.springframework.stereotype.Controller;

import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

/**
 * Class represents the Edit portion for the TwitterTimeline Portlet
 */
@Controller
@RequestMapping("EDIT")
public class PreferencesController  {

    @ModelAttribute("preferences")
  protected PreferencesCommand getPreferencesCommand(PortletRequest request) throws Exception {
    PreferencesCommand command = new PreferencesCommand() ;
    PortletPreferences preferences = request.getPreferences();
    command.setPageSize(Integer.parseInt(
        preferences.getValue("pageSize", PreferencesCommand.DEFAULT_PAGE_SIZE)));

    return command;
  }

    /**
     * Handles and render requests.
     * @param request
     * @param response
     * @return Model to show
     * @throws Exception
     */
    @RenderMapping
  protected ModelAndView handleRenderRequestInternal(RenderRequest request,
      RenderResponse response) throws Exception {
    PreferencesCommand preferences  =getPreferencesCommand(request);
    return new ModelAndView("edit", "preferences", preferences);
  }

 @ActionMapping
  protected void onSubmitAction(@ModelAttribute(value="preferences")PreferencesCommand command,ActionRequest request, ActionResponse response) throws Exception {
    
    PreferencesCommand prefCommand = (PreferencesCommand) command;
    PortletPreferences preferences = request.getPreferences();
    preferences.setValue("pageSize", prefCommand.getPageSize()+"");
    preferences.store();
  }
}
