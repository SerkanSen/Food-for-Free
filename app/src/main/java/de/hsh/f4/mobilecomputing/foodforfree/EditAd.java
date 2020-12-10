package de.hsh.f4.mobilecomputing.foodforfree;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.hsh.f4.mobilecomputing.foodforfree.MyAds.EXTRA_AD_ID;
import static de.hsh.f4.mobilecomputing.foodforfree.MyAds.EXTRA_IMAGE_URL;

public class EditAd extends AppCompatActivity  {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference storageReference;
    ArrayList<String> sFilterOptions = new ArrayList<>();
    String [] categories = new String[7];
    String [] filterOptions = {"Vegetarisch", "Vegan", "Obst/Gemüse", "Konserven", "Gericht", "Süßes", "Snacks"};
    Button pUpdateAdBtn, pUploadAdPhotoBtn, pMakePic;
    Calendar calendar;
    String userId, adId, imageUrl;
    ImageView pAdPhoto;
    Uri imageUri;
    public static final String TAG = "TAG";
    //Falls man Bild resetten einbaut:
    //public static final String DEFAULT_URL = "https://firebasestorage.googleapis.com/v0/b/food-for-free-9663f.appspot.com/o/ads%2FDefault%20Bild.jpg?alt=media&token=57a564e3-006c-4146-b793-cf4346a8f07a";

    EditText pTitle, pDescription, pIngredients, pAmount;
    //Spinner pAmount;
    TextView pPickupLocation;
    CheckBox chBoxVeggie, chBoxVegan, chBoxFruitsVegs, chBoxCans, chBoxMeal, chBoxSweets, chBoxSnacks;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference adRef = db.collection("ads");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ad);


        pTitle = findViewById(R.id.editTitle);
        pDescription = findViewById(R.id.editDescription);
        pIngredients = findViewById(R.id.editIngredients);
        pAmount = findViewById(R.id.editAmount);

        pPickupLocation = findViewById(R.id.pickupLocation);
        chBoxVeggie = findViewById(R.id.chBoxVeggie);
        chBoxVegan = findViewById(R.id.chBoxVegan);
        chBoxFruitsVegs = findViewById(R.id.chBoxFruitsVegs);
        chBoxCans = findViewById(R.id.chBoxCans);
        chBoxMeal = findViewById(R.id.chBoxMeal);
        chBoxSweets = findViewById(R.id.chBoxSweets);
        chBoxSnacks = findViewById(R.id.chBoxSnacks);
        pUpdateAdBtn = findViewById(R.id.placeAdBtn);
        pAdPhoto = findViewById(R.id.adPhoto);
        pUploadAdPhotoBtn = findViewById(R.id.uploadAdPhotoBtn);
        pMakePic = findViewById(R.id.makePic);
/*
        title = findViewById(R.id.adDetails_title);
        pickupLocation = findViewById(R.id.adDetails_pickupLocation);
        description = findViewById(R.id.adDetails_description);
        amount = findViewById(R.id.adDetails_amount);
        ingredients = findViewById(R.id.adDetails_ingredients);
        filterOptions = findViewById(R.id.adDetails_filterOptions);*/
        //image = findViewById(R.id.adDetails_image);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        userId = fAuth.getCurrentUser().getUid();

        //get Intent and adId+imageUrl from clicked itemview
        Intent intent = getIntent();
        adId = intent.getStringExtra(EXTRA_AD_ID);
        imageUrl = intent.getStringExtra(EXTRA_IMAGE_URL);

        //Darstellung der bisherigen Informationen:
        //get Image from storage/ads
        Picasso.get()
                .load(imageUrl)
                .fit()
                .centerCrop()
                .into(pAdPhoto);

        //get document information
        DocumentReference documentReference = fStore.collection("ads").document(adId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                pTitle.setText(documentSnapshot.getString("title"));
                pDescription.setText(documentSnapshot.getString("description"));
                pAmount.setText(documentSnapshot.getString("amount"));
                pIngredients.setText(documentSnapshot.getString("ingredients"));
                pPickupLocation.setText(documentSnapshot.getString("pickupLocation"));
                //List<String> test = documentSnapshot.get("categories");
                //Checkboxen evtl. inkludieren
                //filterOptions = documentSnapshot.get("categories");
                /*Map<String, Object> map = documentSnapshot.getData();
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    if (entry.getKey().equals("categories")) {
                        Log.d("TAG", entry.getValue().toString());
                        Toast.makeText(EditAd.this, "Hallo", Toast.LENGTH_SHORT).show();
                    }
                }*/

                //for(int i = 0; i < 7; i++){
                    /*switch(adRef.whereArrayContains("categories", filterOptions[i])){

                    }*/


                    /*if(adRef.whereArrayContains("categories","Vegetarisch")){

                    }*/
                // }

            }
        });



        pUploadAdPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open gallery
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);

            }
        });
        pMakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,61);
            }
        });

        pUpdateAdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = pTitle.getText().toString().trim();
                String description = pDescription.getText().toString().trim();
                String ingredients = pIngredients.getText().toString().trim();
                String amount = pAmount.getText().toString().trim();
                String pickupLocation = pPickupLocation.getText().toString().trim();
                String filterOptions;
                List<String> filterCat = Arrays.asList(categories);

                calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String timestamp = simpleDateFormat.format(calendar.getTime());

                if(TextUtils.isEmpty(title)){
                    pTitle.setError("Titel wird benötigt.");
                    return;
                }
                if(TextUtils.isEmpty(description)){
                    pDescription.setError("Bitte gib eine kurze Beschreibung ein.");
                    return;
                }
                if(TextUtils.isEmpty(amount)){
                    pAmount.setError("Bitte gib die Portion/en an.");
                    return;
                }
                if(amount.equals("0")){
                    pAmount.setError("Min. eine Portion (1)");
                    return;
                }
                if(TextUtils.isEmpty(ingredients)){
                    pIngredients.setError("Min. eine Zutat wird benötigt. Bitte gib für Allergiker relevante Zutaten unbedingt an!");
                    return;
                }

                //filterOptions as String
                StringBuilder stringBuilder = new StringBuilder();
                for (String s : sFilterOptions)
                    stringBuilder.append("- ").append(s).append("\n");
                filterOptions = stringBuilder.toString();

                userId = fAuth.getCurrentUser().getUid();

                //überschreiben/updaten des bisherigen Dokumentes über die übergebene adId
                DocumentReference documentReference = fStore.collection("ads").document(adId);
                Map<String, Object> ad = new HashMap<>();

                documentReference.update(ad);
                ad.put("title", title);
                ad.put("description", description);
                ad.put("ingredients", ingredients);
                ad.put("amount", amount);
                ad.put("timestamp", timestamp);
                ad.put("pickupLocation", pickupLocation);
                ad.put("filterOptions", filterOptions);
                ad.put("categories", filterCat);

                //falls neues Bild: Bild hochladen
                if(imageUri!=null){
                    uploadImageToFirebase(imageUri);
                }
                documentReference.update(ad).addOnSuccessListener((OnSuccessListener) (aVoid) -> {
                    Toast.makeText(EditAd.this,"Anzeige erfolgreich aktualisiert!", Toast.LENGTH_SHORT).show();
                    //Log.d(TAG, "onSuccess: Anzeige erfolgreich erstellt!");
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.toString());
                    }
                });
                startActivity(new Intent(getApplicationContext(), MyAds.class));

            }

        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                imageUri = data.getData();
                Picasso.get()
                        .load(imageUri)
                        .fit()
                        .centerCrop()
                        .into(pAdPhoto);
            }
        } else if (requestCode == 61 && resultCode == Activity.RESULT_OK) {          //Übergabe Foto an pAdPhoto
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");                   //habe viele Variationen mit dieser und in kombbination deiner variante drüber probiert, Casting in Uri,direkt als Uri

            pAdPhoto.setImageBitmap(bitmap);
            //imageUri = data.getExtras().get("data");
            //pAdPhoto.setImageURI(imageUri);
        }
    }

    public void onBackPressed(){
        return;
    }



    //uploads image to Firebase Storage "ads/adId/adPhoto"
    private void uploadImageToFirebase(Uri imageUri) {
        StorageReference fileRef = storageReference.child("ads/"+ adId +"/adPhoto.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get()
                                .load(uri)
                                .fit()
                                .centerCrop()
                                .into(pAdPhoto);
                        //imageUrl im Dokument speichern
                        DocumentReference documentReference = fStore.collection("ads/").document(adId);
                        documentReference
                                .update("imageUrl", uri.toString())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //Log.d(TAG, "DocumentSnapshot successfully updated!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Url konnte nicht gespeichert werden", e);
                                    }
                                });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditAd.this, "Bild hochladen fehlgeschlagen.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Checkboxen: wenn angeklickt, dann füge der ArrayList die entsprechende Kategorie hinzu
    public void selectItem(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()){
            case R.id.chBoxVeggie:
                if(checked){
                    sFilterOptions.add("Vegetarisch");
                    categories[0] = "Vegetarisch";
                }
                else {
                    sFilterOptions.remove("Vegetarisch");
                    categories[0] = "";
                }
                break;
            case R.id.chBoxVegan:
                if(checked){
                    sFilterOptions.add("Vegan");
                    categories[1] = "Vegan";
                }
                else {
                    sFilterOptions.remove("Vegan");
                    categories[1] = "";
                }
                break;
            case R.id.chBoxFruitsVegs:
                if(checked){
                    sFilterOptions.add("Obst/Gemüse");
                    categories[2] = "Obst/Gemüse";
                }
                else {
                    sFilterOptions.remove("Obst/Gemüse");
                    categories[2] = "";
                }
                break;
            case R.id.chBoxCans:
                if(checked){
                    sFilterOptions.add("Konserven");
                    categories[3] = "Konserven";
                }
                else {
                    sFilterOptions.remove("Konserven");
                    categories[3] = "";
                }
                break;
            case R.id.chBoxMeal:
                if(checked){
                    sFilterOptions.add("Gericht");
                    categories[4] = "Gericht";
                }
                else {
                    sFilterOptions.remove("Gericht");
                    categories[4] = "";
                }
                break;
            case R.id.chBoxSweets:
                if(checked){
                    sFilterOptions.add("Süßes");
                    categories[5] = "Süßes";
                }
                else {
                    sFilterOptions.remove("Süßes");
                    categories[5] = "";
                }
                break;
            case R.id.chBoxSnacks:
                if(checked){
                    sFilterOptions.add("Snacks");
                    categories[6] = "Snacks";
                }
                else {
                    sFilterOptions.remove("Snacks");
                    categories[6] = "";
                }
                break;
        }
    }

    public void ClickClose(View view){
        //Initialze Alert Dialog
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        //Set title
        builder.setTitle("Entwurf verwerfen");
        //Set message
        builder.setMessage("Wenn du die Seite verlässt, wird der Entwurf verworfen. Willst du fortfahren?");
        //positive button
        builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getApplicationContext(), MyAds.class));
            }
        });
        //negative button
        builder.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}