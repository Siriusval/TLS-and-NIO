package fr.istic.chiffrement;

import javax.net.ssl.*;
import java.io.*;
import java.net.ServerSocket;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.Scanner;

public class Serveur {
	static int PORT = 9999;
	// NE FAITES PAS CA EN PROD :
	private static char[] PASSWORD = "123456".toCharArray();

	public static void main(String[] args) throws Exception {

		long time01 = System.currentTimeMillis();
		// Choisir l'un ou l'autre en fonction de la question :
		// creerSocketClassique();
		ServerSocket serverClassic = creerSocketClassique();
		System.out.println("Classic Server");
		System.out.printf("temps écoulé : %d ms %n", System.currentTimeMillis() - time01);
		System.out.println();
		serverClassic.close();

		long time02 = System.currentTimeMillis();
		ServerSocket serverTLS = creerSocketTLS();

		System.out.println("TLS Server");
		System.out.printf("temps écoulé : %d ms %n", System.currentTimeMillis() - time02);
		System.out.println();

		//Force cypher suite (choose)
		String choosenCypherSuite = "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256";
		System.out.println("Want to force "+choosenCypherSuite+" cypher suite [y/n]?");
		Scanner sc = new Scanner(System.in);
		String choice = sc.nextLine();
		if(choice.equals("y")){
			SSLServerSocket sslServerSocket = (SSLServerSocket) serverTLS;
			sslServerSocket.setEnabledCipherSuites(new String[] { "TLS_ECDH_anon_WITH_AES_128_CBC_SHA" });
		}

		//Wait for connexion
		System.out.println("Please connect w/ nc 192.168.1.31 9999 and send PING");
		readPingSendPong(serverTLS);
		serverTLS.close();
	}

	/**
	 * retourner une socket classique qui écoute sur le port 9999
	 * @return the classic http socket on port 9999
	 * @throws Exception, io exeption when socket is initialized
	 */
	public static ServerSocket creerSocketClassique() throws Exception {
		return new ServerSocket(PORT);
	}

	/**
	 * Créer une ServerSocket TLS qui écoute sur le port 9999
	 * 
	 * @return une socket classique
	 * @throws Exception
	 */
	public static ServerSocket creerSocketTLS() throws Exception {
		// Créer une ServerSocket TLS qui écoute sur le port 9999, avec les
		// caractéristiques suivantes :
		// + protocole utilisé : TLS
		SSLContext context = SSLContext.getInstance("TLS");
		// + format de clef PKCS12
		KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
		KeyStore ks = KeyStore.getInstance("PKCS12");
		// + mot de passe store et clef : variable PASSWORD plus haut.
		ks.load(new FileInputStream("serveurstore.keys"), PASSWORD);
		kmf.init(ks, PASSWORD);
		context.init(kmf.getKeyManagers(), null, null);
		Arrays.fill(PASSWORD, '0'); // on efface pour qu'il ne traine pas en mémoire.
		// + keystore, serveurstore.keys

		SSLServerSocketFactory factory = context.getServerSocketFactory();
		SSLServerSocket server = (SSLServerSocket) factory.createServerSocket(PORT);

		return server; // Retourner la socket correctement configurée
	}

	public static void readPingSendPong(ServerSocket server) throws Exception {
		// accepter une connexion
		SSLSocket sslsocket = (SSLSocket) server.accept();
		// lire PING
		InputStream in = sslsocket.getInputStream();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

		//Init line buffer
		String thisLine;

		//Init output
		OutputStream out = sslsocket.getOutputStream();
		PrintWriter printWriter = new PrintWriter(out);

		while ((thisLine = bufferedReader.readLine()) != null) {
			//Log
			System.out.println("Client wrote :"+thisLine);
			if(thisLine.equals("PING")){
				// écrire PONG vers le client
				printWriter.println("PONG");
				printWriter.flush();
				System.out.println("Server sent : PONG");
			}

		}

		sslsocket.close();

	}
}