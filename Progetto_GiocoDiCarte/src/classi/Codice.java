package classi;

public class Codice 
{
	private String codice;
	private boolean nuovoCarica;
	
	public Codice(String codice)
	{
		this.codice = codice;
		this.nuovoCarica = true;
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