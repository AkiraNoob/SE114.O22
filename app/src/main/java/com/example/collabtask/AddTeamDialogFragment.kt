package com.example.collabtask

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.example.collabtask.model.Card
import com.example.collabtask.model.Team
import com.example.collabtask.model.UserJoinedTeam
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddTeamDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.add_team_dialog, null)
        // Set up views inside the custom dialog layout here

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(view)
        val dialog = builder.create()

        val currentUser = FirebaseAuth.getInstance().currentUser

        val teamFirestore = FirebaseFirestore.getInstance().collection("team")
        val userJoinedTeamFirestore = FirebaseFirestore.getInstance().collection("userJoinedTeam")

        val addCard = { name: String ->
            teamFirestore.add(Team(name = name, ownerId = currentUser?.uid.toString()))
                .addOnSuccessListener { team ->

                    userJoinedTeamFirestore.add(
                        UserJoinedTeam(
                            userId = currentUser?.uid.toString(),
                            teamId = team.id
                        )
                    ).addOnSuccessListener {
                        dialog.dismiss()
                    }.addOnFailureListener {
                        teamFirestore.document(team.id).delete()
                        view.findViewById<TextInputLayout>(R.id.team_name_input_layout).error =
                            "Có lỗi xảy ra, vui lòng thử lại sau."
                    }
                }.addOnFailureListener {
                    view.findViewById<TextInputLayout>(R.id.team_name_input_layout).error =
                        "Có lỗi xảy ra, vui lòng thử lại sau."
                }
        }

        view.findViewById<ImageView>(R.id.add_team_close).setOnClickListener {
            dialog.dismiss()
        }

        view.findViewById<EditText>(R.id.team_name)
            .setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                    addCard(view.findViewById<EditText>(R.id.team_name).text.toString())
                }
                false
            }

        return dialog
    }
}