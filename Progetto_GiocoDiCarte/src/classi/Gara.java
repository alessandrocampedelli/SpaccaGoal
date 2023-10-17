package classi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

import application.FormPrincipaleController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
public abstract class Gara 
{
	protected Giocatore[] giocatori;
	protected String codice;
	protected Mazzo carte;

	public Gara(ArrayList<Giocatore> giocatori, String codice)
	{
		this.giocatori = giocatori.toArray(new Giocatore[giocatori.size()]);
		this.codice = codice;
	}
	public String getCodiceGara()
	{
		return this.codice;
	}

	public Giocatore[] getGiocatori()
	{
		return this.giocatori;
	}
	
	public Mazzo getMazzo() 
	{
		return carte;
	}

	public void setMazzo(Mazzo m) 
	{
		this.carte = m;
	}
	
}