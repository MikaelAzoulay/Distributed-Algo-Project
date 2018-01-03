import java.util.ArrayList;

public class Societe {
	private String nomSociete;
	private int prix; //prix actuel du stock
	private int nbActionFlottantes;
	private ArrayList<Commande> commandes;
	
	public String getNomSociete() {
		return nomSociete;
	}
	public void setNomSociete(String nomSociete) {
		this.nomSociete = nomSociete;
	}
	public int getPrix() {
		return prix;
	}
	public void setPrix(int prix) {
		this.prix = prix;
	}
	public int getNbActionFlottantes() {
		return nbActionFlottantes;
	}
	public void setNbActionFlottantes(int nbActionFlottantes) {
		this.nbActionFlottantes = nbActionFlottantes;
	}
	public ArrayList<Commande> getCommandes() {
		return commandes;
	}
	public void setCommandes(ArrayList<Commande> commandes) {
		this.commandes = commandes;
	}
	
	//private int[] stock; // stock[0] = prix actuel stock[1] nbr d'action flottante

}
