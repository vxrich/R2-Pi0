package com.example.juanito.r2_pi0;

/**
 * Created by Riccardo on 14/01/17.
 */

public class VocalTranslator {

    private BluetoothCommunication blue;

    public VocalTranslator (BluetoothCommunication blue)
    {
        this.blue = blue;
    }

    private String comandi[] = {"raggiungimi", "reach me", "zitto", "shut up", "destra", "sinistra", "left", "right", "vieni", "come", "seguimi", "follow", "gira a destra", "gira a sinistra", "turn left", "turn right" };

    public void translate (String text)
    {
        for(int i=0; i < comandi.length; i++)
        {
            if(text.toLowerCase().contains(comandi[i]))
            {
                switch(comandi[i])
                {
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

                    default: blue.makeSound("errore");  // Inserire il nome del suono di errore
                }
            }

        }
    }

}
