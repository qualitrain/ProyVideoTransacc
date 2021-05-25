package qtx.gestorDatos;

import javax.persistence.EntityExistsException;
import javax.persistence.LockTimeoutException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;
import javax.persistence.PessimisticLockException;
import javax.persistence.RollbackException;
import javax.persistence.TransactionRequiredException;

import org.hibernate.dialect.lock.OptimisticEntityLockException;
import org.hibernate.exception.LockAcquisitionException;

public enum EstatusOperBD {
	OK,
	OPER_JPA_ESTADO_ILEGAL,
	LLAVE_DUPLICADA,
	REGISTRO_INEXISTENTE,
	CONEXION_NO_DISPONIBLE,
	FALLA_COMMIT,
	ABRAZO_MORTAL_X_LOCKS,
	FALLA_LOCK_OPTIMISTA,
	FALLA_LOCK_PESIMISTA,
	PROBLEMA_AUTENTICACION, 
	FALLA_JPA, 
	TRANSACCION_REQUERIDA, 
	FALLA_JSE, 
	NO_EXISTE;
	
	public String getMensaje() {
		switch (this) {
		case LLAVE_DUPLICADA: return "Ya hay un registro con la misma llave";
		case ABRAZO_MORTAL_X_LOCKS: return "Se presenta un abrazo mortal por locks con otra transacción. (Reintente)";
		case OK: return "Operación exitosa";
		default: return this.toString();
		}
	}
	public static EstatusOperBD getStatus(Exception ex) {
		if(ex instanceof EntityExistsException)
			return EstatusOperBD.LLAVE_DUPLICADA;
		if(ex instanceof OptimisticLockException)
			return EstatusOperBD.FALLA_LOCK_OPTIMISTA;
		if(ex instanceof PessimisticLockException)
			return EstatusOperBD.FALLA_LOCK_OPTIMISTA;
		if(ex instanceof LockTimeoutException)
			return EstatusOperBD.FALLA_LOCK_PESIMISTA;
		
		if(ex instanceof RollbackException) {
			 Throwable causaEx = ex.getCause();
			 if(causaEx == null)
				 return EstatusOperBD.FALLA_COMMIT;
			 if(esAbrazoMortalPorLocks(ex))
				 return EstatusOperBD.ABRAZO_MORTAL_X_LOCKS;
			 return EstatusOperBD.FALLA_COMMIT;
		}
		
		if(ex instanceof TransactionRequiredException)
			return EstatusOperBD.TRANSACCION_REQUERIDA;
		if(ex instanceof PersistenceException)
			return EstatusOperBD.FALLA_JPA;
		return EstatusOperBD.FALLA_JSE;
		
	}
	private static boolean esAbrazoMortalPorLocks(Exception ex) {
		 Throwable causaEx = ex.getCause();
		 if(causaEx == null)
			 return false;
		 while(causaEx.getCause() != null) {
			 if(causaEx instanceof LockAcquisitionException)
				 return true;
			 causaEx = causaEx.getCause();
		 }
		return false;
	}

}
