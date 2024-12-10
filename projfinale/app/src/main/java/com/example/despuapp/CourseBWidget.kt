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


class CourseBWidget : AppWidgetProvider() {

    private val handler = Handler(Looper.getMainLooper())
    private var isRefreshing = false

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        Log.d("CourseBWidget", "onUpdate called")

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
                Log.d("CourseBWidget", "Periodic refresh triggered")
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
            val remoteViews = RemoteViews(context.packageName, R.layout.widget_course_b)

            fetchCombinedLatestMessage(context) { latestMessage ->
                Log.d("CourseBWidget", "Latest message fetched: $latestMessage")

                remoteViews.setTextViewText(R.id.widgetTitle, "Course B Updates")
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
        Log.d("CourseBWidget", "Fetching messages from Course B and All Courses")

        val databaseReference = FirebaseDatabase.getInstance().reference.child("contactForm")

        // Fetch the latest message from "Course B"
        val fetchCourseB = databaseReference.orderByChild("course").equalTo("Course B").limitToLast(1).get()
        val fetchAllCourses = databaseReference.orderByChild("course").equalTo("All Courses").limitToLast(1).get()

        fetchCourseB.addOnSuccessListener { courseBSnapshot ->
            val courseBMessage = courseBSnapshot.children.firstOrNull()?.child("msgContent")?.value?.toString()
                ?: "No message from Course B"

            fetchAllCourses.addOnSuccessListener { allCoursesSnapshot ->
                val allCoursesMessage = allCoursesSnapshot.children.firstOrNull()?.child("msgContent")?.value?.toString()
                    ?: "No message from All Courses"

                Log.d("CourseBWidget", "Course B Message: $courseBMessage")
                Log.d("CourseBWidget", "All Courses Message: $allCoursesMessage")

                callback("Course B: $courseBMessage\nAll Courses: $allCoursesMessage")
            }.addOnFailureListener {
                Log.e("CourseBWidget", "Failed to load from All Courses", it)
                callback("Course B: $courseBMessage")
            }
        }.addOnFailureListener {
            Log.e("CourseBWidget", "Failed to load from Course B", it)
            callback("Failed to fetch messages")
        }
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        Log.d("CourseBWidget", "onDisabled called")
        handler.removeCallbacksAndMessages(null)
        isRefreshing = false
    }
}
