package com.aso.asomenuadmin.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aso.asomenuadmin.R
import com.aso.asomenuadmin.ui.theme.MenuAdminTheme

@Composable
fun DrawerScreen(
    modifier: Modifier = Modifier,
    onItemClick: (DrawerItem) -> Unit,
    drawerState: DrawerState,
    onUpPressed: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .width(250.dp)
            .background(MaterialTheme.colorScheme.primary)// Set the width to 200.dp (adjust as needed)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.menu),
                    contentDescription = "logo",
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.CenterVertically)
                )

            }
            val coroutineScope = rememberCoroutineScope()

            DrawerItem(icon = ImageVector.vectorResource(id = R.drawable.orders),
                label = stringResource(R.string.orders),
                onClick = {
                    onItemClick(DrawerItem.Orders)
                })
            DrawerItem(icon = ImageVector.vectorResource(id = R.drawable.menu),
                label = stringResource(R.string.menu),
                onClick = {
                    onItemClick(DrawerItem.Menu)
                })
            DrawerItem(icon = ImageVector.vectorResource(id = R.drawable.history),
                label = stringResource(R.string.order_history),
                onClick = {
                    onItemClick(DrawerItem.OrderHistory)
                })
//            DrawerItem(
//                icon = Icons.Default.Home,
//                label = "Order History",
//                onClick = { onItemClick(DrawerItem.OrderHistory) }
//            )
        }
    }
}

@Composable
fun DrawerItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .height(64.dp)
        .clickable { onClick() }
        .padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
//            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

sealed class DrawerItem(val route: String) {
    object Orders : DrawerItem("order")
    object Menu : DrawerItem("menu_list")
    object OrderHistory : DrawerItem("order_history")
}

@Preview(showBackground = true)
@Composable
fun DrawerScreenPreview() {
    val coroutineScope = rememberCoroutineScope()
    MenuAdminTheme {
        DrawerScreen(onItemClick = {},
            drawerState = DrawerState(DrawerValue.Closed),
            onUpPressed = {})
    }
}
