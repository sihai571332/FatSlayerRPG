package fatslayers.fatslayerrpg;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class MainActivity extends AppCompatActivity  {

    private static final String TAG = "pls no error";

    private QuestGame questGame = new QuestGame();
    private ProgressBar expBar = null;
    public boolean inQuest = false;
    public boolean inStats = false;
    public boolean inHome = false;
    private Quest quest;
    private Stats stats;
    private Home home;
    private int Exp = 5;
    private int numSteps = 0;
    private Intent music = new Intent();

    private TextView levelText;
    private TextView nameText;
    private TextView fkme;

    private int level = 1;

    public boolean mSoundOn;

    boolean helm_bool;
    boolean armor_bool;
    boolean leggings_bool;
    boolean boots_bool;

    private int helm_int;
    private int armor_int;
    private int leggings_int;
    private int boots_int;

    private int difficulty_level;

    private int boost;

    private static final int SETTINGS_REQUEST = 0;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */

    private ViewPager mViewPager;

    private boolean mIsBound = false;
    private String username;
    private MusicService mServ;
    private ServiceConnection Scon =new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((MusicService.ServiceBinder)binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    void doBindService(){
        bindService(new Intent(this,MusicService.class),
                Scon, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService()
    {
        if(mIsBound)
        {
            unbindService(Scon);
            mIsBound = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        nameText = (TextView) findViewById(R.id.userId);
        levelText = (TextView) findViewById(R.id.userLvl);
        fkme = (TextView) findViewById(R.id.count_fat_slain);

//        spinner_helm = (Spinner) findViewById(R.id.equip_helm);
//        spinner_armor = (Spinner) findViewById(R.id.equip_armor);
//        spinner_leggings = (Spinner) findViewById(R.id.equip_leggings);
//        spinner_boots = (Spinner) findViewById(R.id.equip_boots);
//
//        List<String> helm_list = new ArrayList<String>();
//        helm_list.add("<None>");
//        List<String> armor_list = new ArrayList<String>();
//        armor_list.add("<None>");
//        List<String> leggings_list = new ArrayList<String>();
//        leggings_list.add("<None>");
//        List<String> boots_list = new ArrayList<String>();
//        boots_list.add("<None>");
//
//
//        // Creating adapter for spinner
//        ArrayAdapter<String> helm_Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, helm_list);
//        ArrayAdapter<String> armor_Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, armor_list);
//        ArrayAdapter<String> leggings_Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, leggings_list);
//        ArrayAdapter<String> boots_Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, boots_list);
//
//        // Drop down layout style - list view with radio button
//        helm_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        armor_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        leggings_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        boots_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // attaching data adapter to spinner
//        spinner_helm.setAdapter(helm_Adapter);
//        spinner_armor.setAdapter(armor_Adapter);
//        spinner_leggings.setAdapter(leggings_Adapter);
//        spinner_boots.setAdapter(boots_Adapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        expBar = (ProgressBar) findViewById(R.id.progressBar);



        doBindService();


        music.setClass(this,MusicService.class);
        if(mSoundOn){
            startService(music);
        }

        restoreData();

        expBar.setMax(Exp);
        if(inHome){
            home.showFigures(level,boost);
        }



//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });





        final Handler handler = new Handler();
        handler.post(new Runnable() {

            int exp_adjusted = 1;
            boolean restored = false;
            ArrayList<String> temp;
            @Override
            public void run() {

                if(boost == 0){
                    boost = 1;
                }
                exp_adjusted = (Exp*difficulty_level)/boost;

                if(exp_adjusted == 0){
                    exp_adjusted = 10;
                }

                System.out.println("DIFFICULTY: " + difficulty_level);
                System.out.println("BOOST: " + boost);


                expBar.setMax(exp_adjusted);
                if(inQuest&& inStats){
                    temp = quest.getItemsList();
                    if(temp.contains("Boots")){
                        boots_bool = true;
                    }
                    if(temp.contains("Helmet")){
                        helm_bool = true;
                    }
                    if(temp.contains("Armor")){
                        armor_bool = true;
                    }
                    if(temp.contains("Leggings")){
                        leggings_bool = true;
                    }
                    stats.addItems(helm_bool,armor_bool,leggings_bool,boots_bool);

                }
                //update here
                if(inQuest){


                    if(quest.getProgress() == 0){
                        quest.setProgress(numSteps);
                    }

                    expBar.setProgress((quest.getProgress()%(exp_adjusted)));

                    //TODO: LEVEL UP
                    if(quest.getProgress()>0 && quest.getProgress()%(exp_adjusted)==0) {
                        expBar.setProgress(0);
                    }
                    numSteps = quest.getProgress();
                    level = numSteps/(exp_adjusted);
                    levelText.setText(String.valueOf(level));
                    numSteps = quest.getProgress();


                }

                if(inStats && !restored){
                    stats.addItems(helm_bool, armor_bool, leggings_bool, boots_bool);
                    stats.restoreState(helm_int, armor_int, leggings_int, boots_int);
                    restored = true;
                }

                if(inStats){
                    helm_int = stats.getHelm();
                    armor_int = stats.getArmor();
                    leggings_int = stats.getLeggings();
                    boots_int = stats.getBoots();
                    boost = stats.getTotalBoost();
                    stats.addItems(helm_bool, armor_bool, leggings_bool, boots_bool);
                    stats.restoreState(helm_int, armor_int, leggings_int, boots_int);
                }
                if(inHome){
                    home.showFigures(level,boost);
                }
                fkme.setText(String.valueOf(numSteps));

                handler.postDelayed(this,500);
            }
        });

        levelText.setText(String.valueOf(level));
        nameText.setText(username);
        fkme.setText(String.valueOf(numSteps));


//        quest.setProgress(numSteps);

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.putExtra("name",username);
            intent.putExtra("difficulty_level", difficulty_level);
            startActivityForResult(intent, SETTINGS_REQUEST);
            return true;
        }else if (id == R.id.about){
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if (inQuest){
            editor.putInt("numSteps", quest.getProgress());

        }
        editor.putInt("expBar", expBar.getProgress());
        editor.putInt("level", level);
        editor.putString("username", username);
        editor.putBoolean("sound", mSoundOn);

        editor.putBoolean("helm", helm_bool);
        editor.putBoolean("armor", armor_bool);
        editor.putBoolean("leggings", leggings_bool);
        editor.putBoolean("boots", boots_bool);

        editor.putInt("helm_int", helm_int);
        editor.putInt("armor_int", armor_int);
        editor.putInt("leggings_int", leggings_int);
        editor.putInt("boots_int", boots_int);

        editor.putInt("boost", boost);
        editor.putInt("difficulty", difficulty_level);

        stopService(music);
        doUnbindService();
        editor.apply();
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(mSoundOn){
            startService(music);
        }
    }

//
//    @Override
//    protected void onPause(){
//        super.onPause();
//        mServ.pauseMusic();
//    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mServ.stopMusic();
//        doUnbindService();
//    }

    private void restoreData(){
        SharedPreferences sharedPref = getPreferences (MODE_PRIVATE);
        mSoundOn = sharedPref.getBoolean ("sound", true);
        expBar.setProgress(sharedPref.getInt("expBar", 0));
        numSteps = sharedPref.getInt("numSteps", 0);
        level = sharedPref.getInt("level", 1);
        username = sharedPref.getString("username", "Player");

        helm_bool = sharedPref.getBoolean("helm", false);
        armor_bool = sharedPref.getBoolean("armor", false);
        leggings_bool = sharedPref.getBoolean("leggings", false);
        boots_bool = sharedPref.getBoolean("boots", false);

        helm_int = sharedPref.getInt("helm_int", 0);
        armor_int = sharedPref.getInt("armor_int", 0);
        leggings_int = sharedPref.getInt("leggings_int", 0);
        boots_int = sharedPref.getInt("boots_int", 0);

        boost = sharedPref.getInt("boost", 1);
        difficulty_level = sharedPref.getInt("difficulty", 1);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        username=data.getStringExtra("name");
        mSoundOn=data.getBooleanExtra("soundState", true);
        Log.d(TAG,"sound is on: " + mSoundOn);
        nameText.setText(username);
        difficulty_level = data.getIntExtra("difficulty_level", 3);
        if (requestCode == SETTINGS_REQUEST) {

            SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
            // Apply potentially new settings


            //mHumanWinString = sharedPref.getString ("victory_message", "You Won!");
            String[] levels = getResources().getStringArray(R.array.difficulty_levels);
            // set difficulty, or use hardest if not present,
            String difficultyLevel
                    = sharedPref.getString("difficulty_level", levels[levels.length - 1]);
            int i = 0;
            while(i < levels.length) {
                if(difficultyLevel.equals(levels[i])) {
                    questGame.setDifficultyLevel(QuestGame.DifficultyLevel.values()[i]);
                    i = levels.length; // to stop loop
                }
                i++;
            }
        }
    }

    public void setDifficulty(int difficulty) {
        // check bounds;
        if (difficulty < 0 || difficulty >= QuestGame.DifficultyLevel.values().length) {
            Log.d(TAG, "Unexpected difficulty: " + difficulty + "." +
                    " Setting difficulty to Easy / 0.");
            difficulty = 0; // if out of bounds set to 0
        }
        QuestGame.DifficultyLevel newDifficulty
                = QuestGame.DifficultyLevel.values()[difficulty];

        questGame.setDifficultyLevel(newDifficulty);
        String message = "Difficulty set to " +
                newDifficulty.toString().toLowerCase() + " .";
    }
            /**
             * A placeholder fragment containing a simple view.
             */
//    public static class PlaceholderFragment extends Fragment {
//        /**
//         * The fragment argument representing the section number for this
//         * fragment.
//         */
//        private static final String ARG_SECTION_NUMBER = "section_number";
//
//        public PlaceholderFragment() {
//        }
//
//        /**
//         * Returns a new instance of this fragment for the given section
//         * number.
//         */
//        public static PlaceholderFragment newInstance(int sectionNumber) {
//            PlaceholderFragment fragment = new PlaceholderFragment();
//            Bundle args = new Bundle();
//            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//            fragment.setArguments(args);
//            return fragment;
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.home_screen, container, false);
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
//            return rootView;
//        }
//    }

            /**
             * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
             * one of the sections/tabs/pages.
             */
            public class SectionsPagerAdapter extends FragmentPagerAdapter {

                public SectionsPagerAdapter(FragmentManager fm) {
                    super(fm);
                }

                @Override
                public Fragment getItem(int position) {
                    switch (position) {
                        case 0:
                            home = new Home();
                            inHome = true;
                            return home;
//                        case 1:
//                            Craft craft = new Craft();
////                            inHome = false;
////                            Log.d(TAG,"Boolean of Home in Craft:" + inHome);
//                            return craft;
                        case 2:
                            quest = new Quest();
                            inQuest = true;

                            return quest;
                        case 1:
                            stats = new Stats();
                            inStats = true;
                            return stats;
                        default:
                            return null;
                    }
                }

                @Override
                public int getCount() {
                    // Show 3 total pages.
                    return 3;
                }

                @Override
                public CharSequence getPageTitle(int position) {
                    switch (position) {
                        case 0:
                            return "HOME";
//                        case 1:
//                            return "CRAFT";
                        case 2:
                            return "QUEST";
                        case 1:
                            return "STATS";
                    }
                    return null;
                }

            }

        }

