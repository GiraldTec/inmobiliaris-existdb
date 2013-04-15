/**
 * 
 */

/**
 * @author Carlos Giraldo Garcia
 * @author Miguel Cisneros Rojas
 *
 */
public class Main {

	/**
	 * @param args
	 * @return 
	 */
	public static int main(String[] args) {
		if(args.length!=4)
		{
			System.out.println("Modo de uso:");
			System.out.println("(coleccion)(directorio XML)(documentoXQUERY)(Nº Query)");
			return -1;
		}
		String colecion = args[0];
		String directorioXML = args[1];
		String documentoQuery = args[2];
		String numeroQuery = args[3];
		return 0;
	}

}

