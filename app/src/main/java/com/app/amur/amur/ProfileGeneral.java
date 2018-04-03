package com.app.amur.amur;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.amur.amur.adapters.Upload;
import com.app.amur.amur.adapters.UserInformation;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


/**
 * Created by Digriz on 05.12.2017.
 */

public class ProfileGeneral extends AppCompatActivity implements View.OnClickListener {

    EditText editTextForAge;
    LinearLayout linearLayoutForGallery;


    Button btnSave;
    int lengthForChecking;
    int yearForChecking;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;

    private DatabaseReference myRef;
    private DatabaseReference refForGetAllDataFromDB;
    private DatabaseReference refForSaveLinksOfPhotos;
    private DatabaseReference photoGetsFirebaseDatabase;

    //Переменные для сохранения данных из полей
    int yearsOld; // Для возвраста
    String name; // Для имени
    int lengthOfMan;  // Для роста
    int weight; // Для веса
    String colorOfHair; // Для цвета волос
    String bodyForm; // Для телосложения
    String national; // Для национальности
    String sex; // Для пола (м\ж)
    String lookingFor; // Виды отношений из Спиннера
    String positionOnMap; // Для сохранения позиции по карте
    String orientationOfPeople; // Виды ориентация для человека из Спиннера
    String aboutYourSelf; // Информация о пользователе
    //FirebaseListAdapter mAdapter;


    ImageView changePhoto;
    private static final String TAG = "AddToDatabase";


    // Инициализация вьюшек для карты
    TextView txtLocationAddress;
    SupportMapFragment mapFragment;
    GoogleMap map;
    LatLng center;
    CardView cardView;

    //Наши поля
    EditText nameEditText;
    EditText lengthOfManEditText;
    EditText weightEditText;
    EditText colorOfHairEditText;
    EditText bodyFormEditText;
    EditText nationalEditText;
    EditText edAboutYourself;
    RadioButton radioBtnWoman;
    RadioButton radioBtnMan;
    ImageView imageAddPhoto;
    CardView firstCard;
    ArrayList<CardView> listOfCards = new ArrayList<>();

    // SheredPreference file
    public static final String APP_PREFERENCES = "mysettings";
    SharedPreferences mSettings;
    public static final String APP_PREFERENCES_NAME = "Name";
    public static final String APP_PREFERENCES_OLD = "Years";
    public static final String APP_PREFERENCES_LENGTH = "Length";
    public static final String APP_PREFERENCES_WEIGHT = "Weight";
    public static final String APP_PREFERENCES_HAIR = "Hair";
    public static final String APP_PREFERENCES_BODY = "Body";
    public static final String APP_PREFERENCES_NATIONAL = "National";
    public static final String APP_PREFERENCES_SEX = "Sex";
    public static final String APP_PREFERENCES_LOOKING_FOR = "LookingFor";
    public static final String APP_PREFERENCES_MAP = "Map";
    public static final String APP_PREFERENCES_ORIENTATION = "Orientation";
    public static final String APP_PREFERENCES_ABOUT = "About";

    // Данные для СтораджФ Загрузка
    private static final int SELECT_PHOTO = 100;
    Uri selectedImage;
    FirebaseStorage storage;
    StorageReference ref;

    StorageReference storageRef, imageRef;
    ProgressDialog progressDialog;
    UploadTask uploadTask;
    ArrayList<Image> images;
    ArrayList<String> imagesOf;
    ArrayList<String> listOfImagesForLargeImage;
    ArrayList<Object> rayListForGetInfo;
    ImageView largeImage;
    CardView imageCard;
    FrameLayout backLargeImage;
    // Реквист код для карты
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    int i = 0;
    int count;
    UserInformation userInformation;
    Upload upload;
    ImageView dots;
    FirebaseUser user = mAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.profile_fragment);
        nameEditText = (EditText) findViewById(R.id.ed_name);
        lengthOfManEditText = (EditText) findViewById(R.id.ed_length);
        weightEditText = (EditText) findViewById(R.id.ed_weight);
        colorOfHairEditText = (EditText) findViewById(R.id.ed_color_hair);
        bodyFormEditText = (EditText) findViewById(R.id.ed_tall);
        nationalEditText = (EditText) findViewById(R.id.ed_national);
        radioBtnWoman = (RadioButton) findViewById(R.id.radioBtnWoman);
        radioBtnMan = (RadioButton) findViewById(R.id.radioBtnMan);
        radioBtnWoman.setOnClickListener(radioButtonClickListener);
        radioBtnMan.setOnClickListener(radioButtonClickListener);
        imageAddPhoto = (ImageView) findViewById(R.id.image_add_photo);
        imageAddPhoto.setOnClickListener(addPhotoButton);
        linearLayoutForGallery = (LinearLayout) findViewById(R.id.linear_layout_for_gallery);
        firstCard = (CardView) findViewById(R.id.card_first);
        largeImage = (ImageView) findViewById(R.id.large_image);

        backLargeImage = (FrameLayout) findViewById(R.id.back_large_image);
        editTextForAge = (EditText) findViewById(R.id.ed_calendar);
        btnSave = (Button) findViewById(R.id.btnSave);
        progressDialog = new ProgressDialog(this);
        edAboutYourself = (EditText) findViewById(R.id.ed_about_yourself);


        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);


        //Установка кастомного экшен бара
        Window window = this.getWindow();
        //Установка: не показывать клавиатуру при запуски активити
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //Календарь Пикер
        TextViewDatePicker editTextDatePicker = new TextViewDatePicker(ProfileGeneral.this, editTextForAge);
        // Загружаем наши фотографии с ФБ
        mFirebaseDatabase = FirebaseDatabase.getInstance();

// Получаем все сохраненые записи о человеке и выводим в поля ЕдитТекс
        refForGetAllDataFromDB = mFirebaseDatabase.getReference().child("users").child(user.getUid());
        progressDialog.setTitle("Загрузка данных...");
        progressDialog.show();
        refForGetAllDataFromDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Проверка если впервые пользователь зашел и записи о его аккаунте еще нету
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data.exists()) {


                        edAboutYourself.setText(dataSnapshot.child("aboutYourSelf").getValue().toString());
                        nationalEditText.setText(dataSnapshot.child("national").getValue().toString());
                        bodyFormEditText.setText(dataSnapshot.child("bodyForm").getValue().toString());
                        colorOfHairEditText.setText(dataSnapshot.child("colorOfHair").getValue().toString());
                        weightEditText.setText(dataSnapshot.child("weight").getValue().toString());
                        lengthOfManEditText.setText(dataSnapshot.child("lengthOfMan").getValue().toString());
                        nameEditText.setText(dataSnapshot.child("name").getValue().toString());
                        progressDialog.dismiss();


                    } else {
                        Log.e(" null------", "---------------");
                        progressDialog.dismiss();
                    }
                }


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });


        myRef = mFirebaseDatabase.getReference();
        // Реф для получения линков фото из ДБ ФБ
        photoGetsFirebaseDatabase = mFirebaseDatabase.getReference().child("linksOfPhotos").child(user.getUid());
        photoGetsFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                imagesOf = new ArrayList<String>();


                for (DataSnapshot keySnapshot : dataSnapshot.getChildren()) {

                    for (DataSnapshot photoUrlSnapshot : keySnapshot.getChildren()) {
                        String value = photoUrlSnapshot.getValue().toString();  // Получаем наш ключ записи

                        imagesOf.add(value);
                    }
                }

                for (i = 0; i < imagesOf.size(); i++) {
                    progressDialog.setTitle("Загрузка фотографий...");
                    progressDialog.show();


                    CardView imageCard = (CardView) getLayoutInflater().inflate(R.layout.item_main_gallery, null);
                    count = i;
                    listOfCards.add(imageCard);
                    imageCard.setTag(i);
                    imageCard.setOnClickListener(ProfileGeneral.this);

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(firstCard.getLayoutParams());
                    layoutParams.setMargins(10, 0, 0, 0);


                    final ImageView imageViewForImage = imageCard.findViewById(R.id.main_photo);

                    dots = imageCard.findViewById(R.id.add_dots);
                    dots.setTag(i);
                    dots.setOnClickListener(dotsClickListener);

                    linearLayoutForGallery.addView(imageCard, layoutParams);


                    Glide.with(ProfileGeneral.this).load(imagesOf.get(i)).into(imageViewForImage); // Загружаем полный урл в КардВью


                }
                progressDialog.dismiss();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // Реф для сохранения линков фото в ДБ ФБ
        refForSaveLinksOfPhotos = mFirebaseDatabase.getReference().child("linksOfPhotos").child(user.getUid());
        //accessing the firebase storage
        storage = FirebaseStorage.getInstance();
        //creates a storage reference
        storageRef = storage.getReference();


        //Spinner`s
// Получаем экземпляр элемента Spinner
        final Spinner spinnerLookingFor = (Spinner) findViewById(R.id.spinner);
        final Spinner spinnerOrientation = (Spinner) findViewById(R.id.spinner_goal);
// Настраиваем адаптер
        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(this, R.array.target_dating, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<?> adapterForOrientation =
                ArrayAdapter.createFromResource(this, R.array.target_orientation, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Вызываем адаптер
        spinnerLookingFor.setAdapter(adapter);
        spinnerLookingFor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                String[] choose = getResources().getStringArray(R.array.target_dating);

                //          "Ваш выбор: " + choose[selectedItemPosition], Toast.LENGTH_SHORT);
                lookingFor = choose[selectedItemPosition];

            }

            public void onNothingSelected(AdapterView<?> parent) {
                lookingFor = "Общение";
            }
        });
        spinnerOrientation.setAdapter(adapterForOrientation);
        spinnerOrientation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                String[] choose = getResources().getStringArray(R.array.target_orientation);

                //          "Ваш выбор: " + choose[selectedItemPosition], Toast.LENGTH_SHORT);
                orientationOfPeople = choose[selectedItemPosition];

            }

            public void onNothingSelected(AdapterView<?> parent) {
                orientationOfPeople = "Гетеро";
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lengthOfManEditText.getText().toString().equals("") || nameEditText.getText().toString().equals("") || editTextForAge.getText().toString().equals("") || weightEditText.getText().toString().equals("") || colorOfHairEditText.getText().toString().equals("") || bodyFormEditText.getText().toString().equals("") || nationalEditText.getText().toString().equals("") || txtLocationAddress.getText().toString().equals("") || sex.equals("") || orientationOfPeople.equals("") || edAboutYourself.getText().toString().equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileGeneral.this);
                    builder.setTitle("Заполните все поля")
                            .setMessage("Заполните все поля и выберите ваше текущее местоположение")
                            .setIcon(R.drawable.ic_pencil)
                            .setCancelable(false)
                            .setNegativeButton("ОК",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();


                } else {

                    CharSequence foo = editTextForAge.getText();
                    String bar = foo.toString();
                    String desiredString = bar.substring(0, 4);
                    yearForChecking = Integer.parseInt(desiredString.toString());
                    Calendar today = Calendar.getInstance();
                    yearsOld = today.get(Calendar.YEAR) - yearForChecking;


                    // Сохранение в переменных данных с полей
                    name = nameEditText.getText().toString();
                    lengthOfMan = Integer.valueOf(lengthOfManEditText.getText().toString());
                    weight = Integer.valueOf(weightEditText.getText().toString());
                    colorOfHair = colorOfHairEditText.getText().toString();
                    bodyForm = bodyFormEditText.getText().toString();
                    national = nationalEditText.getText().toString();
                    positionOnMap = txtLocationAddress.getText().toString();
                    aboutYourSelf = edAboutYourself.getText().toString();

                    SharedPreferences.Editor editor = mSettings.edit();
                    editor.putString(APP_PREFERENCES_NAME, name);
                    editor.putString(APP_PREFERENCES_LENGTH, Integer.valueOf(lengthOfMan).toString());
                    editor.putString(APP_PREFERENCES_WEIGHT, Integer.valueOf(weight).toString());
                    editor.putString(APP_PREFERENCES_HAIR, colorOfHair);
                    editor.putString(APP_PREFERENCES_BODY, bodyForm);
                    editor.putString(APP_PREFERENCES_NATIONAL, national);
                    editor.putString(APP_PREFERENCES_MAP, positionOnMap);
                    editor.putString(APP_PREFERENCES_ABOUT, aboutYourSelf);
                    editor.putString(APP_PREFERENCES_OLD, Integer.valueOf(yearsOld).toString());
                    editor.putString(APP_PREFERENCES_ORIENTATION, orientationOfPeople);
                    editor.putString(APP_PREFERENCES_SEX, sex);
                    editor.putString(APP_PREFERENCES_LOOKING_FOR, lookingFor);

                    editor.apply();


                    myRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(user.getUid()).exists()) {
                                String DateOfBirthday = editTextForAge.getText().toString(); //Получение даты рождения
                                String userId = user.getUid();
                                userInformation = new UserInformation(DateOfBirthday, yearsOld, name, lengthOfMan, weight, colorOfHair, bodyForm, national, sex, lookingFor, positionOnMap, orientationOfPeople, aboutYourSelf, userId);
                                myRef.child("users").child(user.getUid()).setValue(userInformation);


                            } else {
                                String DateOfBirthday = editTextForAge.getText().toString(); //Получение даты рождения
                                String userId = user.getUid();
                                userInformation = new UserInformation(DateOfBirthday, yearsOld, name, lengthOfMan, weight, colorOfHair, bodyForm, national, sex, lookingFor, positionOnMap, orientationOfPeople, aboutYourSelf, userId);
                                myRef.child("users").child(user.getUid()).setValue(userInformation);

                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {


                            //Key exists
                            myRef.child("users").child(user.getUid()).setValue(userInformation);


                        }


                    });


                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileGeneral.this);
                    builder.setTitle("Ваши данные сохранены")
                            .setMessage("Ваши данные успешно записаны, сейчас вас перенаправим на главную")
                            .setIcon(R.drawable.ic_good)
                            .setCancelable(false)
                            .setNegativeButton("ОК",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent intent = new Intent(ProfileGeneral.this, MainActivity.class);


                                            startActivity(intent);
                                            finish();


                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();


                }


            }
        });


// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorAccent));

        // КАРТА
        txtLocationAddress = findViewById(R.id.txtLocationAddress);
        txtLocationAddress.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        txtLocationAddress.setSingleLine(true);
        txtLocationAddress.setMarqueeRepeatLimit(-1);
        txtLocationAddress.setSelected(true);
        txtLocationAddress.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));

        cardView = findViewById(R.id.cardView);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(ProfileGeneral.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    printToast("Google Play Service Repair");
                } catch (GooglePlayServicesNotAvailableException e) {
                    printToast("Google Play Service Not Available");
                }
            }
        });

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                map.getUiSettings().setZoomControlsEnabled(true);
                LatLng latLng = new LatLng(20.5937, 78.9629);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                initCameraIdle();
            }


        });
    }

    //Слушатель ПоПАпМеню для трех точек
    View.OnClickListener dotsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showPopupMenu(v);
        }
    };

    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(ProfileGeneral.this, v);
        popupMenu.inflate(R.menu.menu_settings_photo);

        final int position = Integer.valueOf(v.getTag().toString());

        popupMenu
                .setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // Toast.makeText(PopupMenuDemoActivity.this,
                        // item.toString(), Toast.LENGTH_LONG).show();
                        // return true;
                        switch (item.getItemId()) {

                            case R.id.delete_photo_menu:
                                linearLayoutForGallery.removeView(listOfCards.get(position));
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                Query applesQuery = ref.child("linksOfPhotos").child(user.getUid()).orderByChild("url").equalTo(imagesOf.get(position));

                                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                            appleSnapshot.getRef().removeValue();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.e(TAG, "onCancelled", databaseError.toException());
                                    }
                                });


                                return true;

                            default:
                                return false;
                        }
                    }
                });

        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {

            @Override
            public void onDismiss(PopupMenu menu) {


            }
        });
        popupMenu.show();
    }


    //Cлушатель для радио кнопок
    View.OnClickListener radioButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RadioButton rb = (RadioButton) v;
            switch (rb.getId()) {
                case R.id.radioBtnMan:
                    sex = "Мужчина";
                    break;
                case R.id.radioBtnWoman:
                    sex = "Женщина";
                    break;

                default:
                    break;
            }
        }
    };


    //Слушатель добавить фото
    View.OnClickListener addPhotoButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ImagePicker.create(ProfileGeneral.this) // Activity or Fragment
                    .start(2);


        }
    };


    // МЕТОДЫ КАРТЫ
    private void initCameraIdle() {
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                // TODO Auto-generated method stub

                // lstLatLngs.add(point);
                map.clear();
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker);
                map.addMarker(new MarkerOptions().position(point).icon(icon));


                getAddressFromLocation(point.latitude, point.longitude);

            }
        });
    }


    private void getAddressFromLocation(double latitude, double longitude) {

        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);


        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses.size() > 0) {
                Address fetchedAddress = addresses.get(0);
                StringBuilder strAddress = new StringBuilder();
                for (int i = 0; i < fetchedAddress.getMaxAddressLineIndex(); i++) {
                    strAddress.append(fetchedAddress.getAddressLine(i)).append(" ");
                }

                txtLocationAddress.setText(strAddress.toString());


            } else {
                txtLocationAddress.setText("Searching Current Address");
            }

        } catch (IOException e) {
            e.printStackTrace();
            printToast("Could not get address..!");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                if (!place.getAddress().toString().contains(place.getName())) {
                    txtLocationAddress.setText(place.getName() + ", " + place.getAddress());
                } else {
                    txtLocationAddress.setText(place.getAddress());
                }

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 16);
                map.animateCamera(cameraUpdate);


            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                printToast("Error in retrieving place info");

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
        //Запуск добавления фото
        ArrayList<Image> images = new ArrayList<Image>();
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {

            images = (ArrayList<Image>) ImagePicker.getImages(data);
            final String userId = user.getUid();


            final Uri[] uri = new Uri[images.size()];

            for (i = 0; i < images.size(); i++) {
                progressDialog.setTitle("Загрузка фотографий...");
                progressDialog.show();

                uri[i] = Uri.parse("file://" + images.get(i).getPath());
                ref = storage.getReference("photos").child(user.getUid()).child(uri[i].getLastPathSegment());
                CardView imageCard = (CardView) getLayoutInflater().inflate(R.layout.item_main_gallery, null);
                listOfCards.add(imageCard);
                count++;

                imageCard.setTag(count);
                imageCard.setOnClickListener(ProfileGeneral.this);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(firstCard.getLayoutParams());
                layoutParams.setMargins(10, 0, 0, 0);
                dots = imageCard.findViewById(R.id.add_dots);
                dots.setTag(count);
                dots.setOnClickListener(dotsClickListener);

                final ImageView imageViewForImage = imageCard.findViewById(R.id.main_photo);

                linearLayoutForGallery.addView(imageCard, layoutParams);


                //-------------------------------------

                // final File  mypath =new File(Environment.getExternalStorageDirectory(),System.currentTimeMillis()+"_profile.jpg");
                ref.putFile(uri[i])
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                imagesOf.add(downloadUrl.toString());
                                // Загружаем наши картинки в КардВью
                                Glide.with(ProfileGeneral.this).load(downloadUrl).into(imageViewForImage);
                                // Сохраняем фотографии урлы в БД ФБ
                                upload = new Upload(downloadUrl.toString());

                                String key = refForSaveLinksOfPhotos.push().getKey();
                                refForSaveLinksOfPhotos.child(key).setValue(upload);


                                progressDialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                progressDialog.dismiss();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                //displaying the upload progress


                            }
                        });


            }


        }


    }


    private void printToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


    // Слушатель для фотографий, при нажатии показывает увеличенный размер
    @Override
    public void onClick(View v) {

        final int position = Integer.valueOf(v.getTag().toString());

        Log.e("POSITION", String.valueOf(position));


        largeImage.setVisibility(View.VISIBLE);
        backLargeImage.setVisibility(View.VISIBLE);


        Glide.with(ProfileGeneral.this).load(imagesOf.get(count)).into(largeImage);


        largeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                largeImage.setVisibility(View.GONE);
                backLargeImage.setVisibility(View.GONE);
            }
        });


    }
}