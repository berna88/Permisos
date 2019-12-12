<%@page import="com.consistent.cuervo.permisos.models.Empleado"%>
<%@page import="com.liferay.portal.kernel.model.User"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
Empleado usuario = (Empleado) request.getAttribute("Empleado");
%>
<div class="row justify-content-center">
	<div class="col-md-5">
		<div id="permisos" class="mt-50 mb-50">
			<div class="form-row">
		      <div class="col-md-12 mb-3">
		      	<label>Tipo de permiso:</label>
		      	<div class="form-check mb-1">
			      <label class="form-check-label terminosVacaciones" for="radio1">
			        <input type="radio" class="form-check-input" id="radio1" name="optradio" onclick="getPermiso(this.value)" value="Permiso con goce de sueldo">Permiso con goce de sueldo
			      	<span class="checkmark"></span>
			      </label>
			    </div>
			    <div class="form-check mb-1">
			      <label class="form-check-label terminosVacaciones" for="radio2">
			        <input type="radio" class="form-check-input" id="radio2" name="optradio" onclick="getPermiso(this.value)" value="Permiso sin goce de sueldo">Permiso sin goce de sueldo
			      	<span class="checkmark"></span>
			      </label>
			    </div>
			    <div class="form-check">
			      <label class="form-check-label terminosVacaciones">
			        <input type="radio" class="form-check-input" id="radio3" name="optradio" onclick="getPermiso(this.value)" value="Falta injustificada (sin goce)">Falta injustificada (sin goce)
			      	<span class="checkmark"></span>
			      </label>
			    </div>
		      </div>
		      <div class="form-group col-12 col-sm-12 col-md-12 col-lg-6 col-xl-6">
				<label for="fechaInicio" >
					Fecha de inicio*
				</label>
				<div class="input-group mb-3 ">
					<input type="text" class="form-control form-control-sm calendar" style="background: url('<%=request.getContextPath()+"/img/calendar-cuervo.svg"%>') no-repeat scroll 5px 4px;background-size: 17px;background-position: 96%; " placeholder="Fecha de inicio" id="fechaInicio" autocomplete="off">
				</div>
			</div>
			<div class="form-group col-12 col-sm-12 col-md-12 col-lg-6 col-xl-6">
				<label for="fechaRegreso" >
					Regresa a laborar*
				</label>
				<div class="input-group mb-3 ">
					<input type="text" class="form-control form-control-sm" style="background: url('<%=request.getContextPath()+"/img/calendar-cuervo.svg"%>') no-repeat scroll 5px 4px;background-size: 17px; background-position: 96%;" placeholder="Regresa a laborar" id="fechaRegreso" autocomplete="off">
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
      			<textarea class="form-control mb-3" rows="10" cols="50" id="comentarios" name="text" style="resize: none;color: #CDB874;height: 120px;"></textarea>
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
			    <select id="RecursosHumanos" class="form-control form-control-sm" name="Recursos Humanos">
			    </select>
			    <small id="emailHelp" class="form-text text-muted">Nombre y firma.</small>
			</div>
			<div class="form-group col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 mb-3">
			    <div class="form-check">
					<label id="mensajeError" style="color:red;"></label>
			    </div>
			</div>
			<div class="form-group col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12">
				<button id="Send" class="btn btn-primary">Enviar</button>
				
			</div>
		    </div>
		 </div>
	</div>
</div>
<!-- </form> -->
<script src='<%=request.getContextPath()+"/js/select2.min.js"%>'></script>
<script src='<%=request.getContextPath()+"/js/i18n/es.js"%>'></script>

<datalist id="informacion2">
	<%
List<User> usuarios = (List<User>) request.getAttribute("users");
System.out.println("Tamaño: "+usuarios.size());
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
					if(userLiferay.getExpandoBridge().hasAttribute("Apellido_Materno"))
						apellidoMaterno = (String)userLiferay.getExpandoBridge().getAttribute("Apellido_Materno");
					if(userLiferay.getExpandoBridge().hasAttribute("Apellido_Paterno"))
						apellidoPaterno = (String)userLiferay.getExpandoBridge().getAttribute("Apellido_Paterno");
					
					String fullName = nombre + " " + apellidoPaterno + " " + apellidoMaterno;
					strObjJSON += "{ \"id\": \""+strNo_Empleado+"\", \"text\": \""+fullName+"\"},";
					System.out.println("fullname: "+fullName);
					System.out.println("json: "+strObjJSON);
				}				                           
				                         
%>
				<option id=<%=strNo_Empleado %> value=<%=userLiferay.getFullName() %> >
<%			} 
		}
	}
	strObjJSON += "{\"id\": \"\" , \"text\": \"\",  \"selected\": \"true\"}]}";
	System.out.println(strObjJSON);
}else {
	strObjJSON = "{}";
}
%>
</datalist>
<script>
	var myPermiso = "";
	function getPermiso(tipoPermiso) {
		myPermiso = tipoPermiso;
		console.log(myPermiso);
	}
	
	$(function(){
		var _ojbUser = '<%=strObjJSON%>';
		var _usuario = '<%=usuario%>';
		console.log(_usuario);
		
		var _UsersJSON = JSON.parse(_ojbUser);
		var tipValor = getPermiso();
		console.log(_UsersJSON);
		//_UsersJSON.results.splice(-1,1);
		
		$('#JefeInmediato').select2({
			  data: _UsersJSON.results,
			  placeholder: 'Selecciona una opción',
			  language: "es"
		});
		
		$('#Gerente_Director').select2({
			  data: _UsersJSON.results,
			  placeholder: 'Selecciona una opción',
			  language: "es"
		});
		
		$('#RecursosHumanos').select2({
			  data: _UsersJSON.results,
			  placeholder: 'Selecciona una opción',
			  language: "es"
		});
		
		$.datepicker.regional['es'] = {
				closeText: 'Cerrar',
				prevText: '< Ant',
				nextText: 'Sig >',
				currentText: 'Hoy',
				monthNames: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
				monthNamesShort: ['Ene','Feb','Mar','Abr', 'May','Jun','Jul','Ago','Sep', 'Oct','Nov','Dic'],
				dayNames: ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'],
				dayNamesShort: ['Dom','Lun','Mar','Mié','Juv','Vie','Sáb'],
				dayNamesMin: ['D','L','M','M','J','V','S'],
				weekHeader: 'Sm',
				dateFormat: 'dd/mm/yy',
				firstDay: 1,
				isRTL: false,
				showMonthAfterYear: false,
				yearSuffix: ''
		};
		
		var inputFecha = $( "#fechaInicio" );
		var changeCloseButton = function(input) {
			console.log('fechaInicio');
			setTimeout(function() {
				console.log('changeCloseButton ', input);
		        var headerPanel = inputFecha.datepicker( "widget" ).find( ".ui-datepicker-title" );
		        var closeBtn = $('<button style=\"position:relative; left: 90%;border: none;background: none; color: #CDB874;outline: none;padding: 10px;\">X</button>');
		        
		        closeBtn.bind("click", function() {
		        	$( "#fechaRegreso" ).datepicker( "hide" );
		        });
		        
		        closeBtn.prependTo(headerPanel);
		    }, 1000 );
		};
		
		$( "#fechaInicio" ).datepicker({
			changeMonth: true,
            changeYear: true,
            hideIfNoPrevNext: true,
            minDate: 0,
            dateFormat: "yy-mm-dd",
			beforeShow: changeCloseButton,			
			onChangeMonthYear: changeCloseButton
		}).focus(function () {
	        $(".ui-datepicker-next").hide();
	        $(".ui-datepicker-prev").hide();
	    });
		
		$("#datepicker2").datepicker({
			 showButtonPanel: true,
			 beforeShow: function( input ) {
			 	setTimeout(function() {
			   		var buttonPane = $( input ).datepicker( "widget" ).find( ".ui-datepicker-buttonpane" );

			   		var btn = $('Clear');
			   		btn.unbind("click").bind("click", function () { 
			   			$.datepicker._clearDate( input );
			  		});

			   		btn.appendTo( buttonPane );

			 }, 1 );
			      }
			});
		
		$( "#fechaRegreso" ).datepicker({
			changeMonth: true,
            changeYear: true,
            hideIfNoPrevNext: true,
            minDate: 0,
            dateFormat: "yy-mm-dd",
            beforeShow: function( input ) {
            	console.log('fechaRegreso beforeShow');
            	setTimeout(function() {
    		        var headerPanel = $(input).datepicker( "widget" ).find( ".ui-datepicker-title" );
    		        var closeBtn = $('<button style=\"position:relative; left: 90%;border: none;background: none; color: #CDB874;outline: none;padding: 10px;\">X</button>');
    		        
    		        closeBtn.bind("click", function() {
    		        	$( "#fechaRegreso" ).datepicker( "hide" );
    		        });
    		        
    		        closeBtn.prependTo(headerPanel);
    		    }, 300 );
            },
			onChangeMonthYear: function( input ) {
				console.log('fechaRegreso onChangeMonthYear');
				setTimeout(function() {
			        var headerPanel = $(input).datepicker( "widget" ).find( ".ui-datepicker-title" );
			        var closeBtn = $('<button style=\"position:relative; left: 90%;border: none;background: none; color: #CDB874;outline: none;padding: 10px;\">X</button>');
			        
			        closeBtn.bind("click", function() {
			        	$( "#fechaRegreso" ).datepicker( "hide" );
			        });
			        
			        closeBtn.prependTo(headerPanel);
			    }, 200 );
            },
		}).focus(function () {
	        $(".ui-datepicker-next").hide();
	        $(".ui-datepicker-prev").hide();
	    });		
		
		$.datepicker.setDefaults($.datepicker.regional['es']);
				
		$("#Send").on('click', function(){
			console.log("Entrando a send");
			var _tipPermiso = myPermiso;
			var _comentarios = document.getElementById("comentarios").value;
			var _fechaInicio = document.getElementById("fechaInicio").value;
			var _fechaRegreso = document.getElementById("fechaRegreso").value;
			var _diasSolicitados = document.getElementById("diasSolicitados").value;
			
			var _JefeInmediato = $('#JefeInmediato').val();
			var _JefeInmediatoId = $('#JefeInmediato').val();
			var _Gerente_Director = $('#Gerente_Director').val();
			var _Gerente_DirectorId = $('#Gerente_Director').val();
			var _RecursosHumanos = $('#RecursosHumanos').val();
			var _RecursosHumanosId = $('#RecursosHumanos').val();
			var error = document.getElementById('mensajeError');
			console.log("Solicitados: "+_diasSolicitados);
			console.log("_fechaInicio: "+_fechaInicio);
			console.log("_fechaInicio"+_fechaInicio.trim());
			
			if(_fechaInicio.trim() === "" || _fechaRegreso.trim() === "" || typeof _tipPermiso === 'undefined' || _diasSolicitados.trim() === "" || _JefeInmediatoId.trim() === "" || _Gerente_DirectorId.trim() === "" || _RecursosHumanosId.trim() === ""){
				console.log("Esta vacio");
				error.innerHTML = "*Todos los campos son requeridos";
				return "";
			}
			
			console.log(_fechaInicio, ' ', _fechaRegreso, ' ', _diasSolicitados, ' ',  _JefeInmediato, ' ', _Gerente_Director, ' ', _RecursosHumanos, ' ', _tipPermiso, ' ', _comentarios  );
			
			var _SolicitudJSON = "{\"Inicio\":\""+_fechaInicio+"\",\"Diasatomar\": \""+_diasSolicitados+"\",\"Gerente\":\""
				+_Gerente_DirectorId+"\",\"TipoPermiso\":\""+_tipPermiso+"\",\"Jefe\":\""+_JefeInmediatoId+"\",\"Comentarios\":\""+_comentarios+"\",\"Final\":\""
				+_fechaRegreso+"\", \"Rhvobo\":\""+_RecursosHumanosId+"\"}";
				
			console.log(_SolicitudJSON);	
			var _objSolicitudJSON = JSON.parse(_SolicitudJSON);
			
			var pathname = window.location.pathname; // Returns path only (/path/example.html)
			var url      = window.location.href;     // Returns full URL (https://example.com/path/example.html)
			var origin   = window.location.origin;   // Returns base URL (https://example.com)
			
			console.log('pathname ' , pathname , ' url ', url , ' origin ', origin);
			
			//window.location.href = origin + pathname;
			
			$.ajax({
				url: '${addRequestPermisosURL}',
			    type: 'POST',
			    datatype:'json',
			    cache:false,
			    data: {
			    	<portlet:namespace/>Inicio : _fechaInicio,
			    	<portlet:namespace/>Diasatomar : _diasSolicitados,
			    	<portlet:namespace/>Gerente : _Gerente_DirectorId,
			    	<portlet:namespace/>TipoPermiso : _tipPermiso,
			    	<portlet:namespace/>Jefe : _JefeInmediatoId,
			    	<portlet:namespace/>Comentarios : _comentarios,
			    	<portlet:namespace/>Final : _fechaRegreso,
			    	<portlet:namespace/>Rhvobo : _RecursosHumanosId
			    },
			    success: function(data){
			    	console.log("Response "+data);
			    	window.location.href = origin + pathname;
			    },
			    error : function(XMLHttpRequest, textStatus, errorThrown){
			    	console.log('XMLHttpRequest', XMLHttpRequest);
			        console.log("errorThrown ", errorThrown);
			        console.log("textStatus ", textStatus);
			    }
			    //beforeSend: setHeader
			});
			
			
				
		});
	});
</script>
