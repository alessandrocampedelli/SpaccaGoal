package classi;

public class Codice 
{
	private String codice;
	private boolean nuovoCarica;
	
	public Codice(String codice, boolean nuovoCarica)
	{
		this.codice = codice;
		this.nuovoCarica = nuovoCarica;
	}
	
	public String getCodice() 
	{
		return this.codice;
	}
	public boolean getNuovoCarica() 
	{
		return this.nuovoCarica;
	}
}