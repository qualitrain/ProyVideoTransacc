package qtx.gestorDatos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.*;

import qtx.jpa.dominio.*;
import qtx.util.Util;

public class ServicioDatos {
	
	private static final String UNIDAD_PERSISTENCIA = "bdEjmTransacciones";
	private EntityManagerFactory fabrica;
	
	public ServicioDatos() {
		this.fabrica = Persistence.createEntityManagerFactory(UNIDAD_PERSISTENCIA);
	}
	
	public Cliente getClienteXID(long id) {
		EntityManager em = fabrica.createEntityManager();
		Cliente unCliente = em.find(Cliente.class, id);
//		unCliente.getVentas().size();
	    em.close();
	    return unCliente;		
	}
	
	public Vendedor getVendedorXID(long id) {
		EntityManager em = fabrica.createEntityManager();
		Vendedor unVendedor = em.find(Vendedor.class, id);
//		unVendedor.getVentas().size();
	    em.close();
	    return unVendedor;		
	}
	public Articulo getArticuloXID(String id) {
		EntityManager em = fabrica.createEntityManager();
		Articulo unArticulo = em.find(Articulo.class, id);
	    em.close();
	    return unArticulo;		
	}
	public void cerrar() {
		this.fabrica.close();
	}
	public boolean yaExisteCliente(Cliente p) {
		EntityManager em = fabrica.createEntityManager();
		Cliente clienteBD = em.find(Cliente.class, p.getNumCte());
		if(clienteBD != null) // Ya existe en BD!
			return true;
		else
			return false;
	}
	public boolean yaExisteVendedor(Vendedor p) {
		EntityManager em = fabrica.createEntityManager();
		Vendedor vendedorBD = em.find(Vendedor.class, p.getNumVendedor());
		if(vendedorBD != null) // Ya existe en BD!
			return true;
		else
			return false;
	}
	public EstatusOperBD insertarVendedor(Vendedor nvoVendedor) {
		if(this.yaExisteVendedor(nvoVendedor))
			return EstatusOperBD.LLAVE_DUPLICADA;		
		EntityManager em = fabrica.createEntityManager();
		EntityTransaction transaccion = em.getTransaction();
		transaccion.begin();
		em.persist(nvoVendedor);
		try {
			transaccion.commit();
		}
		catch(Exception ex) {
			return EstatusOperBD.getStatus(ex);
		}
		finally {
			em.close();
		}
		return EstatusOperBD.OK;
	}


	public EstatusOperBD actualizarCliente(Cliente p) { //Premisa: p YA EXISTE en la BD
		EntityManager em = this.fabrica.createEntityManager();
		EntityTransaction transaccion = em.getTransaction();
		transaccion.begin();
		Cliente pMerged = em.merge(p);
		em.persist(pMerged);
		try {
			transaccion.commit();
		}
		catch(Exception ex) {
			return EstatusOperBD.getStatus(ex);
		}
		finally {
			em.close();
		}
		return EstatusOperBD.OK;
	}

	public EstatusOperBD borrarCliente(Cliente p) {
		if (this.yaExisteCliente(p) == false)
			return EstatusOperBD.REGISTRO_INEXISTENTE;
		
		EntityManager em = this.fabrica.createEntityManager();
		EntityTransaction transaccion = em.getTransaction();
		transaccion.begin();
		Cliente pMerged = em.merge(p);
		try {
			em.remove(pMerged);
			transaccion.commit();
		}
		catch(Exception ex) {
			return EstatusOperBD.getStatus(ex);
		}
		finally {
			em.close();
		}
		return EstatusOperBD.OK;
	}
	public EstatusOperBD registrarVenta(Venta nuevaVta) {
		EntityManager em = this.fabrica.createEntityManager();
		EntityTransaction transaccion = em.getTransaction();
		transaccion.begin();
		try {
			Cliente clienteBD = em.find(Cliente.class, nuevaVta.getCliente().getNumCte(),
												LockModeType.OPTIMISTIC);
			double nuevoSaldo = clienteBD.getSaldo() + nuevaVta.getTotal();
			clienteBD.setSaldo(nuevoSaldo);
		
			Vendedor vendedorBD = em.find(Vendedor.class, nuevaVta.getVendedor().getNumVendedor(),
													LockModeType.OPTIMISTIC);
			long nVtas = vendedorBD.getnVtas() + 1;
			vendedorBD.setnVtas(nVtas);
		
			em.persist(nuevaVta);	
			System.out.println("persist (" + nuevaVta + ")");
			
			for(DetalleVenta detI : nuevaVta.getDetalleVentas()) {
				Articulo artBD_I = em.find(Articulo.class, detI.getArticulo().getCveArticulo(), 
													LockModeType.PESSIMISTIC_READ);
				int  existencia = artBD_I.getExistencia() - detI.getCantidad();
				artBD_I.setExistencia(existencia);
			}
			Util.hacerPausaAleatoria();
			transaccion.commit();
		}
		catch(Exception ex) {
			System.out.println("*** ERROR!! en ServicioDatos.RegistrarVenta:" + 
								this.getCadenaCausas(ex) + "***");
			return EstatusOperBD.getStatus(ex);
		}
		finally {
			em.close();
		}
		return EstatusOperBD.OK;
	}
	public EstatusOperBD registrarVentaDebug(Venta nuevaVta) {
		EntityManager em = this.fabrica.createEntityManager();
		EntityTransaction transaccion = em.getTransaction();
		System.out.println("---------------pre transaccion.begin ------------------");
		transaccion.begin();
		System.out.println("---------------post transaccion.begin ------------------");
		
		try {
		
			Cliente clienteBD = em.find(Cliente.class, nuevaVta.getCliente().getNumCte(),
												LockModeType.OPTIMISTIC);
//												LockModeType.OPTIMISTIC_FORCE_INCREMENT);
//												LockModeType.PESSIMISTIC_WRITE);
			System.out.println("===> em.find(Cliente.class, " + nuevaVta.getCliente().getNumCte() +
//					" LockModeType.OPTIMISTIC_FORCE_INCREMENT");	
//					" LockModeType.OPTIMISTIC");	
					" LockModeType.PESSIMISTIC_WRITE");	
			double nuevoSaldo = clienteBD.getSaldo() + nuevaVta.getTotal();
			clienteBD.setSaldo(nuevoSaldo);
		
			Vendedor vendedorBD = em.find(Vendedor.class, nuevaVta.getVendedor().getNumVendedor(),
													LockModeType.OPTIMISTIC);
//													LockModeType.OPTIMISTIC_FORCE_INCREMENT);
//													LockModeType.PESSIMISTIC_WRITE);
			System.out.println("===> em.find(Vendedor.class, " + nuevaVta.getVendedor().getNumVendedor() +
//					" LockModeType.OPTIMISTIC_FORCE_INCREMENT");	
					" LockModeType.OPTIMISTIC");	
//					" LockModeType.PESSIMISTIC_WRITE");	
			long nVtas = vendedorBD.getnVtas() + 1;
			vendedorBD.setnVtas(nVtas);
		
			em.persist(nuevaVta);	
			System.out.println("persist (" + nuevaVta + ")");
			
			for(DetalleVenta detI : nuevaVta.getDetalleVentas()) {
				Articulo artBD_I = em.find(Articulo.class, detI.getArticulo().getCveArticulo(), 
													LockModeType.OPTIMISTIC);
//													LockModeType.OPTIMISTIC_FORCE_INCREMENT);
//													LockModeType.PESSIMISTIC_READ);
				System.out.println("===> em.find(Articulo.class, " + detI.getArticulo().getCveArticulo() +
//						" LockModeType.OPTIMISTIC_FORCE_INCREMENT");	
						" LockModeType.OPTIMISTIC");	
//						" LockModeType.PESSIMISTIC_READ");	
				int  existencia = artBD_I.getExistencia() - detI.getCantidad();
				artBD_I.setExistencia(existencia);
			}
			Util.hacerPausaAleatoria();
			System.out.println("---------------pre  transaccion.commit ------------------");
			transaccion.commit();
			System.out.println("---------------post transaccion.commit ------------------");
		}
		catch(Exception ex) {
			System.out.println("*** ERROR!! en ServicioDatos.RegistrarVenta:" + 
								this.getCadenaCausas(ex) + "***");
			return EstatusOperBD.getStatus(ex);
		}
		finally {
			em.close();
		}
		return EstatusOperBD.OK;
	}
	private String getCadenaCausas(Exception ex) {
		 Throwable causaEx = ex.getCause();
		 StringBuilder sb = new StringBuilder();
		 sb.append(ex.getClass().getName() + " ("+ ex.getMessage() + ")");
		 while(causaEx.getCause() != null) {
			 sb.append("->" + causaEx.getClass().getName() + " (" + causaEx.getMessage() + ")-> ");
			 causaEx = causaEx.getCause();
		 }
		 sb.append(causaEx.getClass().getName() + " (" + causaEx.getMessage() + ")");
		 return sb.toString();
	}

	public List<Cliente> getAllClientes(){
		EntityManager em = this.fabrica.createEntityManager();
		@SuppressWarnings("unchecked")
		List<Cliente> clientes = (List<Cliente>) em.createNamedQuery("Cliente.findAll")
														.getResultList();
	    em.close();
	    return clientes;		
	}
	public List<Vendedor> getAllVendedores(){
		EntityManager em = this.fabrica.createEntityManager();
		@SuppressWarnings("unchecked")
		List<Vendedor> vendedores = (List<Vendedor>) em.createNamedQuery("Vendedor.findAll")
														.getResultList();
	    em.close();
	    return vendedores;		
	}
	public List<Articulo> getAllArticulos(){
		EntityManager em = this.fabrica.createEntityManager();
		@SuppressWarnings("unchecked")
		List<Articulo> articulos = (List<Articulo>) em.createNamedQuery("Articulo.findAll")
														.getResultList();
	    em.close();
	    return articulos;		
	}
	public Map<String,String> getAllDescArticulos(){
		List<Articulo> articulos = this.getAllArticulos();
		Map<String,String> descripciones = new TreeMap<>();
		for(Articulo artI : articulos) {
			descripciones.put(artI.getDescripcionExtendida(),artI.getCveArticulo());
		}
		return descripciones;
	}

}
