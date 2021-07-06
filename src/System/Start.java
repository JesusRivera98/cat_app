package System;

import javax.swing.*;
import java.io.IOException;

public class Start {
    public static void main(String[] args) throws IOException {
        int menu_option = -1;
        String[] buttons = {"1. See cats", "2. See favourites", "3. Exit"};

        do {
            //Main menu
            String option = (String) JOptionPane.showInputDialog(null, "Cats Java", "Main Menu", JOptionPane.INFORMATION_MESSAGE,
                    null, buttons, buttons[0]);

            //Validate which options is selected by the user
            for (int i = 0; i < buttons.length; i++) {
                if (option.equals(buttons[i])){
                    menu_option = i;
                }
            }

            switch (menu_option){
                case 0:
                    CatsService.seeCats();
                    break;
                case 1:
                    Cats cat = new Cats();
                    CatsService.seeFavourites(cat.getApikey());
                    break;
                default:
                    break;
            }
        }while (menu_option != 2);
    }
}
