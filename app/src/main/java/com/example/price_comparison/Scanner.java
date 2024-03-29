package com.example.price_comparison;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

/**
 * Klasa implementująca działanie skanera.
 */
public class Scanner extends AppCompatActivity {

    private ArrayList<String> resultCodes;  /**< \brief tablica kodów wynikowych */
    private ArrayAdapter<String> arrayAdapter; /**< \brief  adapter  */
    boolean isShowingDialog = false;  /**< \brief Flaga okna dialogowego */
    ArrayList<Product> products = new ArrayList<>(); /**< \brief lista produktów  */

    //DialogBox
    private Dialog dialog; /**< \brief obiekt okna dialogowego */
    private int listIndex; /**< \brief indeks listy */
    private String barCodee; /**< \brief zeskanowany kod */
    private EditText dialogEditText; /**< \brief Pole Tekstowe wyświetlane w oknie dialogowym */
    private EditText dialogEditText2; /**< \brief Pole Tekstowe wyświetlane w oknie dialogowym */
    private EditText dialogEditText3; /**< \brief Pole Tekstowe wyświetlane w oknie dialogowym */
    private EditText dialogEditText4; /**< \brief Pole Tekstowe wyświetlane w oknie dialogowym */

    /**
     * \brief Metoda uruchamiana przy starcie obecnego Acrivity.
     * Metoda uruchamiana przy starcie obecnego Acrivity.
     * @param savedInstanceState zapisany stan intencji
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            /**
             * \brief Metoda wywoływana po kliknięciu w przycisk.
             * Metoda wywoływana po kliknięciu w przycisk.
             * @param view widok
             */
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),
                        "Zeskanuj kod kreskowy",
                        Toast.LENGTH_SHORT).show();
                scanBarcode(view);
            }
        });

        updateListItems();

    }


    //Update for List items

    /**
     * \brief Metoda służąca do zaktualizowania listy.
     * Metoda służąca do zaktualizowania listy.
     */
    public void updateListItems(){
        if( resultCodes == null){
            resultCodes = new ArrayList<>();
            resultCodes.add("3424324324"); //usunać WAŻNE!
            products.add(new Product("3424324324", "", 0.0, ""));
            ListView listView = findViewById(R.id.list);
            arrayAdapter = new ArrayAdapter<>(this, R.layout.scanner_list, resultCodes);
            listView.setAdapter(arrayAdapter);
            //Set clickable items in list -> click should show dialog bar for change name, code number, and other informations
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
               @Override
               public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                   //if(position==0){
                       createDialogForItem(resultCodes.get(position),position);
                   //}
               }
            });
        }
        else {
            ListView listView = findViewById(R.id.list);
            arrayAdapter = new ArrayAdapter<>(this, R.layout.scanner_list, resultCodes);
            listView.setAdapter(arrayAdapter);
        }
    }

    /**
     * \brief Metoda tworzy okno dialogowe ze szczegółami produktu.
     * Metoda tworzy okno dialogowe ze szczegółami produktu.
     * @param barCode zeskanowany kod produktu
     * @param index index
     */
    @SuppressLint("SetTextI18n")
    public void createDialogForItem(String barCode, final int index){
        barCodee= barCode;
        this.listIndex= index;
        dialog = new Dialog(Scanner.this);
        dialog.setTitle("Edycja Itemu:");
        dialog.setContentView(R.layout.scanner_dialog_box);
        TextView txtMessage = dialog.findViewById(R.id.txtmessage);
        txtMessage.setText("Update item");
        txtMessage.setTextColor(Color.parseColor("#ff2222"));
        //barcod
        dialogEditText = dialog.findViewById(R.id.txtinput);
        dialogEditText.setText(barCode);
        dialogEditText.setSelection(dialogEditText.getText().length());//sets cursor to end of editText
        //name
        dialogEditText2 = dialog.findViewById(R.id.txtinput2);
        dialogEditText2.setText(products.get(index).getName());
        dialogEditText2.setSelection(dialogEditText2.getText().length());//sets cursor to end of editText
        //price
        dialogEditText3 = dialog.findViewById(R.id.txtinput3);
        dialogEditText3.setText(products.get(index).getPrice().toString());
        dialogEditText3.setSelection(dialogEditText3.getText().length());//sets cursor to end of editText
        //store name
        dialogEditText4 = dialog.findViewById(R.id.txtinput4);
        dialogEditText4.setText(products.get(index).getStoreName());
        dialogEditText4.setSelection(dialogEditText4.getText().length());//sets cursor to end of editText

        onSaveInstanceState(dialog.onSaveInstanceState());
        Button btnDone= dialog.findViewById(R.id.btdone);

        //Listener for background click/touch. When clicked background, flag isShowingDialog take false
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            /**
             * \brief Metoda wywoływana podczas zamykania okna dialogowego.
             * Metoda wywoływana podczas zamykania okna dialogowego.
             * @param dialog okno dialogowe
             */
            @Override
            public void onDismiss(DialogInterface dialog) {
                isShowingDialog= false;
                dialog.dismiss();
            }
        });

        //Listener for Done button
        btnDone.setOnClickListener(new View.OnClickListener() {
            /**
             * \brief Metoda wywoływana po kliknięciu.
             * Metoda wywoływana po kliknięciu.
             * @param v widok
             */
            @Override
            public void onClick(View v) {
                resultCodes.set(index, dialogEditText.getText().toString());
                products.get(index).setAll(dialogEditText.getText().toString(), dialogEditText2.getText().toString(), Double.valueOf(dialogEditText3.getText().toString()), dialogEditText4.getText().toString());
                arrayAdapter.notifyDataSetChanged();
                isShowingDialog = false;
                dialog.dismiss();
            }
        });
        //Listener for Delete button
        Button btnDelete= dialog.findViewById(R.id.btdelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            /**
             * \brief Metoda wywoływana po kliknięciu.
             * Metoda wywoływana po kliknięciu.
             * @param v widok
             */
            @Override
            public void onClick(View v) {
                resultCodes.remove(index);
                products.remove(index);
                arrayAdapter.notifyDataSetChanged();
                isShowingDialog = false;
                dialog.dismiss();
            }
        });
        isShowingDialog = true;
        dialog.show();
    }

    /**
     * \brief Metoda wywoływana po wyjściu z danego okna.
     * Metoda wywoływana po wyjściu z danego okna.
     */
    @Override
    protected void onPause() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        super.onPause();
    }

    //Saves screen before rotate

    /**
     * \brief Metoda zapisuje dane z ekranu, kiedy ma zostać obrócony.
     * Metoda zapisuje dane z ekranu, kiedy ma zostać obrócony.
     * @param outState aktualny stan
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList("resultCodes", resultCodes);
        outState.putBoolean("IS_SHOWING_DIALOG", isShowingDialog);
        if(isShowingDialog){
            barCodee = dialogEditText.getText().toString();
        }
        outState.putString("barcodee", barCodee);
        outState.putInt("listIndex", listIndex);

        outState.putParcelableArrayList("products", products);

        super.onSaveInstanceState(outState);
    }

    //Reload screen after rotate

    /**
     * \brief Metoda odświeża ekran po obróceniu.
     * Metoda odświeża ekran po obróceniu.
     * @param savedInstanceState zapisany ostatni stan
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        resultCodes = savedInstanceState.getStringArrayList("resultCodes");
        products = savedInstanceState.getParcelableArrayList("products");

        isShowingDialog = savedInstanceState.getBoolean("IS_SHOWING_DIALOG", false);
        if(isShowingDialog){
            barCodee = savedInstanceState.getString("barcodee");
            listIndex = savedInstanceState.getInt("listIndex");
            createDialogForItem(barCodee, listIndex);
        }
        updateListItems();
    }

    /**
     * \brief Metoda wywołuje uruchomienie skanera.
     * Metoda wywołuje uruchomienie skanera.
     * @param view widok
     */
    public void scanBarcode(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    /**
     * \brief Metoda uruchamiana po zeskanowaniu kodu kreskowego.
     * Metoda uruchamiana po zeskanowaniu kodu kreskowego.
     * @param RequestCode kod żądania
     * @param ResultCode kod wynikowy
     * @param data dane
     */
    @Override
    public void onActivityResult(int RequestCode, int ResultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(RequestCode, ResultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.d("Scanner", "Cancelled scan");
                Toast.makeText(this, "Cancelled.", Toast.LENGTH_LONG).show();
            } else {
                Log.d("Scanner", "Scanned");
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();

                if( resultCodes == null) {
                    resultCodes = new ArrayList<>();
                    resultCodes.add(result.getContents());
                    products.add(new Product(result.getContents(), "", 0.00, ""));
                    updateListItems();
                }
                else {
                    resultCodes.add(result.getContents());
                    products.add(new Product(result.getContents(), "", 0.00, ""));
                    updateListItems();
                }
                //list();
            }
        }
    }

    /**
     * \brief Metoda dodaje opcje do menu, jeśli są potrzebne.
     * Metoda dodaje opcje do menu, jeśli są potrzebne.
     * @param menu menu
     * @return prawda
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * \brief Metoda wywoływana, jeśli opcje zostaną zasnaczone.
     * Metoda wywoływana, jeśli opcje zostaną zasnaczone.
     * @param item atrybut menu
     * @return zaznaczone pola
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch(item.getItemId()){
            case R.id.scanner_menu_opt1:
                resultCodes.clear();
                products.clear();
                arrayAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(),"Lista wyczyszczona", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
