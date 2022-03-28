package com.luiq54.sockets_compose

import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.luiq54.sockets_compose.ui.theme.SocketscomposeTheme
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.InetSocketAddress
import java.net.Socket


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
//        TrafficStats.setThreadStatsTag(Thread.currentThread().id.toInt())
        val policy = ThreadPolicy.Builder()
            .permitAll().build()
        StrictMode.setThreadPolicy(policy)
        super.onCreate(savedInstanceState)
        setContent {
            SocketscomposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    InputFieldsComposable()
                }
            }
        }
    }
}

@Composable
fun InputFieldsComposable() {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var host by remember { mutableStateOf("192.168.0.100") }
    var port by remember { mutableStateOf("8080") }
    var x by remember { mutableStateOf("0") }
    var radius by remember { mutableStateOf("0") }
    var hasResults by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var output by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .padding(10.dp),

        ) {

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                //TODO multiline and validations for
                value = host,
                onValueChange = { host = it },
                label = { Text("Host") },
                maxLines = 1,
            )
            OutlinedTextField(
                value = port,
                onValueChange = { port = it },
                label = { Text("Port") },
                maxLines = 1
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = x,
                onValueChange = { x = it },
                label = { Text("X") },
                maxLines = 1,
                modifier = Modifier
                    .weight(1f)
            )
            OutlinedTextField(
                value = radius,
                onValueChange = { radius = it },
                label = { Text("Radius") },
                maxLines = 1,
                modifier = Modifier
                    .weight(1f)
            )
        }

        Button(
            onClick = {
                if (!isLoading) {
                    scope.launch {
                        isLoading = true
                        try {
                            output = communicateData(
                                host = host,
                                port = port,
                                x = x,
                                radius = radius
                            )
                        } catch (e: Exception) {
                            Toast.makeText(context, "Connection Error", Toast.LENGTH_LONG).show()
                        }
                        hasResults = true
                        isLoading = false
                    }
                }

            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()

        ) {
            Text("Send Data!")
        }
        if (isLoading) CircularProgressIndicator()
        if (hasResults) Text(output)


    }
}


@Preview(showBackground = true)
@Composable
fun InputFieldsComposablePreview() {
    InputFieldsComposable()
}

fun communicateData(host: String, port: String, x: String, radius: String): String {
    val sc = Socket()
    sc.connect(InetSocketAddress(host, port.toInt()), 1500)
    val outStream = sc.getOutputStream()
    outStream.write(x.toByteArray())
    outStream.write(radius.toByteArray())
    val inStream = BufferedReader(InputStreamReader(sc.getInputStream(), "UTF-8"))
    return inStream.readLine()


}