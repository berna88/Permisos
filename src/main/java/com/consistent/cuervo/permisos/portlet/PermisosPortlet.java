package com.consistent.cuervo.permisos.portlet;

import com.consistent.cuervo.permisos.constants.PermisosPortletKeys;
import com.consistent.cuervo.permisos.models.Empleado;
import com.consistent.cuervo.permisos.portal.Portal;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.util.PortalUtil;

import java.io.IOException;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

/**
 * @author bernardohernandez
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=category.sample",
		"com.liferay.portlet.header-portlet-css=/css/main.css",
		"com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=Permisos",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + PermisosPortletKeys.PERMISOS,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)
public class PermisosPortlet extends MVCPortlet {
	
	private static Log log = LogFactoryUtil.getLog(PermisosPortlet.class.getName());
	private Empleado empleado;
	
	@Override
	public void render(RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException {
		// TODO Auto-generated method stub
		log.info("Render");
		try {
			
			User user = PortalUtil.getUser(renderRequest);
			empleado = new Empleado(user);
			
			if(empleado.getUser()!=null) {
				log.info("Logeado");
				renderRequest.setAttribute("Empleado", empleado);
				renderRequest.setAttribute("users", Portal.getUsers());
			}else {
				log.info("No logeado");
				Empleado empleado = new Empleado();
				renderRequest.setAttribute("Empleado", empleado);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		super.render(renderRequest, renderResponse);
	}
}