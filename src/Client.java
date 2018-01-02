import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Client {
	
	private int nomClient;
	private int nomCourtier;
	private HashMap<Societe, Integer> portfeuille;//liste de societe avec son nombre d'action que le client detient
	private double espece;
	private ArrayList<Commande> listeCommande;
	
	
	
	public Client() {
		listeCommande=new ArrayList<Commande>();
		portfeuille = new HashMap<>();
		
	}

	
	
	public static void main (String[] args){

		int port = 4000;
		InetAddress hote=null;
		Socket sc=null;
		BufferedReader in;
		PrintWriter out;
		Scanner scan;
		String rep;
		Client client =new Client();

		try{
			if (args.length>=2){
				hote= InetAddress.getByName(args[0]);
				port=Integer.parseInt(args[1]);
			}
			else{
				hote = InetAddress.getLocalHost();
				port = 4000;
			}
		}
		catch(UnknownHostException e){
			System.err.println("Machine inconnue :" +e);
		}
		try{

		System.out.println ("connexion au serveur courtier");
		sc = new Socket(hote,port);
		System.out.println ("le client est connecte au courtier");

		in = new BufferedReader(new InputStreamReader(sc.getInputStream()));
		out = new PrintWriter(sc.getOutputStream(),true);
		System.out.println ("taper 1 pour s'inscrire");
	    scan = new Scanner(System.in);
	    rep= scan.nextLine().trim();
		out.println(rep);//envoi de la reponse a la bourse

		String message=in.readLine();//recupere le message de la bourse
		System.out.println (message);


		scan = new Scanner(System.in);
		String nom= scan.nextLine().trim();

		out.println(nom);
		client.nomClient = Integer.parseInt(nom);

		String mess=in.readLine();
		System.out.println (mess);

		if (mess.equals("inscription client terminee aupres du courtier :")){
				System.out.println ("match");
				String nomCourtier = in.readLine();
				System.out.println(nomCourtier);
				client.nomCourtier = Integer.parseInt(nomCourtier);

			}
			
		







		}
		catch(IOException e){
			System.err.println("Impossible de creer la socket du courtier : " +e);
		}
			finally{
				try{
					sc.close();
				}
				catch (IOException e){}
			}
		}
}
