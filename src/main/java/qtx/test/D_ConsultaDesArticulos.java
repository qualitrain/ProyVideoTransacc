package qtx.test;

import qtx.gestorDatos.ServicioDatos;

public class D_ConsultaDesArticulos {

	public static void main(String[] args) {
		ServicioDatos sd = new ServicioDatos();
		for(String descI : sd.getAllDescArticulos().keySet())
			System.out.println(descI);
		sd.cerrar();
	}

}
