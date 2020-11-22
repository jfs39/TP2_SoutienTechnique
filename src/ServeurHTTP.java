import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;



public class ServeurHTTP {

	public static void main(String[] args) {
		final int httpd = 8080;
		int counter=0;
		Socket socketCommunication = null;
		ServerSocket socketServeur = null;

		try {

			socketServeur = new ServerSocket(httpd);
			while(true) {
				
				socketCommunication = socketServeur.accept();
				System.out.println("un client a fait une connexion");
				ConnexionClient connexionClient = new ConnexionClient(socketCommunication);
				connexionClient.getEntete();
				if(counter==0) {
				connexionClient.envoiReponse();//TODO find a way pour garder le serveur up without crashing
				
				counter++;
				}
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
