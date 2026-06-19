package com.vhuthu.clothes.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.SharedTransitionDefaults.BoundsTransform
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.vhuthu.clothes.R
import com.vhuthu.clothes.model.AllStoreItems
import com.vhuthu.clothes.remote.StoreViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreScreen(
    onItemClick: (Int) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    viewModel: StoreViewModel = hiltViewModel()
) {
    val items by viewModel.storeItems.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.errorMessage.collectAsStateWithLifecycle()
    val isOffline by viewModel.isOffline.collectAsStateWithLifecycle()


        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                Column {
                    TopAppBar(
                        title = {
                            Column {
                                Text(
                                    text = "Discover",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = Color.White
                                )
                                Text(
                                    text = "What are you looking for?",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = {}) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = "Cart",
                                    tint = Color.White
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent
                        )
                    )

                    AnimatedVisibility(
                        visible = isOffline,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        OfflineBanner()
                    }
                }
            }
        ) { padding ->
            when {
                isLoading -> StaggeredShimmer(padding)
                error != null && items.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        ErrorBanner(message = error!!, onRetry = { viewModel.retry() })
                    }
                }
                else -> {
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .padding(horizontal = 12.dp),
                        verticalItemSpacing = 10.dp,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(vertical = 12.dp)
                    ) {
                        items(items) { item ->
                            StoreItemCard(
                                item = item,
                                onClick = { onItemClick(item.id) },
                                animatedVisibilityScope = animatedVisibilityScope,
                                sharedTransitionScope = sharedTransitionScope
                            )
                        }
                    }
                }
            }
        }
}


@Composable
fun StoreItemCard(
    item: AllStoreItems,
    onClick: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope
) {
    with(sharedTransitionScope) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            onClick = onClick,
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.12f)
            ),
            border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.25f))
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                AsyncImage(
                    model = item.image,
                    contentDescription = item.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .sharedElement(
                            sharedContentState  = rememberSharedContentState(key = "image_${item.id}"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            boundsTransform = sharedBoundsTransform
                        ),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = item.category,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF00CED1),
                    modifier = Modifier.sharedElement(
                        sharedContentState  = rememberSharedContentState(key = "category_${item.id}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = sharedBoundsTransform
                    )
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = item.title,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    modifier = Modifier.sharedElement(
                        sharedContentState  = rememberSharedContentState(key = "title_${item.id}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = sharedBoundsTransform
                    )
                )
                Spacer(Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "R ${item.price}",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.sharedElement(
                            sharedContentState  = rememberSharedContentState(key = "price_${item.id}"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            boundsTransform = sharedBoundsTransform
                        )
                    )
                    Text(
                        text = "★ ${item.rating.rate}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFFFF69B4)
                    )
                }
            }
        }
    }
}


@Composable
fun StaggeredShimmer(padding: PaddingValues = PaddingValues()) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(horizontal = 12.dp),
        verticalItemSpacing = 10.dp,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        items(6) { index ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(if (index % 3 == 0) 150.dp else 120.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    )
                    Spacer(Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .width(60.dp)
                            .height(10.dp)
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant,
                                RoundedCornerShape(4.dp)
                            )
                    )
                    Spacer(Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant,
                                RoundedCornerShape(4.dp)
                            )
                    )
                    Spacer(Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .height(12.dp)
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant,
                                RoundedCornerShape(4.dp)
                            )
                    )
                    Spacer(Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .width(50.dp)
                            .height(12.dp)
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant,
                                RoundedCornerShape(4.dp)
                            )
                    )
                }
            }
        }
    }
}

val sharedBoundsTransform = BoundsTransform { _, _ ->
    spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    )
}

@Composable
fun AuroraBackground(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "aurora")

    val offset1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset1"
    )

    val offset2 by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset2"
    )

    val offset3 by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset3"
    )

    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // deep navy base
            drawRect(color = Color(0xFF0A0E27))

            // aurora blob 1 — violet/purple
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0x994B0082),
                        Color(0x557B2FBE),
                        Color.Transparent
                    ),
                    center = Offset(
                        x = size.width * (0.2f + offset1 * 0.3f),
                        y = size.height * (0.1f + offset1 * 0.2f)
                    ),
                    radius = size.width * 0.7f
                ),
                radius = size.width * 0.7f,
                center = Offset(
                    x = size.width * (0.2f + offset1 * 0.3f),
                    y = size.height * (0.1f + offset1 * 0.2f)
                )
            )

            // aurora blob 2 — teal/cyan
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0x8000CED1),
                        Color(0x5520B2AA),
                        Color.Transparent
                    ),
                    center = Offset(
                        x = size.width * (0.7f + offset2 * 0.2f),
                        y = size.height * (0.3f + offset2 * 0.15f)
                    ),
                    radius = size.width * 0.65f
                ),
                radius = size.width * 0.65f,
                center = Offset(
                    x = size.width * (0.7f + offset2 * 0.2f),
                    y = size.height * (0.3f + offset2 * 0.15f)
                )
            )

            // aurora blob 3 — rose/pink
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0x66FF69B4),
                        Color(0x44C71585),
                        Color.Transparent
                    ),
                    center = Offset(
                        x = size.width * (0.5f + offset3 * 0.3f),
                        y = size.height * (0.6f + offset3 * 0.2f)
                    ),
                    radius = size.width * 0.6f
                ),
                radius = size.width * 0.6f,
                center = Offset(
                    x = size.width * (0.5f + offset3 * 0.3f),
                    y = size.height * (0.6f + offset3 * 0.2f)
                )
            )

            // aurora blob 4 — deep blue
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0x551E90FF),
                        Color(0x330000CD),
                        Color.Transparent
                    ),
                    center = Offset(
                        x = size.width * (0.1f + offset2 * 0.4f),
                        y = size.height * (0.7f + offset1 * 0.2f)
                    ),
                    radius = size.width * 0.55f
                ),
                radius = size.width * 0.55f,
                center = Offset(
                    x = size.width * (0.1f + offset2 * 0.4f),
                    y = size.height * (0.7f + offset1 * 0.2f)
                )
            )
        }
        content()
    }
}

@Composable
fun ErrorBanner(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}


@Composable
fun OfflineBanner() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFF6B6B).copy(alpha = 0.85f))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.WifiOff,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = "You're offline",
            style = MaterialTheme.typography.labelMedium,
            color = Color.White
        )
    }
}