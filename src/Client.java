import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.io.ObjectInputStream;

import javax.accessibility.AccessibleTableModelChange;

public class Client {
	
	private int nomClient;
	private int nomCourtier;
	private HashMap<String, Integer> portfeuille;//liste de societe avec son nombre d'action que le client detient
	private double espece;
	//private ArrayList<Commande> listeCommande;
	private String etat;
	private double especeAchatAttente;
	private HashMap<String, Integer> venteAttente; //Nombre d'actions en attente d'être vendues pour chaque société
	
	public Client() {
		//listeCommande=new ArrayList<Commande>();
		portfeuille = new HashMap<>();
		venteAttente = new HashMap<>();
		
	}

	
	public static void ordreAchat(Client client, BufferedReader in, PrintWriter out) {
		String societe;
		double prix;
		int nbActions;
		Scanner scan = new Scanner(System.in);
		System.out.println("Société ?");
		societe = scan.nextLine().trim();
		System.out.println("Prix ?");
		prix = (double)Integer.parseInt(scan.nextLine().trim());
		System.out.println("Combien d'actions ?");
		nbActions = Integer.parseInt(scan.nextLine().trim());
		
		//Vérifie que le client possède l'argent nécessaire
		double nvValeur = client.espece - client.especeAchatAttente - (prix * nbActions) - (prix * nbActions)/10;
		
		if(nvValeur > 0) {
			System.out.println("Ressources suffisantes, envoi de l'ordre d'achat au courtier...");
			client.especeAchatAttente += (prix * nbActions) + (prix * nbActions)/10;
			out.println(societe);
			out.println(prix);
			out.println(nbActions);
		} else {
			System.out.println("Ressources insuffisantes, ordre d'achat annulé.");
		}
		
	}
	
	public static void ordreVente(Client client, BufferedReader in, PrintWriter out) {
		String societe;
		double prix;
		int nbActions;
		Scanner scan = new Scanner(System.in);
		System.out.println("Société ?");
		societe = scan.nextLine().trim();
		System.out.println("Prix ?");
		prix = (double)Integer.parseInt(scan.nextLine().trim());
		System.out.println("Combien d'actions ?");
		nbActions = Integer.parseInt(scan.nextLine().trim());
		
		//Vérifie que le client possède les actions nécessaires
		int ventePossible = client.portfeuille.get(societe)-client.venteAttente.get(societe)-nbActions;
		
		if (ventePossible > 0) {
			System.out.println("Vente possible, envoi de l'ordre au courtier...");
			client.venteAttente.put(societe, client.venteAttente.get(societe)+nbActions);
			out.println(societe);
			out.println(prix);
			out.println(nbActions);
		} else {
			System.out.println("Pas assez d'actions pour la société concernée, annulation de l'ordre de vente.");
		}
		
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
		out.println(rep);//envoi de la reponse au courtier

		String message=in.readLine();//recupere le message du courtier
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
				client.etat = "ouvert";
				mess = "Début";
			}
			
		while(client.etat.equals("ouvert")){
			if(mess.equals("Début")) {
				System.out.println("Ordre ?");
				scan = new Scanner(System.in);
				rep= scan.nextLine().trim();
				
				if(rep.equals("achat")){
					out.println(rep);
					ordreAchat(client, in, out);
				} else if(rep.equals("vente")) {
					out.println(rep);
					ordreVente(client, in, out);
				} else if(rep.equals("marche")) {
					out.println(rep);
					System.out.println(in.readLine());
				} else if(rep.equals("fermer")) {
					out.println(rep);
					client.etat = "ferme";
				}
				
			} else if(mess.equals("Achat effectué")) {
				Commande commande = new Commande();
				commande = in.readObject();
				
				client.portfeuille.put(commande.getNomSociete(), client.portfeuille.get(commande.getNomSociete())+commande.getNbActions());
				client.espece = client.espece - commande.getPrix()*commande.getNbActions() - (commande.getPrix()*commande.getNbActions())/10;				
			
			} else if(mess.equals("Vente effecutée")) {
				Commande commande = new Commande();
				commande = in.readObject();
				
				client.portfeuille.put(commande.getNomSociete(), client.portfeuille.get(commande.getNomSociete())-commande.getNbActions());
				client.espece += commande.getPrix()*commande.getNbActions() - (commande.getPrix()*commande.getNbActions())/10;	
			}
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
