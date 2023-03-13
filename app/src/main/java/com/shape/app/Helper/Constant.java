package com.shape.app.Helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.util.Base64;

import java.io.ByteArrayOutputStream;


public class Constant {


    public Constant() {

    }

    public static final String MAIN = "https://androappstech.in/shape-Hopon-backend/";
  // public static final String MAIN = "http://143.110.179.75/HopOn_dev/";
   // public static final String MAIN = "https://androappstech.com/HopOn/";
//  public static final String MAIN = "https://dev.androappstech.com/HopOn/";
    public static final String BASE_URL = MAIN + "api/";


    public static String Token = "c7d3965d49d4a59b0da80e90646aee77548458b3377ba3c0fb43d5ff91d54ea28833080e3de6ebd4fde36e2fb7175cddaf5d8d018ac1467c3d15db21c11b6909";

    public static String profile_pic = MAIN + "/data/profile_pic/";
    public static String FeedImage = MAIN + "/data/Feeds/";
    public static String Event_img = MAIN + "data/Event/";
    public static String Appointment_img = MAIN + "data/Appoinment_type/";

    public static String PDF_Heart_report = MAIN + "data/Heart_report/";
    public static String PDF_PrimaryFitness_report = MAIN + "data/PrimaryFitness_report/";
    public static String PDF_SecondaryFitness_report = MAIN + "data/SecondaryFitness_report/";
    public static String PDF_CBCLevel_report = MAIN + "data/CBCLevel_report/";
    public static String PDF_Muscuskelotal_report = MAIN + "data/Muscuskelotal_report/";
    public static String PDF_Physicaleducation = MAIN + "data/Physicaleducation/";
    public static String PDF_OverallUnit_report = MAIN + "data/OverallUnit_report/";

    public static String VIDEO_PrimaryFitness_media = MAIN + "data/PrimaryFitness_media/";
    public static String VIDEO_PrimaryFitness_images = MAIN + "data/Primary_images/";
    public static String VIDEO_PrimaryFitness_video = MAIN + "data/PrimaryVideo/";

    public static String VIDEO_SecondaryFitness_media = MAIN + "data/SecondaryFitness_media/";
    public static String SecondaryFitness_img = MAIN + "data/Secondary_images/";
    public static String SecondaryFitness_vid= MAIN + "data/SecondaryVideo/";

    public static String VIDEO_OverallUnit_media = MAIN + "data/OverallUnit_media/";

    public static String GameSkills_media = MAIN + "data/GameSkills_media/";
    public static String GameSkills_report = MAIN + "data/GameSkill_report/";
    public static String MentalSkills_report = MAIN + "data/MentalSkills_report/";
    public static String RelaxedAwareness_report = MAIN + "data/RelaxedAwareness_report/";

    public static String Achievement_certificate = MAIN + "data/Achievement_certificate/";
    public static String Achievement_images = MAIN + "data/Achievement_images/";
    public static String OverallPerformance_report = MAIN + "data/OverallPerformance_report/";
    public static String OverallPerformance_reflection = MAIN + "data/OverallPerformance_reflection/";


    public static final String USER_LOGIN = BASE_URL + "student/login";
    public static final String USER_SendOTP = BASE_URL + "student/SendOTP";
    public static final String USER_CONFIRMOTP = BASE_URL + "student/ConfirmOTP";
    public static final String UserProfile = BASE_URL + "student/UserProfile";
    public static final String UpdateUserProfile = BASE_URL + "student/UpdateUserProfile";
    public static final String FEED_LIST_TAB = BASE_URL + "student/AllFeedList"; //get user data from server
    public static final String ALL_EVENT_LIST = BASE_URL + "student/AllEventList"; //update user profile
    public static final String FeedLike = BASE_URL + "student/FeedLike"; //Like Or Dislike Post
    public static final String About_Us = BASE_URL + "student/AppData"; //Drawer Data
    public static final String EventInterested = BASE_URL + "student/EventInterested"; //Drawer Data
    public static final String ADD_APPOINTMENT = BASE_URL + "student/TakeAppoinment"; //Add appointment Data
    public static final String List_APPOINTMENT = BASE_URL + "student/AppoinetmentType"; //List appointment Data
    public static final String ADD_FEED_COMMENT = BASE_URL + "student/AddFeedComment"; //Add Feed Comment
    public static final String LIST_FEED_COMMENT = BASE_URL + "student/FeedCommentList"; //Add Feed Comment
    public static final String EventFilter = BASE_URL + "student/EventFilter"; //EventFilter
    public static final String FeedDetails = BASE_URL + "student/FeedById"; //FeedDetails By ID
    public static final String UserInterestedEventList = BASE_URL + "student/UserInterestedEventList"; //EventFilter
    public static final String Primary_List = BASE_URL + "Report/FitnessReport"; //FitnessReport
    public static final String HealthReport = BASE_URL + "Report/HealthReport"; //HealthReport
    public static final String OverallGrade = BASE_URL + "Report/OverallGrade"; //EventFilter
    public static final String PhysicalEducation = BASE_URL + "Report/PhysicalEducation"; //
    public static final String GameSkill = BASE_URL + "Report/GameSkill"; //
    public static final String MentalSkill = BASE_URL + "Report/MentalSkill"; //
    public static final String RelaxedAwareness = BASE_URL + "Report/RelaxedAwareness"; //
    public static final String Achievement = BASE_URL + "Report/Achievement"; //
    public static final String OverallPerformance = BASE_URL + "Report/OverallPerformance"; //
    public static final String FetchBMIReport = BASE_URL + "Report/FetchBMIReport"; //
    public static final String NotificationListt = BASE_URL + "Student/NotificationList"; //
    public static final String PhysicalEducationForGraph = BASE_URL + "Report/OverallPerformanceForGraph"; //
    public static final String GetPermission = BASE_URL + "Student/GetPermission"; //
    public static final String AddContactUs = BASE_URL + "Student/AddContactUs"; //
    public static final String ChangePassword = BASE_URL + "Student/ChangePassword"; //
    public static final String AppData = BASE_URL + "Student/AppData"; //



    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public static String getStringImageBase64(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }


}
