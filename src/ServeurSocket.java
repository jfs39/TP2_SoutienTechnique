import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



/**
 * @author Jean-François Sergerie et Stéphanie Leduc
 *
 */
public class ServeurSocket {

	
	public static void main(String[] args) throws Exception {
		ExecutorService service = Executors.newFixedThreadPool(6);
		final int httpd = 8085;
		Socket socketCommunication = null;
		ServerSocket socketServeur = null;

		try {

			socketServeur = new ServerSocket(httpd);
			while(true) {
			socketCommunication = socketServeur.accept();
			service.execute(new HandleConnexions(socketCommunication));
			}
		}
		catch (IOException e) {
			 e.printStackTrace();
		} finally {
			try {
				
				if (socketServeur != null)
					socketServeur.close();
				if (socketCommunication != null)
					socketCommunication.close();
					
			} catch (IOException e) {
				System.out.println("Erreur !" + e.getMessage());
			}
		}
		
	}
	
}
