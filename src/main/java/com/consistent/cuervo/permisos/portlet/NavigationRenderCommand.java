package com.consistent.cuervo.permisos.portlet;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import com.consistent.cuervo.permisos.constants.PermisosPortletKeys;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;

@Component(
		immediate = true, 
		property = {
			"javax.portlet.name=" + PermisosPortletKeys.PERMISOS,
			"mvc.command.name=permiso-navigation"
		},
		service = MVCRenderCommand.class
	)
public class NavigationRenderCommand implements MVCRenderCommand {

	@Override
	public String render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {
		// TODO Auto-generated method stub
		return "/jsp/body/form.jsp";
	}

}
