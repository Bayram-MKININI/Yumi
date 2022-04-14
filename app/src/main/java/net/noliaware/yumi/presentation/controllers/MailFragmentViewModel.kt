package net.noliaware.yumi.presentation.controllers

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.noliaware.yumi.commun.*
import net.noliaware.yumi.domain.model.*
import net.noliaware.yumi.commun.util.FileIOUtils
import org.json.JSONArray
import org.json.JSONObject

class MailFragmentViewModel(application: Application) : AndroidViewModel(application) {

    val messagesInboxList = mutableListOf<Message>()
    val liveMessagesInboxList = MutableLiveData<MutableList<Message>>()
    //val errorInboxResponseLiveData = MutableLiveData<ErrorResponse>()

    private val messagesOutboxList = mutableListOf<Message>()
    val liveMessagesOutboxList = MutableLiveData<MutableList<Message>>()
    //val errorOutboxResponseLiveData = MutableLiveData<ErrorResponse>()

    val messageSent = MutableLiveData<Boolean>()
    //val errorSentResponseLiveData = MutableLiveData<ErrorResponse>()

    init {
        liveMessagesInboxList.value = ArrayList()
        liveMessagesOutboxList.value = ArrayList()
    }

    fun loadInboxMessages() {

        if (messagesInboxList.isNotEmpty())
            messagesInboxList.clear()

        viewModelScope.launch {

            val decrypted = FileIOUtils.readEncryptedDataFromFile(
                getApplication<Application>().applicationContext,
                MESSAGE_INBOX_DAT
            )

            if (decrypted.isBlank()) {
                callGetInboxMessageWebservice()
                return@launch
            }

            JSONArray(decrypted).let {

                for (i in 0 until it.length()) {
                    val message = Message()
                    message.fillObjectFromJSON(it.getJSONObject(i))
                    messagesInboxList.add(message)
                }
            }

            messagesInboxList.sortBy { it.timestamp }

            callGetInboxMessageWebservice()
        }
    }

    private fun callGetInboxMessageWebservice() {

        val parameters = hashMapOf<String, String>()
        parameters[LIMIT] = 12.toString()
        parameters[OFFSET] = messagesInboxList.size.toString()

        /*WebserviceProxy(
            urlString = GET_MESSAGES_INBOX_URL,
            parameters = parameters,
            onJSONObjectReturned = { responseJsonObject ->

                fillListWithMessageResponse(
                    messagesInboxList,
                    responseJsonObject,
                    notifyCompletion = {
                        liveMessagesInboxList.postValue(messagesInboxList)
                    })

                if (messagesInboxList.isNotEmpty())
                    saveMessagesToStorage(messagesInboxList, MESSAGE_INBOX_DAT)
            },
            onServiceError = { errorResponse ->
                errorInboxResponseLiveData.postValue(errorResponse)
            }
        ).call()

         */
    }

    fun loadOutboxMessages() {

        if (messagesOutboxList.isNotEmpty())
            messagesOutboxList.clear()

        viewModelScope.launch {

            val decrypted = FileIOUtils.readEncryptedDataFromFile(
                getApplication<Application>().applicationContext,
                MESSAGE_OUTBOX_DAT
            )

            Log.e("decrypted", decrypted)

            if (decrypted.isBlank()) {
                callGetOutboxMessageWebservice()
                return@launch
            }

            JSONArray(decrypted).let {

                for (i in 0 until it.length()) {
                    val message = Message()
                    message.fillObjectFromJSON(it.getJSONObject(i))
                    messagesOutboxList.add(message)
                }
            }

            messagesOutboxList.sortBy { it.timestamp }

            callGetOutboxMessageWebservice()
        }
    }

    private fun callGetOutboxMessageWebservice() {

        val parameters = hashMapOf<String, String>()
        parameters[LIMIT] = 12.toString()
        parameters[OFFSET] = messagesOutboxList.size.toString()

        /*WebserviceProxy(
            urlString = GET_MESSAGES_OUTBOX_URL,
            parameters = parameters,
            onJSONObjectReturned = { responseJsonObject ->

                fillListWithMessageResponse(
                    messagesOutboxList,
                    responseJsonObject,
                    notifyCompletion = {
                        liveMessagesOutboxList.postValue(messagesOutboxList)
                    })

                if (messagesOutboxList.isNotEmpty())
                    saveMessagesToStorage(messagesOutboxList, MESSAGE_OUTBOX_DAT)
            },
            onServiceError = { errorResponse ->
                errorOutboxResponseLiveData.postValue(errorResponse)
            }
        ).call()

         */
    }

    fun sendMessageWebservice(subject: String, text: String) {

        val parameters = hashMapOf<String, String>()
        parameters[SUBJECT] = subject
        parameters[MESSAGE] = text

        /*WebserviceProxy(
            urlString = SEND_MESSAGES_URL,
            parameters = parameters,
            onJSONObjectReturned = {
                messageSent.postValue(true)
            },
            onServiceError = { errorResponse ->
                errorSentResponseLiveData.postValue(errorResponse)
            }
        ).call()

         */
    }

    private fun fillListWithMessageResponse(
        messagesList: MutableList<Message>,
        responseJsonObject: JSONObject,
        notifyCompletion: () -> Unit
    ) {

        responseJsonObject.optJSONArray(MESSAGES)?.let { jsonArray ->

            Log.e(MESSAGES, jsonArray.toString())

            for (i in 0 until jsonArray.length()) {
                val message = Message()
                message.fillObjectFromJSON(jsonArray.getJSONObject(i))
                messagesList.add(message)
            }

            messagesList.sortBy { it.timestamp }
        }

        notifyCompletion()
    }

    private fun saveMessagesToStorage(messagesList: MutableList<Message>, fileName: String) {

        val messagesJsonArray = JSONArray()

        for (message in messagesList) {
            messagesJsonArray.put(message.parseToJSON())
        }

        FileIOUtils.encryptDataAndSaveInFile(
            getApplication<Application>().applicationContext,
            fileName,
            messagesJsonArray.toString()
        )
    }
}