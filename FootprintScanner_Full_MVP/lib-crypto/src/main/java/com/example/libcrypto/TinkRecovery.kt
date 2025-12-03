/*
Tink-based hybrid encryption example (conceptual).
Requires: implementation('com.google.crypto.tink:tink-android:1.8.0')
This file demonstrates how to use Tink's HybridEncrypt to encrypt the data key
to the org's public key (in practice use proper key management).
*/
package com.example.libcrypto

import android.content.Context
import com.google.crypto.tink.hybrid.HybridConfig
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.hybrid.HybridEncrypt
import com.google.crypto.tink.HybridEncryptFactory
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import java.nio.charset.StandardCharsets
import org.json.JSONObject
import android.util.Base64

object TinkRecovery {
    init {
        HybridConfig.register()
    }

    // orgPublicKeysetJson: the org's public keyset in JSON (obtained securely)
    fun createHybridWrappedKey(ctx: Context, orgPublicKeysetJson: String): String {
        // In production: load KeysetHandle from JSON using cleartext for demo only.
        // Use AndroidKeysetManager or a secure keyset storage in real apps.
        val publicHandle = KeysetHandle.fromJson(orgPublicKeysetJson)
        val hybridEncrypt: HybridEncrypt = publicHandle.getPrimitive(HybridEncrypt::class.java)
        val vm = VaultManager(ctx)
        val file = java.io.File(ctx.filesDir, "vault.bin")
        if (!file.exists()) throw IllegalStateException("no vault")
        val vaultJson = file.readText()
        val obj = JSONObject(vaultJson)
        val wrappedB64 = obj.getString("wrapped_data_key")
        val wrapped = Base64.decode(wrappedB64, Base64.NO_WRAP)
        // unwrap local wrapped_data_key to get raw dataKey
        val dataKey = CryptoManager.unwrapDataKey(wrapped)
        // encrypt dataKey with org public key
        val ciphertext = hybridEncrypt.encrypt(dataKey, null)
        val packet = JSONObject()
        packet.put("version", "1")
        packet.put("vault", obj)
        packet.put("wrapped_for_org", Base64.encodeToString(ciphertext, Base64.NO_WRAP))
        return packet.toString()
    }
}
