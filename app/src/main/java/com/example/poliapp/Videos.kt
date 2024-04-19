
package com.example.poliapp

import PoliSQLiteOpenHelper
import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.MediaController
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.poliapp.db.contracts.PhotosContract
import com.example.poliapp.db.contracts.VideosContract
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Random

class Videos : Fragment() {

    companion object {
        fun newInstance() = Videos()
    }

    private val PICK_VIDEO_REQUEST = 1
    private lateinit var btnSaveVideo : Button
    private var video =""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_videos, container, false)
        btnSaveVideo = view.findViewById(R.id.btnSaveVideo)
        view.findViewById<ScrollView>(R.id.scrollView)?.post {
            view.findViewById<ScrollView>(R.id.scrollView).fullScroll(View.FOCUS_UP)
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Configurar el OnClickListener para el botón de cargar video
        val cardView = view?.findViewById<CardView>(R.id.uploadVideo)

        cardView?.setOnClickListener {
            openFileChooser()
        }

        btnSaveVideo.setOnClickListener {
            if (!video.isNullOrEmpty()){
                saveInformationInBD()
            }else {
                Toast.makeText(requireContext(), "Seleccione un video", Toast.LENGTH_SHORT).show()
            }
        }

        loadInformationFromDB()

    }

    private fun loadInformationFromDB(){
        val linearLayout = view?.findViewById<LinearLayout>(R.id.linearLayout)
        linearLayout?.removeAllViews()

        val dbHelper = PoliSQLiteOpenHelper(requireContext())
        val db = dbHelper.readableDatabase


        val cursor = db.query(
            VideosContract.VideosEntry.TABLE_NAME,
            arrayOf(
                VideosContract.VideosEntry.COLUMN_VIDEO,
            ), null, null, null, null, null
        )


        if (cursor.moveToFirst()) {

            do {

                val video =
                    cursor.getString(cursor.getColumnIndexOrThrow(VideosContract.VideosEntry.COLUMN_VIDEO))

                val listItemView = layoutInflater.inflate(R.layout.list_item_videos, null)
                val videoView = listItemView.findViewById<VideoView>(R.id.videoShow)

                if (!video.isNullOrEmpty() && video != "null"){
                    val videoUri = Uri.parse(video)

                    videoView.setVideoURI(videoUri)

                    val mediaController = MediaController(requireContext())

                    mediaController.setAnchorView(videoView)
                    mediaController.setMediaPlayer(videoView)

                    videoView.setMediaController(mediaController)
                    linearLayout?.addView(listItemView)
                }

            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
    }

    private fun openFileChooser() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        intent.type = "video/*"
        startActivityForResult(intent, PICK_VIDEO_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_VIDEO_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val videoUri = data.data
            video = videoUri.toString()
        }
    }

    private fun saveInformationInBD(){

        btnSaveVideo.isEnabled=false
        val newVideo = Uri.parse(video)


        val videoPath = newVideo?.let { copyVideoToAppStorage(it) }
        updateVideosInDB(videoPath.toString())
    }

    private fun updateVideosInDB(newVideo :String){

        val dbHelper= PoliSQLiteOpenHelper(requireContext())
        val db= dbHelper.writableDatabase

        val valores = ContentValues().apply {
            put(VideosContract.VideosEntry.COLUMN_VIDEO, newVideo)
        }

        val rowsInsert = db.insert(VideosContract.VideosEntry.TABLE_NAME, null, valores)

        if (rowsInsert != -1L) {
            Toast.makeText(requireContext(), "Video guardado exitosamente", Toast.LENGTH_SHORT).show()
            loadInformationFromDB()
        } else {
            Toast.makeText(requireContext(), "Ocurrio un error", Toast.LENGTH_SHORT).show()
        }

        video = ""
        btnSaveVideo.isEnabled=true

        db.close()
    }

    fun generateRandomFilename(): String {
        val random = Random()
        val randomSuffix = random.nextInt(100000)
        return "video-$randomSuffix.mp4"
    }

    private fun copyVideoToAppStorage(videoUri: Uri): String? {
        try {
            val inputStream = requireActivity().contentResolver.openInputStream(videoUri)
            val randomFilename = generateRandomFilename()
            val outputDir = File(requireContext().filesDir, "videos")

            if (!outputDir.exists()) {
                outputDir.mkdirs()
            }

            val outputFile = File(outputDir, randomFilename)

            inputStream?.use { input ->
                FileOutputStream(outputFile).use { output ->
                    input.copyTo(output)
                }
            }

            return outputFile.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }

}
