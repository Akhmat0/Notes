package ru.smak.lazyelems

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.smak.lazyelems.ui.theme.LazyElemsTheme
import ru.smak.lazyelems.viewmodels.MainViewModel
import ru.smak.lazyelems.viewmodels.Pages
import kotlin.getValue

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            LazyElemsTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(stringResource(R.string.title))
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                            ),
                            navigationIcon = {
                                if (viewModel.page != Pages.MAIN) {
                                    IconButton(
                                        onClick = {
                                            viewModel.back()
                                        }
                                    ) {
                                        Icon(
                                            painter = painterResource(R.drawable.outline_arrow_back_24),
                                            contentDescription = stringResource(R.string.back)
                                        )
                                    }
                                }
                            }
                        )
                    },
                    floatingActionButton = {
                        if (viewModel.page == Pages.LIST) {
                            FloatingActionButton(onClick = {
                                viewModel.addValue()
                            }) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = stringResource(R.string.add)
                                )
                            }
                        }
                    },
                    floatingActionButtonPosition = FabPosition.EndOverlay,
                ) { innerPadding ->
                    when (viewModel.page) {
                        Pages.MAIN -> MainContent(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                        ){
                            viewModel.toList()
                        }
                        Pages.LIST -> ListContent(
                            viewModel.values,
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    onPageChange: ()->Unit = {},
){
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(stringResource(R.string.main_content))
        Button(onClick = {
            onPageChange()
        }) {
            Text(stringResource(R.string.to_list))
        }
    }
}

@Composable
fun ListContent(
    list: List<Int>,
    modifier: Modifier = Modifier,
){
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        if (list.isEmpty()) {
            Text(stringResource(R.string.list_content))
        } else {
            LazyVerticalStaggeredGrid(
                modifier = Modifier.fillMaxSize(),
                columns = StaggeredGridCells.Adaptive(128.dp),
            ) {
                items(list.reversed()){ item ->
                    CardWithValue(item, modifier = Modifier.fillMaxWidth().padding(8.dp))
                }
            }
        }
    }
}

@Preview
@Composable
fun ListContentPreview(){
    LazyElemsTheme {
        ListContent(
            listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16),
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun CardWithValue(
    value: Int,
    modifier: Modifier = Modifier,
){
    ElevatedCard(
        modifier = modifier
    ) {
        Text(
            value.toString(),
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
        )
    }
}

@Preview
@Composable
fun CardWithValuePreview(){
    LazyElemsTheme {
        CardWithValue(3, modifier = Modifier.fillMaxWidth())
    }
}