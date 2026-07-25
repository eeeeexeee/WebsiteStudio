package com.example.data.remote

import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

sealed class PublishResult {
    data class Success(val liveUrl: String, val commitSha: String) : PublishResult()
    data class Error(val message: String) : PublishResult()
}

class GitHubPublisher {

    private val client = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .build()

    suspend fun publishWebsite(
        username: String,
        repoName: String,
        patToken: String,
        htmlContent: String,
        commitMessage: String
    ): PublishResult = withContext(Dispatchers.IO) {
        try {
            val cleanUsername = username.trim()
            val cleanRepo = repoName.trim()
            val cleanToken = patToken.trim()

            if (cleanUsername.isEmpty() || cleanRepo.isEmpty() || cleanToken.isEmpty()) {
                return@withContext PublishResult.Error("Please provide GitHub Username, Repository Name, and Personal Access Token (PAT).")
            }

            val fileUrl = "https://api.github.com/repos/$cleanUsername/$cleanRepo/contents/index.html"

            // Step 1: Check if index.html already exists to retrieve SHA
            val getRequest = Request.Builder()
                .url(fileUrl)
                .addHeader("Authorization", "Bearer $cleanToken")
                .addHeader("Accept", "application/vnd.github.v3+json")
                .addHeader("User-Agent", "WebStudio-Android-App")
                .get()
                .build()

            var existingSha: String? = null

            try {
                client.newCall(getRequest).execute().use { response ->
                    if (response.isSuccessful) {
                        val bodyString = response.body?.string()
                        if (!bodyString.isNullOrEmpty()) {
                            val json = JSONObject(bodyString)
                            existingSha = json.optString("sha", null)
                        }
                    }
                }
            } catch (e: Exception) {
                // Ignore GET error, file might not exist yet
            }

            // Step 2: Encode HTML content to Base64
            val base64Content = Base64.encodeToString(
                htmlContent.toByteArray(Charsets.UTF_8),
                Base64.NO_WRAP
            )

            // Step 3: Build JSON Payload
            val jsonPayload = JSONObject().apply {
                put("message", commitMessage.ifBlank { "Publish website via WebStudio Android" })
                put("content", base64Content)
                if (!existingSha.isNullOrEmpty()) {
                    put("sha", existingSha)
                }
            }

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = jsonPayload.toString().toRequestBody(mediaType)

            val putRequest = Request.Builder()
                .url(fileUrl)
                .addHeader("Authorization", "Bearer $cleanToken")
                .addHeader("Accept", "application/vnd.github.v3+json")
                .addHeader("User-Agent", "WebStudio-Android-App")
                .put(requestBody)
                .build()

            client.newCall(putRequest).execute().use { response ->
                val responseBodyStr = response.body?.string() ?: ""
                if (response.isSuccessful || response.code == 200 || response.code == 201) {
                    val responseJson = if (responseBodyStr.isNotBlank()) JSONObject(responseBodyStr) else JSONObject()
                    val commitSha = responseJson.optJSONObject("commit")?.optString("sha") ?: "latest"
                    val liveUrl = "https://$cleanUsername.github.io/$cleanRepo"
                    PublishResult.Success(liveUrl = liveUrl, commitSha = commitSha)
                } else {
                    val errorMessage = try {
                        val errorJson = JSONObject(responseBodyStr)
                        errorJson.optString("message", "HTTP Error ${response.code}")
                    } catch (e: Exception) {
                        "HTTP ${response.code}: ${response.message}"
                    }
                    
                    val hint = when (response.code) {
                        401 -> " (Authentication failed. Check your PAT token permissions)"
                        404 -> " (Repository not found. Ensure repository exists under https://github.com/$cleanUsername/$cleanRepo)"
                        422 -> " (Unprocessable entity. Ensure PAT has repo workflow/write permissions)"
                        else -> ""
                    }
                    
                    PublishResult.Error("GitHub API Error: $errorMessage$hint")
                }
            }
        } catch (e: Exception) {
            PublishResult.Error("Failed to connect to GitHub: ${e.localizedMessage ?: "Network error"}")
        }
    }
}
