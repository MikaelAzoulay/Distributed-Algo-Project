
public class Commande {

	
	private double prix;
	private String choix; // soit achat soit vente (achat 0 ou vente 1)
	private int nbActions;
	private String nomSociete;
	private int nomClient;
	private int nomCourtier;
	
	
	
	
	public Commande() {
		
	}
	public double getPrix() {
		return prix;
	}
	public void setPrix(double prix) {
		this.prix = prix;
	}
	public String getChoix() {
		return choix;
	}
	public void setChoix(String choix) {
		this.choix = choix;
	}
	public int getNbActions() {
		return nbActions;
	}
	public void setNbActions(int nbActions) {
		this.nbActions = nbActions;
	}
	public String getNomSociete() {
		return nomSociete;
	}
	public void setNomSociete(String nomSociete) {
		this.nomSociete = nomSociete;
	}
	public int getNomClient() {
		return nomClient;
	}
	public void setNomClient(int nomClient) {
		this.nomClient = nomClient;
	}
	public int getNomCourtier() {
		return nomCourtier;
	}
	public void setNomCourtier(int nomCourtier) {
		this.nomCourtier = nomCourtier;
	}
	
	
	
}
