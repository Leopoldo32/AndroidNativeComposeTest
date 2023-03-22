package com.example.testandroidcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.runtime.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import coil.compose.rememberImagePainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.navArgument
import com.example.testandroidcompose.ui.theme.TestAndroidComposeTheme
import com.example.testandroidcompose.Interface.ApiInterface
import com.example.testandroidcompose.Model.CharacterDetail
import com.example.testandroidcompose.Model.CharacterResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestAndroidComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    //CharacterListScreen()
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    NavHost( navController, startDestination = "characterListScreen" ) {
        composable("characterListScreen") {
            CharacterListScreen(navController = navController)
        }
        composable("characterDetailsScreen/{idElemento}") { backStackEntry ->

            val characterId = (backStackEntry.arguments?.getString("idElemento"))!!.toInt()?: -1

            CharacterDetailsScreenContent(characterId = characterId)
        }
    }
}

@Composable
fun CharacterListItem(
    character: CharacterDetail,
    onClick: (CharacterDetail) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clickable(onClick = { onClick(character) }),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = rememberImagePainter(data = character.image),
                contentDescription = character.name,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = character.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}


@Composable
fun CharacterListScreen(
    viewModel: CharactherListViewModel = viewModel(),
    navController: NavController
) {
    val characters by viewModel.characters.observeAsState(emptyList())
    var searchTerm by remember { mutableStateOf("") }
    var filteredCharacters by remember(characters) { mutableStateOf(characters) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Caracteristicas de Rick And Morty",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            elevation = 4.dp,
            shape = RoundedCornerShape(8.dp)
        ){
            TextField(
                value = searchTerm,
                onValueChange = {
                    searchTerm = it
                    filteredCharacters = characters.filter { character ->
                        character.name.contains(searchTerm, ignoreCase = true)
                    }
                },
                placeholder = {
                    Text(
                        text = "Buscar personaje",
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredCharacters) { character ->
                CharacterListItem(
                    character = character,
                    onClick = {
                        var idElemento = character.id
                        navController.navigate("characterDetailsScreen/$idElemento")
                    }
                )
            }
        }
    }
}


@Composable
fun CharacterDetailsScreenContent(characterId: Int) {

    val viewModel: CharacterDetailsViewModel = viewModel()
    var characterDetails by remember { mutableStateOf<CharacterDetail?>(null) }

    LaunchedEffect(characterId) {
        val response = viewModel.getCharacterDetailId(characterId)
        characterDetails = response
    }
    if (characterDetails == null) {
        // En caso de que no se haya encontrado el personaje
        Text(text = "Personaje no encontrado")
    } else {
        // En caso de que se haya encontrado el personaje
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            characterDetails?.let { details ->

                Text(
                    text = "Nombre: ${details.name}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Image(
                    painter = rememberImagePainter(data = details.image),
                    contentDescription = details.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(shape = RoundedCornerShape(8.dp))
                )
                Text(
                    text = "Status: ${details.status}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Text(
                    text = "Especie: ${details.species}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Text(
                    text = "Genero: ${details.gender}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TestAndroidComposeTheme {
        Greeting("Android")
    }
}