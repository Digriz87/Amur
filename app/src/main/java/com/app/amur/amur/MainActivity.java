package com.app.amur.amur;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


import com.app.amur.amur.adapters.ExpandableListAdapter;
import com.app.amur.amur.adapters.RecyclerViewForShowAllUsersAdapter;
import com.app.amur.amur.adapters.UserInformation;
import com.app.amur.amur.splashScreensSignInAndSignOut.SplashScreenEnterActivity;
import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.model.Image;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import devlight.io.library.ntb.NavigationTabBar;

import static com.app.amur.amur.ProfileGeneral.APP_PREFERENCES;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressBar progressBar;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    // Переменные для вывода в выпадающие меню
    String name;
    String years;
    String national;
    String sex;
    String positionOnMap;
    String aboutYourSelf;
    String body;
    String colorOfHair;
    String weight;
    String lengthOfMan;
    String orientationOfPeople;
    String lookingFor;
    ArrayList<String> status;
    ArrayList<String> userIdData;
    LinearLayout linearsLayoutForGallery;

    CardView firstCardAdd;
    UserInformation userInformation;
    ArrayList<String> imagesOfImage;
    ImageView largeImage;
    Button buttonRedaction;
    FrameLayout backLargeImage;
    DatabaseReference refForGetAllDataFromDB;

    RecyclerView.Adapter recyclerView_Adapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    Context context;

    ArrayList<String> listOfNames;
    ArrayList<String> listForCount;
    ArrayList<String> listOfMainPhoto;
    CircularImageView circleImageView;
    RecyclerView recyclerView;
    SharedPreferences mSettings;
    // Expandent List Adapter
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    ArrayList<User> users;
    ProgressDialog progressDialog;
    ArrayList<CardView> listOfCards = new ArrayList<>();
    private FirebaseDatabase mFirebaseDatabase;
    int count;
    private DatabaseReference photoGetsFirebaseDatabase;
    private DatabaseReference myRef;
    String userIdStr;
    String userPhoto;
    String statusUser;
    String nameOfUser;
    String photoUserChat;
    Button mButtonSignOut;

    FirebaseUser user = auth.getInstance().getCurrentUser();

    private DatabaseReference refRooms;
    ConstraintLayout constratLayout;
    // Глобальные чата
    ListView mListRooms;
    FloatingActionButton mFloatBtnAddRoom;
    EditText mEditTextNameRoom;
    ArrayAdapter<String> mArrayAdapter;
    ArrayList<String> array_list_rooms = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //Data of cards
        linearsLayoutForGallery = (LinearLayout) findViewById(R.id.linear_layout);
        firstCardAdd = (CardView) findViewById(R.id.cards);
        largeImage = (ImageView) findViewById(R.id.large_size_for_image);
        backLargeImage = (FrameLayout) findViewById(R.id.back_size_large_image);
        buttonRedaction = (Button) findViewById(R.id.button_redaction);


        progressDialog = new ProgressDialog(this);
        context = getApplicationContext();

        Intent intent = getIntent();

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        name = mSettings.getString("Name", null);
        national = mSettings.getString("National", null);
        body = mSettings.getString("Body", null);
        colorOfHair = mSettings.getString("Hair", null);
        weight = mSettings.getString("Weight", null);
        lengthOfMan = mSettings.getString("Length", null);
        years = mSettings.getString("Years", null);
        lookingFor = mSettings.getString("LookingFor", null);
        sex = mSettings.getString("Sex", null);
        positionOnMap = mSettings.getString("Map", null);
        orientationOfPeople = mSettings.getString("Orientation", null);
        aboutYourSelf = mSettings.getString("About", null);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        listOfNames = new ArrayList<>();
        userIdData = new ArrayList<>();
        status = new ArrayList<>();
        listOfMainPhoto = new ArrayList<>();
        listForCount = new ArrayList<>();
        users = new ArrayList<User>();





        DatabaseReference ref = myRef.child("users");
        ValueEventListener eventListenerForPhotos = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.setTitle("Загрузка данных...");
                progressDialog.show();
                if (listForCount != null){
                    listForCount.clear();
                }
                for (DataSnapshot userDetails : dataSnapshot.getChildren()) {

                    String usersID = String.valueOf(userDetails.child("userId").getValue());


                    Query query = myRef.child("linksOfPhotos").child(usersID).limitToFirst(1);
                    ValueEventListener eventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot dSnapshot : dataSnapshot.getChildren()) {

                                listForCount.add(dSnapshot.child("url").getValue().toString());
                                count = listForCount.size();
                            }
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    };
                    query.addListenerForSingleValueEvent(eventListener);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        ref.addListenerForSingleValueEvent(eventListenerForPhotos);





//Установка флага в ФБ Онлайн
        onlineUser();

        //Иницив=ализация УИ для Табов
        initUI();




        setSupportActionBar(toolbar);
        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorAccent));


        //get firebase auth instance
        auth = FirebaseAuth.getInstance();


        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, SplashScreenEnterActivity.class));
                    finish();
                }
            }
        };

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }


    }


    //sign out method
    public void signOut() {
        auth.signOut();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onlineUser();
        progressBar.setVisibility(View.GONE);
        initUI();

    }

    @Override
    public void onStart() {
        super.onStart();

        auth.addAuthStateListener(authListener);
       // initUI();

    }

    @Override
    public void onStop() {
        super.onStop();
        offlineUser();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }

    }

    private void initUI() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.vp_horizontal_ntb);

        prepareDataForShowAllUsers();
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 5;
            }

            @Override
            public boolean isViewFromObject(final View view, final Object object) {
                return view.equals(object);
            }

            @Override
            public void destroyItem(final View container, final int position, final Object object) {
                ((ViewPager) container).removeView((View) object);
            }

            @Override
            public Object instantiateItem(final ViewGroup container, final int position) {

                if (position == 0) {

                    final View view = LayoutInflater.from(
                            getBaseContext()).inflate(R.layout.layout_show_all_users, null, false);
                    users.clear();
                    // Добавляем все через обьект User в РейЛист users
                    Log.d("CHEKING", String.valueOf(count));

                    for (int i = 0 ; i<count;i++){

                         userIdStr = userIdData.get(i);
                         userPhoto = listOfMainPhoto.get(i);
                         statusUser = status.get(i);
                         nameOfUser = listOfNames.get(i);
                        users.add(new User(userIdStr,userPhoto , statusUser,nameOfUser));

                    }


                     constratLayout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.circle_layout_images, null);
                    circleImageView = (CircularImageView) constratLayout.findViewById(R.id.picture);
                    recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_for_show);
                    recyclerViewLayoutManager = new GridLayoutManager(context, 3);
                    recyclerView.setLayoutManager(recyclerViewLayoutManager);

                    recyclerView_Adapter = new RecyclerViewForShowAllUsersAdapter(context, null, listOfMainPhoto, users);


                    recyclerView.setAdapter(recyclerView_Adapter);
                    recyclerView_Adapter.notifyDataSetChanged();




                    container.addView(view);
                    return view;
                }
                if (position == 2) {

                    final View view = LayoutInflater.from(
                            getBaseContext()).inflate(R.layout.profile_fragment_main, null, false);


                    firstCardAdd = (CardView) view.findViewById(R.id.cards);
                    linearsLayoutForGallery = (LinearLayout) view.findViewById(R.id.linear_layout);
                    largeImage = (ImageView) view.findViewById(R.id.large_size_for_image);
                    backLargeImage = (FrameLayout) view.findViewById(R.id.back_size_large_image);
                    buttonRedaction = (Button) view.findViewById(R.id.button_redaction);
                    mButtonSignOut = (Button) view.findViewById(R.id.button_sign_out);
                    mButtonSignOut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            auth.signOut();

                            FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
                                @Override
                                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    if (user == null) {

                                        startActivity(new Intent(MainActivity.this, SplashScreenEnterActivity.class));
                                        offlineUser();
                                        finish();
                                    }
                                }
                            };
                        }
                    });

                    expListView = (ExpandableListView) view.findViewById(R.id.lvExp);

                    // Cбор всех данных для передачи в ExpListView
                    prepareListData();

                    listAdapter = new ExpandableListAdapter(MainActivity.this, listDataHeader, listDataChild);
                    expListView.setAdapter(listAdapter);

                    buttonRedaction.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, ProfileGeneral.class);
                            startActivity(intent);
                        }
                    });
                    firebaseStartForGallery();
                    container.addView(view);
                    return view;
                }
                if (position == 3) {
                  // Вьюшка чата
                    final View view = LayoutInflater.from(
                            getBaseContext()).inflate(R.layout.chat_rooms_fragment, null, false);

                    mListRooms = (ListView) view.findViewById(R.id.list_view_rooms);
                    mFloatBtnAddRoom = (FloatingActionButton) view.findViewById(R.id.fl_add_room);
                    mEditTextNameRoom = (EditText) view.findViewById(R.id.ed_room_name);

                   mArrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, array_list_rooms);
                   mListRooms.setAdapter(mArrayAdapter);
                    refRooms = myRef.child("chatRooms");
                    //nameOfUserForPositionThree =
                    mFloatBtnAddRoom.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Map<String,Object> map = new HashMap<String, Object>();
                            map.put(mEditTextNameRoom.getText().toString(),"");
                            refRooms.updateChildren(map);

                        }
                    });

                    refRooms.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Set<String> set = new HashSet<String>();
                            Iterator i = dataSnapshot.getChildren().iterator();

                            while (i.hasNext()){
                                set.add(((DataSnapshot)i.next()).getKey());
                            }

                            array_list_rooms.clear();
                            array_list_rooms.addAll(set);

                            mArrayAdapter.notifyDataSetChanged();



                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    mListRooms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getApplicationContext(),Chat_Room.class);
                            intent.putExtra("room_name",((TextView)view).getText().toString() );
                            intent.putExtra("user_name",name);
                            intent.putExtra("photo", photoUserChat);
                            startActivity(intent);

                        }
                    });





                    container.addView(view);
                    return view;
                }
                final View view = LayoutInflater.from(
                        getBaseContext()).inflate(R.layout.item_vp, null, false);

                final TextView txtPage = (TextView) view.findViewById(R.id.txt_vp_item_page);
                txtPage.setText(String.format("Page #%d", position));

                container.addView(view);
                return view;
            }
        });

        final String[] colors = getResources().getStringArray(R.array.default_preview);

        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_first),
                        Color.parseColor(colors[0]))
                        .selectedIcon(getResources().getDrawable(R.drawable.ic_sixth))
                        .title("Знакомства")
                        .badgeTitle("Поиск")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_second),
                        Color.parseColor(colors[1]))
//                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
                        .title("Рейтинг")
                        .badgeTitle("Ваш рейтинг")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_third),
                        Color.parseColor(colors[2]))
                        .selectedIcon(getResources().getDrawable(R.drawable.ic_seventh))
                        .title("Профиль")
                        .badgeTitle("Ваш профиль")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.icon_chat),
                        Color.parseColor(colors[3]))
//                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
                        .title("Чат")
                        .badgeTitle("Онлайн чат")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_fifth),
                        Color.parseColor(colors[4]))
                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
                        .title("Акции")
                        .badgeTitle("Акции")
                        .build()
        );

        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 2);
        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                navigationTabBar.getModels().get(position).hideBadge();
                prepareDataForShowAllUsers();


            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });


        navigationTabBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < navigationTabBar.getModels().size(); i++) {
                    final NavigationTabBar.Model model = navigationTabBar.getModels().get(i);
                    navigationTabBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            model.showBadge();
                        }
                    }, i * 100);
                }
            }
        }, 500);
    }




    public void firebaseStartForGallery() {
        // Реф для получения линков фото из ДБ ФБ
        photoGetsFirebaseDatabase = mFirebaseDatabase.getReference().child("linksOfPhotos").child(user.getUid());
        photoGetsFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                imagesOfImage = new ArrayList<String>();


                for (DataSnapshot keySnapshot : dataSnapshot.getChildren()) {

                    for (DataSnapshot photoUrlSnapshot : keySnapshot.getChildren()) {
                        String value = photoUrlSnapshot.getValue().toString();  // Получаем наш ключ записи

                        imagesOfImage.add(value);
                    }
                }
                photoUserChat = imagesOfImage.get(0); // Получаем нашу первое фото юзера чтобы использовать для чата
                for (int i = 0; i < imagesOfImage.size(); i++) {
                    progressDialog.setTitle("Загрузка фотографий...");
                    progressDialog.show();


                    CardView imageCards = (CardView) getLayoutInflater().inflate(R.layout.item_main_gallery, null);

                    listOfCards.add(imageCards);
                    imageCards.setTag(i);
                    imageCards.setOnClickListener(MainActivity.this);

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(firstCardAdd.getLayoutParams());
                    layoutParams.setMargins(10, 0, 0, 0);


                    final ImageView imageViewForImage = imageCards.findViewById(R.id.main_photo);


                    linearsLayoutForGallery.addView(imageCards, layoutParams);


                    Glide.with(MainActivity.this).load(imagesOfImage.get(i)).into(imageViewForImage); // Загружаем полный урл в КардВью


                }
                progressDialog.dismiss();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    // Слушатель для фотографий, при нажатии показывает увеличенный размер

    public void onClick(View v) {

        final int position = Integer.valueOf(v.getTag().toString());

        Log.e("POSITION", String.valueOf(position));


        largeImage.setVisibility(View.VISIBLE);
        backLargeImage.setVisibility(View.VISIBLE);


        Glide.with(MainActivity.this).load(imagesOfImage.get(position)).into(largeImage);


        largeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                largeImage.setVisibility(View.GONE);
                backLargeImage.setVisibility(View.GONE);
            }
        });

    }


    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Общее:");
        listDataHeader.add("Цели знакомства:");
        listDataHeader.add("Ваши данные:");


        // Adding child data
        List<String> generalData = new ArrayList<String>();
        generalData.add("О себе: " + aboutYourSelf);
        generalData.add("Имя: " + name);
        generalData.add("Возраст: " + years);
        generalData.add("Национальность: " + national);
        generalData.add("Пол: " + sex);


        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("Цель знакомства: " + lookingFor);
        nowShowing.add("Ваша ориентация: " + orientationOfPeople);


        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("Местоположение: " + positionOnMap);
        comingSoon.add("Ваше телосложение: " + body);
        comingSoon.add("Цвет волос: " + colorOfHair);
        comingSoon.add("Вес: " + weight);
        comingSoon.add("Ваш рост: " + lengthOfMan);

        listDataChild.put(listDataHeader.get(0), generalData); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
    }

    private void prepareDataForShowAllUsers() {


        progressDialog.setTitle("Загрузка данных...");
        progressDialog.show();
        // Получаем фотографии с каждого юзера по одной главной
        DatabaseReference ref = myRef.child("users");
        ValueEventListener eventListenerForPhotos = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (listOfMainPhoto != null){
                    listOfMainPhoto.clear();
                }
                for (DataSnapshot userDetails : dataSnapshot.getChildren()) {
                    Log.d("valueName:", String.valueOf(userDetails.child("userId").getValue()));
                    String usersID = String.valueOf(userDetails.child("userId").getValue());

                    // Получаем по юзерАйди только по одной фотографии для отображения
                    Query query = myRef.child("linksOfPhotos").child(usersID).limitToFirst(1);
                    ValueEventListener eventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot dSnapshot : dataSnapshot.getChildren()) {
                                String username = dSnapshot.child("url").getValue(String.class);
                                listOfMainPhoto.add(String.valueOf(dSnapshot.child("url").getValue()));
                                Log.d("LINKS_OF_PHOTO", username);
                                Log.d("SIZE_ARRAY_PHOTO", String.valueOf(listOfMainPhoto.size()));


                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    };
                    query.addListenerForSingleValueEvent(eventListener);

                }
                getAllUsersId();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        ref.addListenerForSingleValueEvent(eventListenerForPhotos);


    }

    public void getAllUsersId() {


        DatabaseReference ref = myRef.child("users");
        ValueEventListener eventListenerForPhotos = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (userIdData != null){
                    userIdData.clear();
                }
                    Query query = myRef.child("users");
                    ValueEventListener eventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot dSnapshot : dataSnapshot.getChildren()) {
                                String username = dSnapshot.child("userId").getValue(String.class);
                                userIdData.add(String.valueOf(dSnapshot.child("userId").getValue()));
                                Log.d("USERS_ID", String.valueOf(username));
                                Log.d("SIZE_USERS_IDS", String.valueOf(userIdData.size()));



                            }
                            getAllUsersNames ();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    };
                    query.addListenerForSingleValueEvent(eventListener);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        ref.addListenerForSingleValueEvent(eventListenerForPhotos);



  progressDialog.dismiss();


    }

    public void getAllUsersNames (){
        // Получаем все имена зарегистрированных пользователей
        DatabaseReference usersdRef = myRef.child("users");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (listOfNames != null){
                    listOfNames.clear();
                }

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    listOfNames.add(String.valueOf(ds.child("name").getValue()));

                    Log.d("NAMES", ds.child("name").getValue(String.class));

                }
                getAllUsersOnline();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        usersdRef.addListenerForSingleValueEvent(eventListener);



    }

    public void getAllUsersOnline(){



        // Получение всех  юзерстатусов
        DatabaseReference usersdRef = myRef.child("userStatus");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (status != null){
                    status.clear();
                }
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    status.add(String.valueOf(ds.child("userOnline").getValue()));
                    Log.d("STATUS_USER", ds.child("userOnline").getValue(String.class));

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        usersdRef.addListenerForSingleValueEvent(eventListener);







    }

    public void onlineUser() {
        //  Cохраняем данные о статусе ОнЛайн и ОффЛайн у юзера в ФБ
        refForGetAllDataFromDB = mFirebaseDatabase.getReference();
        refForGetAllDataFromDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                refForGetAllDataFromDB.child("userStatus").child(user.getUid()).child("userOnline").setValue("Online");


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });





    }

    public void offlineUser() {
        //  Cохраняем данные о статусе ОнЛайн и ОффЛайн у юзера в ФБ
        refForGetAllDataFromDB = mFirebaseDatabase.getReference();
        refForGetAllDataFromDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                refForGetAllDataFromDB.child("userStatus").child(user.getUid()).child("userOnline").setValue("Offline");


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });


    }



}
