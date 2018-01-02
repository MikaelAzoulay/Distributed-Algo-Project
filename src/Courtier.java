import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class Courtier{

 
    private int nomCourtier;
    private HashMap<Integer,String> listeClient;// faire hashmap (nom,etat)
	private HashMap<Integer, Commande> listeAttenteCommande;
	private ArrayList<Societe> societes ;
	private double tauxCommission;
	private double especes;
	private int port = 4020;
	private InetAddress hote=null;
	private Socket sc=null;
	private String etat;


	
	public Courtier() {
		listeClient = new HashMap<Integer,String>();
	    listeAttenteCommande= new HashMap<Integer,Commande>();
	}
	
	public Courtier(int nomCourtier) {
		this.nomCourtier = nomCourtier;
		listeClient = new HashMap<Integer,String>();
		listeAttenteCommande= new HashMap<Integer,Commande>();
	}
	
	
	
	public int getNomCourtier() {
		return nomCourtier;
	}

	public void setNomCourtier(int nomCourtier) {
		this.nomCourtier = nomCourtier;
	}

	public HashMap<Integer, String> getListeClient() {
		return listeClient;
	}

	public void setListeClient(HashMap<Integer, String> listeClient) {
		this.listeClient = listeClient;
	}

	public HashMap<Integer, Commande> getListeAttenteCommande() {
		return listeAttenteCommande;
	}

	public void setListeAttenteCommande(HashMap<Integer, Commande> listeAttenteCommande) {
		this.listeAttenteCommande = listeAttenteCommande;
	}

	public ArrayList<Societe> getSocietes() {
		return societes;
	}

	public void setSocietes(ArrayList<Societe> societes) {
		this.societes = societes;
	}

	public double getTauxCommission() {
		return tauxCommission;
	}

	public void setTauxCommission(double tauxCommission) {
		this.tauxCommission = tauxCommission;
	}

	public double getEspeces() {
		return especes;
	}

	public void setEspeces(double especes) {
		this.especes = especes;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public InetAddress getHote() {
		return hote;
	}

	public void setHote(InetAddress hote) {
		this.hote = hote;
	}

	public Socket getSc() {
		return sc;
	}

	public void setSc(Socket sc) {
		this.sc = sc;
	}

	public void Actualisation_connexion() throws IOException {
		BufferedReader in;
		PrintWriter out;
		in = new BufferedReader(new InputStreamReader(sc.getInputStream()));
		out = new PrintWriter(sc.getOutputStream(),true);
		out.println("nouveau client inscrit");
		out.println(nomCourtier);
		
	}
	public void EnvoyerCommandeBourse(Commande com) throws IOException {
//		BufferedReader in;
//		PrintWriter out;
//		in = new BufferedReader(new InputStreamReader(sc.getInputStream()));
//		out = new PrintWriter(sc.getOutputStream(),true);
//		out.println("nouveau client inscrit");
//		out.println(nomCourtier);
		
	}
	
	public void ActualisationClients() {
		
		
		
	}
	
	
	
	
	

	public static void main (String[] args){


	BufferedReader in;
	PrintWriter out;
	Scanner scan;
	String rep;
	Courtier courtier =new Courtier();

	try{
		if (args.length>=2){
			courtier.hote= InetAddress.getByName(args[0]);
			courtier.port=Integer.parseInt(args[1]);
		}
		else{
			courtier.hote = InetAddress.getLocalHost();
			courtier.port = 4020;
		}
	}
	catch(UnknownHostException e){
		System.err.println("Machine inconnue :" +e);
	}
	try{

	System.out.println ("connexion a la bourse");
	courtier.sc = new Socket(courtier.hote,courtier.port);
	System.out.println ("le courtier est connecte a la bourse");

	in = new BufferedReader(new InputStreamReader(courtier.sc.getInputStream()));
	out = new PrintWriter(courtier.sc.getOutputStream(),true);
	System.out.println ("taper 1 pour s'inscrire");
    scan = new Scanner(System.in);
    rep= scan.nextLine().trim();
	out.println(rep);//envoi de la reponse a la bourse

	String message=in.readLine();//recupere le message de la bourse
	System.out.println (message);


	scan = new Scanner(System.in);
	String nom= scan.nextLine().trim();

	out.println(nom);
	courtier.nomCourtier = Integer.parseInt(nom);

	String mess=in.readLine();
	System.out.println (mess);

	

		if (mess.equals("inscription courtier terminee")){
			System.out.println ("match");
			ServeurCourtier serveurCourtier = new ServeurCourtier(4000,courtier);
			serveurCourtier.start();
		}
		
	







	}
	catch(IOException e){
		System.err.println("Impossible de creer la socket du courtier : " +e);
	}
		/*finally{
			try{
				courtier.sc.close();
			}
			catch (IOException e){}
		}*/
	}

	public String getEtat() {
		return etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}

	
}
