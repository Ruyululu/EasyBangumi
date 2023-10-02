package com.heyanle.easybangumi4.ui.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.heyanle.easy_i18n.R
import com.heyanle.easybangumi4.preferences.SettingPreferences
import com.heyanle.easybangumi4.ui.common.StringSelectPreferenceItem
import com.heyanle.easybangumi4.ui.common.moeSnackBar
import com.heyanle.easybangumi4.utils.stringRes
import com.heyanle.injekt.core.Injekt

/**
 * Created by heyanlin on 2023/10/2.
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ColumnScope.DownloadSetting(
    nestedScrollConnection: NestedScrollConnection
){

    val settingPreferences: SettingPreferences by Injekt.injectLazy()

    Column(
        modifier = Modifier
            .weight(1f)
            .nestedScroll(nestedScrollConnection)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        val permissionState = rememberMultiplePermissionsState(
            permissions = listOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        ){
            if(it.filter {
                !it.value
            }.isEmpty()){
                stringRes(R.string.retry_when_permission).moeSnackBar()
            }else{
                stringRes(R.string.please_permission).moeSnackBar()
            }
        }

        val path by settingPreferences.downloadPath.flow().collectAsState(initial = settingPreferences.downloadPath.get())
        val selection = settingPreferences.downloadPathSelection
        StringSelectPreferenceItem(
            title = { Text(text = stringResource(id = R.string.download_path)) },
            textList = selection.map { it.second },
            select = selection.indexOfFirst { it.first == path }.let { if (it == -1) 0 else it }
        ) {
            // 私有目录无需权限
            if(it != 0 && permissionState.allPermissionsGranted){
                settingPreferences.downloadPath.set(selection[it].first)
                stringRes(R.string.next_download_work).moeSnackBar()
            }else{
                stringRes(R.string.please_permission).moeSnackBar()
                permissionState.launchMultiplePermissionRequest()
            }

        }
    }

}