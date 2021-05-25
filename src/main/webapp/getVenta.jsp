<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Venta</title>
<script type="text/javascript">
function enrutarAregistroVenta(){
	var hdnAccion = document.getElementById("hdnAccion");
	hdnAccion.value = "registrarVta";
}
</script>
</head>
<body>
	<div id="titForma">
		<h3>Registro de Ventas</h3>
	</div>
	<form id="fmVenta" action="./Controlador" method="get">
		<div id="involucrados">
			<label for="selIdCte">Cliente:</label> <select id="selIdCte"
				name="idCte">
				<c:forEach items="${clientes}" var="clienteI">
					<c:choose>
						<c:when test="${clienteI.numCte eq idCteSel}">
							<option value="${clienteI.numCte}" selected="selected">${clienteI.nombre}</option>
						</c:when>
						<c:otherwise>
							<option value="${clienteI.numCte}">${clienteI.nombre}</option>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</select>
			<label for="selIdVendedor">Vendedor:</label> <select id="selIdVendedor"
				name="idVendedor">
				<c:forEach items="${vendedores}" var="vendedorI">
					<c:choose>
						<c:when test="${vendedorI.numVendedor eq idVendedorSel}">
							<option value="${vendedorI.numVendedor}" selected="selected">${vendedorI.nombre}</option>
						</c:when>
						<c:otherwise>
							<option value="${vendedorI.numVendedor}">${vendedorI.nombre}</option>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</select>
		</div>
		<p>
		
		<div id="carrito">
			<table>
				<thead>
					<tr>
						<th>Clave</th>
						<th>Descripci&oacute;n</th>
						<th>Cant</th>
						<th>Precio</th>
						<th>Total</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${carritoCompra}" var="detalleI">
						<tr>
						<td>${detalleI.cveArticulo}</td>
						<td>${detalleI.descripcion}</td>
						<td>${detalleI.cantidad}</td>
						<td>${detalleI.precioFormateado}</td>
						<td>${detalleI.importeItemFormateado}</td>
						</tr>
					</c:forEach>
						<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>${importeVenta}</td>
						</tr>
				</tbody>
			</table>
		</div>
		<p>
		
		<div id="selecArt">
			<label for="selIdArt">Art&iacute;culo:</label> <select id="selIdArt"
				name="idArt">
				<c:forEach items="${mapArticulos}" var="artI">
					<option value="${artI.value}">${artI.key}</option>
				</c:forEach>
			</select> <label for="idCantArt">Cantidad:</label><input name="cantArt"
				name="cantArt" type="number" value="1" maxlength="4"/>
			<p>
		</div>
		<input type="hidden" name="vista" value="getVenta.jsp"> 
		<input type="hidden" id="hdnAccion" name="accion" value="agregarArt"> 
		<input type="submit"
			value="agregar" > 
		<button type="submit" onclick="enrutarAregistroVenta()">registrar Venta</button>
	</form>
	<div id="seccmensajes">
	<span id="error">${mensaje}</span>
	</div>
</body>
</html>