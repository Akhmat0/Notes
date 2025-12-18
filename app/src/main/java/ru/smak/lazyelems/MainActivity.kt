package ru.smak.lazyelems

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.smak.lazyelems.db.Card
import ru.smak.lazyelems.ui.theme.LazyElemsTheme
import ru.smak.lazyelems.viewmodels.MainViewModel
import ru.smak.lazyelems.viewmodels.Pages
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LazyElemsTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text(stringResource(R.string.title)) },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                            ),
                            navigationIcon = {
                                if (viewModel.page != Pages.MAIN) {
                                    IconButton(onClick = { viewModel.back() }) {
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
                                viewModel.editableCard = null
                                viewModel.showDialog = true
                            }) {
                                Icon(
                                    painterResource(R.drawable.baseline_add_24),
                                    contentDescription = stringResource(R.string.add)
                                )
                            }
                        }
                    },
                    floatingActionButtonPosition = FabPosition.EndOverlay,
                ) { innerPadding ->
                    Crossfade(
                        targetState = viewModel.page,
                        animationSpec = tween(500),
                    ) { page ->
                        when (page) {
                            Pages.MAIN -> MainContent(
                                modifier = Modifier
                                    .padding(innerPadding)
                                    .fillMaxSize()
                            ) {
                                viewModel.toList()
                            }

                            Pages.LIST -> ListContent(
                                list = viewModel.values,
                                onLongClick = { card ->
                                    // редактирование
                                    viewModel.editableCard = card
                                    viewModel.showDialog = true
                                },
                                onDelete = { card ->
                                    viewModel.deleteValue(card)
                                },
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                    .padding(innerPadding)
                                    .fillMaxSize()
                            )
                        }
                    }

                    if (viewModel.showDialog) {
                        TextDialog(
                            card = viewModel.editableCard,
                            onDismiss = { viewModel.showDialog = false },
                            onSave = { title, text, priority ->
                                val editable = viewModel.editableCard
                                if (editable == null) {
                                    viewModel.addValue(title, text, priority)
                                } else {
                                    viewModel.updateValue(editable, title, text, priority)
                                }
                                viewModel.showDialog = false
                            }
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
    onPageChange: () -> Unit = {},
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(stringResource(R.string.main_content))
        Button(onClick = onPageChange) {
            Text(stringResource(R.string.to_list))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListContent(
    list: List<Card>,
    modifier: Modifier = Modifier,
    onLongClick: (Card) -> Unit,
    onDelete: (Card) -> Unit,
) {
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
                items(list) { item ->
                    CardWithValue(
                        value = item,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .combinedClickable(
                                onClick = { onDelete(item) },       // короткое нажатие — удалить
                                onLongClick = { onLongClick(item) } // долгое — редактировать
                            )
                    )
                }
            }
        }
    }
}
@Composable
fun CardWithValue(
    value: Card,
    modifier: Modifier = Modifier,
) {
    val formatter = remember {
        SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    }
    val dateText = formatter.format(value.lastModified)

    ElevatedCard(
        modifier = modifier
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {

            // Заголовок, если есть
            value.title?.takeIf { it.isNotBlank() }?.let { title ->
                Text(
                    title,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

            // Разделитель только если есть и заголовок, и текст
            if (!value.title.isNullOrBlank() && !value.text.isNullOrBlank()) {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    thickness = 1.dp
                )
            }

            // Текст, если есть
            value.text?.takeIf { it.isNotBlank() }?.let { text ->
                Text(
                    text,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    fontSize = 14.sp,
                )
            }

            // Дата и приоритет мелким шрифтом
            Text(
                text = "Изм.: $dateText | Приоритет: ${when (value.priority) {
                    0 -> "высокий"
                    1 -> "нормальный"
                    else -> "низкий"
                }}",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                fontSize = 12.sp,
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
fun TextDialog(
    card: Card? = null,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    onSave: (String, String, Int) -> Unit = { _, _, _ -> },
) {
    var userTitle by remember { mutableStateOf(card?.title ?: "") }
    var userText by remember { mutableStateOf(card?.text ?: "") }
    var priority by remember { mutableIntStateOf(card?.priority ?: 1) }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                onSave(userTitle, userText, priority)
            }) {
                Text("Ок")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) { Text("Отмена") }
        },
        text = {
            Column {
                OutlinedTextField(
                    value = userTitle,
                    onValueChange = { userTitle = it },
                    label = { Text("Заголовок (может быть пустым)") }
                )
                OutlinedTextField(
                    value = userText,
                    onValueChange = { userText = it },
                    label = { Text("Текст (может быть пустым)") }
                )

                Text(
                    text = "Приоритет:",
                    modifier = Modifier.padding(top = 8.dp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    PriorityChip("Высокий", 0, priority) { priority = it }
                    PriorityChip("Нормальный", 1, priority) { priority = it }
                    PriorityChip("Низкий", 2, priority) { priority = it }
                }


            }
        },
    )
}

@Composable
private fun PriorityChip(
    label: String,
    value: Int,
    selected: Int,
    onSelect: (Int) -> Unit
) {
    Button(
        onClick = { onSelect(value) },
        enabled = selected != value,
        modifier = Modifier.padding(end = 4.dp)   // без fillMaxWidth
    ) {
        Text(label, fontSize = 14.sp)
    }
}

