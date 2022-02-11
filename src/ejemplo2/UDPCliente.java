package ejemplo2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

/**
 * Clase UDPCliente
 * @author javie
 *
 */
public class UDPCliente {
	
	/** Atributo estático que recogerá el Tiempo de espera **/
	private static final int TIEMPO_ESPERA = 5000;
	
	/** Atributo estático que recogerá la posición**/
	private static final int POSICION = 5;

	/**
	 * Método main encargado de ejecutar la aplicación
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner S = new Scanner(System.in);

		// Interacción con el usuario para que introduzca texto
		System.out.println("Escriba un mensaje para que el servidor lo reciba. \n");
		String texto = S.nextLine();
		
		// Muestra del mensaje
		System.out.println("Mensaje enviado al servidor " + texto + "\n");

		// Almacenamiento en un array de bytes los bytes de la variable recogida por el usuario
		byte[] bytes = texto.getBytes();
		
		// Declaración de la variable de la clase DatagramSocket a null
		DatagramSocket datagramSocket = null;
		try {
			// Inicialización de la variable creada anteriormente
			datagramSocket = new DatagramSocket();
			
			// Modificación del tiempo de espera con el método setSoTimeout de la clase DatagramSocket
			datagramSocket.setSoTimeout(TIEMPO_ESPERA);
			
			// Creación de un objeto de la clase DatagramPacket llamado paqueteEnviado
			DatagramPacket paqueteEnviado = new DatagramPacket(bytes, bytes.length, InetAddress.getLocalHost(), 8889);
			
			// Creación de un objeto de la clase DatagramPacket llamado paqueteRecibido
			DatagramPacket paqueteRecibido = new DatagramPacket(new byte[bytes.length], bytes.length);

			// Creación de una variable de tipo Int que guardan los intentos
			int intentos = 0;
			
			// Creación de una variable de tipo Boolean inicializada a False
			boolean recibido = false;
			do {
				// Uso del método send para enviar la variable paqueteEnviado
				datagramSocket.send(paqueteEnviado);
				try {
					
					// Uso del método receive para recibir la variable paqueteRecibido
					datagramSocket.receive(paqueteRecibido);
					
					// Establecimiento de condiciones
					if (!paqueteRecibido.getAddress().equals(paqueteEnviado.getAddress())) {
						throw new IOException("ERROR");
					}
					recibido = true;
				} catch (IOException e) {
					// En caso de fallo, se vuelve a intenar
					intentos += 1;
					System.out.println("Tiempo acabado ,  quedan " + (POSICION - intentos) + " intentos");
				}
			} while (!recibido && (intentos < POSICION));

			// Establecimiento de condiciones
			if (recibido) {
				System.out.println("El mensaje del servidor es : " + new String(paqueteRecibido.getData()));
			} else {
				System.out.println("Saliendo ...");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// Se cierra el Socket con el método close
			if (datagramSocket != null) {
				datagramSocket.close();
			}
		}
	}
}
