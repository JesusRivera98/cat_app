package System;

import com.google.gson.Gson;
import com.squareup.okhttp.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class CatsService {
    public static void seeCats() throws IOException {
        //1. Will pull the data from the API
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.thecatapi.com/v1/images/search")
                .method("GET", null).build();
        Response response = client.newCall(request).execute();

        String theJson = response.body().string();

        //Cut the brackets
        theJson = theJson.substring(1, theJson.length()-1);

        //Create a Gson object
        Gson gson = new Gson();
        Cats cats = gson.fromJson(theJson, Cats.class);

        //Resize if necessary
        Image image = null;
        try {
            URL url = new URL(cats.getUrl());
            image = ImageIO.read(url);

            ImageIcon backgroundCat = new ImageIcon(image);

            if(backgroundCat.getIconWidth() > 800){
                //Resize
                Image background = backgroundCat.getImage();
                Image modified = background.getScaledInstance(
                        800, 600, Image.SCALE_SMOOTH);
                backgroundCat = new ImageIcon(modified);
            }

            String menu = "Options: \n"
                    + "1. See another image\n"
                    + "2. Favorite\n"
                    + "3. Back\n";

            String[] buttons = {"See another image", "Favorite", "Back" };
            String cat_id = cats.getId();
            String option = (String) JOptionPane.showInputDialog(
                    null, menu, cat_id, JOptionPane.INFORMATION_MESSAGE,
                    backgroundCat, buttons, buttons[0]);

            int selection = -1;
            //Validate which option selects the user
            for (int i = 0; i < buttons.length; i++) {
                if (option.equals(buttons[i])){
                    selection = i;
                }
            }
            System.out.println(selection);


            switch (selection){
                case 0:
                    System.out.println("see");
                    seeCats();
                    break;
                case 1:
                    System.out.println("fac");
                    favoriteCat(cats);
                    break;
                default:
                    break;
            }
        }catch (IOException e){
            System.out.println(e);
        }

    }
    public static void favoriteCat(Cats cat){
        System.out.println(cat.getApikey());
        try{
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\n\t\"image_id\":\""+cat.getId()+"\"\n}");
            Request request = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/favourites")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-key", cat.getApikey())
                    .build();
            Response response = client.newCall(request).execute();
            System.out.println(response);

        }catch(IOException e){
            System.out.println(e);
        }
    }

    public static void seeFavourites(String apiKey)throws IOException{

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.thecatapi.com/v1/favourites")
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("x-api-key", apiKey)
                .build();

        Response response = client.newCall(request).execute();

        // Save the string with the answer
        String theJson = response.body().string();

        //Crate the Gson object
        Gson gson = new Gson();

        FavCats[] catsArray = gson.fromJson(theJson,FavCats[].class);

        if(catsArray.length > 0){
            int min = 1;
            int max  = catsArray.length;
            int random = (int) (Math.random() * ((max-min)+1)) + min;
            int index = random-1;

            FavCats favCat = catsArray[index];

            //Resize if necessary
            Image image = null;
            try {
                URL url = new URL(favCat.image.getUrl());
                image = ImageIO.read(url);

                ImageIcon backgroundCat = new ImageIcon(image);

                if(backgroundCat.getIconWidth() > 800){
                    //Resize
                    Image background = backgroundCat.getImage();
                    Image modified = background.getScaledInstance(
                            800, 600, Image.SCALE_SMOOTH);
                    backgroundCat = new ImageIcon(modified);
                }

                String menu = "Options: \n"
                        + "1. See another image\n"
                        + "2. Delete favorite\n"
                        + "3. Back\n";

                String[] buttons = {"See another image", "Delete favorite", "Back" };
                String cat_id = favCat.getId();
                String option = (String) JOptionPane.showInputDialog(
                        null, menu, cat_id, JOptionPane.INFORMATION_MESSAGE,
                        backgroundCat, buttons, buttons[0]);

                int selection = -1;
                //Validate which option selects the user
                for (int i = 0; i < buttons.length; i++) {
                    if (option.equals(buttons[i])){
                        selection = i;
                    }
                }

                switch (selection){
                    case 0:
                        seeFavourites (apiKey);
                        break;
                    case 1:
                        deleteFavourite(favCat);
                        break;
                    default:
                        break;
                }
            }catch (IOException e){
                System.out.println(e);
            }
        }

    }

    public static void deleteFavourite(FavCats favCat){
        try{
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/favourites/"+favCat.getId()+"")
                    .delete(null)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-key", favCat.getApiKey())
                    .build();

            Response response = client.newCall(request).execute();
        }catch(IOException e){
            System.out.println(e);
        }

    }
}
