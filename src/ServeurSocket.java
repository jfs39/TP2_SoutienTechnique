import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class ServeurSocket {
static ClientSocket connexionClient;
	public static void main(String[] args) throws Exception {
		final int httpd = 8085;
		int counter=0;
		Socket socketCommunication = null;
		ServerSocket socketServeur = null;

		try {

			socketServeur = new ServerSocket(httpd);

			while(true) {
				
				socketCommunication = socketServeur.accept();
		
			
				System.out.println("un client a fait une connexion");
				connexionClient = new ClientSocket(socketCommunication);
				connexionClient.getEntete();
				connexionClient.envoiReponse();
				connexionClient.fermetureFlux();
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
