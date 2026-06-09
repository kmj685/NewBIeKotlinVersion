package com.newBie.new_bie.core.components

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.newBie.new_bie.core.utils.Constants.TAG

@Composable
fun rememberPhotoPicker(
    maxItems: Int = 10,
    onImageSelected: (List<Uri>) -> Unit
): () -> Unit{

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(maxItems)) { uris ->
        // Callback is invoked after the user selects media items or closes the
        // photo picker.
        if (uris.isNotEmpty()) {
            onImageSelected(uris)
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }
    return {
        launcher.launch( PickVisualMediaRequest(
            ActivityResultContracts.PickVisualMedia.ImageAndVideo))
    }
}

@Composable
fun rememberSinglePhotoPicker(
    onImageSelected: (Uri) -> Unit
): () -> Unit{

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects media items or closes the
        // photo picker.
        if (uri != null) {
            onImageSelected(uri)

        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }
    return {
        launcher.launch(PickVisualMediaRequest(
            ActivityResultContracts.PickVisualMedia.ImageAndVideo))
    }
}