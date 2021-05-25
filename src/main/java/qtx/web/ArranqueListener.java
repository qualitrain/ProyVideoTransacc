package qtx.web;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

//import qtx.gestorDatos.ServicioDatos;
import qtx.jpa.dominio.Cliente;
import qtx.jpa.dominio.Vendedor;
import qtx.servicios.GestorVentaEnLinea;

/**
 * Application Lifecycle Listener implementation class ArranqueListener
 *
 */
@WebListener
public class ArranqueListener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public ArranqueListener() {
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  { 
     }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  { 
    	GestorVentaEnLinea gVtas = new GestorVentaEnLinea();
    	List<Cliente> clientes = gVtas.getListaClientes();
    	List<Vendedor> vendedores = gVtas.getListaVendedores();
    	Map<String,String> mapArticulos = gVtas.getMapaArticulos();
    	arg0.getServletContext().setAttribute("clientes", clientes);
    	arg0.getServletContext().setAttribute("vendedores", vendedores);
    	arg0.getServletContext().setAttribute("mapArticulos", mapArticulos);
    }
	
}
