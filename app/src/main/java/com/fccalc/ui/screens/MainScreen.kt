package com.fccalc.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricalServices
import androidx.compose.material.icons.filled.Factory
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fccalc.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToCableSelection: () -> Unit,
    onNavigateToSCGPP: () -> Unit,
    onNavigateToSubstation: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.main_title),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1976D2),
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Main content with buttons
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Button 1: Cable Selection
                MainMenuButton(
                    icon = Icons.Default.ElectricalServices,
                    title = stringResource(R.string.cable_selection_title),
                    subtitle = stringResource(R.string.cable_selection_subtitle),
                    onClick = onNavigateToCableSelection
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Button 2: Short Circuit GPP
                MainMenuButton(
                    icon = Icons.Default.FlashOn,
                    title = stringResource(R.string.sc_gpp_title),
                    subtitle = stringResource(R.string.sc_gpp_subtitle),
                    onClick = onNavigateToSCGPP
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Button 3: Substation
                MainMenuButton(
                    icon = Icons.Default.Factory,
                    title = stringResource(R.string.substation_title),
                    subtitle = stringResource(R.string.substation_subtitle),
                    onClick = onNavigateToSubstation
                )
            }

            // Footer
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.footer_version),
                    fontSize = 12.sp,
                    color = Color(0xFF757575)
                )
                Text(
                    text = stringResource(R.string.footer_android),
                    fontSize = 12.sp,
                    color = Color(0xFF757575)
                )
                Text(
                    text = stringResource(R.string.footer_copyright),
                    fontSize = 12.sp,
                    color = Color(0xFF757575)
                )
            }
        }
    }
}

@Composable
fun MainMenuButton(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF1976D2)
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }
    }
}
