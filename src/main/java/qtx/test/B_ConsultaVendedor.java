package qtx.test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import qtx.jpa.dominio.Vendedor;

public class B_ConsultaVendedor {

	public static void main(String[] args) {
			EntityManagerFactory fabrica = Persistence.createEntityManagerFactory("bdEjmTransacciones");
			EntityManager em = fabrica.createEntityManager();
					    
			long id = 1;
			Vendedor unaPersona = em.find(Vendedor.class, id);
		    if(unaPersona != null)
		    	System.out.println("La persona con id " + id + " se llama "+ unaPersona.getNombre());
		    else
		    	System.out.println("La persona con id " + id + " no existe ");
	}
}

