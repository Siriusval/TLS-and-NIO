package fr.istic.chiffrement;

public class Client {

	private static char[] PASSWORD = "654321".toCharArray();

	public static void main(String[] args) throws Exception {
		// Création d'un SSLContext comme pour le serveur
		// avec les caractéristiques suivantes :
		// + protocole utilisé : TLS
		// + format de clef PKCS12
		// + mot de passe store et clef : variable PASSWORD plus haut (ou null)
		// + keystore : clientstore.keys

		// ...
		// ks.load(new FileInputStream("clientstore.keys"), null);
		// ..

		// Creation des printwriter et buffered reader puis :

		// out.write("PING");
		// out.flush();
		// System.out.println(in.readLine());
	}

}