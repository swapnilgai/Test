package com.kotlin.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kotlin.test.feature.SearchViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {


                    HomeScreen()


                }
            }
        }
    }
}

@Composable
fun HomeScreen(searchViewModel : SearchViewModel = koinViewModel()){

    val state = searchViewModel.state.collectAsStateWithLifecycle()

    val onQueryChange = remember {
        {
            query : String -> searchViewModel.loadData(query)
        }
    }

    when(val cur = state.value){
       is UiState.Content -> {
           Column() {
               USearchBar(onQueryChange)
               LazyColumn() {
                   items( items = cur.data.response, key = { it -> it.id}){
                       Text("${it.name}")
                   }
               }
           }
       }
        is UiState.Error -> {}

    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun USearchBar(onQueryChange: (String) -> Unit) {
    var queryText by remember { mutableStateOf("") }
    var isActive by remember { mutableStateOf(false) }

    SearchBar(
        query = queryText,
        onQueryChange = {
            queryText = it
            onQueryChange(it)
        },
        onSearch = { isActive = false },
        active = isActive,
        onActiveChange = {}) {

    }
}



//johnny.nguyen@robinhood.com