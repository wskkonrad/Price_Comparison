package com.example.price_comparison;

import android.app.ListActivity;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.price_comparison.api.ApiController;
import com.example.price_comparison.api.ConcreteStore;
import com.example.price_comparison.api.Product;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
//import android.widget.Toolbar;

/**
 * Klasa służąca do zarządzania bazą danych z poziomu aplikacji.
 */
public class Database extends AppCompatActivity {

    private final String[] dropdownMenu = {"Produkty", "Sklepy"};

    private ArrayAdapter<String> arrayAdapter;
    private ArrayAdapter<String[]> arrayAdapter2;
    private ApiController apiController = new ApiController();  /**< Obiekt kontrolera bazy danych */
    private Spinner dropdown; /**< Służy do zarządzania listą rozwijaną */

    /**
     * \brief Metoda uruchamiana przy starcie obecnego activity.
     * Metoda uruchamiana przy starcie obecnego activity.
     * @param savedInstanceState zapisane stan instancji
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        setContentView(R.layout.activity_database);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dropdown = findViewById(R.id.spinner1);

        ArrayAdapter<String>adapter = new ArrayAdapter<String>(Database.this,
                android.R.layout.simple_spinner_item,dropdownMenu);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * \brief Metoda uruchamiana przy starcie obecnego activity.
             * Metoda uruchamiana przy starcie obecnego activity.
             * @param parent rodzic
             * @param view widok
             * @param position pozycja
             * @param id indeks
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dropdownSelectedItem(position);
            }

            /**
             * \brief Metoda uruchamiana, jeśli nie został wybrany żaden atrybut
             * Metoda uruchamiana, jeśli nie został wybrany żaden atrybut
             * @param parent rodzic
             */
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //setActionBar(toolbar);

    }

    /**
     * \brief Metoda rozwija listę wybranego atrybutu.
     * Metoda rozwija listę wybranego atrybutu.
     * @param position pozycja
     */
    private void dropdownSelectedItem(int position){
        ArrayList<String> text = new ArrayList<>();
        final ArrayList<String[]> text2 = new ArrayList<>();
        ListView listView = findViewById(R.id.listview_database);
        switch (position){
            case 0:
                text2.clear();
                //Toast.makeText(getApplicationContext(),"Pozycja: 0", Toast.LENGTH_SHORT).show();
                ArrayList<Product> products = new ArrayList<>();
                try {
                    products = apiController.getAllProducts();

                    for (Product product : products){
                        text2.add(new String[]{ product.getName(), product.getProducerCode()});
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                arrayAdapter2 = new ArrayAdapter<String[]>(this, android.R.layout.simple_list_item_2, android.R.id.text1, text2){

                    /**
                     * \brief Uzyskuje widok, który wyświetla dane w określonej pozycji w zestawie danych.
                     * Uzyskuje widok, który wyświetla dane w określonej pozycji w zestawie danych.
                     * @param position pozycja
                     * @param convertView stary widok, który można ponownie użyć
                     * @param parent Rodyic, do którego ten widok zostanie dołączony
                     * @return widok
                     */
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);

                        String[] entry = text2.get(position);
                        TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                        TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                        text1.setText(entry[0]);
                        text2.setText(entry[1]);

                        return view;
                    }
                };
                listView.setAdapter(arrayAdapter2);

                break;
            case 1:

                text2.clear();
                //Toast.makeText(getApplicationContext(),"Pozycja: 1", Toast.LENGTH_SHORT).show();
                ArrayList<ConcreteStore> stores = new ArrayList<>();
                try {
                    stores = apiController.getAllStores();

                    for (ConcreteStore store : stores){
                        text2.add(new String[] {store.getStoreName(), store.getCity()+", ul."+store.getStreet()});
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                arrayAdapter2 = new ArrayAdapter<String[]>(this, android.R.layout.simple_list_item_2, android.R.id.text1, text2){

                    /**
                     * \brief Uzyskuje widok, który wyświetla dane w określonej pozycji w zestawie danych.
                     * Uzyskuje widok, który wyświetla dane w określonej pozycji w zestawie danych.
                     * @param position pozycja
                     * @param convertView stary widok, który można ponownie użyć
                     * @param parent Rodyic, do którego ten widok zostanie dołączony
                     * @return widok
                     */
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);

                        String[] entry = text2.get(position);
                        TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                        TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                        text1.setText(entry[0]);
                        text2.setText(entry[1]);

                        return view;
                    }
                };
                listView.setAdapter(arrayAdapter2);
                break;
            case 2:
                //Toast.makeText(getApplicationContext(),"Pozycja: 2", Toast.LENGTH_SHORT).show();
                break;
        }

    }

}
