package main;
/**
 * @author Carlos Giraldo Garcia
 * @author Miguel Cisneros Rojas
 *
 */

import java.io.File;
import org.xmldb.api.*;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.*;


public class Main {

	private static final String PASSWORD = "karurosu"; //TODO user y pass
	private static final String USER = "admin";
	private static final String DIRECCION =  "xmldb:exist://localhost:8080/exist/xmlrpc/db";
	private static String urlBD;
	
	@SuppressWarnings("unused")
	public static int main(String[] args) {
		if((args.length!=4)||!(args[0].startsWith("/")))
		{
			System.out.println("Modo de uso:");
			System.out.println("('/'+coleccion)(directorio XML)(documentoXQUERY)(Nº Query)");
			return -1;
		}
		String coleccion = args[0];
		String directorioXML = args[1];
		String documentoQuery = args[2];
		String numeroQuery = args[3];
		
		//Eliminar "/db" si es especificado en la entrada
		if (coleccion.startsWith("/db")) {
			coleccion = coleccion.substring(3);
		}
		String urlBD1 = DIRECCION + coleccion;
		
		// Se inicializa el driver
		String driver = "org.exist.xmldb.DatabaseImpl";
		//Cargar el driver
		@SuppressWarnings("rawtypes")
		Class cl;
		Database database;
		try {
			cl = Class.forName(driver);
			
			//Instancia de la BD
			database = (Database) cl.newInstance();
			database.setProperty("create-database", "true");
			
			//Registrar la BD
			DatabaseManager.registerDatabase(database);
			
			// Se intenta acceder a la coleccion especificada como primer argumento
			Collection col = DatabaseManager.getCollection( urlBD, USER, PASSWORD);
			XPathQueryService service =	(XPathQueryService) col.getService("XPathQueryService", "1.0");
			
			if (col == null) {
				Collection root = DatabaseManager.getCollection(  DIRECCION ,USER, PASSWORD);
				CollectionManagementService mgtService =
				(CollectionManagementService) root.getService("CollectionManagementService","1.0");
				col = mgtService.createCollection(coleccion);
			}
			
			enviarXML(col, directorioXML);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		
		
		
		
		
		
		return 0;
	}

	private static void enviarXML(Collection col, String directorio) throws Exception
	{
		//---------------------------------------------------
		// Se crea un recurso XML para almacenar el documento
		// --------------------------------------------------
		XMLResource document = (XMLResource) col.createResource(null, "XMLResource");
		File dir = new File(directorio);
		if(!dir.isDirectory())
			throw new Exception("No es un directorio " + directorio);
		for(File f:dir.listFiles())
		{
			if (!f.canRead())
				throw new Exception("No es posible leer el archivo " + f.getName());
			
			//System.out.println(f.toString());
			document.setContent(f);
			//System.out.println("Almacenando el archivo " + document.getId() + "...");
			col.storeResource(document);
			//System.out.println("Almacenado correctamente");
		}
	}
}

