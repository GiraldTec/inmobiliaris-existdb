package tests;

import org.xmldb.api.*;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.*;
import java.io.*;
import java.net.URI;

import org.exist.util.*;
import org.exist.xmldb.*;

public class Test1 {

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
	// Se intenta acceder a la coleccion especificada como primer argumento
	//---------------------------------------------------------------------
	String collection = args[0];
	String file = args[1];
	//Eliminar "/db" si es especificado en la entrada
	if (collection.startsWith("/db")) {
		collection = collection.substring(3);
	}
	
	String urlBD1 = "xmldb:exist://localhost:8080/exist/xmlrpc/db" + collection;
	Collection col = DatabaseManager.getCollection( urlBD1, "admin", "karurosu");
	
	// Si la coleccion especificada como primer argumento no existe,
	// entonces se crea
	if (col == null) {
		String urlBD2 = "xmldb:exist://localhost:8080/exist/xmlrpc/db";
		Collection root = DatabaseManager.getCollection(urlBD2,"admin","karurosu");
		CollectionManagementService mgtService =
		(CollectionManagementService) root.getService("CollectionManagementService",
		"1.0");
		col = mgtService.createCollection(collection);
	}else{
		XPathQueryService service =	(XPathQueryService) col.getService("XPathQueryService", "1.0");
	}
	
	
	//---------------------------------------------------
	// Se crea un recurso XML para almacenar el documento
	// --------------------------------------------------
	File f = new File(file);
	XMLResource document = (XMLResource) col.createResource(f.getName(), "XMLResource");
	
	if (!f.canRead()) {
		System.err.println("No es posible leer el archivo " + file);
	}
	System.out.println(f.toString());
	document.setContent(f);
	System.out.println("Almacenando el archivo " + document.getId() + "...");
	col.storeResource(document);
	System.out.println("Almacenado correctamente");
	}
}