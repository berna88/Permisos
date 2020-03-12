package com.consistent.cuervo.permisos.models;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;

public class Empleado {
	private static Log log = LogFactoryUtil.getLog(Empleado.class.getName());
	
	private String noEmpleado;
	private String fechaIngreso;
	private String puesto;
	private String departamento;
	private String centroCostos;
	private String centrotrabajo;
	private int aniversario;
	private String diasDisponibles;
	private String nombre;
	private String apellidos;
	private User user;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getEmail() {
		return this.user.getEmailAddress();
	}
	
	public String getNoEmpleado() {
		try {
			noEmpleado = (String) user.getExpandoBridge().getAttribute("No_Empleado");
		} catch (Exception e) {
			// TODO: handle exception
			//log.error("Method: getNoEmpleado");
			noEmpleado = "";
		}
		return noEmpleado;
	}
	public void setNoEmpleado(String noEmpleado) {
		this.noEmpleado = noEmpleado;
	}
	public String getFechaIngreso() {
		try {
			fechaIngreso = (String) user.getExpandoBridge().getAttribute("Fecha_Antiguedad");
			if(!fechaIngreso.isEmpty() && fechaIngreso != null) {
				String ano = "";
				String mes = "";
				String dia = "";
				for (int i=0; i < fechaIngreso.length(); i++) {
					if(i<=3) {
						ano += fechaIngreso.charAt(i);
					}
					if (i>=4 && i<=5) {
						mes += fechaIngreso.charAt(i);
					}
					if (i>=6 && i<=7) {
						dia += fechaIngreso.charAt(i);
					}
				}
				String fechaFinal = dia+"/"+mes+"/"+ano;
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("es", "ES"));
				Date myDate = sdf.parse(fechaFinal);
				sdf.applyPattern("EEEE, MMMMM d, yyyy");
				String sMyDate = sdf.format(myDate);
				fechaIngreso = sMyDate.substring(0, 1).toUpperCase() + sMyDate.substring(1);
			}
		} catch (Exception e) {
			// TODO: handle exception
			fechaIngreso = "";
		}
		return fechaIngreso;
	}
	public void setFechaIngreso(String fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}
	public String getPuesto() {
		try {
			puesto = (String) user.getExpandoBridge().getAttribute("Desc_Puesto_Trabajo");
		} catch (Exception e) {
			// TODO: handle exception
			//log.error("Method: getPuesto");
			puesto = "";
		}
		return puesto;
	}
	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}
	public String getDepartamento() {
		try {
			departamento = (String) user.getExpandoBridge().getAttribute("Desc_Depto");
		} catch (Exception e) {
			// TODO: handle exception
			//log.error("Method: getDepartamento");
			departamento = "";
		}
		return departamento;
	}
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	public String getCentroCostos() {
		try {
			centroCostos = (String) user.getExpandoBridge().getAttribute("Clave_Centro_Costos");
		} catch (Exception e) {
			// TODO: handle exception
			//log.error("Method: getCentroCostos");
			centroCostos = "";
		}
		return centroCostos;
	}
	public void setCentroCostos(String centroCostos) {
		this.centroCostos = centroCostos;
	}
	public String getCentrotrabajo() {
		try {
			centrotrabajo = (String) user.getExpandoBridge().getAttribute("Desc_Lugar_de_Trabajo");
		} catch (Exception e) {
			// TODO: handle exception
			//log.error("Method: getCentrotrabajo");
			centrotrabajo = "";
		}
		return centrotrabajo;
	}
	public void setCentrotrabajo(String centrotrabajo) {
		this.centrotrabajo = centrotrabajo;
	}
	public int getAniversario() {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		try {
			fechaIngreso = (String) user.getExpandoBridge().getAttribute("Fecha_Antiguedad");
			if(!fechaIngreso.isEmpty() && fechaIngreso != null) {
				String ano = "";
				
				for (int i=0; i < fechaIngreso.length(); i++) {
					if(i<=3) {
						ano += fechaIngreso.charAt(i);
					}
					
				}
				aniversario = year - Integer.parseInt(ano);
			}
		} catch (Exception e) {
			//log.error("getAniversario"+e.getMessage());
			// TODO: handle exception
			aniversario = 0;
		}
		return aniversario;
	}
	public void setAniversario(int aniversario) {
		this.aniversario = aniversario;
	}
	public String getDiasDisponibles() {
		return diasDisponibles;
	}
	public void setDiasDisponibles(String diasDisponibles) {
		this.diasDisponibles = diasDisponibles;
	}
	public String getNombre() {
		try {
			nombre = (String) user.getExpandoBridge().getAttribute("Nombres");
		} catch (Exception e) {
			// TODO: handle exception
			//log.error("Method: getNombre");
			nombre = "";
		}
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellidos() {
		try {
			apellidos = (String) user.getExpandoBridge().getAttribute("Apellido_Paterno");
			apellidos = apellidos + " " + (String) user.getExpandoBridge().getAttribute("Apellido_Materno");
			;
		} catch (Exception e) {
			// TODO: handle exception
			//log.error("Method: getApellidos");
			apellidos = "";
		}
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	
	public Empleado(User user) {
		this.user = user;
	}
	
	public Empleado(String noEmpleado, String fechaIngreso, String puesto, String departamento, String centroCostos,
			String centrotrabajo, int aniversario, String diasDisponibles, String nombre, String apellidos) {
		super();
		this.noEmpleado = noEmpleado;
		this.fechaIngreso = fechaIngreso;
		this.puesto = puesto;
		this.departamento = departamento;
		this.centroCostos = centroCostos;
		this.centrotrabajo = centrotrabajo;
		this.aniversario = aniversario;
		this.diasDisponibles = diasDisponibles;
		this.nombre = nombre;
		this.apellidos = apellidos;
	}
	
	public Empleado() {
		super();
		this.noEmpleado = "";
		this.fechaIngreso = "";
		this.puesto = "";
		this.departamento = "";
		this.centroCostos = "";
		this.centrotrabajo = "";
		this.aniversario = 0;
		this.diasDisponibles = "";
		this.nombre = "";
		this.apellidos = "";
	}
	
	
}
