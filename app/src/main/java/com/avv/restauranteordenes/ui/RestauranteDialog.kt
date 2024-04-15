package com.avv.restauranteordenes.ui

import android.R
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.avv.restauranteordenes.application.RestauranteDBApp
import com.avv.restauranteordenes.data.RestauranteRepository
import com.avv.restauranteordenes.data.db.model.RestauranteEntity
import com.avv.restauranteordenes.databinding.RestauranteDialogBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException



class RestauranteDialog(
    private val newComida: Boolean = true,
    private var restaurante: RestauranteEntity = RestauranteEntity(
        comida = "",
        nombre = "",
        propina = ""
    ),
    private val updateUI: () -> Unit,
    private val message: (String) -> Unit
) : DialogFragment() {
    private var _binding: RestauranteDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: Dialog

    private lateinit var comidaImageView: ImageView

    private var saveButton: Button? = null

    private lateinit var repository: RestauranteRepository


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = RestauranteDialogBinding.inflate(requireActivity().layoutInflater)

        builder = AlertDialog.Builder(requireContext())

        repository = (requireContext().applicationContext as RestauranteDBApp).repository


        val comidasOptions = arrayOf("Selecciona Comida", "Chilaquiles", "Tamales", "Quesadillas")
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, comidasOptions)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.spPlayer.adapter= adapter

        var selectedRestaurante: String? = null


        binding.spPlayer.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                saveButton?.isEnabled = validateFields()


                selectedRestaurante = comidasOptions[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {


            }
        })

        binding.apply {


            binding.tietTeam.setText(restaurante.nombre)
            binding.tietNationality.setText(restaurante.propina)

        }

        dialog = if (newComida)
            buildDialog(context?.getString(com.avv.restauranteordenes.R.string.textSave) ?: "Save", context?.getString(com.avv.restauranteordenes.R.string.textCancel) ?: "Cancel", {

                restaurante.apply {

                    comida = selectedRestaurante ?: ""

                    nombre = binding.tietTeam.text.toString()
                    propina = binding.tietNationality.text.toString()
                }
                try {
                    lifecycleScope.launch {

                        val result = async{
                            repository.insertOrden(restaurante)
                        }

                        result.await()

                        withContext(Dispatchers.Main){
                            message(context?.getString(com.avv.restauranteordenes.R.string.textPlayerSaved) ?: "Game saved successfully")

                            updateUI()
                        }

                    }

                }catch(e: IOException){
                    e.printStackTrace()

                    message(context?.getString(com.avv.restauranteordenes.R.string.textErrorPlayerSaved) ?: "Error saving game")

                }
            }, {


            })
        else
            buildDialog(context?.getString(com.avv.restauranteordenes.R.string.textUpdate) ?: "Update", context?.getString(com.avv.restauranteordenes.R.string.textDelete) ?: "Delete", {

                restaurante.apply {

                    comida = selectedRestaurante?: ""
                    nombre = binding.tietTeam.text.toString()
                    propina = binding.tietNationality.text.toString()
                }
                try {
                    lifecycleScope.launch {
                        val result = async {
                            repository.updateOrden(restaurante)
                        }

                        result.await()

                        withContext(Dispatchers.Main){
                            message(context?.getString(com.avv.restauranteordenes.R.string.textPlayerUpdated) ?: "Player saved successfully")
                            updateUI()
                        }
                    }

                }catch(e: IOException) {
                    e.printStackTrace()

                    message(context?.getString(com.avv.restauranteordenes.R.string.textErrorPlayerSaved) ?: "Error saving player")

                }
            }, {

                val context = requireContext()

                AlertDialog.Builder(requireContext())
                    .setTitle(context?.getString(com.avv.restauranteordenes.R.string.textConfirmation) ?: "Confirmation")
                    .setMessage(getString(com.avv.restauranteordenes.R.string.confirm_delete_player, restaurante.comida))
                    .setPositiveButton(context?.getString(com.avv.restauranteordenes.R.string.textAccept) ?: "Accept"){ _, _ ->
                        try {
                            lifecycleScope.launch {

                                val result = async {
                                    repository.deleteOrden(restaurante)
                                }

                                result.await()

                                withContext(Dispatchers.Main){
                                    message(context?.getString(com.avv.restauranteordenes.R.string.textPlayerDeleted) ?: "Player deleted successfully")

                                    updateUI()
                                }
                            }

                        }catch(e: IOException) {
                            e.printStackTrace()

                            message(context?.getString(com.avv.restauranteordenes.R.string.textErrorPlayerDeleted) ?: "Error deleting player")

                        }
                    }
                    .setNegativeButton(context?.getString(com.avv.restauranteordenes.R.string.textCancel) ?: "Cancel"){ dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            })

        return dialog
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    override fun onStart() {
        super.onStart()


        val alertDialog = dialog as AlertDialog
        saveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        saveButton?.isEnabled = false




        binding.tietTeam.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })

        binding.tietNationality.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })

    }

    private fun validateFields(): Boolean {
        val selectedRestaurante = binding.spPlayer.selectedItem?.toString() ?: ""
        val nombre = binding.tietTeam.text.toString()
        val propina = binding.tietNationality.text.toString()

        return ((selectedRestaurante != "Selecciona una comida") && nombre.isNotEmpty() && propina.isNotEmpty())
    }

    private fun buildDialog(
        btn1Text: String,
        btn2Text: String,
        positiveButton: () -> Unit,
        negativeButton: () -> Unit
    ): Dialog =
        builder.setView(binding.root)
            .setTitle("Orden")
            .setPositiveButton(btn1Text) { _, _ ->

                positiveButton()
            }
            .setNegativeButton(btn2Text) { _, _ ->

                negativeButton()
            }
            .create()
}