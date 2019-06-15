package com.example.price_comparison;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Klasa zarządza widokiem, przeznaczonym dla administratora.
 */
public class GodMode extends AppCompatActivity {
    /**
     * Metoda uruchamiana przy starcie obecnego activity.
     * @param savedInstanceState zapisane stan instancji
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_god_mode);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     * Metoda przekierowuje do skanera.
     * @param view widok
     */
    public void toScanner(View view){
        Intent intencja = new Intent(this, Scanner.class);
        startActivity(intencja);
        //Toast.makeText(getApplicationContext(), "Kliknięto przycisk Login", Toast.LENGTH_SHORT).show();
    }

    /**
     * Metoda przekierowuje do bazy danych.
     * @param view widok
     */
    public void toDatabase(View view){
        Intent intencja = new Intent(this, Database.class);
        startActivity(intencja);
        //Toast.makeText(getApplicationContext(), "Kliknięto przycisk Login", Toast.LENGTH_SHORT).show();
    }

    /**
     * Metoda przekierowuje do porównywarki cen.
     * @param view widok
     */
    public void toFindCheapest(View view){
        Intent intent = new Intent(this, FindCheapest.class);
        startActivity(intent);
    }

}
