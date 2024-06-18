// https://apptractor.ru/info/articles/pobochnye-effekty-jetpack-compose-v-podrobnostyah.html?noamp=mobile

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// SideEffect
/*
SideEffect — это функция Composable, которая позволяет нам выполнять побочный эффект при перекомпоновке
родительской Composable. Побочный эффект — это операция, которая не влияет непосредственно на
пользовательский интерфейс, например, ведение лога, аналитика или обновление внешнего состояния.
Эта функция полезна для выполнения операций, которые не зависят от состояния или свойств Composable.

При перекомпоновке Composable весь код внутри функции Composable выполняется заново, включая все
побочные эффекты. Однако пользовательский интерфейс будет обновлен только теми изменениями, которые
были внесены в состояние или реквизиты Composable.

Примеры использования SideEffect
- Ведение логов и аналитика
- Выполнение однократной инициализации, например, установка соединения с Bluetooth-устройством,
загрузка данных из файла или инициализация библиотеки.
*/
@Composable
fun Counter() {
    val count = remember { mutableStateOf(0) }

    SideEffect {
        Log.d("TEST", "Count is ${count.value}")
    }

    Column {
        Button(onClick = { count.value++ }) {
            Text("Increase Count")
        }

        Text("Counter ${count.value}")
    }
}

@Composable
fun Counter2() {
    val count = remember { mutableStateOf(0) }

    SideEffect {
        Log.d("TEST", "Count is ${count.value}")
    }

    Column {
        Button(onClick = { count.value++ }) {
            Text("Increase Count ${count.value}")
        }
    }
}

@Composable
fun Counter3() {
    val count = remember { mutableStateOf(0) }

    SideEffect {
        Log.d("TEST", "Outer Count is ${count.value}")
    }

    Column {
        Button(onClick = { count.value++ }) {
            SideEffect {
                Log.d("TEST", "Inner Count is ${count.value}")
            }

            Text("Increase Count ${count.value}")
        }
    }
}

// LaunchedEffect
/*
LaunchedEffect — это Composable-функция, выполняющая побочный эффект в отдельном скоупе корутины.
Эта функция полезна для выполнения операций, которые могут занимать длительное время, например,
сетевых вызовов или анимации, без блокировки потока пользовательского интерфейса.

Примеры использования LaunchedEffect
- Получение данных из сети
- Выполнение обработки изображений
- Обновление базы данных
 */
@Composable
fun MyComposable() {
    val isLoading = remember { mutableStateOf(false) }
    val data = remember { mutableStateOf(listOf<String>()) }

    LaunchedEffect(isLoading.value) {
        if (isLoading.value) {
            val newData = fetchData()
            data.value = newData
            isLoading.value = false
        }
    }

    Column {
        Button(onClick = { isLoading.value = true }) {
            Text("Fetch Data")
        }
        if (isLoading.value) {
            CircularProgressIndicator()
        } else {
            LazyColumn {
                items(data.value.size) { index ->
                    Text(text = data.value[index])
                }
            }
        }
    }
}

private suspend fun fetchData(): List<String> {
    delay(2000)
    return listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
}

// DisposableEffect
/*
DisposableEffect — это функция Composable, выполняющая побочный эффект при первом рендеринге родительского
Composable и утилизирующая эффект при удалении Composable из иерархии пользовательского интерфейса.
Эта функция полезна для управления ресурсами, которые должны быть очищены, когда Composable больше
не используется, например, слушателями событий или анимациями.

Примеры использования DisposableEffect
- Добавление и удаление слушателей событий
- Запуск и остановка анимации
- Привязка и отвязка ресурсов сенсоров, таких как Camera, LocationManager и т.д.
- Управление соединениями с базой данных
 */
@Composable
fun RunTimerScreen() {
    val isVisible = remember { mutableStateOf(true) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        if (isVisible.value)
            TimerScreen()

        Button(onClick = { isVisible.value = false }) {
            Text("Hide the timer")
        }
    }
}

@Composable
fun TimerScreen() {
    val elapsedTime = remember { mutableStateOf(0) }

    DisposableEffect(Unit) {
        val scope = CoroutineScope(Dispatchers.Default)
        val job = scope.launch {
            while (true) {
                delay(1000)
                elapsedTime.value += 1
                Log.d("TEST", "Timer is still working ${elapsedTime.value}")
            }
        }

        onDispose {
            job.cancel()
        }
    }

    Text(
        text = "Elapsed Time: ${elapsedTime.value}",
        modifier = Modifier.padding(16.dp),
        fontSize = 24.sp
    )
}