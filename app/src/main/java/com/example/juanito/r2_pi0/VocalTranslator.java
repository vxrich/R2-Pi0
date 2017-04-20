package com.example.juanito.r2_pi0;

/**
 * Created by Riccardo on 14/01/17.
 */

// Classe per la ricerca delle parole chiave nella stringa prodotta dal riconoscimento vocale
public class VocalTranslator {

    private BluetoothCommunication blue;

    public VocalTranslator (BluetoothCommunication blue)
    {
        this.blue = blue;
    }

    // Stringhe delle parole che vengono cercate
    private String comandi[] = {"raggiungimi", "reach me", "zitto", "shut up", "destra", "sinistra", "left", "right", "vieni", "come", "seguimi", "follow", "gira a destra", "gira a sinistra", "turn left", "turn right", "proiettore", "proietta", "project" };

    public void translate (String text)
    {
        for(int i=0; i < comandi.length; i++)
        {
            if(text.toLowerCase().contains(comandi[i]))
            {
                switch(comandi[i])
                {

                    case "proiettore":
                    case "proietta":
                    case "project":
                    {
                        blue.project();
                        break;
                    }


                    case "vieni":
                    case "come":
                    case "seguimi":
                    case "follow":
                    {
                        blue.follow();
                        break;
                    }

                    case "reach me":
                    case "raggiungimi":
                    {
                        blue.reach();
                        break;
                    }

                    case "destra":
                    case "right":
                    case "turn right":
                    {
                        blue.rotate(30);
                        break;
                    }

                    case "sinistra":
                    case "left":
                    case "turn left":
                    {
                        blue.rotate(-30);
                        break;
                    }

                    case "shut up":
                    case "zitto":
                    {
                        blue.shutup();
                        break;
                    }

                    // Se non vengono trovate parole viene inviato un comando per far suonare da errore il robot
                    default: blue.makeSound("errore");
                }
            }

        }
    }

}
