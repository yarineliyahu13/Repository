package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etFirstName;
    private EditText etLastName;
    private EditText etEmail;

    private RadioGroup rgGrade;
    private Spinner spMajor;

    private CheckBox cbNotifications;
    private CheckBox cbTerms;

    private TextView tvResult;
    private TextView tvTermsError;
    private TextView tvGradeError;
    private TextView tvMajorError;

    private Button btnRegister;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // חיבור רכיבי המסך
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);

        rgGrade = findViewById(R.id.rgGrade);
        spMajor = findViewById(R.id.spMajor);

        cbNotifications = findViewById(R.id.cbNotifications);
        cbTerms = findViewById(R.id.cbTerms);

        tvResult = findViewById(R.id.tvResult);
        tvTermsError = findViewById(R.id.tvTermsError);
        tvGradeError = findViewById(R.id.tvGradeError);
        tvMajorError = findViewById(R.id.tvMajorError);

        btnRegister = findViewById(R.id.btnRegister);

        // הגדרת SharedPreferences
        preferences = getSharedPreferences(
                getString(R.string.saved_key),
                MODE_PRIVATE
        );

        // טעינת אפשרויות המגמה
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(
                        this,
                        R.array.majors,
                        android.R.layout.simple_spinner_item
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spMajor.setAdapter(adapter);

        // הצגת נתונים שנשמרו בעבר
        loadStudent();

        // הסרת שגיאת תנאי השימוש לאחר סימון התיבה
        cbTerms.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    if (isChecked) {
                        tvTermsError.setVisibility(View.GONE);
                    }
                }
        );

        // בדיקת הטופס בלחיצה
        btnRegister.setOnClickListener(
                v -> registerStudent()
        );
    }

    private void registerStudent() {

        // איפוס הודעות שגיאה קודמות
        etFirstName.setError(null);
        etLastName.setError(null);
        etEmail.setError(null);

        tvTermsError.setVisibility(View.GONE);
        tvGradeError.setVisibility(View.GONE);
        tvMajorError.setVisibility(View.GONE);

        // קליטת הנתונים
        String firstName =
                etFirstName.getText().toString().trim();

        String lastName =
                etLastName.getText().toString().trim();

        String email =
                etEmail.getText().toString().trim();

        boolean valid = true;

        // בדיקת שם פרטי
        if (TextUtils.isEmpty(firstName)) {
            etFirstName.setError(
                    getString(R.string.error_first_name)
            );
            valid = false;
        }

        // בדיקת שם משפחה
        if (TextUtils.isEmpty(lastName)) {
            etLastName.setError(
                    getString(R.string.error_last_name)
            );
            valid = false;
        }

        // בדיקת אימייל
        if (TextUtils.isEmpty(email)) {
            etEmail.setError(
                    getString(R.string.error_email_empty)
            );
            valid = false;

        } else if (!Patterns.EMAIL_ADDRESS
                .matcher(email).matches()) {

            etEmail.setError(
                    getString(R.string.error_email_invalid)
            );
            valid = false;
        }

        // בדיקת בחירת כיתה
        int checkedId = rgGrade.getCheckedRadioButtonId();
        String grade = "";

        if (checkedId == -1) {
            tvGradeError.setVisibility(View.VISIBLE);
            valid = false;

        } else {
            RadioButton selected =
                    findViewById(checkedId);

            grade = selected.getText().toString();
        }

        // בדיקת בחירת מגמה
        String major = "";

        if (spMajor.getSelectedItemPosition() == 0) {
            tvMajorError.setVisibility(View.VISIBLE);
            valid = false;

        } else {
            major = spMajor.getSelectedItem().toString();
        }

        // בדיקת תיבות הסימון
        boolean notifications =
                cbNotifications.isChecked();

        boolean termsAccepted =
                cbTerms.isChecked();

        if (!termsAccepted) {
            tvTermsError.setVisibility(View.VISIBLE);
            valid = false;
        }

        // אין לשמור או להציג הרשמה לא תקינה
        if (!valid) {
            tvResult.setVisibility(View.GONE);
            return;
        }

        // יצירת אובייקט תלמיד
        Student student = new Student(
                firstName,
                lastName,
                email,
                grade,
                major,
                notifications,
                termsAccepted
        );

        // שמירת התלמיד
        saveStudent(student);

        // הצגת פרטי התלמיד
        tvResult.setText(
                getString(R.string.saved_title)
                        + "\n"
                        + student.toDisplayString(this)
        );

        tvResult.setVisibility(View.VISIBLE);
    }

    private void saveStudent(Student student) {

        preferences.edit()
                .putBoolean(
                        getString(R.string.saved_exists),
                        true
                )
                .putString(
                        getString(R.string.saved_first_name),
                        student.getFirstName()
                )
                .putString(
                        getString(R.string.saved_last_name),
                        student.getLastName()
                )
                .putString(
                        getString(R.string.saved_email),
                        student.getEmail()
                )
                .putString(
                        getString(R.string.saved_grade),
                        student.getGrade()
                )
                .putString(
                        getString(R.string.saved_major),
                        student.getMajor()
                )
                .putBoolean(
                        getString(R.string.saved_notifications),
                        student.isNotifications()
                )
                .putBoolean(
                        getString(R.string.saved_terms),
                        student.isTermsAccepted()
                )
                .apply();
    }

    private void loadStudent() {

        boolean hasSavedStudent = preferences.getBoolean(
                getString(R.string.saved_exists),
                false
        );

        // אם אין נתונים שמורים, לא מציגים תוצאה
        if (!hasSavedStudent) {
            tvResult.setVisibility(View.GONE);
            return;
        }

        // יצירת אובייקט מהנתונים השמורים
        Student student = new Student(
                preferences.getString(
                        getString(R.string.saved_first_name), ""
                ),
                preferences.getString(
                        getString(R.string.saved_last_name), ""
                ),
                preferences.getString(
                        getString(R.string.saved_email), ""
                ),
                preferences.getString(
                        getString(R.string.saved_grade), ""
                ),
                preferences.getString(
                        getString(R.string.saved_major), ""
                ),
                preferences.getBoolean(
                        getString(R.string.saved_notifications),
                        false
                ),
                preferences.getBoolean(
                        getString(R.string.saved_terms),
                        false
                )
        );

        // הצגת התלמיד השמור
        tvResult.setText(
                getString(R.string.saved_title)
                        + "\n"
                        + student.toDisplayString(this)
        );

        tvResult.setVisibility(View.VISIBLE);
    }
}
