package com.example.collabtask

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.example.collabtask.model.Board
import com.example.collabtask.model.Card
import com.example.collabtask.model.UserJoinedBoard
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddPersonalBoardDialogFragment  : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.add_personal_board_dialog, null)
        // Set up views inside the custom dialog layout here

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(view)
        val dialog = builder.create()

        val currentUser = FirebaseAuth.getInstance().currentUser

        val boardFirestore = FirebaseFirestore.getInstance().collection("board")
        val userJoinedBoardFirestore = FirebaseFirestore.getInstance().collection("userJoinedBoard")
        val addCard = { name: String ->
            boardFirestore.add(Board(name = name, ownerId = currentUser?.uid.toString())).addOnSuccessListener {board ->
                userJoinedBoardFirestore.add(UserJoinedBoard(boardId = board.id, userId = currentUser?.uid.toString())).addOnSuccessListener {
                    dialog.dismiss()
                }.addOnFailureListener {
                    boardFirestore.document(board.id).delete()
                    view.findViewById<TextInputLayout>(R.id.personal_board_name_input_layout).error =
                        "Có lỗi xảy ra, vui lòng thử lại sau."
                }
            }.addOnFailureListener {
                view.findViewById<TextInputLayout>(R.id.personal_board_name_input_layout).error =
                    "Có lỗi xảy ra, vui lòng thử lại sau."
            }
        }

        view.findViewById<ImageView>(R.id.add_personal_board_close).setOnClickListener {
            dialog.dismiss()
        }

        view.findViewById<EditText>(R.id.personal_board_name)
            .setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                    addCard(view.findViewById<EditText>(R.id.personal_board_name).text.toString())
                }
                false
            }

        return dialog
    }
}