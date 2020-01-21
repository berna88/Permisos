package com.consistent.cuervo.permisos.portlet;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.ImageIO;

import com.consistent.cuervo.permisos.email.SendMail;
import com.itextpdf.io.IOException;
import com.itextpdf.io.codec.Base64;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import com.liferay.expando.kernel.model.ExpandoValue;
import com.liferay.expando.kernel.service.ExpandoValueLocalServiceUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;

public class PermisosPDF {
    /* Constants form itext5 */
    public static final PdfNumber INVERTEDPORTRAIT = new PdfNumber(180);
    public static final PdfNumber LANDSCAPE = new PdfNumber(90);
    public static final PdfNumber PORTRAIT = new PdfNumber(0);
    public static final PdfNumber SEASCAPE = new PdfNumber(270);
	
	private static final String PATH = "META-INF/resources/fonts";
	private static final String BOLD = PATH + "/SourceSansPro-Bold.ttf";
	private static final String LIGHT = PATH + "/SourceSansPro-Light.ttf";
	private static final String SEMIBOLD = PATH + "/SourceSansPro-SemiBold.ttf";
	private static final String LIGHT_ITALIC = PATH + "/SourceSansPro-LightItalic.ttf";
	public static final String TAHOMA_BOLD = PATH + "/tahomabd.ttf";
	
	private static Log log = LogFactoryUtil.getLog(PermisosPDF.class.getName());
	
	public static File generarPDF(String strInicio, String strRegreso, String strDiasTomar, String strGerente, String strTipoPermiso, String strJefe, String strComentarios, String strRhVoBo, User objUser, ThemeDisplay td, String strPortalURL) {
		
		String strNombre = "";
		String strApellidoMaterno = "";
		String strApellidoPaterno = "";
		String strNo_Empleado = "";
		String strDepartamento = "";
		String strPuesto = "";
		String strLocalidad = "";		
		
		if(objUser.getExpandoBridge().hasAttribute("Nombres"))
			strNombre = (String) objUser.getExpandoBridge().getAttribute("Nombres");
		if(objUser.getExpandoBridge().hasAttribute("Apellido_Materno"))
			strApellidoMaterno = (String) objUser.getExpandoBridge().getAttribute("Apellido_Materno");
		if(objUser.getExpandoBridge().hasAttribute("Apellido_Paterno"))
			strApellidoPaterno = (String) objUser.getExpandoBridge().getAttribute("Apellido_Paterno");		
		if(objUser.getExpandoBridge().hasAttribute("No_Empleado"))
			strNo_Empleado = (String) objUser.getExpandoBridge().getAttribute("No_Empleado");
		if(objUser.getExpandoBridge().hasAttribute("Desc_Depto"))
			strDepartamento = (String) objUser.getExpandoBridge().getAttribute("Desc_Depto");
		if(objUser.getExpandoBridge().hasAttribute("Desc_Puesto_Trabajo"))
			strPuesto = (String) objUser.getExpandoBridge().getAttribute("Desc_Puesto_Trabajo");
		if(objUser.getExpandoBridge().hasAttribute("Tienda_localidad"))
			strLocalidad = (String) objUser.getExpandoBridge().getAttribute("Tienda_localidad");
		
		OutputStream os = null;
		PdfDocument pdf = null;
		Document document = null;
		
		try {
			
			FontProgram fontProgramBOLD = FontProgramFactory.createFont(BOLD);
			FontProgram fontProgramSEMIBOLD = FontProgramFactory.createFont(SEMIBOLD);
			FontProgram fontProgramLIGHT = FontProgramFactory.createFont(LIGHT);
			FontProgram fontProgramLIGHT_ITALIC = FontProgramFactory.createFont(LIGHT_ITALIC);
			
			PdfFont fontLIGHT = PdfFontFactory.createFont(fontProgramLIGHT, PdfEncodings.WINANSI, true);
			PdfFont fontLIGHT_ITALIC = PdfFontFactory.createFont(fontProgramLIGHT_ITALIC, PdfEncodings.WINANSI, true);
		    PdfFont fontBOLD = PdfFontFactory.createFont(fontProgramBOLD, PdfEncodings.WINANSI, true);
		    PdfFont fontSEMIBOLD = PdfFontFactory.createFont(fontProgramSEMIBOLD, PdfEncodings.WINANSI, true);
			
		    File parent = new File(System.getProperty("java.io.tmpdir"));
		    File temp = new File(parent, "Solicitud_permiso.pdf");
		    if (temp.exists()) {
		        temp.delete();
		    }
		    
		    temp.createNewFile();
			
			os = new FileOutputStream(temp);
			pdf = new PdfDocument(new PdfWriter(os));
			document = new Document(pdf);
			document.setMargins(30, 46, 30, 46);
		    
		    //Renglon 1
		    //Columna 1
		    Text textNoEmpleado = new Text("NO. DE EMPLEADO: ").setFont(fontSEMIBOLD).setFontSize(10);
		    Text textNoEmpleadoVal = new Text(strNo_Empleado).setFont(fontLIGHT).setFontSize(10);
		    Paragraph paragraphNoEmpleado = new Paragraph().add(textNoEmpleado).add(textNoEmpleadoVal).setMultipliedLeading(0.9f);
		    //Columna 2
		    String[] strMonth = {"ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};
		    Calendar objCalendar = new java.util.GregorianCalendar(java.util.TimeZone.getTimeZone("America/Mexico_City"));
		    int intDay = objCalendar.get(Calendar.DATE);
		    int intMonth = objCalendar.get(Calendar.MONTH);
		    int intYear = objCalendar.get(Calendar.YEAR);
		    Text textFechaElaboracion = new Text("FECHA DE ELABORACIÓN: ").setFont(fontSEMIBOLD).setFontSize(10);
		    Text textFechaElaboracionVal = new Text(intDay+" - "+strMonth[intMonth]+" - "+intYear).setFont(fontLIGHT).setFontSize(10);
		    Paragraph paragraphFechaElaboracion = new Paragraph().add(textFechaElaboracion).add(textFechaElaboracionVal).setMultipliedLeading(0.9f);
		    
		    //Renglon 2
		    //Columna 1
		    Text textDepartamento = new Text("DEPARTAMENTO: ").setFont(fontSEMIBOLD).setFontSize(10);
		    Text textDepartamentoVal = new Text(strDepartamento).setFont(fontLIGHT).setFontSize(10);
		    Paragraph paragraphDepartamento = new Paragraph().add(textDepartamento).add(textDepartamentoVal).setMultipliedLeading(0.9f);
		    //Columna 2
		    Text textPuesto = new Text("PUESTO: ").setFont(fontSEMIBOLD).setFontSize(10);
		    Text textPuestoVal = new Text(strPuesto).setFont(fontLIGHT).setFontSize(10);
		    Paragraph paragraphPuesto = new Paragraph().add(textPuesto).add(textPuestoVal).setMultipliedLeading(0.9f);;
		    
		    //Renglon 3
		    //Columna 1
		    Text textLocalidad = new Text("LOCALIDAD: ").setFont(fontSEMIBOLD).setFontSize(10);
		    Text textLocalidadVal = new Text(strLocalidad).setFont(fontLIGHT).setFontSize(10);
		    Paragraph paragraphLocalidad = new Paragraph().add(textLocalidad).add(textLocalidadVal).setMultipliedLeading(0.9f);
		    
			Table tableHeader = new Table(2);
			tableHeader.setWidth(UnitValue.createPercentValue(100));
			tableHeader.setBorder(Border.NO_BORDER);
			tableHeader.addCell(
					new Cell().setBorder(Border.NO_BORDER).setPadding(0).setMargin(0).add(paragraphNoEmpleado)
					.setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.MIDDLE));
			tableHeader.addCell(
					new Cell().setBorder(Border.NO_BORDER).setPadding(0).setMargin(0).add(paragraphFechaElaboracion)
					.setTextAlignment(TextAlignment.RIGHT).setVerticalAlignment(VerticalAlignment.MIDDLE));
			
			tableHeader.addCell(
					new Cell().setBorder(Border.NO_BORDER).setPadding(0).setMargin(0).add(paragraphDepartamento)
					.setTextAlignment(TextAlignment.LEFT));
			tableHeader.addCell(
					new Cell().setBorder(Border.NO_BORDER).setPadding(0).setMargin(0).add(paragraphLocalidad)
					.setTextAlignment(TextAlignment.RIGHT));
			
			tableHeader.addCell(
					new Cell().setBorder(Border.NO_BORDER).setPadding(0).add(paragraphPuesto)
					.setTextAlignment(TextAlignment.LEFT));
			tableHeader.addCell(
					new Cell().setBorder(Border.NO_BORDER).setPadding(0).add(new Paragraph(""))
					.setTextAlignment(TextAlignment.RIGHT));
			
			tableHeader.setMarginTop(18);
			document.add(tableHeader);
			
			Text textAviso = new Text("SOLICITUD DE PERMISO").setFont(fontBOLD).setFontColor(new DeviceRgb(205, 184, 116));
			Paragraph paragraphAviso = new Paragraph(textAviso).setTextAlignment(TextAlignment.CENTER).setFontSize(11).setMargin(22);
			document.add(paragraphAviso);
			
			Text textArea = new Text(strTipoPermiso.toUpperCase()).setFont(fontSEMIBOLD).setFontColor(new DeviceRgb(205, 184, 116));
			Paragraph paragraphArea = new Paragraph(textArea).setFontSize(11).setMarginTop(-2);
			document.add(paragraphArea);
			
			String linaBase64 = "iVBORw0KGgoAAAANSUhEUgAABZYAAAAvCAYAAABwk58dAAAACXBIWXMAAAsSAAALEgHS3X78AAABx0lEQVR4nO3dMXEDQRAAwX2XgAiDRchMTM0GIGMwkxOAT34SlV7VHe5lu9kkt621BgAAAAAAjvqwKQAAAAAACmEZAAAAAIBEWAYAAAAAIBGWAQAAAABIhGUAAAAAABJhGQAAAACARFgGAAAAACARlgEAAAAASIRlAAAAAAASYRkAAAAAgERYBgAAAAAgEZYBAAAAAEiEZQAAAAAAEmEZAAAAAIBEWAYAAAAAIBGWAQAAAABIhGUAAAAAABJhGQAAAACARFgGAAAAACARlgEAAAAASC5nW9ff/X6dmevuAQAAAADgBX3ebj/vdpfTheWZ+ZqZ790UAAAAAOA1be92lzOG5f+Z+d1NAQAAAAB4im2tZdMAAAAAABzm8z4AAAAAABJhGQAAAACARFgGAAAAACARlgEAAAAASIRlAAAAAAASYRkAAAAAgERYBgAAAAAgEZYBAAAAAEiEZQAAAAAAEmEZAAAAAIBEWAYAAAAAIBGWAQAAAABIhGUAAAAAABJhGQAAAACARFgGAAAAACARlgEAAAAASIRlAAAAAAASYRkAAAAAgERYBgAAAAAgEZYBAAAAAEiEZQAAAAAAEmEZAAAAAIDjZuYBohcM3i//cAYAAAAASUVORK5CYII=";
			byte[] base64DecodeLinea = Base64.decode(linaBase64);
			ImageData imgDataLinea = ImageDataFactory.create(base64DecodeLinea);
			Image imgLinea = new Image(imgDataLinea).setWidth(540).setMarginLeft(-18).setMarginTop(-4); 
			document.add(imgLinea);
			
			String imageUser = null;
			try {
				imageUser = objUser.getPortraitURL(td);
			} catch (PortalException e) {
				imageUser = null;
			}
			
			String usuarioBase64 = null;
			if(imageUser != null) {
				URL url = new URL(strPortalURL+imageUser);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				BufferedImage image = ImageIO.read(connection.getInputStream());
				usuarioBase64 = encodeToString(image, "png");
			} else				
				usuarioBase64 = "iVBORw0KGgoAAAANSUhEUgAAADEAAAAxCAYAAABznEEcAAAACXBIWXMAAAsSAAALEgHS3X78AAAD60lEQVRogdVayXHbMBT9yeRudRB1YA0aEHPCMeogTAc6kHfmTB7UQdRBeMUpTAMYdhC5A6UCZyA/UDABLgAha/xmOJaJ9QF/wwc/PD8/03vHpxjzlyJbEVFCRBv8XRPRqVdN1TnjaYmoZrxqrc4CsGgnpMh2RJRi8jURNephvDpbla9t1iCqH9XmsIRQEAkpMjXxIsYEsBB7/FswXjVWpQl4kZAiUyt+hDioAfsiEwwpsgQLo/rcj+1mH7NJSJGpAS7iE0uWXTB2OZ27K5MkoLRHKKTXCoUCelNDVI+LSIBAM7ezyETU2Af1m/EqtSoYGCRxTwK9eaixT4xXhVUIfLTeXKFW4XhPAnTdhQS64oSThBTZHh0crML74GKGYR0tWCSgVKlhu+8OGJM9DIwFiwQqvokV8gHMbaOlxMQrxdYOh/EqiTGwFLnu58R4udgxwtgoH7UxF7m/EwVEaeFg+U6KXA3yG89fKfJGitwp03OBiR/7c+xIQBdoaSghRa4G+EVED72i7Ys45GurkR8OgyTGFMeDQOegBvAwUT4J7EYL0bdI7CL4hI1jB/r4ar3xRw2zeyUBUYoR1EUxCFNgvKrNsfROJJFI3Cy6deAMa9WRWCNOWgrVx7+JPv5Yb8LQQnw7EhvHmdgbjJfnGYprOatAnLD4HYlVrFMa46XyNT+sAqInIvrCeBlL5FpNIkq2ow9FRIr8YGRAGsbLGOLqRFQS8BM7I22jkUiRE3SmZbysrcYLECnv1Dm53YSf2KL+00uMVkY5q5hhx8oqnQHEQ0qfvk0QMPGZiH4ingoaFzt9Nkk02lwFEGg8Jt/HdoFp7xy0JnEOIQERCiWg8ShFPnh+HkGi3ULwTiAa3VoFYdgFtFprt3AhgWSYb9yzNKQ28Wi9GQHO2p0YmlFsY4a3MxDz+PpkvRlHOkSi9jnVwfPGioN8zxiJeWzoSOjw1sfUMl4mCDGmgr4hqB34zng5mwTyT69Cl76zKxCgzbYWiJUKmNvVDN3SlyyhyYOiP4aVxpQiO2G7oqXtYwHpGmWVXkXCrrAjxVn7TU5pcwEx37tcgZU8Q5KqdSWp7ox6KKlnkQAu+SdPk3szSJEdXqLfyhn9OkmArfKix6Ek7hsSSF16YMJJgq5JNEWkvhcRENhP+a9BEnQNR/SOjHYUG7hc2cFSWnpgwjKxLsAy1Mat6WinS2Dc19Vjt0PeJDRgsdJbXIEZJjT1uTmlkMt4rFQBe72YTG/yx7mrbyL4swiQ2Wvlx+cQThM40DZB28tigECQmEb5ygafNuj0zAonrn5+aWWUnxFKR/lI5SafCmGlXYem9hZG4f1/70RE/wFSE8TVhxRmFAAAAABJRU5ErkJggg==";
			byte[] base64DecodeUsuario = Base64.decode(usuarioBase64);
			ImageData imgDataUsuario = ImageDataFactory.create(base64DecodeUsuario);
			
		    PdfFormXObject xObject = new PdfFormXObject(new Rectangle(100, 100));
		    PdfCanvas xObjectCanvas = new PdfCanvas(xObject, pdf);
		    xObjectCanvas.roundRectangle(0, 0, 100, 100, 50);
		    xObjectCanvas.clip();
		    xObjectCanvas.newPath();
		    xObjectCanvas.addImage(imgDataUsuario, 100, 0, 0, 100, 0, 0);
		    com.itextpdf.layout.element.Image clipped = new com.itextpdf.layout.element.Image(xObject);
		    clipped.scale(0.5f, 0.5f).setMarginLeft(0.5f).setMarginTop(10.5f);
		    document.add(clipped);
			
			Text textNombre = new Text(strNombre).setFont(fontLIGHT).setFontSize(11);
			Paragraph paragraphNombre = new Paragraph(textNombre).setFixedPosition(100, 613, 200);
			document.add(paragraphNombre);
			
			Text textApellido = new Text(strApellidoPaterno + " " + strApellidoMaterno).setFont(fontBOLD).setFontSize(11);
			Paragraph paragraphApellido = new Paragraph(textApellido).setFixedPosition(100, 601, 200);
			document.add(paragraphApellido);
			
			
			Text textDiaAviso = new Text(strDiasTomar).setFont(fontBOLD).setFontColor(new DeviceRgb(205, 184, 116));
			Paragraph paragraphDiaAviso = new Paragraph(textDiaAviso).setTextAlignment(TextAlignment.RIGHT)
			.setMarginTop(-83.5f).setMarginRight(66.5f).setFontSize(67);
			document.add(paragraphDiaAviso);
			
			Text textDiaTAviso = new Text("DÍA (S)").setFont(fontSEMIBOLD);
			Paragraph paragraphDiaTAviso = new Paragraph(textDiaTAviso).setWidth(80).setTextAlignment(TextAlignment.RIGHT)
			.setFontSize(11).setFixedPosition(419, 613, 100);
			document.add(paragraphDiaTAviso);
			
			Text textDiaT2Aviso = new Text("A DISFRUTAR").setFont(fontSEMIBOLD);	
			Paragraph paragraphDiaT2Aviso = new Paragraph(textDiaT2Aviso).setWidth(80).setTextAlignment(TextAlignment.RIGHT)
			.setFixedPosition(449.5f, 601, 100).setFontSize(11);
			document.add(paragraphDiaT2Aviso);				
		    
			Text textCellInicioVacaciones = new Text("INICIO DE PERMISO: ").setFont(fontSEMIBOLD).setFontSize(10);
		    Paragraph paragraphCellVacaciones = new Paragraph().add(textCellInicioVacaciones);
		    
		    java.text.DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
		    java.util.Date dateStart = null;
		    try {
			      dateStart = format.parse(strInicio);
			    } catch (java.text.ParseException e) {
			      e.printStackTrace();
			    }
		    objCalendar.setTime(dateStart);
		    intDay = objCalendar.get(Calendar.DATE);
		    intMonth = objCalendar.get(Calendar.MONTH)+1;
		    intYear = objCalendar.get(Calendar.YEAR);
		    Text textCellVacacionesVal = new Text(intDay+" - "+strMonth[intMonth]+" - "+intYear).setFont(fontLIGHT).setFontSize(10);
		    Paragraph paragraphCellVacacionesVal = new Paragraph().add(textCellVacacionesVal);
		    
		    Text textCellSaldo = new Text("").setFont(fontSEMIBOLD).setFontSize(10);
		    Paragraph paragraphCellVal = new Paragraph().add(textCellSaldo);		    
		    Text textCellSaldoVal = new Text("").setFont(fontLIGHT).setFontSize(10);
		    Paragraph paragraphCellSaldoVal = new Paragraph().add(textCellSaldoVal);
		    
		    Table tableAviso = new Table(4);
		    tableAviso.setWidth(UnitValue.createPercentValue(80));
		    tableAviso.setBorder(Border.NO_BORDER);
		    tableAviso.addCell(
					new Cell().setBorder(Border.NO_BORDER).setWidth(110).setPadding(0).add(paragraphCellVacaciones)
					.setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.TOP));
		    tableAviso.addCell(
					new Cell().setBorder(Border.NO_BORDER).setPadding(0).setMargin(0).add(paragraphCellVacacionesVal)
					.setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.TOP));
		    tableAviso.addCell(
					new Cell().setBorder(Border.NO_BORDER).setPadding(0).add(paragraphCellVal)
					.setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.TOP));
		    tableAviso.addCell(
					new Cell().setBorder(Border.NO_BORDER).setPadding(0).setMargin(0).add(paragraphCellSaldoVal)
					.setTextAlignment(TextAlignment.RIGHT).setVerticalAlignment(VerticalAlignment.TOP));
		    
		    
		    Text textCellRegresaLaborar = new Text("REGRESA A LABORAR: ").setFont(fontSEMIBOLD).setFontSize(10);
		    Paragraph paragraphCellRegresaLaborar= new Paragraph().add(textCellRegresaLaborar);	
		    
		    java.util.Date dateBack = null;
		    try {
		    		dateBack = format.parse(strRegreso);
			    } catch (java.text.ParseException e) {
			      e.printStackTrace();
			    }
		    objCalendar.setTime(dateBack);
		    intDay = objCalendar.get(Calendar.DATE);
		    intMonth = objCalendar.get(Calendar.MONTH)+1;
		    intYear = objCalendar.get(Calendar.YEAR);
		    
		    Text textCellRegresaLaborarVal = new Text(intDay+" - "+strMonth[intMonth]+" - "+intYear).setFont(fontLIGHT).setFontSize(10);
		    Paragraph paragraphCellRegresaLaborarVal = new Paragraph().add(textCellRegresaLaborarVal);
		    
		    Text textCellDiasDisponibles = new Text("").setFont(fontSEMIBOLD).setFontSize(10);
		    Paragraph avisoParagraphCellDiasDisponibles = new Paragraph().add(textCellDiasDisponibles);		    
		    Text avisoSecondCellDiasDisponiblesVal = new Text("").setFont(fontLIGHT).setFontSize(10);
		    Paragraph avisoParagraphCellDiasDisponiblesVal = new Paragraph().add(avisoSecondCellDiasDisponiblesVal);
		    
		    tableAviso.addCell(
					new Cell().setBorder(Border.NO_BORDER).setPadding(0).setMargin(0).add(paragraphCellRegresaLaborar)
					.setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.TOP));
		    tableAviso.addCell(
					new Cell().setBorder(Border.NO_BORDER).setWidth(0).setPadding(0).setMargin(0).add(paragraphCellRegresaLaborarVal)
					.setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.TOP));
		    tableAviso.addCell(
					new Cell().setBorder(Border.NO_BORDER).setPadding(0).setMargin(0).add(avisoParagraphCellDiasDisponibles)
					.setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.TOP));
		    tableAviso.addCell(
					new Cell().setBorder(Border.NO_BORDER).setPadding(0).setMargin(0).add(avisoParagraphCellDiasDisponiblesVal)
					.setTextAlignment(TextAlignment.RIGHT).setVerticalAlignment(VerticalAlignment.TOP));
		    
		    tableAviso.setFontSize(11);
		    tableAviso.setFixedPosition(45, 541, 504);
			document.add(tableAviso);	
			
			String logoFirmasBase64 = "iVBORw0KGgoAAAANSUhEUgAABZYAAAGOCAYAAAAEpFbiAAAACXBIWXMAAAsSAAALEgHS3X78AAAPVUlEQVR4nO3dQY4U1x3A4UeUZSRzA1hkD+k6AOQE5gbmCNzA+AQhNzAnyPgEnjlAtfA+i/ENQMp+oo5eS4gRmF9kQdX4+yTU6ulqqev1Ynp+/fjXvZubmwEAAAAAAJ/rT1YKAAAAAIBCWAYAAAAAIBGWAQAAAABIhGUAAAAAABJhGQAAAACARFgGAAAAACARlgEAAAAASIRlAAAAAAASYRkAAAAAgERYBgAAAAAgEZYBAAAAAEiEZQAAAAAAEmEZAAAAAIBEWAYAAAAAIBGWAQAAAABIhGUAAAAAABJhGQAAAACARFgGAAAAACARlgEAAAAASIRlAAAAAAASYRkAAAAAgERYBgAAAAAgEZYBAAAAAEiEZQAAAAAAEmEZAAAAAIBEWAYAAAAAIBGWAQAAAABI/rzX5Tqu6/0xxtMxxuNbDwIAAAAAbMvlGOPNYVne3oX35d7Nzc2tH27ZcV1PMfnlGOPJrl44AAAAAMAYP40xXh2W5XLPa7GbsHxc14djjB8/CMq/zNJ/Jyo/wO/k+RjjwRjj9Rjj2qICfLbv54E/WDKAz3L6O/27Mcav8+91AD7uPH3h0XtHnALz873uYN5FWD6u67P5S+qbMca7U9E/3T8si2AC8IHjul7OL+H+vvdvPwG+pOO6/u+D8WFZ7ll4gN82/0fxz2OMq8OyPLVkAL9tbp59Ob+YG7N1Pj0sy5u9Ld/mL953XNfTzrt/zah8dZqpfFiWl6IyAAAAALAnp6Z5WJZT7/zbnMZwap6Xx3Xd3XXkNh2W54K+mndfn74BFZQBAAAAgD2bO5SffhCX7+/plLa+Y/k8/uJqlnwAAAAAgN2bs5Xfj8sXezqnzYblOQLj0Zwz8uzWAQAAAAAAOzbj8nlD7ZM5v34Xtrxj+eX5dq9XRgQAAAAA+JQ5FuP1POTFJw7dlE2G5Tlb+cHcrfzjrQMAAAAAAO6O8ybbb/cya3mrO5bPoy8u7FYGAAAAAO6yw7Jcz1nLY85d3rythuWH8/bNrUcAAAAAAO6ey3lGj/dwZsIyAAAAAMDXt6vJDVu+eB8AAAAAwB/FeZOtURgAAAAAAHwWO5YBAAAAALi7hGUAAAAAABJhGQAAAACARFgGAAAAACARlgEAAAAASIRlAAAAAAASYRkAAAAAgERYBgAAAAAgEZYBAAAAAEiEZQAAAAAAEmEZAAAAAIBEWAYAAAAAIBGWAQAAAABIhGUAAAAAABJhGQAAAACARFgGAAAAACARlgEAAAAASIRlAAAAAAASYRkAAAAAgERYBgAAAAAgEZYBAAAAAEiEZQAAAAAAEmEZAAAAAIBEWAYAAAAAIBGWAQAAAABIhGUAAAAAABJhGQAAAACARFgGAAAAACARlgEAAAAASIRlAAAAAAASYRkAAAAAgERYBgAAAAAgEZYBAAAAAEiEZQAAAAAAEmEZAAAAAIBEWAYAAAAAIBGWAQAAAABIhGUAAAAAABJhGQAAAACARFgGAAAAACARlgEAAAAASIRlAAAAAAASYRkAAAAAgERYBgAAAAAgEZYBAAAAAEiEZQAAAAAAEmEZAAAAAIBEWAYAAAAAIBGWAQAAAABIhGUAAAAAABJhGQAAAACARFgGAAAAACARlgEAAAAASIRlAAAAAAASYRkAAAAAgERYBgAAAAAgEZYBAAAAAEiEZQAAAAAAEmEZAAAAAIBEWAYAAAAAIBGWAQAAAABIhGUAAAAAABJhGQAAAACARFgGAAAAACARlgEAAAAASIRlAAAAAAASYRkAAAAAgERYBgAAAAAgEZYBAAAAAEiEZQAAAAAAEmEZAAAAAIBEWAYAAAAAIBGWAQAAAABIhGUAAAAAABJhGQAAAACARFgGAAAAACARlgEAAAAASIRlAAAAAAASYRkAAAAAgERYBgAAAAAgEZYBAAAAAEiEZQAAAAAAEmEZAAAAAIBEWAYAAAAAIBGWAQAAAABIhGUAAAAAABJhGQAAAACARFgGAAAAACARlgEAAAAASIRlAAAAAAASYRkAAAAAgERYBgAAAAAgEZYBAAAAAEiEZQAAAAAAEmEZAAAAAIBEWAYAAAAAIBGWAQAAAABIhGUAAAAAABJhGQAAAACARFgGAAAAACARlgEAAAAASIRlAAAAAAASYRkAAAAAgERYBgAAAAAgEZYBAAAAAEiEZQAAAAAAEmEZAAAAAIBEWAYAAAAAIBGWAQAAAABIhGUAAAAAABJhGQAAAACARFgGAAAAACARlgEAAAAASIRlAAAAAAASYRkAAAAAgERYBgAAAAAgEZYBAAAAAEiEZQAAAAAAEmEZAAAAAIBEWAYAAAAAIBGWAQAAAABIhGUAAAAAABJhGQAAAACARFgGAAAAACARlgEAAAAASIRlAAAAAAASYRkAAAAAgERYBgAAAAAgEZYBAAAAAEiEZQAAAAAAEmEZAAAAAIBEWAYAAAAAIBGWAQAAAABIhGUAAAAAABJhGQAAAACARFgGAAAAACARlgEAAAAASIRlAAAAAAASYRkAAAAAgERYBgAAAAAgEZYBAAAAAEiEZQAAAAAAEmEZAAAAAIBEWAYAAAAAIBGWAQAAAABIhGUAAAAAABJhGQAAAACARFgGAAAAACARlgEAAAAASIRlAAAAAAASYRkAAAAAgERYBgAAAAAgEZYBAAAAAEiEZQAAAAAAEmEZAAAAAIBEWAYAAAAAIBGWAQAAAABIhGUAAAAAABJhGQAAAACARFgGAAAAACARlgEAAAAASIRlAAAAAAASYRkAAAAAgERYBgAAAAAgEZYBAAAAAEiEZQAAAAAAEmEZAAAAAIBEWAYAAAAAIBGWAQAAAABIhGUAAAAAABJhGQAAAACARFgGAAAAACARlgEAAAAASIRlAAAAAAASYRkAAAAAgERYBgAAAAAgEZYBAAAAAEiEZQAAAAAAEmEZAAAAAIBEWAYAAAAAIBGWAQAAAABIhGUAAAAAABJhGQAAAACARFgGAAAAACARlgEAAAAASIRlAAAAAAASYRkAAAAAgERYBgAAAAAgEZYBAAAAAEiEZQAAAAAAEmEZAAAAAIBEWAYAAAAAIBGWAQAAAABIhGUAAAAAABJhGQAAAACARFgGAAAAACARlgEAAAAASIRlAAAAAAASYRkAAAAAgERYBgAAAAAgEZYBAAAAAEiEZQAAAAAAEmEZAAAAAIBEWAYAAAAAIBGWAQAAAABIhGUAAAAAABJhGQAAAACARFgGAAAAACARlgEAAAAASIRlAAAAAAASYRkAAAAAgERYBgAAAAAgEZYBAAAAAEiEZQAAAAAAEmEZAAAAAIBEWAYAAAAAIBGWAQAAAABIhGUAAAAAABJhGQAAAACARFgGAAAAACARlgEAAAAASIRlAAAAAAASYRkAAAAAgERYBgAAAAAgEZYBAAAAAEiEZQAAAAAAEmEZAAAAAIBEWAYAAAAAIBGWAQAAAABIhGUAAAAAABJhGQAAAACARFgGAAAAACARlgEAAAAASIRlAAAAAAASYRkAAAAAgERYBgAAAAAgEZYBAAAAAEiEZQAAAAAAEmEZAAAAAIBEWAYAAAAAIBGWAQAAAABIhGUAAAAAABJhGQAAAACARFgGAAAAACARlgEAAAAASIRlAAAAAAASYRkAAAAAgERYBgAAAAAgEZYBAAAAAEiEZQAAAAAAEmEZAAAAAIBEWAYAAAAAIBGWAQAAAABIhGUAAAAAABJhGQAAAACARFgGAAAAACARlgEAAAAASIRlAAAAAAASYRkAAAAAgERYBgAAAAAgEZYBAAAAAEiEZQAAAAAAEmEZAAAAAIBEWAYAAAAAIBGWAQAAAABIhGUAAAAAABJhGQAAAACARFgGAAAAACARlgEAAAAASIRlAAAAAAASYRkAAAAAgERYBgAAAAAgEZYBAAAAAEiEZQAAAAAAEmEZAAAAAIBEWAYAAAAAIBGWAQAAAABIhGUAAAAAABJhGQAAAACARFgGAAAAACARlgEAAAAASIRlAAAAAAASYRkAAAAAgERYBgAAAAAgEZYBAAAAAEiEZQAAAAAAEmEZAAAAAIBEWAYAAAAAIBGWAQAAAABIhGUAAAAAABJhGQAAAACARFgGAAAAACARlgEAAAAASIRlAAAAAAASYRkAAAAAgERYBgAAAAAgEZYBAAAAAEiEZQAAAAAAEmEZAAAAAIBEWAYAAAAAIBGWAQAAAABIhGUAAAAAABJhGQAAAACARFgGAAAAACARlgEAAAAASIRlAAAAAAASYRkAAAAAgERYBgAAAAAgEZYBAAAAAEiEZQAAAAAAEmEZAAAAAIBEWAYAAAAAIBGWAQAAAABIhGUAAAAAABJhGQAAAACARFgGAAAAACARlgEAAAAASIRlAAAAAAASYRkAAAAAgERYBgAAAAAgEZYBAAAAAL6+h/MVvN3De7H1sHz/1k8AAAAAAO6ec1h+s4cz22pYPi/e41uPAAAAAADcPQ/3dEZbD8vPbj0CAAAAAHD3PJ1ndLmHM9tqWD4v3qPjuu6q1AMAAAAAFMd1PUXlB2OMd4dlEZb/X4dluR5j/DSf/nKLrxEAAAAA4HdybqAXe1nQLV+879W8/W4WewAAAACAO+W4rs/HGE/mOe1mk+1mw/Lc8v3PeffiuK4u5AcAAAAA3BmzeZ432P4wJznswpZ3LI9Z6H8ZY3xzmrssLgMAAAAAd8Gc0nA52+fVYVl2NRJ402H5sCxv59UQ34/LL24dCAAAAACwA8d1vX9c11NE/nk2z1P7fLa3927rO5bfj8tXc6H/cVzX61NgPq7rw1tPAAAAAADYmNM0hhmUT+Muvp+v7vWpfc4Guiv3bm5udvN6527llzMwn70bY7y5dTDAH9ejMcb9+Y3n7n4xAXxF5wumXHkTAD7L/fnZ8+387AnAxz354JFfxxgvDsty8dFnbNyuwvKYW8Xn1vDTv29vHQAAAAAAsD3v5kzli8Oy/Lj392d3YflDcxyGkRgAAADwZf1ljPHXMcZ/xhj/tvYAn3R9WJbrTx2wN7sPywAAAAAAfFmbv3gfAAAAAADbIiwDAAAAAJAIywAAAAAAJMIyAAAAAACJsAwAAAAAQCIsAwAAAACQCMsAAAAAACTCMgAAAAAAibAMAAAAAEAiLAMAAAAAkAjLAAAAAAAkwjIAAAAAAImwDAAAAABAIiwDAAAAAJAIywAAAAAAJMIyAAAAAACJsAwAAAAAQCIsAwAAAACQCMsAAAAAACTCMgAAAAAAn2+M8V+zpPquEDsu4gAAAABJRU5ErkJggg==";
			byte[] base64DecodedFirmas = Base64.decode(logoFirmasBase64);
	        ImageData imgFirmas = ImageDataFactory.create(base64DecodedFirmas);
		    Image pdfImgTabla = new Image(imgFirmas).setWidth(535).setMarginLeft(-15)
		    .setMarginTop(33);
		    document.add(pdfImgTabla);
			
			Text textEmpleado = new Text(strNombre).setFont(fontSEMIBOLD);	
			Paragraph paragraphEmpleado = new Paragraph(textEmpleado).setFontSize(8).setMultipliedLeading(0.9f);			
			Text textEmpleadoApellido = new Text(strApellidoPaterno + " " + strApellidoMaterno).setFont(fontSEMIBOLD);	
			Paragraph paragraphEmpleadoApellido = new Paragraph(textEmpleadoApellido).setFontSize(8).setMultipliedLeading(0.9f);
			
			java.util.List<String> listNameJefe = getUser(strJefe);
			Text textJefe = new Text(listNameJefe.size() > 0?listNameJefe.get(0).toString():"").setFont(fontSEMIBOLD);	
			Paragraph paragraphJefe = new Paragraph(textJefe).setFontSize(8).setMultipliedLeading(0.9f);
			Text textJefeApelido = new Text(listNameJefe.size() > 1?listNameJefe.get(1).toString() + " " + listNameJefe.get(2).toString() :"").setFont(fontSEMIBOLD);	
			Paragraph paragraphJefeApellido = new Paragraph(textJefeApelido).setFontSize(8).setMultipliedLeading(0.9f);			
			
			java.util.List<String> listNameGteDir = getUser(strGerente);
			Text textGteDir = new Text(listNameGteDir.size() > 0?listNameGteDir.get(0).toString():"").setFont(fontSEMIBOLD);	
			Paragraph paragraphGteDir = new Paragraph(textGteDir).setFontSize(8).setMultipliedLeading(0.9f);
			Text textGteDirApellido = new Text(listNameGteDir.size() > 1?listNameGteDir.get(1).toString() + " " + listNameGteDir.get(2).toString() :"").setFont(fontSEMIBOLD);	
			Paragraph paragraphGteDirApellido = new Paragraph(textGteDirApellido).setFontSize(8).setMultipliedLeading(0.9f);
			
			java.util.List<String> listNameRH = getUser(strRhVoBo);
			Text textRH = new Text(listNameRH.size() > 0?listNameRH.get(0).toString():"").setFont(fontSEMIBOLD);	
			Paragraph paragraphRH = new Paragraph(textRH).setFontSize(8).setMultipliedLeading(0.9f);
			Text textRHApellido = new Text(listNameRH.size() > 1?listNameRH.get(1).toString() + " " + listNameRH.get(2).toString() :"").setFont(fontSEMIBOLD);	
			Paragraph paragraphRHApellido = new Paragraph(textRHApellido).setFontSize(8).setMultipliedLeading(0.9f);
			
			Table tableFirmasInfo = new Table(4);
			tableFirmasInfo.setWidth(UnitValue.createPercentValue(80));
			tableFirmasInfo.setBorder(Border.NO_BORDER);
			tableFirmasInfo.addCell(
						new Cell().setWidth(123).setBorder(Border.NO_BORDER).setPadding(0).setMargin(0).add(paragraphEmpleado)
						.setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.TOP));
			tableFirmasInfo.addCell(
						new Cell().setWidth(125).setBorder(Border.NO_BORDER).setPadding(0).setMargin(0).add(paragraphJefe)
						.setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.TOP));
			tableFirmasInfo.addCell(
						new Cell().setWidth(125).setBorder(Border.NO_BORDER).setPadding(0).setMargin(0).add(paragraphGteDir)
						.setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.TOP));
			tableFirmasInfo.addCell(
						new Cell().setPadding(0).setBorder(Border.NO_BORDER).setMargin(0).add(paragraphRH)
						.setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.TOP));
			
			tableFirmasInfo.addCell(
					new Cell().setWidth(123).setBorder(Border.NO_BORDER).setPadding(0).setMargin(0).add(paragraphEmpleadoApellido)
					.setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.TOP));
			tableFirmasInfo.addCell(
					new Cell().setWidth(125).setBorder(Border.NO_BORDER).setPadding(0).setMargin(0).add(paragraphJefeApellido)
					.setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.TOP));
			tableFirmasInfo.addCell(
					new Cell().setWidth(125).setBorder(Border.NO_BORDER).setPadding(0).setMargin(0).add(paragraphGteDirApellido)
					.setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.TOP));
			tableFirmasInfo.addCell(
					new Cell().setPadding(0).setBorder(Border.NO_BORDER).setMargin(0).add(paragraphRHApellido)
					.setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.TOP));
			tableFirmasInfo.setFontSize(11);
			tableFirmasInfo.setFixedPosition(47, 414, 500);
			document.add(tableFirmasInfo);
			
			Text textEmpleadoDefault = new Text("NOMBRE Y FIRMA EMPLEADO").setFont(fontLIGHT_ITALIC);	
			Paragraph paragraphEmpleadoDefault = new Paragraph(textEmpleadoDefault).setFontSize(6);
			
			Text textJefeDefault = new Text("NOMBRE Y FIRMA JEFE INMEDIATO").setFont(fontLIGHT_ITALIC);	
			Paragraph paragraphJefeDefault = new Paragraph(textJefeDefault).setFontSize(6);
			
			Text textGteDirDefault = new Text("NOMBRE Y FIRMA GTE. O DIR. DEL ÁREA").setFont(fontLIGHT_ITALIC);	
			Paragraph paragraphGteDirDefault = new Paragraph(textGteDirDefault).setFontSize(6);
			
			Text textRHDefault = new Text("NOMBRE Y FIRMA DE RECURSOS HUMANOS").setFont(fontLIGHT_ITALIC);	
			Paragraph paragraphRHDefault = new Paragraph(textRHDefault).setFontSize(6);
			
			Table tableFirmas = new Table(4);
			tableFirmas.setWidth(UnitValue.createPercentValue(80));
			tableFirmas.setBorder(Border.NO_BORDER);
			tableFirmas.addCell(
						new Cell().setBorder(Border.NO_BORDER).setWidth(123).setPadding(0).setMargin(0).add(paragraphEmpleadoDefault)
						.setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.TOP));
			tableFirmas.addCell(
						new Cell().setBorder(Border.NO_BORDER).setWidth(125).setPadding(0).setMargin(0).add(paragraphJefeDefault)
						.setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.TOP));
			tableFirmas.addCell(
						new Cell().setBorder(Border.NO_BORDER).setWidth(125).setPadding(0).setMargin(0).add(paragraphGteDirDefault)
						.setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.TOP));
			tableFirmas.addCell(
						new Cell().setBorder(Border.NO_BORDER).setPadding(0).setMargin(0).add(paragraphRHDefault)
						.setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.TOP));
			tableFirmas.setFontSize(11);
			tableFirmas.setFixedPosition(47, 402, 500);
			document.add(tableFirmas);
			
			Text textImportante = new Text("COMENTARIOS").setFont(fontSEMIBOLD).setFontSize(10);	
			Text textImportante2 = new Text("Motivo del permiso o falta.").setFont(fontLIGHT).setFontSize(10);
			Paragraph paragraphImportante = new Paragraph(textImportante).setMarginTop(20).setMultipliedLeading(0.9f);
			Paragraph paragraphImportante2 = new Paragraph(textImportante2).setMarginTop(-3).setWidth(315).setMultipliedLeading(0.9f);
			//paragraphImportante.setFixedPosition(30, 270, 300);
			document.add(paragraphImportante);
			document.add(paragraphImportante2);
			
			Text textComentarios = new Text(strComentarios).setFont(fontLIGHT).setFontSize(10);
			Paragraph paragraphComentarios = new Paragraph(textComentarios).setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.TOP);
			paragraphComentarios.setFixedPosition(47, 320, 500);
			document.add(paragraphComentarios);	
			document.close();
			
			SendMail.mail(objUser.getEmailAddress(), "intranet@cuervo.com.mx", "Permiso", temp);
			temp.deleteOnExit();
			return temp;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		} finally {
			pdf.close();
			try {
				os.flush();
				os.close();
			} catch (java.io.IOException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	public static File generarPDF_OLD(String strInicio, String strRegreso, String strDiasTomar, String strGerente, String strTipoPermiso, String strJefe, String strComentarios, String strRhVoBo, User objUser) {		
		log.info("generarPDF");
		String strNombre = "";
		String strApellidoMaterno = "";
		String strApellidoPaterno = "";
		String strNo_Empleado = "";
		String strDepartamento = "";
		String strPuesto = "";
		String strLocalidad = "";		
		
		if(objUser.getExpandoBridge().hasAttribute("Nombres"))
			strNombre = (String) objUser.getExpandoBridge().getAttribute("Nombres");
		if(objUser.getExpandoBridge().hasAttribute("Apellido_Materno"))
			strApellidoMaterno = (String) objUser.getExpandoBridge().getAttribute("Apellido_Materno");
		if(objUser.getExpandoBridge().hasAttribute("Apellido_Paterno"))
			strApellidoPaterno = (String) objUser.getExpandoBridge().getAttribute("Apellido_Paterno");		
		if(objUser.getExpandoBridge().hasAttribute("No_Empleado"))
			strNo_Empleado = (String) objUser.getExpandoBridge().getAttribute("No_Empleado");
		if(objUser.getExpandoBridge().hasAttribute("Desc_Depto"))
			strDepartamento = (String) objUser.getExpandoBridge().getAttribute("Desc_Depto");
		if(objUser.getExpandoBridge().hasAttribute("Desc_Puesto_Trabajo"))
			strPuesto = (String) objUser.getExpandoBridge().getAttribute("Desc_Puesto_Trabajo");
		if(objUser.getExpandoBridge().hasAttribute("Tienda_localidad"))
			strLocalidad = (String) objUser.getExpandoBridge().getAttribute("Tienda_localidad");		
		
		String fullName = strNombre + " " + strApellidoPaterno + " " + strApellidoMaterno;
		log.info(fullName);
		try {
			FontProgram fontBOLD = FontProgramFactory.createFont(BOLD);
			FontProgram fontLIGHT = FontProgramFactory.createFont(LIGHT);
			FontProgram fontTAHOMA_BOLD = FontProgramFactory.createFont(TAHOMA_BOLD);	
			
			File tempFile = null;	
			java.nio.file.Path tempPath = Files.createTempFile("Permiso", ".pdf");
    		tempFile = tempPath.toFile();
			OutputStream os = new FileOutputStream(tempFile);
			PdfDocument pdf = new PdfDocument(new PdfWriter(os));
			Document document = new Document(pdf, PageSize.A4);
			document.setMargins(20, 20, 20, 20);
						
			String logoCuervoBase64 = "data:image/png;base64,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";
			String[] data64 = logoCuervoBase64.split(",");
			//Logo Cuervo
			byte[] base64DecodedByteArray = Base64.decode(data64[1]);
	        ImageData imgLogoCuervo = ImageDataFactory.create(base64DecodedByteArray);
		    Image pdfImgLogoCuervo = new Image(imgLogoCuervo);
		    pdfImgLogoCuervo.setWidth(80);
		    pdfImgLogoCuervo.setMarginTop(10);
		    document.add(pdfImgLogoCuervo);	    
					    
		    PdfFont textRegular = PdfFontFactory.createFont(fontLIGHT);
		    PdfFont textBold = PdfFontFactory.createFont(fontBOLD);
		    Text firstTextCellOneL = new Text("Nombre empleado: ").setFont(textBold);
		    Text secondTextCellOneL = new Text(fullName).setFont(textRegular);
		    Paragraph paragraphCellOneL = new Paragraph().add(firstTextCellOneL).add(secondTextCellOneL);
		    
		    Text firstTextCellTwoL = new Text("No. de empleado: ").setFont(textBold);
		    Text secondTextCellTwoL = new Text(strNo_Empleado).setFont(textRegular);
		    Paragraph paragraphCellTwoL = new Paragraph().add(firstTextCellTwoL).add(secondTextCellTwoL);
		    
		    Text firstTextCellThreeL = new Text("Departamento: ").setFont(textBold);
		    Text secondTextCellThreeL = new Text(strDepartamento).setFont(textRegular);
		    Paragraph paragraphCellThreeL = new Paragraph().add(firstTextCellThreeL).add(secondTextCellThreeL);
		    
		    /*Text firstTextCellFourthL = new Text("Puesto: ").setFont(textBold);
		    Text secondTextCellFourthL = new Text("Cordinadora Gral. RH").setFont(textRegular);
		    Paragraph paragraphCellFourthL = new Paragraph().add(firstTextCellFourthL).add(secondTextCellFourthL);*/
		    
		    Date today = new Date();
		    SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yy");
		    String date = DATE_FORMAT.format(today);
		    Text firstTextCellOneR = new Text("Fecha de elaboración (mm/dd/aa): ").setFont(textBold);
		    Text secondTextCellOneR = new Text(date).setFont(textRegular);
		    Paragraph paragraphCellOneR = new Paragraph().add(firstTextCellOneR).add(secondTextCellOneR);
		    
		    Text firstTextCellTwoR = new Text("Puesto: ").setFont(textBold);
		    Text secondTextCellTwoR = new Text(strPuesto).setFont(textRegular);
		    Paragraph paragraphCellTwoR = new Paragraph().add(firstTextCellTwoR).add(secondTextCellTwoR);
		    
		    Text firstTextCellThreeR = new Text("Localidad: ").setFont(textBold);
		    Text secondTextCellThreeR = new Text(strLocalidad).setFont(textRegular);
		    Paragraph paragraphCellThreeR = new Paragraph().add(firstTextCellThreeR).add(secondTextCellThreeR);
		    
			Table tableHeader = new Table(2);
			tableHeader.setWidth(UnitValue.createPercentValue(100));
			tableHeader.setBorder(Border.NO_BORDER);
			tableHeader.addCell(
					new Cell().setHeight(17).setBorder(Border.NO_BORDER).setPadding(0).setMargin(0).add(paragraphCellOneL)
					.setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.TOP));
			tableHeader.addCell(
					new Cell().setHeight(17).setBorder(Border.NO_BORDER).setPadding(0).setMargin(0).add(paragraphCellOneR)
					.setTextAlignment(TextAlignment.RIGHT).setVerticalAlignment(VerticalAlignment.TOP));
			
			tableHeader.addCell(
					new Cell().setBorder(Border.NO_BORDER).setHeight(17).setPadding(0).setMargin(0).add(paragraphCellTwoL)
					.setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.TOP));
			tableHeader.addCell(
					new Cell().setBorder(Border.NO_BORDER).setHeight(17).setPadding(0).setMargin(0).add(paragraphCellTwoR)
					.setTextAlignment(TextAlignment.RIGHT).setVerticalAlignment(VerticalAlignment.TOP));
			
			tableHeader.addCell(
					new Cell().setBorder(Border.NO_BORDER).setHeight(17).setPadding(0).setMargin(0).add(paragraphCellThreeL)
					.setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.TOP));
			tableHeader.addCell(
					new Cell().setBorder(Border.NO_BORDER).setHeight(17).setPadding(0).setMargin(0).add(paragraphCellThreeR)
					.setTextAlignment(TextAlignment.RIGHT).setVerticalAlignment(VerticalAlignment.TOP));
			
			/*tableHeader.addCell(
					new Cell().setBorder(Border.NO_BORDER).setPadding(0).add(paragraphCellFourthL)
					.setTextAlignment(TextAlignment.LEFT));
			tableHeader.addCell(
					new Cell().setBorder(Border.NO_BORDER).setPadding(0).add(new Paragraph(""))
					.setTextAlignment(TextAlignment.RIGHT));*/
			tableHeader.setMarginTop(20);
			tableHeader.setFontSize(11);
			//tableHeader.setFixedPosition(20, bottom, width)
			document.add(tableHeader);
			log.info("Se agrega cabecera informacion de usuario");
			
			Text textAviso = new Text("PERMISO").setFont(textBold);
			textAviso.setFontColor(new DeviceRgb(205, 184, 116));
			Paragraph paragraphAviso = new Paragraph(textAviso);
			paragraphAviso.setTextAlignment(TextAlignment.CENTER);
			paragraphAviso.setFontSize(14);
			document.add(paragraphAviso);
			
			Text textRHHeader = new Text("Recursos Humanos").setFont(textBold);
			textRHHeader.setFontColor(new DeviceRgb(205, 184, 116));
			Paragraph paragraphRHHeader = new Paragraph(textRHHeader);
			paragraphRHHeader.setTextAlignment(TextAlignment.RIGHT);
			paragraphRHHeader.setFontSize(10);
			document.add(paragraphRHHeader);
			
			base64DecodedByteArray = Base64.decode("iVBORw0KGgoAAAANSUhEUgAABAAAAAH+CAYAAAACvXxNAAAACXBIWXMAAAsSAAALEgHS3X78AAASAUlEQVR4nO3dwW0cWX7A4TfGBEBHsHIEolkJ6MajGQKdAQ8MQAEQsDKwNgMezRsTKIKKYDURmI5ARu08YrTTh91VF6WRft8H1KW6MZhWTQ/0//WrVz99+vRpAAAAAD+2f3F9AQAA4McnAAAAAECAAAAAAAABAgAAAAAECAAAAAAQIAAAAABAgAAAAAAAAQIAAAAABAgAAAAAECAAAAAAQIAAAAAAAAECAAAAAAQIAAAAABAgAAAAAECAAAAAAAABP3/Lj/hwd306xjg5eAEAAAB+PE9n5zeP3+pT/fTp06eDky9lDvyXY4w3Y4zXY4xfxhgfv/aHBgAAgG/g1RjjT2OMD2OM+zHG+68ZBL5KAHi4u74YY1yNMbYAcDuP+7Pzm6eDNwMAAMAP6uHu+mT+KH4xjy0AvDs7v7l96U/8ogHg4e56qxvvZ+V4e3Z+8/7gTQAAABD1cHe9rZJ/O1fHX56d37zYKvkX2wRwfojHuazh1PAPAAAAf2vOyqdzdn6cs/SLeJEVAA931+/mvf4XZ+c39wdvAAAAAH4/S7+Zt8xvewNc7f2ns3sA+Gz4f/MtdzcEAACA783cPP/+JSLArrcAzKUKhn8AAAD4AnOW3lYCXO59O8BuKwA+qxSW/QMAAMARPrsdYLcf2PdcAfBuPrrA8A8AAABHmLP1u3nsYpcAMJclvDo7v3l78CIAAADwT5sz9qu9bgXYawXA23kAAAAA+9lt3j46AMx7/4fn/AMAAMC+nmft59n7GHusALicGxMAAAAA+7uds/dR9ggAFwIAAAAAvJjbOXsf5ajHAD7cXZ+MMf737Pzmp4MXAQAAgF083F1vw/u/np3fPH3pP+/YFQDbPQgfDs4CAAAAe/owZ/AvtsctAF9cHwAAAIB/yNGz97EB4JUAAAAAAC/uac7gX2yPAPB4cBYAAADY0+O3DgAAAADAd0AAAAAAgAABAAAAAAIEAAAAAAgQAAAAACBAAAAAAIAAAQAAAAACBAAAAAAIEAAAAAAgQAAAAACAAAEAAAAAAgQAAAAACBAAAAAAIEAAAAAAgAABAAAAAAIEAAAAAAgQAAAAACBAAAAAAIAAAQAAAAACBAAAAAAIEAAAAAAgQAAAAACAAAEAAAAAAgQAAAAACBAAAAAAIEAAAAAAgAABAAAAAAIEAAAAAAgQAAAAACBAAAAAAIAAAQAAAAACBAAAAAAIEAAAAAAgQAAAAACAAAEAAAAAAgQAAAAACBAAAAAAIEAAAAAAgAABAAAAAAIEAAAAAAgQAAAAACBAAAAAAIAAAQAAAAACBAAAAAAIEAAAAAAgQAAAAACAAAEAAAAAAgQAAAAACBAAAAAAIEAAAAAAgAABAAAAAAIEAAAAAAgQAAAAACBAAAAAAIAAAQAAAAACBAAAAAAIEAAAAAAgQAAAAACAAAEAAAAAAgQAAAAACBAAAAAAIEAAAAAAgAABAAAAAAIEAAAAAAgQAAAAACBAAAAAAIAAAQAAAAACBAAAAAAIEAAAAAAgQAAAAACAAAEAAAAAAgQAAAAACBAAAAAAIEAAAAAAgAABAAAAAAIEAAAAAAgQAAAAACBAAAAAAIAAAQAAAAACBAAAAAAIEAAAAAAgQAAAAACAAAEAAAAAAgQAAAAACBAAAAAAIEAAAAAAgAABAAAAAAIEAAAAAAgQAAAAACBAAAAAAIAAAQAAAAACBAAAAAAIEAAAAAAgQAAAAACAAAEAAAAAAgQAAAAACBAAAAAAIEAAAAAAgAABAAAAAAIEAAAAAAgQAAAAACBAAAAAAIAAAQAAAAACBAAAAAAIEAAAAAAgQAAAAACAAAEAAAAAAgQAAAAACBAAAAAAIEAAAAAAgAABAAAAAAIEAAAAAAgQAAAAACBAAAAAAIAAAQAAAAACBAAAAAAIEAAAAAAgQAAAAACAAAEAAAAAAgQAAAAACBAAAAAAIEAAAAAAgAABAAAAAAIEAAAAAAgQAAAAACBAAAAAAIAAAQAAAAACBAAAAAAIEAAAAAAgQAAAAACAAAEAAAAAAgQAAAAACBAAAAAAIEAAAAAAgAABAAAAAAIEAAAAAAgQAAAAACBAAAAAAIAAAQAAAAACBAAAAAAIEAAAAAAgQAAAAACAAAEAAAAAAgQAAAAACBAAAAAAIEAAAAAAgAABAAAAAAIEAAAAAAgQAAAAACBAAAAAAIAAAQAAAAACBAAAAAAIEAAAAAAgQAAAAACAAAEAAAAAAgQAAAAACBAAAAAAIEAAAAAAgAABAAAAAAIEAAAAAAgQAAAAACBAAAAAAIAAAQAAAAACBAAAAAAIEAAAAAAgQAAAAACA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");
			ImageData imageTabla = ImageDataFactory.create(base64DecodedByteArray);
		    Image pdfImgTabla = new Image(imageTabla);
		    pdfImgTabla.setWidth(570);
		    pdfImgTabla.setFixedPosition(12, 365);
		    document.add(pdfImgTabla);
		    
		    PdfFont textTAHOMA_BOLD = PdfFontFactory.createFont(fontTAHOMA_BOLD);
		    Text textDiaAviso = new Text(strDiasTomar).setFont(textTAHOMA_BOLD);
		    textDiaAviso.setFontColor(new DeviceRgb(205, 184, 116));
			Paragraph paragraphDiaAviso = new Paragraph(textDiaAviso);
			paragraphDiaAviso.setFixedPosition(40, 560, 130);
			paragraphDiaAviso.setFontSize(52);
			document.add(paragraphDiaAviso);
			
			Text textDiaTAviso = new Text("Día(s)").setFont(textRegular);	
			Paragraph paragraphDiaTAviso = new Paragraph(textDiaTAviso);
			paragraphDiaTAviso.setFixedPosition(110, 600, 100);
			//paragraphDiaTAviso.setBorder(new SolidBorder(Border.SOLID));
			paragraphDiaTAviso.setFontSize(14);
			document.add(paragraphDiaTAviso);
			
			Text textDiaT2Aviso = new Text("solicitados").setFont(textRegular);	
			Paragraph paragraphDiaT2Aviso = new Paragraph(textDiaT2Aviso);
			paragraphDiaT2Aviso.setFixedPosition(110, 580, 100);
			//paragraphDiaT2Aviso.setBorder(new SolidBorder(Border.SOLID));
			paragraphDiaT2Aviso.setFontSize(14);
			document.add(paragraphDiaT2Aviso);
		    
		    Text avisoFirstTextCellOneL = new Text(strTipoPermiso).setFont(textBold);
		    Paragraph avisoParagraphCellOneL = new Paragraph().add(avisoFirstTextCellOneL).setMultipliedLeading(1.2f);		    
		    Text avisoSecondTextCellTwoL = new Text("").setFont(textRegular);
		    Paragraph avisoParagraphCellTwoL = new Paragraph().add(avisoSecondTextCellTwoL).setMultipliedLeading(1.2f);
		    Text avisoFirstTextCellThreeL = new Text("Inicio de vacaciones: ").setFont(textBold);
		    Paragraph avisoParagraphCellThreeL = new Paragraph().add(avisoFirstTextCellThreeL).setMultipliedLeading(1.2f);		    
		    Text avisoSecondTextCellFourthL = new Text(strInicio).setFont(textRegular);
		    Paragraph avisoParagraphCellFourthL = new Paragraph().add(avisoSecondTextCellFourthL).setMultipliedLeading(1.2f);
		    
		    Table tableAviso = new Table(4);
		    tableAviso.setWidth(UnitValue.createPercentValue(80));
		    tableAviso.setBorder(Border.NO_BORDER);
		    tableAviso.addCell(
					new Cell().setBorder(Border.NO_BORDER).setPadding(0).setMargin(0).add(avisoParagraphCellOneL)
					.setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.TOP));
		    tableAviso.addCell(
					new Cell().setBorder(Border.NO_BORDER).setWidth(100).setPadding(0).setMargin(0).add(avisoParagraphCellTwoL)
					.setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.TOP));
		    tableAviso.addCell(
					new Cell().setBorder(Border.NO_BORDER).setPadding(0).setMargin(0).add(avisoParagraphCellThreeL)
					.setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.TOP));
		    tableAviso.addCell(
					new Cell().setBorder(Border.NO_BORDER).setPadding(0).setMargin(0).add(avisoParagraphCellFourthL)
					.setTextAlignment(TextAlignment.RIGHT).setVerticalAlignment(VerticalAlignment.TOP));
		    
		    Text avisoFirstCellRowTwo = new Text("").setFont(textBold);
		    Paragraph avisoParagraphCellOneRowTwo = new Paragraph().add(avisoFirstCellRowTwo);		    
		    Text avisoSecondCellRowTwo = new Text("").setFont(textRegular);
		    Paragraph avisoParagraphCellTwoRowTwo = new Paragraph().add(avisoSecondCellRowTwo);
		    Text avisoThirdTextCellRowTwo = new Text("Regresa a laborar: ").setFont(textBold);
		    Paragraph avisoParagraphCellThreeRowTwo = new Paragraph().add(avisoThirdTextCellRowTwo);		    
		    Text avisoFourthTextCellFourth = new Text(strRegreso).setFont(textRegular);
		    Paragraph avisoParagraphCellFourthRowTwo = new Paragraph().add(avisoFourthTextCellFourth);
		    
		    tableAviso.addCell(
					new Cell().setBorder(Border.NO_BORDER).setPadding(0).setMargin(0).add(avisoParagraphCellOneRowTwo)
					.setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.TOP));
		    tableAviso.addCell(
					new Cell().setBorder(Border.NO_BORDER).setWidth(100).setPadding(0).setMargin(0).add(avisoParagraphCellTwoRowTwo)
					.setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.TOP));
		    tableAviso.addCell(
					new Cell().setBorder(Border.NO_BORDER).setPadding(0).setMargin(0).add(avisoParagraphCellThreeRowTwo)
					.setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.TOP));
		    tableAviso.addCell(
					new Cell().setBorder(Border.NO_BORDER).setPadding(0).setMargin(0).add(avisoParagraphCellFourthRowTwo)
					.setTextAlignment(TextAlignment.RIGHT).setVerticalAlignment(VerticalAlignment.TOP));
		    
		    /*tableAviso.addCell(new Cell(1,4).setPadding(0).setMargin(0).add(new Paragraph("COMENTARIOS (Anotar el motivo del permiso ó falta)")
		    		.setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.TOP).setBold()));*/
		    
		    tableAviso.setFontSize(11);
		    tableAviso.setFixedPosition(45, 520, 500);
			document.add(tableAviso);			
			
			Text textEmpleado = new Text(fullName).setFont(textBold);	
			Paragraph paragraphEmpleado = new Paragraph(textEmpleado);
			//paragraphEmpleado.setFixedPosition(50, 410, 120);
			//paragraphDiaT2Aviso.setBorder(new SolidBorder(Border.SOLID));
			paragraphEmpleado.setFontSize(8);
			//document.add(paragraphEmpleado);
			
			//Text textJefe = new Text(getUser(strJefe)).setFont(textBold);	
			Paragraph paragraphJefe = new Paragraph("");
			paragraphJefe.setFontSize(8);
			
			//Text textGteDir = new Text(getUser(strGerente)).setFont(textBold);	
			Paragraph paragraphGteDir = new Paragraph("");
			paragraphGteDir.setFontSize(8);
			
			//Text textRH = new Text(getUser(strRhVoBo)).setFont(textBold);	
			Paragraph paragraphRH = new Paragraph("");
			paragraphRH.setFontSize(8);
			
			Table tableFirmasInfo = new Table(4);
			tableFirmasInfo.setWidth(UnitValue.createPercentValue(80));
			tableFirmasInfo.setBorder(Border.NO_BORDER);
			tableFirmasInfo.addCell(
						new Cell().setWidth(123).setBorder(Border.NO_BORDER).setPadding(0).setMargin(0).add(paragraphEmpleado)
						.setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.TOP));
			tableFirmasInfo.addCell(
						new Cell().setWidth(125).setBorder(Border.NO_BORDER).setPadding(0).setMargin(0).add(paragraphJefe)
						.setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.TOP));
			tableFirmasInfo.addCell(
						new Cell().setWidth(125).setBorder(Border.NO_BORDER).setPadding(0).setMargin(0).add(paragraphGteDir)
						.setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.TOP));
			tableFirmasInfo.addCell(
						new Cell().setPadding(0).setBorder(Border.NO_BORDER).setMargin(0).add(paragraphRH)
						.setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.TOP));
			tableFirmasInfo.setFontSize(11);
			tableFirmasInfo.setFixedPosition(47, 410, 500);
			document.add(tableFirmasInfo);
			
			Text textEmpleadoDefault = new Text("Nombre y firma Empleado").setFont(textRegular);	
			Paragraph paragraphEmpleadoDefault = new Paragraph(textEmpleadoDefault);
			//paragraphEmpleadoDefault.setFixedPosition(55, 400, 120);
			//paragraphDiaT2Aviso.setBorder(new SolidBorder(Border.SOLID));
			paragraphEmpleadoDefault.setFontSize(9);
			//document.add(paragraphEmpleadoDefault);
			
			Text textJefeDefault = new Text("Nombre y firma Jefe Inmediato").setFont(textRegular);	
			Paragraph paragraphJefeDefault = new Paragraph(textJefeDefault);
			paragraphJefeDefault.setFontSize(9);
			
			Text textGteDirDefault = new Text("Nombre y firma Gte. o Dir del Área").setFont(textRegular);	
			Paragraph paragraphGteDirDefault = new Paragraph(textGteDirDefault);
			paragraphGteDirDefault.setFontSize(9);
			
			Text textRHDefault = new Text("VoBo. Recursos Humanos").setFont(textRegular);	
			Paragraph paragraphRHDefault = new Paragraph(textRHDefault);
			paragraphRHDefault.setFontSize(9);
			
			Table tableFirmas = new Table(4);
			tableFirmas.setWidth(UnitValue.createPercentValue(80));
			tableFirmas.setBorder(Border.NO_BORDER);
			tableFirmas.addCell(
						new Cell().setWidth(123).setBorder(Border.NO_BORDER).setPadding(0).setMargin(0).add(paragraphEmpleadoDefault)
						.setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.TOP));
			tableFirmas.addCell(
						new Cell().setWidth(125).setBorder(Border.NO_BORDER).setPadding(0).setMargin(0).add(paragraphJefeDefault)
						.setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.TOP));
			tableFirmas.addCell(
						new Cell().setWidth(125).setBorder(Border.NO_BORDER).setPadding(0).setMargin(0).add(paragraphGteDirDefault)
						.setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.TOP));
			tableFirmas.addCell(
						new Cell().setPadding(0).setBorder(Border.NO_BORDER).setMargin(0).add(paragraphRHDefault)
						.setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.TOP));
			tableFirmas.setFontSize(11);
			tableFirmas.setFixedPosition(47, 400, 500);
			document.add(tableFirmas);
			
			Text textImportante = new Text("COMENTARIOS").setFont(textBold);	
			Paragraph paragraphImportante = new Paragraph(textImportante).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.TOP);
			//paragraphImportante.setFontSize(9);
			paragraphImportante.setFixedPosition(30, 300, UnitValue.createPercentValue(100));
			paragraphImportante.setFontSize(11);
			document.add(paragraphImportante);
			
			Text textImportante2 = new Text("Anotar el motivo del permiso ó falta").setFont(textRegular);
			Paragraph paragraphImportante2 = new Paragraph(textImportante2).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.TOP);
			paragraphImportante2.setFixedPosition(30, 280, UnitValue.createPercentValue(100));
			paragraphImportante2.setFontSize(11);
			document.add(paragraphImportante2);
			
			
			Text textComentarios = new Text(strComentarios).setFont(textRegular);
			Paragraph paragraphComentarios = new Paragraph(textComentarios).setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.TOP);
			paragraphComentarios.setFixedPosition(30, 260, UnitValue.createPercentValue(100));
			paragraphComentarios.setFontSize(11);
			document.add(paragraphComentarios);			
			document.close();
			
			log.info("Se envia correo***");
			SendMail.mail(objUser.getEmailAddress(), "intranet@cuervo.com.mx", "Permiso", tempFile);
			log.info("se envio correo");
			return tempFile;

		} catch (IOException e) {
			e.printStackTrace();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static java.util.List<String> getUser(String strNo_Empleado) {
		DynamicQuery dQ = DynamicQueryFactoryUtil.forClass(ExpandoValue.class, PortalClassLoaderUtil.getClassLoader());
		dQ.add(PropertyFactoryUtil.forName("data").eq(strNo_Empleado));
		java.util.List<ExpandoValue> listExpando = ExpandoValueLocalServiceUtil.dynamicQuery(dQ);
		
		java.util.List<String> listName = new java.util.ArrayList<String>();
		if(!listExpando.isEmpty()) {
			ExpandoValue modelExpando = listExpando.get(0);
			dQ = DynamicQueryFactoryUtil.forClass(User.class, PortalClassLoaderUtil.getClassLoader());
			dQ.add(PropertyFactoryUtil.forName("userId").eq(modelExpando.getClassPK()));
			java.util.List<User> listUser = UserLocalServiceUtil.dynamicQuery(dQ);
			
			if(!listUser.isEmpty()) {
				String nombre = listUser.get(0).getFirstName();
				String apellidoMaterno = "";
				String apellidoPaterno = "";
				if(listUser.get(0).getExpandoBridge().hasAttribute("Nombres"))
					nombre = (String)listUser.get(0).getExpandoBridge().getAttribute("Nombres");
				if(listUser.get(0).getExpandoBridge().hasAttribute("Apellido_Materno"))
					apellidoMaterno = (String)listUser.get(0).getExpandoBridge().getAttribute("Apellido_Materno");
				if(listUser.get(0).getExpandoBridge().hasAttribute("Apellido_Paterno"))
					apellidoPaterno = (String)listUser.get(0).getExpandoBridge().getAttribute("Apellido_Paterno");
				
				listName.add(nombre.toUpperCase());
				listName.add(apellidoPaterno.toUpperCase());
				listName.add(apellidoMaterno.toUpperCase());
			}
		}
				
		return listName;
	}
	
	public static String encodeToString(BufferedImage image, String type) {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
 
        try {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();
            imageString = Base64.encodeBytes(imageBytes); 
            bos.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return imageString;
    }
	
}
