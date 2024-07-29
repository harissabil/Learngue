package com.harissabil.learngue.ui.navigation

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.HistoryEdu
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.harissabil.learngue.MainViewModel
import com.harissabil.learngue.R
import com.harissabil.learngue.ui.components.BottomBarItem
import com.harissabil.learngue.ui.components.CropperTopAppBar
import com.harissabil.learngue.ui.components.MainBottomBar
import com.harissabil.learngue.ui.screen.history.HistoryScreen
import com.harissabil.learngue.ui.screen.home.HomeScreen
import com.harissabil.learngue.ui.screen.quiz.QuizScreen
import com.harissabil.learngue.ui.screen.vocab_detail.VocabDetailScreen
import com.mr0xf00.easycrop.CropError
import com.mr0xf00.easycrop.CropResult
import com.mr0xf00.easycrop.crop
import com.mr0xf00.easycrop.rememberImageCropper
import com.mr0xf00.easycrop.ui.ImageCropperDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.io.File

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = koinViewModel(),
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val bottomBarItems = listOf(
        BottomBarItem(
            title = "Home",
            unselectedIcon = Icons.Outlined.Home,
            selectedIcon = Icons.Filled.Home,
        ),
        BottomBarItem(null, null, null),
        BottomBarItem(
            title = "History",
            unselectedIcon = Icons.Outlined.HistoryEdu,
            selectedIcon = Icons.Filled.HistoryEdu,
        ),
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = remember(navBackStackEntry) {
        Screen.fromRoute(navBackStackEntry?.destination?.route ?: "")
    }
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }
    selectedItem = when (currentRoute) {
        Screen.Home -> 0
        Screen.History -> 2
        else -> 1
    }

    var isBottomBarVisible by rememberSaveable { mutableStateOf(true) }
    isBottomBarVisible = when (currentRoute) {
        is Screen.VocabDetail -> false
        Screen.History -> true
        Screen.Home -> true
        Screen.Quiz -> false
        null -> false
    }

    val directory = File(context.cacheDir, "images")
    val tempUri = remember { mutableStateOf<Uri?>(null) }
    val authority = stringResource(id = R.string.fileprovider)

    val imageCropper = rememberImageCropper()
    val cropState = imageCropper.cropState
    if (cropState != null) ImageCropperDialog(
        state = cropState,
        topBar = { CropperTopAppBar(cropState) }
    )

    // for takePhotoLauncher used
    fun getTempUri(): Uri? {
        directory.let {
            it.mkdirs()
            val file = File.createTempFile(
                "image_" + System.currentTimeMillis().toString(),
                ".jpg",
                it
            )

            return FileProvider.getUriForFile(
                context,
                authority,
                file
            )
        }
    }

    val takePhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { _ ->
            tempUri.value?.let { imageUri ->
                scope.launch {
                    val result = imageCropper.crop(
                        uri = imageUri,
                        context = context
                    )
                    when (result) {
                        CropError.LoadingError -> {
                            tempUri.value = null
                        }

                        CropError.SavingError -> {
                            tempUri.value = null
                        }

                        CropResult.Cancelled -> {
                            tempUri.value = null
                        }

                        is CropResult.Success -> {
                            viewModel.onScanFromCamera(
                                context = context,
                                result.bitmap.asAndroidBitmap()
                            )
                        }
                    }
                }
            }
        }
    )

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission is granted, launch takePhotoLauncher
            val tmpUri = getTempUri()
            tempUri.value = tmpUri
            tempUri.value?.let { takePhotoLauncher.launch(it) }
        } else {
            Toast.makeText(context, "Camera permission is required.", Toast.LENGTH_SHORT).show()
        }
    }

    val textFromImage by viewModel.textFromImage.collectAsState()

    LaunchedEffect(key1 = textFromImage) {
        if (textFromImage != null) {
            navController.navigate(
                Screen.VocabDetail(
                    scannedTextId = 0,
                    textFromImage = textFromImage!!
                )
            )
            delay(500)
            viewModel.resetTextFromImage()
        }
    }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (isBottomBarVisible) {
                MainBottomBar(
                    items = bottomBarItems,
                    selectedIndex = selectedItem,
                    onItemSelected = { index ->
                        when (index) {
                            0 -> navController.navigate(Screen.Home)
                            2 -> navController.navigate(Screen.History)
                        }
                    }
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            if (isBottomBarVisible) {
                Box {
                    FloatingActionButton(
                        onClick = {
                            val permission = Manifest.permission.CAMERA
                            if (ContextCompat.checkSelfPermission(
                                    context,
                                    permission
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                // Permission is already granted, proceed to step 2
                                val tmpUri = getTempUri()
                                tempUri.value = tmpUri
                                tempUri.value?.let { takePhotoLauncher.launch(it) }
                            } else {
                                // Permission is not granted, request it
                                cameraPermissionLauncher.launch(permission)
                            }
                        },
                        shape = CircleShape,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(72.dp)
                            .offset(y = 52.dp),
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_launcher_foreground),
                            contentDescription = "Snap",
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home,
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            composable<Screen.Home> {
                HomeScreen(
                    onNavigateToQuizScreen = { navController.navigate(Screen.Quiz) }
                )
            }

            composable<Screen.VocabDetail> {
                LaunchedEffect(key1 = Unit) {
                    isBottomBarVisible = false
                }
                val args = it.toRoute<Screen.VocabDetail>()
                VocabDetailScreen(
                    scannedTextId = args.scannedTextId,
                    textFromImage = args.textFromImage,
                    onNavigateUp = { navController.navigateUp() }
                )
            }

            composable<Screen.History> {
                HistoryScreen(
                    onScannedTextClick = { scannedTextId ->
                        navController.navigate(
                            Screen.VocabDetail(
                                scannedTextId = scannedTextId,
                                textFromImage = null
                            )
                        )
                    },
                )
            }

            composable<Screen.Quiz> {
                LaunchedEffect(key1 = Unit) {
                    isBottomBarVisible = false
                }
                QuizScreen(
                    onNavigateUp = { navController.navigateUp() }
                )
            }
        }
    }
}