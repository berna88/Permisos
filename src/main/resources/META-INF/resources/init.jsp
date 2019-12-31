<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.consistent.cuervo.permisos.models.Empleado" %>
<%@ page import="com.liferay.portal.kernel.model.User" %>
<%@ page import="java.util.List" %>

<liferay-theme:defineObjects />
<portlet:defineObjects />

<portlet:resourceURL var="addRequestPermisosURL">
	<portlet:param name="mvcPath" value="addRequestPermissions"/>
</portlet:resourceURL>

<%
Empleado usuario = (Empleado) request.getAttribute("Empleado");

List<User> usuarios = (List<User>) request.getAttribute("users");
String strObjJSON = "";
if(!usuarios.isEmpty()  && usuarios.size() > 0){
	strObjJSON = "{\"results\":[";
	for(User userLiferay: usuarios){
		if(!userLiferay.isDefaultUser()){
			if(userLiferay.isActive() && userLiferay.getExpandoBridge().hasAttribute("No_Empleado")){	
				String strNo_Empleado = (String)userLiferay.getExpandoBridge().getAttribute("No_Empleado");
				if( strNo_Empleado != null && !strNo_Empleado.equalsIgnoreCase("")){
					String nombre = userLiferay.getFirstName();
					String apellidoMaterno = "";
					String apellidoPaterno = "";
					if(userLiferay.getExpandoBridge().hasAttribute("Nombres"))
						nombre = (String)userLiferay.getExpandoBridge().getAttribute("Nombres");
					if(userLiferay.getExpandoBridge().hasAttribute("Apellido_Materno"))
						apellidoMaterno = (String)userLiferay.getExpandoBridge().getAttribute("Apellido_Materno");
					if(userLiferay.getExpandoBridge().hasAttribute("Apellido_Paterno"))
						apellidoPaterno = (String)userLiferay.getExpandoBridge().getAttribute("Apellido_Paterno");
					
					String fullName = nombre + " " + apellidoPaterno + " " + apellidoMaterno;
					strObjJSON += "{ \"id\": \""+strNo_Empleado+"\", \"text\": \""+fullName+"\"},";
				}				                           
			} 
		}
	}
	strObjJSON += "{\"id\": \"\" , \"text\": \"\",  \"selected\": \"true\"}]}";
}else {
	strObjJSON = "{}";
}
%>