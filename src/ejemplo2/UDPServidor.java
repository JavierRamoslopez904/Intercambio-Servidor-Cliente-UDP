package ejemplo2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Scanner;

/**
 * Clase UDPServidor
 * 
 * @author javie
 *
 */
public class UDPServidor {

	/** Atributo estático de tipo Int **/
	private static final int ECHOMAX = 255;

	/**
	 * Método main
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		// Instanciación de un objeto de la clase DatagramSocket
		DatagramSocket datagramSocket = null;
		Scanner S = new Scanner(System.in);

		try {
			// Inicialización del objeto creado anteriormente vinculado al puerto 8889
			datagramSocket = new DatagramSocket(8889);

			// Creación de un objeto de la clase DatagramPacket llamado paqueteRecibido
			DatagramPacket paqueteRecibido = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
			
			// Creación de un objeto de la clase DatagramPacket llamado paqueteEnviado
			DatagramPacket paqueteEnviado;
			while (true) {
				
				// Uso del método receive pasando por parámetro la variable paqueteRecibido
				datagramSocket.receive(paqueteRecibido);
				System.out.println("Recibido en : " + paqueteRecibido.getSocketAddress());
				System.out.println("El mensaje del cliente es : " + new String(paqueteRecibido.getData()));
				
				// Creación de una variable de tipo String
				String variable = new String(paqueteRecibido.getData(), java.nio.charset.StandardCharsets.UTF_8);
				
				// Creación de una variable de tipo String que almacena la palabra anterior pero en mayúscula
				String mayuscula = variable.toUpperCase();
				
				// Creación de un array de bytes que va a almacenar los bytes de la variable creada anteriormente
				byte[] frase = mayuscula.getBytes(java.nio.charset.StandardCharsets.UTF_8);

				// Inicialización de la variable paqueteEnviado
				paqueteEnviado = new DatagramPacket(frase , frase.length, paqueteRecibido.getSocketAddress());
				
				// Uso del método send, enviado por parámetro la variable paqueteEnviado
				datagramSocket.send(paqueteEnviado);

				String option = "";
				do {
					try {
						System.out.println("\n Escriba un * para salir del programa");
						option = S.next();
						
						// Establecimiento de condiciones por si la variable es *
						if (option.equals("*")) {
							
							// Cierre
							datagramSocket.close();
						}
					} catch (Exception e) {
						System.out.println("Debes de introducir *");
						S.nextLine();
					}
				} while (option.equals(""));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// Establecimiento de condiciones por si la variable datagramSocket es null
			if (datagramSocket != null) {
				datagramSocket.close();
			}
		}
	}
}