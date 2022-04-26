package net.noliaware.yumi.feature_login.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import net.noliaware.yumi.commun.DEVICE_ID
import net.noliaware.yumi.commun.LOGIN
import net.noliaware.yumi.feature_login.data.repository.DataStoreRepository.PreferencesKeys.DEVICE_ID_PREF
import net.noliaware.yumi.feature_login.data.repository.DataStoreRepository.PreferencesKeys.LOGIN_PREF
import net.noliaware.yumi.feature_login.domain.model.UserPreferences
import java.io.IOException

class DataStoreRepository(private val dataStore: DataStore<Preferences>) {

    private object PreferencesKeys {
        val LOGIN_PREF = stringPreferencesKey(LOGIN)
        val DEVICE_ID_PREF = stringPreferencesKey(DEVICE_ID)
    }

    suspend fun saveLogin(login: String) {
        dataStore.edit { preference ->
            preference[LOGIN_PREF] = login
        }
    }

    suspend fun saveDeviceId(deviceId: String) {
        dataStore.edit { preference ->
            preference[DEVICE_ID_PREF] = deviceId
        }
    }

    val readFromDataStore: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.d("DataStoreRepository", exception.message.toString())
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preference ->
            val login = preference[LOGIN_PREF] ?: ""
            val deviceId = preference[DEVICE_ID_PREF] ?: ""
            UserPreferences(login, deviceId)
        }

    suspend fun clearDataStore() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}