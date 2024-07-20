package com.example.collabtask

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.example.collabtask.model.Card
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AddCardDialogFragment(
    private val listId: String,
    private val onSuccessListener: (DocumentReference) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.add_card_dialog, null)
        // Set up views inside the custom dialog layout here

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(view)
        val dialog = builder.create()

        val cardFirestore = FirebaseFirestore.getInstance().collection("card")
        val addCard = { name: String ->
            cardFirestore.add(Card(name = name, listId = listId)).addOnSuccessListener {
                onSuccessListener(it)
                dialog.dismiss()
            }.addOnFailureListener {
                view.findViewById<TextInputLayout>(R.id.card_name_input_layout).error =
                    "Có lỗi xảy ra, vui lòng thử lại sau."
            }
        }

        view.findViewById<ImageView>(R.id.add_card_close).setOnClickListener {
            dialog.dismiss()
        }

        view.findViewById<EditText>(R.id.card_name)
            .setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                    addCard(view.findViewById<EditText>(R.id.card_name).text.toString())
                }
                false
            }

        return dialog
    }
}