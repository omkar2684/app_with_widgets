package com.example.despuapp

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.RemoteViews
import com.google.firebase.database.FirebaseDatabase


class CourseCWidget : AppWidgetProvider() {

    private val handler = Handler(Looper.getMainLooper())
    private var isRefreshing = false

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        Log.d("CourseCWidget", "onUpdate called")

        if (!isRefreshing) {
            isRefreshing = true
            schedulePeriodicRefresh(context, appWidgetManager, appWidgetIds)
        }

        refreshWidgets(context, appWidgetManager, appWidgetIds)
    }

    private fun schedulePeriodicRefresh(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        handler.postDelayed(object : Runnable {
            override fun run() {
                Log.d("CourseCWidget", "Periodic refresh triggered")
                refreshWidgets(context, appWidgetManager, appWidgetIds)
                handler.postDelayed(this, 5000)
            }
        }, 5000)
    }

    private fun refreshWidgets(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            val remoteViews = RemoteViews(context.packageName, R.layout.widget_course_c)

            fetchCombinedLatestMessage(context) { latestMessage ->
                Log.d("CourseCWidget", "Latest message fetched: $latestMessage")

                remoteViews.setTextViewText(R.id.widgetTitle, "Course C Updates")
                remoteViews.setTextViewText(R.id.widgetMessage, latestMessage)

                val intent = Intent(context, MainActivity::class.java)
                val pendingIntent = PendingIntent.getActivity(
                    context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                remoteViews.setOnClickPendingIntent(R.id.widgetRoot, pendingIntent)

                appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
            }
        }
    }

    private fun fetchCombinedLatestMessage(context: Context, callback: (String) -> Unit) {
        Log.d("CourseCWidget", "Fetching messages from Course C and All Courses")

        val databaseReference = FirebaseDatabase.getInstance().reference.child("contactForm")

        val fetchCourseC = databaseReference.orderByChild("course").equalTo("Course C").limitToLast(1).get()
        val fetchAllCourses = databaseReference.orderByChild("course").equalTo("All Courses").limitToLast(1).get()

        fetchCourseC.addOnSuccessListener { courseCSnapshot ->
            val courseCMessage = courseCSnapshot.children.firstOrNull()?.child("msgContent")?.value?.toString()
                ?: "No message from Course C"

            fetchAllCourses.addOnSuccessListener { allCoursesSnapshot ->
                val allCoursesMessage = allCoursesSnapshot.children.firstOrNull()?.child("msgContent")?.value?.toString()
                    ?: "No message from All Courses"

                Log.d("CourseCWidget", "Course C Message: $courseCMessage")
                Log.d("CourseCWidget", "All Courses Message: $allCoursesMessage")

                callback("Course C: $courseCMessage\nAll Courses: $allCoursesMessage")
            }.addOnFailureListener {
                Log.e("CourseCWidget", "Failed to load from All Courses", it)
                callback("Course C: $courseCMessage")
            }
        }.addOnFailureListener {
            Log.e("CourseCWidget", "Failed to load from Course C", it)
            callback("Failed to fetch messages")
        }
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        Log.d("CourseCWidget", "onDisabled called")
        handler.removeCallbacksAndMessages(null)
        isRefreshing = false
    }
}
