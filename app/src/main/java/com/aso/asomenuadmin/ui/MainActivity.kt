package com.aso.asomenuadmin.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aso.asomenuadmin.ui.component.DrawerScreen
import com.aso.asomenuadmin.ui.navigation.MainDestinations
import com.aso.asomenuadmin.ui.navigation.rememberMyNavController
import com.aso.asomenuadmin.ui.screens.addMenuItem.AddMenuItemScreen
import com.aso.asomenuadmin.ui.screens.menu.MenuScreen
import com.aso.asomenuadmin.ui.screens.orders.OrdersScreen
import com.aso.asomenuadmin.ui.screens.recipe.RecipeScreen
import com.aso.asomenuadmin.ui.theme.MenuAdminTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            // set rtl layout direction
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                MenuAdminTheme {
                    MyApp()
                }
            }

        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyApp() {
    val myNavController = rememberMyNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    var isDrawerGestureEnabled by remember { mutableStateOf(false) }


    Scaffold(modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
//            TopAppBar(title = myNavController.currentRoute?.uppercase() ?: "4:20 Coffee",
//                onMenuClick = { coroutineScope.launch { drawerState.open() } })
        }) {
        ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
            DrawerScreen(drawerState = drawerState,
                onUpPressed = { coroutineScope.launch { drawerState.close() } },
                onItemClick = { route ->
                    coroutineScope.launch { drawerState.close() }
                    myNavController.navigateToBottomBarRoute(route.route)
                })

        }, content = {
            NavHost(
                navController = myNavController.navController,
                startDestination = MainDestinations.ADD_MENU_ITEM_ROUTE
            ) {
                myNavGraph(upPress = myNavController::upPress,
                    onNavigateToBottomBarRoute = { route ->
                        isDrawerGestureEnabled = true
                        myNavController.navigateToBottomBarRoute(route)
                    },
                    onNavigateToAnySubScreen = { route, backStackEntry ->
                        isDrawerGestureEnabled = true
                        myNavController.navigateToAnyScreen(
                            route, backStackEntry
                        )
                    },
                    drawerState = drawerState,
                    onNavigateWithParam = { route, id, backStackEntry ->
                        isDrawerGestureEnabled = true
                        myNavController.navigateWithParam(
                            route = route, param = id, from = backStackEntry
                        )
                    })
            }

        })
    }

}

@OptIn(ExperimentalMaterial3Api::class)
private fun NavGraphBuilder.myNavGraph(
    upPress: () -> Unit,
    onNavigateToBottomBarRoute: (String) -> Unit,
    onNavigateToAnySubScreen: (String, NavBackStackEntry) -> Unit,
    drawerState: DrawerState,
    onNavigateWithParam: (String, Long, NavBackStackEntry) -> Unit,
) {

    composable(
        route = MainDestinations.ORDER_ROUTE,
    ) { backStackEntry ->
        OrdersScreen(drawerState, onNavigateWithParam = { route, param ->
            onNavigateWithParam(
                route, param, backStackEntry
            )
        })
//        LoginScreen(
//            onNavigateToSubScreen = onNavigateToAnySubScreen,
//            backStackEntry = backStackEntry
//        )
    }

    composable(
        route = "${MainDestinations.RECIPE_ROUTE}/{productId}",
        arguments = listOf(navArgument("productId") { type = NavType.LongType })
    ) {
        val productId = it.arguments?.getLong("productId") ?: return@composable
        RecipeScreen(productId)
    }

    composable(route = MainDestinations.MENU_ITEM_ROUTE) {

    }
    composable(route = MainDestinations.ORDER_HISTORY_ROUTE) {
        OrdersScreen(drawerState, onNavigateWithParam = { route, param ->
            onNavigateWithParam(
                route, param, it
            )
        })
    }
    composable(route = MainDestinations.ADD_MENU_ITEM_ROUTE) {
        AddMenuItemScreen()
    }
    composable(route = MainDestinations.MENU_LIST_ROUTE) {
        MenuScreen()
    }
}
