package com.example.poliapp

import PoliSQLiteOpenHelper
import android.content.ClipData.Item
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBindings
import com.example.poliapp.db.contracts.PhotosContract
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Photos : Fragment() {

    companion object {
        fun newInstance() = Photos()
    }
    private lateinit var viewModel: PhotosViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_photos, container, false)

    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(PhotosViewModel::class.java)
        // Obtener referencia al botón

        // Obtener referencia al botón fab_add_photo y asignar el listener
        view.findViewById<FloatingActionButton>(R.id.fab_add_photo)?.setOnClickListener {
            findNavController().navigate(R.id.nav_photosadd)
        }

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PhotosViewModel::class.java)
        val button = view?.findViewById<FloatingActionButton>(R.id.fab_add_photo)

        button?.setOnClickListener {
            println("Botón presionado")
            findNavController().navigate(R.id.nav_photosadd)
        }

        val buttonSearch = view?.findViewById<Button>(R.id.btnSearchImage)
        val editTextSearch = view?.findViewById<EditText>(R.id.editTextSearch)

        buttonSearch?.setOnClickListener {
            val searchText = editTextSearch?.text.toString()
            loadInformationFromDB(searchText)
        }

        loadInformationFromDB(null)
    }

    private fun loadInformationFromDB(searchText: String?){
        val linearLayout = view?.findViewById<LinearLayout>(R.id.linearLayout)
        linearLayout?.removeAllViews()

        val dbHelper = PoliSQLiteOpenHelper(requireContext())
        val db = dbHelper.readableDatabase

        var selection: String? = null
        var selectionArgs: Array<String>? = null
        if (!searchText.isNullOrEmpty()) {
            selection = "${PhotosContract.PhotosEntry.COLUMN_NAME} LIKE ?"
            selectionArgs = arrayOf("%$searchText%")
        }

        val cursor = db.query(
            PhotosContract.PhotosEntry.TABLE_NAME,
            arrayOf(
                PhotosContract.PhotosEntry.COLUMN_NAME,
                PhotosContract.PhotosEntry.COLUMN_DESCRIPTION,
                PhotosContract.PhotosEntry.COLUMN_IMAGE
            ), selection, selectionArgs, null, null, null
        )
        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndexOrThrow(PhotosContract.PhotosEntry.COLUMN_NAME))
                val description = cursor.getString(cursor.getColumnIndexOrThrow(PhotosContract.PhotosEntry.COLUMN_DESCRIPTION))
                val image = cursor.getString(cursor.getColumnIndexOrThrow(PhotosContract.PhotosEntry.COLUMN_IMAGE))

                val listItemView = layoutInflater.inflate(R.layout.list_item, null)
                val imageView = listItemView.findViewById<ImageView>(R.id.imageView)
                val nameTextView = listItemView.findViewById<TextView>(R.id.nameTextView)
                val descriptionTextView = listItemView.findViewById<TextView>(R.id.descriptionTextView)

                if (!image.isNullOrEmpty() && image != "null"){
                    val imageUri = Uri.parse(image)

                    imageView.setImageURI(imageUri)
                    nameTextView.text = "$name"
                    descriptionTextView.text = "Descripción: $description"

                    linearLayout?.addView(listItemView)
                }

            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
    }
}