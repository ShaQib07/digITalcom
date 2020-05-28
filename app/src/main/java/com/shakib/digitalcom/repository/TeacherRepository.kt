package com.shakib.digitalcom.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.shakib.digitalcom.model.Teacher
import com.shakib.digitalcom.utils.Constants
import com.shakib.digitalcom.utils.Constants.CURRENT_USER
import com.shakib.digitalcom.utils.FirebaseEndpoints.TEACHERS
import java.util.*

class TeacherRepository {
    private val database = FirebaseDatabase.getInstance()
    private val teachersReference = database.getReference(TEACHERS)
    private val teachersDataSet = ArrayList<Teacher>()

    companion object{
        private val instance = TeacherRepository()
        fun getInstance(): TeacherRepository = instance
    }

    fun getTeachers(): LiveData<List<Teacher>> {

        val teachersData = MutableLiveData<List<Teacher>>()

        teachersReference.addChildEventListener(object : ChildEventListener {

            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                val teacher: Teacher? = dataSnapshot.getValue(Teacher::class.java)
                teacher?.let { teachersDataSet.add(it) }
                teachersData.value = teachersDataSet
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        return teachersData
    }

    fun getSavedTeacher(){

        teachersReference.child(Constants.PHONE).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val teacher: Teacher? = dataSnapshot.getValue(Teacher::class.java)
                Log.i("INFO", "Inside get saved teacher -> onDataChange -> TeacherName: "+teacher?.name)
                Constants.CURRENT_USER = teacher
            }
        })
    }

    fun teacherExists(phoneNumber: String): LiveData<Boolean> {

        val isTeacherExists = MutableLiveData<Boolean>()
        teachersReference.child(phoneNumber).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                isTeacherExists.value = dataSnapshot.exists()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        Log.i("INFO", "Returning: ${isTeacherExists.value}")
        return isTeacherExists
    }

    fun writeNewUser(userName: String, designation: String, phoneNumber: String) {
        Log.i("INFO", "Inside repo: writeNewUser")
        val teacher = Teacher(userName, designation, phoneNumber)
        teachersReference.child("+88$phoneNumber").setValue(teacher)
        CURRENT_USER = teacher
    }
}