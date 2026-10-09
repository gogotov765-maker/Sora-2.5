package com.sora25.app2.data

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.sora25.app2.R
import kotlinx.coroutines.tasks.await

/** Вход через Google (Credential Manager) -> Firebase Auth. */
object GoogleAuth {
    suspend fun signIn(context: Context): Result<Unit> = runCatching {
        val option = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            // default_web_client_id генерирует плагин google-services из google-services.json
            .setServerClientId(context.getString(R.string.default_web_client_id))
            .build()
        val request = GetCredentialRequest.Builder().addCredentialOption(option).build()
        val result = CredentialManager.create(context).getCredential(context, request)
        val cred = result.credential
        check(cred is CustomCredential && cred.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            "Неожиданный тип входа"
        }
        val idToken = GoogleIdTokenCredential.createFrom(cred.data).idToken
        FirebaseAuth.getInstance()
            .signInWithCredential(GoogleAuthProvider.getCredential(idToken, null)).await()
        Unit
    }

    fun signOut() = FirebaseAuth.getInstance().signOut()
}
