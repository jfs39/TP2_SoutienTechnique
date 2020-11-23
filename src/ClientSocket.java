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
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
/*AUTEURS : Jean-François Sergerie et Stéphanie Leduc*/

public class ClientSocket {
	Socket socketCommunication;
	PrintWriter out = null; 
	BufferedReader in = null;
	String fullUrl;
	
	ClientSocket(Socket socketCommunication) {
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
			if(initialLine != "") {
			fullUrl = initialLine.split(" ")[1];
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void envoiReponse() throws IOException, Exception {
		
		String urlSite = fullUrl.substring(fullUrl.lastIndexOf("/")+1);
		//String urllll="siteHTTP/"+urlSite;
		String corps = "";
		URL url = getClass().getResource(urlSite);
		
		File file = new File(url.getPath());
		//if(fichierCourant.exists() ) {
			corps= lireFichier(file);
	       // } else if(urlFile.equals("secret.html")) {
	      // corps = lireFichier(fichierErreur);
	       // }
		
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
 br.close();
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
