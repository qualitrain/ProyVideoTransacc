package qtx.servicios;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import qtx.gestorDatos.EstatusOperBD;
import qtx.gestorDatos.ServicioDatos;
import qtx.jpa.dominio.Articulo;
import qtx.jpa.dominio.Cliente;
import qtx.jpa.dominio.Vendedor;
import qtx.jpa.dominio.Venta;
import qtx.jpa.dominio.DetalleVenta;
import qtx.web.DetalleCarritoCompra;

public class GestorVentaEnLinea {

	public GestorVentaEnLinea() {
	}
	public List<Cliente>  getListaClientes()  {
	   	ServicioDatos sd = new ServicioDatos();
    	List<Cliente> clientes;
    	try {
    		clientes = sd.getAllClientes();
    	}
    	finally {
    		sd.cerrar();
    	}
		return clientes;
	}
	public List<Vendedor> getListaVendedores() {
	   	ServicioDatos sd = new ServicioDatos();
    	List<Vendedor> vendedores;
		try {
			vendedores = sd.getAllVendedores();
		}
    	finally {
    		sd.cerrar();
    	}
		return vendedores;
	}
	public Map<String,String> getMapaArticulos(){
    	ServicioDatos sd = new ServicioDatos();
    	Map<String,String> mapArticulos;
		try {
    		mapArticulos = sd.getAllDescArticulos();
		}
		finally {
			sd.cerrar();
		}
		return mapArticulos;
	}
	public Articulo getArticulo(String llave) {
		ServicioDatos sd = new ServicioDatos();
		Articulo articulo;
		try {
			articulo = sd.getArticuloXID(llave);
		}
		finally {
			sd.cerrar();
		}
		return articulo;
	}
	public Cliente getCliente(long llave) {
		ServicioDatos sd = new ServicioDatos();
		Cliente cte;
		try {
			cte = sd.getClienteXID(llave);
		}
		finally {
			sd.cerrar();
		}
		return cte;
	}
	public Vendedor getVendedor(long llave){
		ServicioDatos sd = new ServicioDatos();
		Vendedor vendedor;
		try {
			vendedor = sd.getVendedorXID(llave);
		}
		finally {
			sd.cerrar();
		}
		return vendedor;
	}
	public Venta registrarVenta(String cadIdCte, String cadIdVendedor, 
			List<DetalleCarritoCompra> carritoCompra) throws Exception{
		Venta vta = new Venta();
		long idCte = Long.parseLong(cadIdCte);
		long idVendedor = Long.parseLong(cadIdVendedor);
		
		ArrayList<DetalleVenta> detallesVta = new ArrayList<>();
		int nDetVtaI = 1;
		for(DetalleCarritoCompra detCarritoI : carritoCompra) {
			DetalleVenta detVtaI = new DetalleVenta();
			
			detVtaI.setNumDetalleVta(nDetVtaI);
			nDetVtaI++;
			detVtaI.setCantidad(detCarritoI.getCantidad());
			Articulo articuloI = this.getArticulo(detCarritoI.getCveArticulo());
			detVtaI.setArticulo(articuloI);
			detVtaI.setPrecioUnitario(articuloI.getPrecioLista());
			detVtaI.setVenta(vta);
			detallesVta.add(detVtaI);
		}
		Cliente cte = this.getCliente(idCte);
		Vendedor vendedor = this.getVendedor(idVendedor);
		vta.setCliente(cte);
		vta.setVendedor(vendedor);
		vta.setDetalleVentas(detallesVta);
		vta.setFechaVenta(new Date());
		
		ServicioDatos sd = new ServicioDatos();
		EstatusOperBD statusVta;
		try {
			statusVta =  sd.registrarVenta(vta);
		}
		finally {
			sd.cerrar();
		}
		if(statusVta != EstatusOperBD.OK) {
			vta.setNumVenta(0);
			throw new VtaEnLineaException(statusVta.getMensaje() + " al intentar registrar la venta");
		}
		return vta;
	}
}
