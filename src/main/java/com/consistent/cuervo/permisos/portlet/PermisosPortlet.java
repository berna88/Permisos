package com.consistent.cuervo.permisos.portlet;

import com.consistent.cuervo.permisos.constants.PermisosPortletKeys;
import com.consistent.cuervo.permisos.models.Empleado;
import com.consistent.cuervo.permisos.portal.Portal;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.apache.commons.io.IOUtils;
import org.osgi.service.component.annotations.Component;

@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=Cuervo",
		"com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=Permisos",
		"com.liferay.portlet.header-portlet-css=/css/bootstrap-datepicker.css",
		"com.liferay.portlet.header-portlet-css=/css/main.css",
		"com.liferay.portlet.header-portlet-css=/css/banner.css",
		"com.liferay.portlet.header-portlet-css=/css/form.css",
		"com.liferay.portlet.header-portlet-css=/css/general.css",
		"com.liferay.portlet.header-portlet-css=/css/calendar.css",
		//"com.liferay.portlet.header-portlet-css=/css/jquery-ui.css",
		//"com.liferay.portlet.footer-portlet-javascript=/js/jquery-ui.js",
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
	public void render(RenderRequest renderRequest, RenderResponse renderResponse) throws IOException, PortletException {
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
				List<User> users = new ArrayList<>();
				renderRequest.setAttribute("Empleado", empleado);
				renderRequest.setAttribute("users", users);
			}
			super.render(renderRequest, renderResponse);
			
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getCause());
			e.printStackTrace();
		}
	}
	
	@Override
	public void serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse) throws IOException, PortletException {

		String nameParam = ParamUtil.getString(resourceRequest, "mvcPath");
		try {
			if(nameParam != null && nameParam.equalsIgnoreCase("addRequestPermissions")) {
				String strInicio = ParamUtil.getString(resourceRequest, "Inicio");
				String strRegreso = ParamUtil.getString(resourceRequest, "Final");
				String strDiasTomar = ParamUtil.getString(resourceRequest, "Diasatomar");
				String strGerente = ParamUtil.getString(resourceRequest, "Gerente");
				String strTipoPermiso = ParamUtil.getString(resourceRequest, "TipoPermiso");
				String strJefe = ParamUtil.getString(resourceRequest, "Jefe");
				String strComentarios = ParamUtil.getString(resourceRequest, "Comentarios");
				String strRhVoBo = ParamUtil.getString(resourceRequest, "Rhvobo");
				String strPortalURL = ParamUtil.getString(resourceRequest, "portalURL");
				
				/*log.info("Email: "+empleado.getEmail());
				SendMail.mail(empleado.getEmail(), "tienda@cuervo.com", "permisos", null);
				log.info("Correo enviado");*/
				ThemeDisplay td  =(ThemeDisplay)resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
				User user = td.getUser();
				File objFilePDF = PermisosPDF.generarPDF(strInicio, strRegreso, strDiasTomar, strGerente, strTipoPermiso, strJefe, strComentarios, strRhVoBo, user, td, strPortalURL);
				
				if(resourceResponse.getStatus() == 200) {
					if(objFilePDF != null){
						resourceResponse.setContentType("application/pdf");
						resourceResponse.addProperty("Content-disposition", "atachment; filename=Solicitud_permiso.pdf");
						OutputStream out = null;
						InputStream in = null;
						try {
							out = resourceResponse.getPortletOutputStream();
							in = new FileInputStream(objFilePDF);
							IOUtils.copy(in, out);		
						} catch (final IOException e) {
							e.printStackTrace();
						} finally {
							try {
								if (Validator.isNotNull(out)) {
									out.flush();
									out.close();
								}
								if (Validator.isNotNull(in)) {
									in.close();
								}

							} catch (final IOException e) {
								e.printStackTrace();
							}
						}
					}
						
				}
				
			}
			super.serveResource(resourceRequest, resourceResponse);
		}catch (Exception e) {
			log.error("serveResource");
			log.error(e.getCause());
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void doView(RenderRequest renderRequest, RenderResponse renderResponse) throws IOException, PortletException {
		super.doView(renderRequest, renderResponse);
	}
	
}