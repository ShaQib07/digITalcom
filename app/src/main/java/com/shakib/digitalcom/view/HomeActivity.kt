package com.shakib.digitalcom.view

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.shakib.digitalcom.adapters.ChatAdapter
import com.shakib.digitalcom.databinding.ActivityHomeBinding
import com.shakib.digitalcom.model.Chat
import com.shakib.digitalcom.utils.Constants.ACCESS_TOKEN
import com.shakib.digitalcom.utils.Constants.ACCESS_TOKEN_TWO
import com.shakib.digitalcom.viewmodel.HomeViewModel
import com.tbruyelle.rxpermissions2.RxPermissions
import com.twilio.video.*
import com.twilio.video.CameraCapturer
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var chatAdapter: ChatAdapter
    private var chatList: ArrayList<Chat> = ArrayList()

    // webRtc
    private lateinit var room: Room
    private val enable = true
    private lateinit var localAudioTrack: LocalAudioTrack
    private lateinit var localVideoTrack: LocalVideoTrack
    private var localAudioTracks = ArrayList<LocalAudioTrack>()
    private var localVideoTracks = ArrayList<LocalVideoTrack>()
    private lateinit var rxPermissions: RxPermissions

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Binding View with ViewModels
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        binding.mHomeViewModel = homeViewModel

        setRecyclerView()

        // Calling ViewModel Functions
        homeViewModel.getChatMessages().observe(this, Observer {
            chatList.add(it)
            chatAdapter.notifyDataSetChanged()
        })

        // run time permission
        rxPermissions = RxPermissions(this)
        rxPermissions
            .request(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            )
            .subscribe { granted ->
                if (granted) {
                    Log.e("GSK", "Granted")
                    initiateTracks()
                } else {
                    Log.e("GSK", "Denied")
                }
            }
    }

    private fun setRecyclerView() {
        rvChat.setHasFixedSize(true)
        rvChat.layoutManager = LinearLayoutManager(this)
        chatAdapter = ChatAdapter(this, chatList)
        rvChat.adapter = chatAdapter
    }

    // webRtc
    private fun initiateTracks() {
        // Create an audio track
        localAudioTrack = LocalAudioTrack.create(this, enable)!!
        localAudioTracks.add(localAudioTrack)

        // A video track requires an implementation of VideoCapturer
        val cameraCapturer = CameraCapturer(
            this,
            CameraCapturer.CameraSource.FRONT_CAMERA
        )

        // Create a video track
        localVideoTrack = LocalVideoTrack.create(this, enable, cameraCapturer)!!
        localVideoTracks.add(localVideoTrack)
        binding.videoView.mirror = true

        // Rendering a local video track requires an implementation of VideoRenderer
        // Let's assume we have added a VideoView in our view hierarchy
        // Render a local video track to preview your camera
        localVideoTrack.addRenderer(binding.videoView)

        // Release the audio track to free native memory resources
        connectToRoom("MyRoom")
    }

    private fun connectToRoom(roomName: String?) {
        val connectOptions = ConnectOptions.Builder(ACCESS_TOKEN)
            .roomName(roomName!!)
            .audioTracks(localAudioTracks)
            .videoTracks(localVideoTracks)
            //.dataTracks(localDataTracks)
            .build()
        room = Video.connect(this, connectOptions, roomListener)
    }

    private val roomListener = object : Room.Listener {
        override fun onRecordingStopped(room: Room) {
            Log.e("GSK", "Recording stopped on " + room.name)
        }

        override fun onParticipantDisconnected(room: Room, remoteParticipant: RemoteParticipant) {
            Log.e("GSK", remoteParticipant.identity + " Disconnected from " + room.name)
        }

        override fun onRecordingStarted(room: Room) {
            Log.e("GSK", "Recording started on " + room.name)
        }

        override fun onConnectFailure(room: Room, twilioException: TwilioException) {
            Log.e("GSK", "Connection failure on ${room.name}")
            Log.e("GSK", twilioException.message)
        }

        override fun onReconnected(room: Room) {
            Log.e("GSK", "Reconnected to " + room.name)
        }

        override fun onParticipantConnected(room: Room, remoteParticipant: RemoteParticipant) {
            Log.e("GSK", remoteParticipant.identity + " connected to " + room.name)
            remoteParticipant.setListener(remoteParticipantListener)
        }

        override fun onConnected(room: Room) {
            Log.e("GSK", "Connected to ${room.name}")
            Log.e("GSK", room.localParticipant?.identity)
        }

        override fun onDisconnected(room: Room, twilioException: TwilioException?) {
            Log.e("GSK", "Disconnected from " + room.name)
            Log.e("GSK", twilioException?.message)
        }

        override fun onReconnecting(room: Room, twilioException: TwilioException) {
            Log.e("GSK", "Reconnecting to " + room.name)
        }

    }

    private val remoteParticipantListener = object : RemoteParticipant.Listener{
        override fun onDataTrackPublished(
            remoteParticipant: RemoteParticipant,
            remoteDataTrackPublication: RemoteDataTrackPublication
        ) {
            TODO("Not yet implemented")
        }

        override fun onAudioTrackEnabled(
            remoteParticipant: RemoteParticipant,
            remoteAudioTrackPublication: RemoteAudioTrackPublication
        ) {
            TODO("Not yet implemented")
        }

        override fun onAudioTrackPublished(
            remoteParticipant: RemoteParticipant,
            remoteAudioTrackPublication: RemoteAudioTrackPublication
        ) {
            TODO("Not yet implemented")
        }

        override fun onVideoTrackPublished(
            remoteParticipant: RemoteParticipant,
            remoteVideoTrackPublication: RemoteVideoTrackPublication
        ) {
            TODO("Not yet implemented")
        }

        override fun onVideoTrackSubscribed(
            remoteParticipant: RemoteParticipant,
            remoteVideoTrackPublication: RemoteVideoTrackPublication,
            remoteVideoTrack: RemoteVideoTrack
        ) {
            localVideoTrack.release()
            binding.videoView.mirror = false
            remoteVideoTrack.addRenderer(binding.videoView)
        }

        override fun onVideoTrackEnabled(
            remoteParticipant: RemoteParticipant,
            remoteVideoTrackPublication: RemoteVideoTrackPublication
        ) {
            TODO("Not yet implemented")
        }

        override fun onVideoTrackDisabled(
            remoteParticipant: RemoteParticipant,
            remoteVideoTrackPublication: RemoteVideoTrackPublication
        ) {
            TODO("Not yet implemented")
        }

        override fun onVideoTrackUnsubscribed(
            remoteParticipant: RemoteParticipant,
            remoteVideoTrackPublication: RemoteVideoTrackPublication,
            remoteVideoTrack: RemoteVideoTrack
        ) {
            TODO("Not yet implemented")
        }

        override fun onDataTrackSubscriptionFailed(
            remoteParticipant: RemoteParticipant,
            remoteDataTrackPublication: RemoteDataTrackPublication,
            twilioException: TwilioException
        ) {
            TODO("Not yet implemented")
        }

        override fun onAudioTrackDisabled(
            remoteParticipant: RemoteParticipant,
            remoteAudioTrackPublication: RemoteAudioTrackPublication
        ) {
            TODO("Not yet implemented")
        }

        override fun onDataTrackSubscribed(
            remoteParticipant: RemoteParticipant,
            remoteDataTrackPublication: RemoteDataTrackPublication,
            remoteDataTrack: RemoteDataTrack
        ) {
            TODO("Not yet implemented")
        }

        override fun onAudioTrackUnsubscribed(
            remoteParticipant: RemoteParticipant,
            remoteAudioTrackPublication: RemoteAudioTrackPublication,
            remoteAudioTrack: RemoteAudioTrack
        ) {
            TODO("Not yet implemented")
        }

        override fun onAudioTrackSubscribed(
            remoteParticipant: RemoteParticipant,
            remoteAudioTrackPublication: RemoteAudioTrackPublication,
            remoteAudioTrack: RemoteAudioTrack
        ) {

        }

        override fun onVideoTrackSubscriptionFailed(
            remoteParticipant: RemoteParticipant,
            remoteVideoTrackPublication: RemoteVideoTrackPublication,
            twilioException: TwilioException
        ) {
            TODO("Not yet implemented")
        }

        override fun onAudioTrackSubscriptionFailed(
            remoteParticipant: RemoteParticipant,
            remoteAudioTrackPublication: RemoteAudioTrackPublication,
            twilioException: TwilioException
        ) {
            TODO("Not yet implemented")
        }

        override fun onAudioTrackUnpublished(
            remoteParticipant: RemoteParticipant,
            remoteAudioTrackPublication: RemoteAudioTrackPublication
        ) {
            TODO("Not yet implemented")
        }

        override fun onVideoTrackUnpublished(
            remoteParticipant: RemoteParticipant,
            remoteVideoTrackPublication: RemoteVideoTrackPublication
        ) {
            TODO("Not yet implemented")
        }

        override fun onDataTrackUnsubscribed(
            remoteParticipant: RemoteParticipant,
            remoteDataTrackPublication: RemoteDataTrackPublication,
            remoteDataTrack: RemoteDataTrack
        ) {
            TODO("Not yet implemented")
        }

        override fun onDataTrackUnpublished(
            remoteParticipant: RemoteParticipant,
            remoteDataTrackPublication: RemoteDataTrackPublication
        ) {
            TODO("Not yet implemented")
        }

    }
}
