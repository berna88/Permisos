package com.consistent.cuervo.permisos.models;

import com.liferay.portal.kernel.model.User;

public class Empleado {
	
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
	public String getNoEmpleado() {
		return noEmpleado;
	}
	public void setNoEmpleado(String noEmpleado) {
		this.noEmpleado = noEmpleado;
	}
	public String getFechaIngreso() {
		return fechaIngreso;
	}
	public void setFechaIngreso(String fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}
	public String getPuesto() {
		return puesto;
	}
	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}
	public String getDepartamento() {
		return departamento;
	}
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	public String getCentroCostos() {
		return centroCostos;
	}
	public void setCentroCostos(String centroCostos) {
		this.centroCostos = centroCostos;
	}
	public String getCentrotrabajo() {
		return centrotrabajo;
	}
	public void setCentrotrabajo(String centrotrabajo) {
		this.centrotrabajo = centrotrabajo;
	}
	public int getAniversario() {
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
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellidos() {
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
