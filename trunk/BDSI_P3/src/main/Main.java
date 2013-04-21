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

	private static String PASSWORD = "XMLFdILABs"; //TODO user y pass
	private static String USER = "admin";
	private static final String DIRECCION =  "xmldb:exist://localhost:8080/exist/xmlrpc/db";
	private static String urlBD;
	
	private static String coleccion;
	private static String directorioXML;
	private static String documentoQuery;
	private static String numeroQuery;
	
	@SuppressWarnings({ "unused", "rawtypes" })
	public static void main(String[] args) {
		
		switch (args.length){
			case 4:{
				coleccion = args[0];
				directorioXML = args[1];
				documentoQuery =args[2];
				numeroQuery = args[3];
				break;}
						
			case 6:{
				USER = args[0];
				PASSWORD = args[1];
				coleccion = args[2];
				directorioXML = args[3];
				documentoQuery =args[4];
				numeroQuery = args[5];
				break;}
		
			default:{
				System.out.println("Modo de uso en Laboratorio");
				System.out.println("('/'+coleccion)(directorio XML)(documentoXQUERY)(Nº Query)");
				System.out.println("Modo de uso en casa");
				System.out.println("(usurio)(contraseña)('/'+coleccion)(directorio XML)(documentoXQUERY)(Nº Query)");
				//return -1;
				}
		}
		
		//Eliminar "/db" si es especificado en la entrada
		if (coleccion.startsWith("/db")) {
			coleccion = coleccion.substring(3);
		}
		String urlBD1 = DIRECCION + coleccion;
		
		String driver = "org.exist.xmldb.DatabaseImpl";
		Class cl;
		Database database;
		Collection col;
		try {
			cl = Class.forName(driver);
			//Instancia de la BD
			database = (Database) cl.newInstance();
			database.setProperty("create-database", "true");
			
			//Registrar la BD
			DatabaseManager.registerDatabase(database);
			
			// Se intenta acceder a la coleccion especificada como primer argumento
			col = DatabaseManager.getCollection(urlBD1, USER, PASSWORD);
			if (col == null) {
				Collection root = DatabaseManager.getCollection(  DIRECCION ,USER, PASSWORD);
				CollectionManagementService mgtService =
				(CollectionManagementService) root.getService("CollectionManagementService","1.0");
				col = mgtService.createCollection(coleccion);
			}
			
			//FIXME
			XPathQueryService service =	(XPathQueryService) col.getService("XPathQueryService", "1.0");
			
			cargarXMLs(col, directorioXML);
			
		
			String miQuery = obtenerQuery(numeroQuery,documentoQuery);
			
			ResourceSet result = service.query(miQuery);
			ResourceIterator i = result.getIterator();
			while (i.hasMoreResources()) {
				Resource r = i.nextResource();
				System.out.println((String) r.getContent());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		//return 0;

	}

	private static String obtenerQuery(String numeroQuery2,	String documentoQuery2) {
		// TODO Auto-generated method stub
		return null;
	}

	private static void cargarXMLs(Collection col, String directorioXML) {		
		try{
			File dir = new File(directorioXML);
			if(!dir.isDirectory())
				throw new Exception("No es un directorio " + directorioXML);
			for(File fichero:dir.listFiles())
			{
				if (fichero.getName().endsWith(".xml")){
					if (!fichero.canRead())
						throw new Exception("No es posible leer el archivo " + fichero.getName());
					XMLResource xmlRes = (XMLResource) col.createResource(fichero.getName(), "XMLResource");
					
					//System.out.println(f.toString());
					xmlRes.setContent(fichero);
					//System.out.println("Almacenando el archivo " + document.getId() + "...");
					col.storeResource(xmlRes);
					//System.out.println("Almacenado correctamente");
				}
			}
			System.out.println("putapita");
		}catch(Exception e){
			
		}
		
	}
		
}

