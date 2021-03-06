package fatslayers.fatslayerrpg;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;

import static android.R.id.message;

/**
 * Created by drako on 7/24/2017.
 */

public class SettingsActivity extends AppCompatActivity {

    private static String name;
    private static CheckBoxPreference soundPref;
    private static boolean ItsLookingGood;

    private static int difficulty_level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
        name = getIntent().getStringExtra("name");
        difficulty_level = getIntent().getIntExtra("difficulty_level", 64);

    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent();
        intent.putExtra("name",name);
        intent.putExtra("soundState",ItsLookingGood);
        intent.putExtra("difficulty_level", difficulty_level);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    public static class SettingsFragment extends PreferenceFragment {
        private SharedPreferences sharedPrefs;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // set up shared preferences file
            getPreferenceManager().setSharedPreferencesName("FatSlayerRPG");
            sharedPrefs = getActivity().getSharedPreferences("FatSlayerRPG", MODE_PRIVATE);
            // display preferences
            addPreferencesFromResource(R.xml.preferences);
            // handle difficulty summary and listener
            updateDifficultyLevelSummary();
            updateUsernameSummary();
            setDifficultyListener();
            setUserID();
            setSoundListener();


//            setVictoryListener();
            // handle victory message summary and listener
        }

        private void setSoundListener(){
            soundPref = (CheckBoxPreference) findPreference("sound");
            ItsLookingGood = soundPref.isChecked();
            soundPref.setOnPreferenceChangeListener(
                    new Preference.OnPreferenceChangeListener(){
                        @Override
                        public boolean onPreferenceChange(Preference preference, Object newValue){
                            ItsLookingGood = Boolean.valueOf(newValue.toString());

                            return true;
                        }
                    }
            );
        }

        private void setDifficultyListener() {
            final Preference difficultyLevelPref = findPreference("difficulty_level");
            difficultyLevelPref.setOnPreferenceChangeListener(
                    new Preference.OnPreferenceChangeListener() {
                        @Override
                        public boolean onPreferenceChange(Preference preference, Object newValue) {
                            // update the summary with the new difficulty
                            String difficultySummary = "Current difficulty: " + newValue;
                            difficultyLevelPref.setSummary(difficultySummary);
                            // Since we are handling the pref, we must save it
                            SharedPreferences.Editor ed = sharedPrefs.edit();
                            ed.putString("difficulty_level", newValue.toString());
                            ed.apply();
                            setDifficultyLevel(newValue.toString());
                            return true;
                        }
                    });

        }


        private void setDifficultyLevel(String value){
            if(value.equals("Expert")){
                difficulty_level = 64;
            }
            if(value.equals("Harder")){
                difficulty_level = 16;
            }

            if(value.equals("Easy")){
                difficulty_level = 1;
            }
        }

        private void setUserID() {
            final Preference usernamePref = findPreference("user_id");
            usernamePref.setOnPreferenceChangeListener(
                    new Preference.OnPreferenceChangeListener() {
                        @Override
                        public boolean onPreferenceChange(Preference preference, Object newValue) {
                            // update the summary with the new difficulty
                            String difficultySummary = "Username: " + newValue;
                            usernamePref.setSummary(difficultySummary);
                            // Since we are handling the pref, we must save it
                            SharedPreferences.Editor ed = sharedPrefs.edit();
                            name = newValue.toString();
                            ed.putString("user_id", newValue.toString());
                            ed.apply();
                            return true;
                        }
                    });
        }

        private void updateDifficultyLevelSummary() {
            String difficultySummary = "Current difficulty: " +
                    sharedPrefs.getString("difficulty_level",
                            getString(R.string.difficulty_expert));
            Preference difficultyLevelPref = findPreference("difficulty_level");
            difficultyLevelPref.setSummary(difficultySummary);



        }

        private void updateUsernameSummary() {
            String usernameSummary = "Username: " +
                    sharedPrefs.getString("user_id",
                            getString(R.string.userid));
            Preference usernamePref = findPreference("user_id");
            usernamePref.setSummary(usernameSummary);
        }
    }
}
