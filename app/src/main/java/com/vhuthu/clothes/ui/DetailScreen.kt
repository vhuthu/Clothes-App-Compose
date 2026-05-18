package com.vhuthu.clothes.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.vhuthu.clothes.R
import com.vhuthu.clothes.model.SingleStoreItem
import com.vhuthu.clothes.remote.data.DetailStoreViewModel

@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun DetailScreen(
//    itemId: String,
//    onBack: () -> Unit,
//    viewModel: DetailStoreViewModel = hiltViewModel<DetailStoreViewModel, DetailStoreViewModel.Factory>(
//        creationCallback = { factory -> factory.create(itemId) }
//    )
//) {
//    val item by viewModel.storeItem.collectAsStateWithLifecycle()
//    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
//    val error by viewModel.errorMessage.collectAsStateWithLifecycle()
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {},
//                navigationIcon = {
//                    Icon(
//                        modifier = Modifier.clickable{ onBack() },
//                        painter = painterResource(id = R.drawable.warning_icon),
//                        contentDescription = "back button",
//                        tint = MaterialTheme.colorScheme.error
//                    )
//                },
//                actions = {
////                    IconButton(onClick = {}) {
////                        Icon(Icons.Default.Share, contentDescription = "Share")
////                    }
//
//                    Icon(
//                        modifier = Modifier.clickable{  },
//                        painter = painterResource(id = R.drawable.warning_icon),
//                        contentDescription = "back button",
//                        tint = MaterialTheme.colorScheme.error
//                    )
//                }
//            )
//        }
//    ) { padding ->
//        when {
//            isLoading -> DetailShimmer(padding)
//            error != null -> ErrorBanner(message = error!!, onRetry = {})
//            item != null -> DetailContent(item = item!!, padding = padding)
//        }
//    }
//}

@Composable
fun DetailScreen(
    itemId: String,
    onBack: () -> Unit,
    viewModel: DetailStoreViewModel = hiltViewModel<DetailStoreViewModel, DetailStoreViewModel.Factory>(
        creationCallback = { factory -> factory.create(itemId) }
    )
) {
    val item by viewModel.storeItem.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.errorMessage.collectAsStateWithLifecycle()

    AuroraBackground(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {}) {
                            Icon(
                                Icons.Default.Share,
                                contentDescription = "Share",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        ) { padding ->
            when {
                isLoading -> DetailShimmer(padding)
                error != null -> ErrorBanner(message = error!!, onRetry = {})
                item != null -> DetailContent(item = item!!, padding = padding)
            }
        }
    }
}

//@Composable
//fun DetailContent(item: SingleStoreItem, padding: PaddingValues) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .verticalScroll(rememberScrollState())
//            .padding(padding)
//    ) {
//        // Image
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(280.dp)
//                .background(MaterialTheme.colorScheme.surfaceVariant),
//            contentAlignment = Alignment.Center
//        ) {
//            AsyncImage(
//                model = item.image,
//                contentDescription = item.title,
//                modifier = Modifier
//                    .size(220.dp)
//                    .padding(16.dp),
//                contentScale = ContentScale.Fit
//            )
//        }
//
//        Column(modifier = Modifier.padding(16.dp)) {
//            // Category badge
//            Surface(
//                shape = RoundedCornerShape(99.dp),
//                color = MaterialTheme.colorScheme.primaryContainer
//            ) {
//                Text(
//                    text = item.category,
//                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
//                    style = MaterialTheme.typography.labelSmall,
//                    color = MaterialTheme.colorScheme.onPrimaryContainer
//                )
//            }
//
//            Spacer(Modifier.height(10.dp))
//
//            // Title
//            Text(
//                text = item.title,
//                style = MaterialTheme.typography.titleMedium,
//                lineHeight = 22.sp
//            )
//
//            Spacer(Modifier.height(12.dp))
//
//            // Price + Rating row
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    text = "R${item.price}",
//                    style = MaterialTheme.typography.headlineSmall,
//                    fontWeight = FontWeight.Medium
//                )
//                Surface(
//                    shape = RoundedCornerShape(8.dp),
//                    color = MaterialTheme.colorScheme.surfaceVariant
//                ) {
//                    Row(
//                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.spacedBy(4.dp)
//                    ) {
////                        Icon(
////                            Icons.Default.Star,
////                            contentDescription = null,
////                            tint = Color(0xFFEF9F27),
////                            modifier = Modifier.size(16.dp)
////                        )
//                        Icon(
//                            painter = painterResource(id = R.drawable.warning_icon),
//                            contentDescription = "back button",
//                            tint = MaterialTheme.colorScheme.error
//                        )
//                        Text(
//                            text = "${item.rating.rate}",
//                            style = MaterialTheme.typography.bodyMedium,
//                            fontWeight = FontWeight.Medium
//                        )
//                        Text(
//                            text = "(${item.rating.count})",
//                            style = MaterialTheme.typography.bodySmall,
//                            color = MaterialTheme.colorScheme.onSurfaceVariant
//                        )
//                    }
//                }
//            }
//
//            HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp))
//
//            // Description
//            Text(
//                text = "Description",
//                style = MaterialTheme.typography.labelSmall,
//                color = MaterialTheme.colorScheme.onSurfaceVariant
//            )
//            Spacer(Modifier.height(6.dp))
//            Text(
//                text = item.description,
//                style = MaterialTheme.typography.bodySmall,
//                color = MaterialTheme.colorScheme.onSurfaceVariant,
//                lineHeight = 20.sp
//            )
//
//            Spacer(Modifier.height(16.dp))
//
//            // Stats row
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.spacedBy(10.dp)
//            ) {
//                StatCard(label = "Reviews", value = "${item.rating.count}", modifier = Modifier.weight(1f))
//                StatCard(label = "Rating", value = "${item.rating.rate} / 5", modifier = Modifier.weight(1f))
//            }
//
//            Spacer(Modifier.height(20.dp))
//
//            // Buttons
//            Button(
//                onClick = {},
//                modifier = Modifier.fillMaxWidth().height(52.dp),
//                shape = RoundedCornerShape(12.dp)
//            ) {
////                Icon(Icons.Default.ShoppingCart, contentDescription = null)
//                Icon(
//                    painter = painterResource(id = R.drawable.warning_icon),
//                    contentDescription = "back button",
//                    tint = MaterialTheme.colorScheme.error
//                )
//                Spacer(Modifier.width(8.dp))
//                Text("Add to cart", style = MaterialTheme.typography.bodyLarge)
//            }
//
//            Spacer(Modifier.height(10.dp))
//
//            OutlinedButton(
//                onClick = {},
//                modifier = Modifier.fillMaxWidth().height(48.dp),
//                shape = RoundedCornerShape(12.dp)
//            ) {
////                Icon(Icons.Default.FavoriteBorder, contentDescription = null)
//                Icon(
//                    painter = painterResource(id = R.drawable.warning_icon),
//                    contentDescription = "back button",
//                    tint = MaterialTheme.colorScheme.error
//                )
//                Spacer(Modifier.width(8.dp))
//                Text("Save to wishlist")
//            }
//        }
//    }
//}

@Composable
fun DetailContent(item: SingleStoreItem, padding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(padding)
    ) {
        // Image container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .background(Color.White.copy(alpha = 0.08f)),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = item.image,
                contentDescription = item.title,
                modifier = Modifier
                    .size(220.dp)
                    .padding(16.dp),
                contentScale = ContentScale.Fit
            )
        }

        Column(modifier = Modifier.padding(16.dp)) {

            // Category badge
            Surface(
                shape = RoundedCornerShape(99.dp),
                color = Color(0xFF00CED1).copy(alpha = 0.2f),
                border = BorderStroke(0.5.dp, Color(0xFF00CED1).copy(alpha = 0.5f))
            ) {
                Text(
                    text = item.category,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF00CED1)
                )
            }

            Spacer(Modifier.height(10.dp))

            // Title
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                lineHeight = 22.sp,
                color = Color.White
            )

            Spacer(Modifier.height(12.dp))

            // Price + Rating row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "R ${item.price}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White.copy(alpha = 0.1f),
                    border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.2f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFF69B4),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "${item.rating.rate}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                        Text(
                            text = "(${item.rating.count})",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 14.dp),
                color = Color.White.copy(alpha = 0.15f)
            )

            // Description
            Text(
                text = "Description",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.5f),
                letterSpacing = 0.08.sp
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.75f),
                lineHeight = 20.sp
            )

            Spacer(Modifier.height(16.dp))

            // Stats row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    label = "Reviews",
                    value = "${item.rating.count}",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "Rating",
                    value = "${item.rating.rate} / 5",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(20.dp))

            // Add to cart
            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7B2FBE).copy(alpha = 0.85f)
                )
            ) {
                Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = Color.White)
                Spacer(Modifier.width(8.dp))
                Text("Add to cart", style = MaterialTheme.typography.bodyLarge, color = Color.White)
            }

            Spacer(Modifier.height(10.dp))

            // Wishlist
            OutlinedButton(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.3f)),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White.copy(alpha = 0.08f)
                )
            ) {
                Icon(Icons.Default.FavoriteBorder, contentDescription = null, tint = Color(0xFFFF69B4))
                Spacer(Modifier.width(8.dp))
                Text("Save to wishlist", color = Color.White)
            }
        }
    }
}

//@Composable
//fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
//    Surface(
//        modifier = modifier,
//        shape = RoundedCornerShape(8.dp),
//        color = MaterialTheme.colorScheme.surfaceVariant
//    ) {
//        Column(modifier = Modifier.padding(12.dp)) {
//            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
//            Spacer(Modifier.height(4.dp))
//            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Medium)
//        }
//    }
//}

@Composable
fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = Color.White.copy(alpha = 0.1f),
        border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.5f)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }
    }
}

//@Composable
//fun DetailShimmer(padding: PaddingValues) {
//    Column(modifier = Modifier.padding(padding)) {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(280.dp)
//                .background(MaterialTheme.colorScheme.surfaceVariant)
//        )
//        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
//            Box(Modifier.width(80.dp).height(24.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(99.dp)))
//            Box(Modifier.fillMaxWidth().height(20.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(4.dp)))
//            Box(Modifier.fillMaxWidth(0.6f).height(20.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(4.dp)))
//            Box(Modifier.fillMaxWidth().height(120.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp)))
//        }
//    }
//}


@Composable
fun DetailShimmer(padding: PaddingValues) {
    Column(modifier = Modifier.padding(padding)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .background(Color.White.copy(alpha = 0.08f))
        )
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                Modifier
                    .width(80.dp)
                    .height(24.dp)
                    .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(99.dp))
            )
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
            )
            Box(
                Modifier
                    .fillMaxWidth(0.6f)
                    .height(20.dp)
                    .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
            )
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
            )
        }
    }
}