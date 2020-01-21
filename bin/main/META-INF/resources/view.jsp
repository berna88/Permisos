		<%@ include file="/init.jsp" %>
		<%@ include file="jsp/header/banner.jsp" %>
		
		<!-- <link rel="stylesheet" type="text/css" href="/o/Permisos-portlet/css/gijgo.min.css">
		<link rel="stylesheet" type="text/css" href="/o/Permisos-portlet/css/calendar.css"> -->
		
		<section class="row justify-content-center">
	 		<article class="col-10 col-sm-10 col-md-10 col-lg-10 col-xl-10">	 			
	 			<div class="row justify-content-center">
					<div class="col-md-5">
						<div id="permisos" class="mt-50 mb-50">
							<div class="form-row">
								<div class="col-md-12 mb-3">
									<label>Tipo de permiso:</label>
									<div class="form-check mb-1">
										<label class="form-check-label terminosVacaciones" for="radio1">
											<input type="radio" class="form-check-input" id="radio1" name="optradio" onclick="getPermiso(this.value)" value="Permiso con goce de sueldo">Permiso con goce de sueldo<span class="checkmark"></span>
										</label>
									</div>
									<div class="form-check mb-1">
										<label class="form-check-label terminosVacaciones" for="radio2">
											<input type="radio" class="form-check-input" id="radio2" name="optradio" onclick="getPermiso(this.value)" value="Permiso sin goce de sueldo">Permiso sin goce de sueldo<span class="checkmark"></span>
										</label>
									</div>
									<div class="form-check">
										<label class="form-check-label terminosVacaciones">
											<input type="radio" class="form-check-input" id="radio3" name="optradio" onclick="getPermiso(this.value)" value="Falta injustificada (sin goce)">Falta injustificada (sin goce)<span class="checkmark"></span>
										</label>
									</div>
								</div>
								<div class="form-group col-12 col-sm-12 col-md-12 col-lg-6 col-xl-6">
									<label for="fechaInicio" >
										Fecha de inicio*
									</label>
									<div class="input-group mb-3 " style="background: black;">
										<!-- <input type="text" class="form-control form-control-sm calendar" style="background: url('<%=request.getContextPath()+"/img/calendar-cuervo.svg"%>') no-repeat scroll 5px 4px;background-size: 17px;background-position: 96%; " placeholder="Fecha de inicio" id="fechaInicio" autocomplete="off"> -->
										<input id="fechaInicio" type="text" class="form-control form-control-sm" style="background: url('<%=request.getContextPath()+"/img/calendar-cuervo.svg"%>') no-repeat scroll 5px 4px;background-size: 17px;background-position: 96%; " name="<portlet:namespace />fechaSolicitud" placeholder="Fecha de inicio" autocomplete="off" readonly="readonly"/>
									</div>
								</div>
								<div class="form-group col-12 col-sm-12 col-md-12 col-lg-6 col-xl-6">
									<label for="fechaRegreso" >
										Regresa a laborar*
									</label>
									<div class="input-group mb-3 " style="background: black;">
										<!-- <input type="text" class="form-control form-control-sm" style="background: url('<%=request.getContextPath()+"/img/calendar-cuervo.svg"%>') no-repeat scroll 5px 4px;background-size: 17px; background-position: 96%;" placeholder="Regresa a laborar" id="fechaRegreso" autocomplete="off"> -->
										<input id="fechaRegreso" type="text" class="form-control form-control-sm" style="background: url('<%=request.getContextPath()+"/img/calendar-cuervo.svg"%>') no-repeat scroll 5px 4px;background-size: 17px;background-position: 96%; " name="<portlet:namespace />fechaRegreso" placeholder="Regresa a laborar" autocomplete="off" readonly="readonly" />
									</div>
								</div>
								<div class="form-group col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12">
									<label for="fechaInicio" >
										Días solicitados*
									</label>
									<input class="form-control form-control-sm mb-3" placeholder="Días solicitados" id="diasSolicitados" min="0" max="50">
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
									<select id="JefeInmediato" class="form-control form-control-sm" name="Jefe Inmediato"></select>
									<small id="emailHelp" class="form-text text-muted">Nombre y firma.</small>
								</div>
								<div class="form-group col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 select">
									<label for="exampleFormControlSelect2">
										Recursos Humanos*
									</label>
									<!-- <input type="text" class="form-control form-control-sm"  id="Gerente_Director" list="informacion2"> -->
									<select id="Gerente_Director" class="form-control form-control-sm" name="Gerente Director"></select>
									<small id="emailHelp" class="form-text text-muted">Nombre y firma.</small>
								</div>
								<div class="form-group col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 select">
									<label for="exampleFormControlSelect2">
										Gerente o Director de área*
									</label>
									<!-- <input type="text" class="form-control form-control-sm"  id="Gerente_Director" list="informacion2"> -->
									<select id="RecursosHumanos" class="form-control form-control-sm" name="Recursos Humanos"></select>
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
			</article>
		</section>
		<script src="/o/Permisos-portlet/js/select2.min.js"></script>
		<script src="/o/Permisos-portlet/js/i18n/es.js"></script>
		<script src="/o/Permisos-portlet/js/jquery-ui.js"></script>
		<!-- <script src="/o/Permisos-portlet/js/gijgo.min.js"></script>
		<script src="/o/Permisos-portlet/js/core.js"></script>
		<script src="/o/Permisos-portlet/js/datepicker.js"></script> -->
		<script>
		
			var myPermiso = "";
			function getPermiso(tipoPermiso) {
				myPermiso = tipoPermiso;
			}
			
			var changeCloseButton = function(input) {
				setTimeout(function() {
			        var headerPanel = $( input ).datepicker( "widget" ).find( ".ui-widget-header" );
			        var closeBtn = $('<button style=\"position:relative; left: 90%;border: none;background: none; color: #CDB874;outline: none;padding: 10px;\">X</button>');
			        
			        closeBtn.bind("click", function() {
			        	$( "#fechaInicio" ).datepicker( "hide" );
			        	$( "#fechaRegreso" ).datepicker( "hide" );
			        });
			        
			        closeBtn.prependTo(headerPanel);
			    }, 1 );
			};
			
			function select2Init(){
				var _ojbUser = '<%=strObjJSON%>';
				
				var _UsersJSON = JSON.parse(_ojbUser);
				var tipValor = getPermiso();
				
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
				
				/*$('#fechaInicio').datepicker({
				    uiLibrary: 'bootstrap4',
				    locale: 'es-es',
				    format: 'dd/mm/yyyy'
				});
				

				$('#fechaRegreso').datepicker({
				    uiLibrary: 'bootstrap4',
				    locale: 'es-es',
				    format: 'dd/mm/yyyy'
				});*/
			}
			
			
			 
			 $(document).ready(function(){
				 var _usuario = '<%=usuario%>';
				 
				 select2Init();					
				 
				 $.fn.inputFilter = function(inputFilter) {
					 return this.on("input keydown keyup mousedown mouseup select contextmenu drop", function() {
						 if (inputFilter(this.value)) {
					        this.oldValue = this.value;
					        this.oldSelectionStart = this.selectionStart;
					        this.oldSelectionEnd = this.selectionEnd;
					      } else if (this.hasOwnProperty("oldValue")) {
					        this.value = this.oldValue;
					        this.setSelectionRange(this.oldSelectionStart, this.oldSelectionEnd);
					      } else {
					        this.value = "";
					      }
					 });
				};
				
				$("#diasSolicitados").inputFilter(function(value) {return /^-?\d*$/.test(value); });
				
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
				
				$.extend($.datepicker, {
					
				    // Reference the orignal function so we can override it and call it later
				    _inlineDatepicker2: $.datepicker._inlineDatepicker,
				
				    // Override the _inlineDatepicker method
				    _inlineDatepicker: function (target, inst) {
				
				        // Call the original
				        this._inlineDatepicker2(target, inst);
				
				        var beforeShow = $.datepicker._get(inst, 'beforeShow');
				
				        if (beforeShow) {
				            beforeShow.apply(target, [target, inst]);
				        }
				    }
				});
				
				
				$( "#fechaInicio" ).datepicker({
					changeMonth: true,
				    changeYear: true,
				    hideIfNoPrevNext: true,
				    dateFormat: "yy-mm-dd",
				    maxDate: '+2y',
				    minDate: '-2y',
					beforeShow: changeCloseButton,			
					onChangeMonthYear: changeCloseButton,
					onSelect: function(date){
						var selectedDate = new Date(date);
						var msecsInADay = 86400000;
						var endDate = new Date(selectedDate.getTime() + msecsInADay);
						
						$("#fechaRegreso").datepicker( "option", "minDate", endDate );
						$("#fechaRegreso").datepicker( "option", "maxDate", '+2y' );
					}
				}).focus(function () {
				    $(".ui-datepicker-next").hide();
				    $(".ui-datepicker-prev").hide();
				});
			
				$( "#fechaRegreso" ).datepicker({
					changeMonth: true,
				    changeYear: true,
				    hideIfNoPrevNext: true,
				    //minDate: 0,
				    dateFormat: "yy-mm-dd",
				    beforeShow: changeCloseButton,
					onChangeMonthYear: changeCloseButton
				}).focus(function () {
				    $(".ui-datepicker-next").hide();
				    $(".ui-datepicker-prev").hide();
				});	
				
				$.datepicker.setDefaults($.datepicker.regional['es']);
							
				$("#Send").on('click', function(){
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
					
					if(_fechaInicio.trim() === "" || _fechaRegreso.trim() === "" || typeof _tipPermiso === 'undefined' || _diasSolicitados.trim() === "" || _JefeInmediatoId.trim() === "" || _Gerente_DirectorId.trim() === "" || _RecursosHumanosId.trim() === ""){
						
						error.innerHTML = "*Todos los campos son requeridos";
						return "";
					}
					
					//console.log(_fechaInicio, ' ', _fechaRegreso, ' ', _diasSolicitados, ' ',  _JefeInmediato, ' ', _Gerente_Director, ' ', _RecursosHumanos, ' ', _tipPermiso, ' ', _comentarios  );
					
					var _SolicitudJSON = "{\"Inicio\":\""+_fechaInicio+"\",\"Diasatomar\": \""+_diasSolicitados+"\",\"Gerente\":\""
						+_Gerente_DirectorId+"\",\"TipoPermiso\":\""+_tipPermiso+"\",\"Jefe\":\""+_JefeInmediatoId+"\",\"Comentarios\":\""+_comentarios+"\",\"Final\":\""
						+_fechaRegreso+"\", \"Rhvobo\":\""+_RecursosHumanosId+"\"}";
						
					//console.log(_SolicitudJSON);	
					var _objSolicitudJSON = JSON.parse(_SolicitudJSON);
					
					var pathname = window.location.pathname; // Returns path only (/path/example.html)
					var url      = window.location.href;     // Returns full URL (https://example.com/path/example.html)
					var origin   = window.location.origin;   // Returns base URL (https://example.com)
					
					//console.log('pathname ' , pathname , ' url ', url , ' origin ', origin);
					
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
					    	<portlet:namespace/>Rhvobo : _RecursosHumanosId,
					    	<portlet:namespace/>portalURL : '<%=portalURL%>'
					    },
					    xhrFields: {
				            responseType: 'blob'
				        },
					    success: function(data){
					    	// Internet Explorer 6-11
					    	var isIE = /*@cc_on!@*/false || !!document.documentMode;
					    	
					        if(isIE){
					        	// To create a mouse event , first we need to create an event and then initialize it.
					        	var binaryData = [];
						    	binaryData.push(data);
						    	var url = window.URL.createObjectURL(new Blob(binaryData, {type: "application/zip"}))
						    	window.navigator.msSaveBlob(new Blob(binaryData, {type: "application/zip"}), "Solicitud_Permiso-" + new Date().getTime() + ".pdf");
					        }
					        else{
					        	var binaryData = [];
						    	binaryData.push(data);
						    	var url = window.URL.createObjectURL(new Blob(binaryData, {type: "application/zip"}))
						    	var a = document.createElement('a');
					            //var url = window.URL.createObjectURL(data);
					            a.href = url;
					            a.download = "Solicitud_Permiso-" + new Date().getTime() + '.pdf';
					            document.body.append(a);
					            a.click();
					            a.remove();
					            window.URL.revokeObjectURL(url); 
					        }
					    },
					    error : function(XMLHttpRequest, textStatus, errorThrown){
					    	console.log('XMLHttpRequest', XMLHttpRequest);
					        console.log("errorThrown ", errorThrown);
					        console.log("textStatus ", textStatus);
					    }
					});			
						
				});
			});
		</script>