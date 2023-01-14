package com.heyanle.easybangumi

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.heyanle.easybangumi.ui.home.Home
import com.heyanle.easybangumi.ui.home.search.Search
import com.heyanle.easybangumi.ui.player.Play
import com.heyanle.lib_anim.entity.Bangumi

/**
 * Created by HeYanLe on 2023/1/7 13:38.
 * https://github.com/heyanLE
 */

val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error("AppNavController Not Provide")
}


const val HOME = "home"
const val SEARCH = "search"

const val PLAY = "play"

// 缺省路由
const val DEFAULT = HOME

fun NavHostController.navigationSearch(keyword: String){
    navigate("${SEARCH}?keyword=${keyword}")
}

fun NavHostController.navigationPlay(bangumi: Bangumi){
    navigationPlay(bangumi.source, bangumi.detailUrl)
}

fun NavHostController.navigationPlay(source: String, detailUrl: String){
    navigate("${PLAY}?source=${source}&detailUrl=${detailUrl}")
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Nav() {
    val nav = rememberAnimatedNavController()
    CompositionLocalProvider(LocalNavController provides nav) {
        AnimatedNavHost(nav, DEFAULT,
            enterTransition = { slideInHorizontally(tween()) { it } },
            exitTransition = { slideOutHorizontally(tween()) { -it } + fadeOut(tween()) },
            popEnterTransition = { slideInHorizontally(tween()) { -it } },
            popExitTransition = { slideOutHorizontally(tween()) { it } })
        {

            composable(HOME){
                Home()
            }

            composable(
                "${SEARCH}?keyword={keyword}",
                arguments = listOf(navArgument("keyword") { defaultValue = "" })
            ){
                Search(it.arguments?.getString("keyword")?:"")
            }

            composable(
                "${PLAY}?source={source}&detailUrl={detailUrl}",
                arguments = listOf(
                    navArgument("source") { defaultValue = "" },
                    navArgument("detailUrl") { defaultValue = ""},
                )
            ){
                val source = it.arguments?.getString("source")?:""
                val detailUrl = it.arguments?.getString("detailUrl")?:""
                Play(source = source, detail = detailUrl)
            }


        }
    }
}