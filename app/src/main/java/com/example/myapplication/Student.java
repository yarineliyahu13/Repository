
package com.example.myapplication;

import android.content.Context;

public class Student {

    private String firstName;
    private String lastName;
    private String email;
    private String grade;
    private String major;
    private boolean notifications;
    private boolean termsAccepted;

    public Student(String firstName, String lastName,
                   String email, String grade, String major,
                   boolean notifications, boolean termsAccepted) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.grade = grade;
        this.major = major;
        this.notifications = notifications;
        this.termsAccepted = termsAccepted;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getGrade() {
        return grade;
    }

    public String getMajor() {
        return major;
    }

    public boolean isNotifications() {
        return notifications;
    }

    public boolean isTermsAccepted() {
        return termsAccepted;
    }

    public String toDisplayString(Context context) {
        return context.getString(
                R.string.result_format,
                context.getString(R.string.result_first_name, firstName),
                context.getString(R.string.result_last_name, lastName),
                context.getString(R.string.result_email, email),
                context.getString(R.string.result_grade, grade),
                context.getString(R.string.result_major, major),
                context.getString(
                        R.string.result_notifications,
                        context.getString(
                                notifications ? R.string.yes : R.string.no)),
                context.getString(
                        R.string.result_terms,
                        context.getString(
                                termsAccepted ? R.string.yes : R.string.no))
        );
    }
}
