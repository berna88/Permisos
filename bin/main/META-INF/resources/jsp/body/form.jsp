<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="row justify-content-center">
	<div class="col-md-5">
		<form id="permisos" class="mt-50 mb-50">
			<div class="form-row">
		      <div class="col-md-12 mb-3">
		      	<label>Tipo de permiso:</label>
		      	<div class="form-check mb-1">
			      <label class="form-check-label terminosVacaciones" for="radio1">
			        <input type="radio" class="form-check-input" id="radio1" name="optradio" value="option1" checked>Permiso con goce de sueldo
			      	<span class="checkmark"></span>
			      </label>
			    </div>
			    <div class="form-check mb-1">
			      <label class="form-check-label terminosVacaciones" for="radio2">
			        <input type="radio" class="form-check-input" id="radio2" name="optradio" value="option2">Permiso sin goce de sueldo
			      	<span class="checkmark"></span>
			      </label>
			    </div>
			    <div class="form-check">
			      <label class="form-check-label terminosVacaciones">
			        <input type="radio" class="form-check-input" id="radio3" name="optradio" value="option3">Falta injustificada (sin goce)
			      	<span class="checkmark"></span>
			      </label>
			    </div>
		      </div>
		      <div class="form-group col-12 col-sm-12 col-md-12 col-lg-6 col-xl-6">
				<label for="fechaInicio" >
					Fecha de inicio*
				</label>
				<div class="input-group mb-3 ">
					<input type="text" class="form-control form-control-sm calendar" style="background: url('<%=request.getContextPath()+"/img/calendar-cuervo.svg"%>') no-repeat scroll 5px 4px;background-size: 18px;background-position: 99%; " placeholder="Fecha de inicio" id="fechaInicio" >
				</div>
			</div>
			<div class="form-group col-12 col-sm-12 col-md-12 col-lg-6 col-xl-6">
				<label for="fechaRegreso" >
					Regresa a laborar*
				</label>
				<div class="input-group mb-3 ">
					<input type="text" class="form-control form-control-sm" style="background: url('<%=request.getContextPath()+"/img/calendar-cuervo.svg"%>') no-repeat scroll 5px 4px;background-size: 18px; background-position: 99%;" placeholder="Regresa a laborar" id="fechaRegreso" >
				</div>
			</div>
			<div class="form-group col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12">
				<label for="fechaInicio" >
					Días solicitados*
				</label>
				<input type="number" class="form-control form-control-sm mb-3" placeholder="Días solicitados" id="diasSolicitados" min="0" max="50" >
			</div>
		      <div class="col-md-12">
		      	<label for="comment">Comentarios:</label>
      			<textarea class="form-control mb-3" rows="2" id="comment" name="text"></textarea>
		      </div>
		      <div class="form-group col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 select">
				<label for="exampleFormControlSelect2">
					Jefe Inmediato*
				</label>
				<!-- <input type="text" class="form-control form-control-sm"  id="JefeInmediato" list="informacion2"> -->
			    <select id="JefeInmediato" class="form-control form-control-sm" name="Jefe Inmediato">
			    </select>
			    <small id="emailHelp" class="form-text text-muted">Nombre y firma.</small>
			</div>
			<div class="form-group col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 select">
				<label for="exampleFormControlSelect2">
					Recursos Humanos*
				</label>
				<!-- <input type="text" class="form-control form-control-sm"  id="Gerente_Director" list="informacion2"> -->
			    <select id="Gerente_Director" class="form-control form-control-sm" name="Gerente Director">
			    </select>
			    <small id="emailHelp" class="form-text text-muted">Nombre y firma.</small>
			</div>
			<div class="form-group col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 select">
				<label for="exampleFormControlSelect2">
					Gerente o Director de área*
				</label>
				<!-- <input type="text" class="form-control form-control-sm"  id="Gerente_Director" list="informacion2"> -->
			    <select id="Gerente_Director" class="form-control form-control-sm" name="Gerente Director">
			    </select>
			    <small id="emailHelp" class="form-text text-muted">Nombre y firma.</small>
			</div>
			<div class="form-group col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12">
				<button id="Send" class="btn btn-primary">Enviar</button>
			</div>
		    </div>
		 </form>
	</div>
</div>
<!-- </form> -->
<script src='<%=request.getContextPath()+"/js/select2.min.js"%>'></script>
<script src='<%=request.getContextPath()+"/js/i18n/es.js"%>'></script>
