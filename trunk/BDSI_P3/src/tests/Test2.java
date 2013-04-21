package tests;

import org.xmldb.api.*;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.*;
import java.io.*;
import org.exist.util.*;
import org.exist.xmldb.*;
	@SuppressWarnings("unused")
	public class Test2 {
		@SuppressWarnings("rawtypes")
		public static void main(String args[]) throws Exception {
		
		// Se inicializa el driver
		String driver = "org.exist.xmldb.DatabaseImpl";
		//Cargar el driver
		Class cl = Class.forName(driver);//Cargar el driver
		//Instancia de la BD
		Database database = (Database) cl.newInstance();
		database.setProperty("create-database", "true");
		//Registrar la BD
		DatabaseManager.registerDatabase(database);
		//---------------------------------------------------------------------
		// Accedemos a la coleccion y realizamos una consulta
		//---------------------------------------------------------------------
		String collection = args[0];
		String file = args[1];
		//Eliminar "/db" si es especificado en la entrada
		if (collection.startsWith("/db")) {
			collection = collection.substring(3);
		}
		
		String urlBD1 = "xmldb:exist://localhost:8080/exist/xmlrpc/db" + collection;
		Collection col = DatabaseManager.getCollection( urlBD1, "admin", "karurosu");
		XPathQueryService service = (XPathQueryService)
		col.getService("XPathQueryService", "1.0");
		service.setProperty("pretty", "true");
		service.setProperty("encoding", "ISO-8859-1");
		
		//---------------------------------------------------
		// Consulta a lanzar (el documento debe existir ya en la base de datos)
		// --------------------------------------------------
		ResourceSet result =
		service.query("for $b in doc('agenda.xml')//persona return $b");
		ResourceIterator i = result.getIterator();
		//Se procesa el resultado.
		while (i.hasMoreResources()) {
			Resource r = i.nextResource();
			System.out.println((String) r.getContent());
		}						
	}
}
