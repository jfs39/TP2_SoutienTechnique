import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
/*AUTEURS : Jean-François Sergerie et Stéphanie Leduc*/

public class ConnexionClient {
	Socket socketCommunication;
	PrintWriter out = null; 
	BufferedReader in = null;
	String fullUrl;
	
	ConnexionClient(Socket socketCommunication) {
		this.socketCommunication = socketCommunication;
		try {
			out = new PrintWriter(socketCommunication.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socketCommunication.getInputStream()));
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
	
	void getEntete() {
		
		try {
			String initialLine = in.readLine();
			fullUrl = initialLine.split(" ")[1];
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void envoiReponse() throws IOException {
		
		//socketCommunication.
		String urlFile = fullUrl.substring(fullUrl.lastIndexOf("/")+1);
		String corps = "";
		File fichierCourant = new File(ConnexionClient.class.getResource("pages/"+ urlFile).getFile());
		//File fichierErreur = new File(ConnexionClient.class.getResource("pages/erreur.html").getFile());
		if(fichierCourant.exists()) {
			corps= lireFichier(fichierCourant);
	        } else {
	     //  corps = lireFichier(fichierErreur);
	        }
			

		// longueur du corps de la réponse
		int len = corps.length();

		// envoie de la line de début, entêtes, la ligne vide et le corps
		out.print("HTTP-1.0 200 OK\r\n");
		out.print("Content-Length: " + len + "\r\n");
		out.print("Content-Type: text/html\r\n\r\n");
		out.print(corps);

		out.flush();

	}
	
    public static String lireFichier(File file) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStream in = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + System.lineSeparator());
        }
 
        return sb.toString();
    }
	
	
	void fermetureFlux() {
		try {
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
