package fr.istic.chiffrement;

import java.net.ServerSocket;

public class Serveur {
	static int PORT = 9999;
	// NE FAITES PAS CA EN PROD :
	private static char[] PASSWORD = "123456".toCharArray();

	public static void main(String[] args) throws Exception {
		long time = System.currentTimeMillis();
		// Choisir l'un ou l'autre en fonction de la question :
		ServerSocket server = creerSocketTLS(); // creerSocketClassique();
		System.out.printf("temps écoulé : %d ms %n", System.currentTimeMillis() - time);
		readPingSendPong(server);
	}

	public static ServerSocket creerSocketClassique() throws Exception {
		// TODO : retourner une socket classique qui écoute sur le port 9999
		return null;
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
		// + format de clef PKCS12
		// + mot de passe store et clef : variable PASSWORD plus haut.
		// + keystore, serveurstore.keys

		return null; // Retourner la socket correctement configurée
	}

	public static void readPingSendPong(ServerSocket server) throws Exception {
		// accepter une connexion
		// lire PING
		// écrire PONG vers le client
	}
}