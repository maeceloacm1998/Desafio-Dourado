package com.app.desafiodourado.feature.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.desafiodourado.core.utils.UiState
import com.app.desafiodourado.feature.home.domain.GetChallengersUseCase
import com.app.desafiodourado.feature.home.domain.GetCoinsUseCase
import com.app.desafiodourado.feature.home.domain.SetChallengersUseCase
import com.app.desafiodourado.feature.home.ui.model.Challenger
import com.app.desafiodourado.feature.home.ui.model.TopTabIndexType
import com.app.desafiodourado.feature.home.ui.model.TopTabIndexType.COMPLETED
import com.app.desafiodourado.feature.home.ui.model.TopTabIndexType.PENDING
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getChallengersUseCase: GetChallengersUseCase,
    private val setChallengersUseCase: SetChallengersUseCase,
    private val getCoinsUseCase: GetCoinsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<List<Challenger.Card>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Challenger.Card>>> = _uiState

    private var challengerList: MutableList<Challenger.Card> = mutableListOf()

    fun getChallengers() {
        viewModelScope.launch {
            getChallengersUseCase().onSuccess { document ->
                val challenger = checkNotNull(document.toObject(Challenger::class.java))
                challengerList = challenger.challengers.toMutableList()
                val filtersChallengerNotCompleted = challenger.challengers.filter { !it.isComplete }
                _uiState.value = UiState.Success(filtersChallengerNotCompleted)
            }.onFailure {
                _uiState.value = UiState.Error(it)
            }
        }
    }

    fun updateChallengerList(index: Int) {
        when (TopTabIndexType.fromValue(index)) {
            PENDING -> {
                val challengerNotCompleted = challengerList.filter { !it.isComplete }
                _uiState.value = UiState.Success(challengerNotCompleted)
            }

            COMPLETED -> {
                val challengerCompleted = challengerList.filter { it.isComplete }
                _uiState.value = UiState.Success(challengerCompleted)
            }
        }
    }

    fun getCoins() = getCoinsUseCase()

    fun setChallengers() {
        val json = "[\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default.png?alt=media&token=853a1cf6-62bf-4ac8-a0f2-19286af6efdf\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult.png?alt=media&token=49d3b7ff-b38b-4315-8b7e-0af40f6589c9\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 1500,\n" +
                "    \"award\": \"Assistir a um filme ou s\\u00e9rie juntos\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box.png?alt=media&token=f79e0f2c-4179-41f7-8b85-1fc56356737e\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default.png?alt=media&token=853a1cf6-62bf-4ac8-a0f2-19286af6efdf\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult.png?alt=media&token=49d3b7ff-b38b-4315-8b7e-0af40f6589c9\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 1500,\n" +
                "    \"award\": \"Fazer uma noite de degusta\\u00e7\\u00e3o de chocolates\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box.png?alt=media&token=f79e0f2c-4179-41f7-8b85-1fc56356737e\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default.png?alt=media&token=853a1cf6-62bf-4ac8-a0f2-19286af6efdf\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult.png?alt=media&token=49d3b7ff-b38b-4315-8b7e-0af40f6589c9\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 500,\n" +
                "    \"award\": \"Fazer um passeio de bicicleta\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box.png?alt=media&token=f79e0f2c-4179-41f7-8b85-1fc56356737e\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/golldChallenger.png?alt=media&token=692ca564-3ac1-459f-82af-f9293647267d\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeGoldChallenger.png?alt=media&token=a15c4c0e-8b04-4979-8aa0-7d4dc44ecf3e\",\n" +
                "    \"type\": \"GOLD_CHALLENGER\",\n" +
                "    \"details\": \"Essa \\u00e9 a carta de Desafio Dourado, normalmente ela cont\\u00e9m premios que podem ser bastante especiais e sempre vir\\u00e1 um premio.\",\n" +
                "    \"value\": 41000,\n" +
                "    \"isComplete\": false,\n" +
                "    \"award\": \"Um dia no spa\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/boxGolden.png?alt=media&token=05070a50-6fde-49ee-b7df-42c624c6a3e1\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default.png?alt=media&token=853a1cf6-62bf-4ac8-a0f2-19286af6efdf\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult.png?alt=media&token=49d3b7ff-b38b-4315-8b7e-0af40f6589c9\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 1500,\n" +
                "    \"award\": \"Ir a um spa\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box.png?alt=media&token=f79e0f2c-4179-41f7-8b85-1fc56356737e\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default.png?alt=media&token=853a1cf6-62bf-4ac8-a0f2-19286af6efdf\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult.png?alt=media&token=49d3b7ff-b38b-4315-8b7e-0af40f6589c9\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 1000,\n" +
                "    \"award\": \"Fazer uma noite de degusta\\u00e7\\u00e3o de queijos\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box.png?alt=media&token=f79e0f2c-4179-41f7-8b85-1fc56356737e\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default.png?alt=media&token=853a1cf6-62bf-4ac8-a0f2-19286af6efdf\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult.png?alt=media&token=49d3b7ff-b38b-4315-8b7e-0af40f6589c9\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 750,\n" +
                "    \"award\": \"Piquenique no parque\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box.png?alt=media&token=f79e0f2c-4179-41f7-8b85-1fc56356737e\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default.png?alt=media&token=853a1cf6-62bf-4ac8-a0f2-19286af6efdf\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult.png?alt=media&token=49d3b7ff-b38b-4315-8b7e-0af40f6589c9\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 1000,\n" +
                "    \"award\": \"Jogar um jogo de tabuleiro\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box.png?alt=media&token=f79e0f2c-4179-41f7-8b85-1fc56356737e\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default.png?alt=media&token=853a1cf6-62bf-4ac8-a0f2-19286af6efdf\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult.png?alt=media&token=49d3b7ff-b38b-4315-8b7e-0af40f6589c9\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 1500,\n" +
                "    \"award\": \"Jogar um jogo de tabuleiro\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box.png?alt=media&token=f79e0f2c-4179-41f7-8b85-1fc56356737e\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default.png?alt=media&token=853a1cf6-62bf-4ac8-a0f2-19286af6efdf\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult.png?alt=media&token=49d3b7ff-b38b-4315-8b7e-0af40f6589c9\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 750,\n" +
                "    \"award\": \"Jantar rom\\u00e2ntico\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box.png?alt=media&token=f79e0f2c-4179-41f7-8b85-1fc56356737e\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default.png?alt=media&token=853a1cf6-62bf-4ac8-a0f2-19286af6efdf\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult.png?alt=media&token=49d3b7ff-b38b-4315-8b7e-0af40f6589c9\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 750,\n" +
                "    \"award\": \"Piquenique no parque\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box.png?alt=media&token=f79e0f2c-4179-41f7-8b85-1fc56356737e\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default.png?alt=media&token=853a1cf6-62bf-4ac8-a0f2-19286af6efdf\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult.png?alt=media&token=49d3b7ff-b38b-4315-8b7e-0af40f6589c9\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 1250,\n" +
                "    \"award\": \"Um Caf\\u00e9 da manh\\u00e3\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box.png?alt=media&token=f79e0f2c-4179-41f7-8b85-1fc56356737e\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default.png?alt=media&token=853a1cf6-62bf-4ac8-a0f2-19286af6efdf\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult.png?alt=media&token=49d3b7ff-b38b-4315-8b7e-0af40f6589c9\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 500,\n" +
                "    \"award\": \"Piquenique no parque\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box.png?alt=media&token=f79e0f2c-4179-41f7-8b85-1fc56356737e\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/golldChallenger.png?alt=media&token=692ca564-3ac1-459f-82af-f9293647267d\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeGoldChallenger.png?alt=media&token=a15c4c0e-8b04-4979-8aa0-7d4dc44ecf3e\",\n" +
                "    \"type\": \"GOLD_CHALLENGER\",\n" +
                "    \"details\": \"Essa \\u00e9 a carta de Desafio Dourado, normalmente ela cont\\u00e9m premios que podem ser bastante especiais e sempre vir\\u00e1 um premio.\",\n" +
                "    \"value\": 31750,\n" +
                "    \"isComplete\": false,\n" +
                "    \"award\": \"Rodizio de Sushi\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/boxGolden.png?alt=media&token=05070a50-6fde-49ee-b7df-42c624c6a3e1\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default.png?alt=media&token=853a1cf6-62bf-4ac8-a0f2-19286af6efdf\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult.png?alt=media&token=49d3b7ff-b38b-4315-8b7e-0af40f6589c9\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 1000,\n" +
                "    \"award\": \"Piquenique no parque\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box.png?alt=media&token=f79e0f2c-4179-41f7-8b85-1fc56356737e\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default.png?alt=media&token=853a1cf6-62bf-4ac8-a0f2-19286af6efdf\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult.png?alt=media&token=49d3b7ff-b38b-4315-8b7e-0af40f6589c9\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 500,\n" +
                "    \"award\": \"Fazer uma noite de jogos\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box.png?alt=media&token=f79e0f2c-4179-41f7-8b85-1fc56356737e\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/golldChallenger.png?alt=media&token=692ca564-3ac1-459f-82af-f9293647267d\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeGoldChallenger.png?alt=media&token=a15c4c0e-8b04-4979-8aa0-7d4dc44ecf3e\",\n" +
                "    \"type\": \"GOLD_CHALLENGER\",\n" +
                "    \"details\": \"Essa \\u00e9 a carta de Desafio Dourado, normalmente ela cont\\u00e9m premios que podem ser bastante especiais e sempre vir\\u00e1 um premio.\",\n" +
                "    \"value\": 33000,\n" +
                "    \"isComplete\": false,\n" +
                "    \"award\": \"Um par de \\u00f3culos de sol elegantes\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/boxGolden.png?alt=media&token=05070a50-6fde-49ee-b7df-42c624c6a3e1\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default.png?alt=media&token=853a1cf6-62bf-4ac8-a0f2-19286af6efdf\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult.png?alt=media&token=49d3b7ff-b38b-4315-8b7e-0af40f6589c9\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 1250,\n" +
                "    \"award\": \"Jogar um jogo de tabuleiro\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box.png?alt=media&token=f79e0f2c-4179-41f7-8b85-1fc56356737e\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default.png?alt=media&token=853a1cf6-62bf-4ac8-a0f2-19286af6efdf\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult.png?alt=media&token=49d3b7ff-b38b-4315-8b7e-0af40f6589c9\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 1000,\n" +
                "    \"award\": \"Fazer uma noite de jogos\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box.png?alt=media&token=f79e0f2c-4179-41f7-8b85-1fc56356737e\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default.png?alt=media&token=853a1cf6-62bf-4ac8-a0f2-19286af6efdf\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult.png?alt=media&token=49d3b7ff-b38b-4315-8b7e-0af40f6589c9\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 1500,\n" +
                "    \"award\": \"Fazer uma sess\\u00e3o de fotos\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box.png?alt=media&token=f79e0f2c-4179-41f7-8b85-1fc56356737e\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default.png?alt=media&token=853a1cf6-62bf-4ac8-a0f2-19286af6efdf\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult.png?alt=media&token=49d3b7ff-b38b-4315-8b7e-0af40f6589c9\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 500,\n" +
                "    \"award\": \"Fazer uma sess\\u00e3o de fotos\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box.png?alt=media&token=f79e0f2c-4179-41f7-8b85-1fc56356737e\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default.png?alt=media&token=853a1cf6-62bf-4ac8-a0f2-19286af6efdf\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult.png?alt=media&token=49d3b7ff-b38b-4315-8b7e-0af40f6589c9\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 750,\n" +
                "    \"award\": \"Fazer uma noite de degusta\\u00e7\\u00e3o de doces\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box.png?alt=media&token=f79e0f2c-4179-41f7-8b85-1fc56356737e\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default.png?alt=media&token=853a1cf6-62bf-4ac8-a0f2-19286af6efdf\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult.png?alt=media&token=49d3b7ff-b38b-4315-8b7e-0af40f6589c9\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 1500,\n" +
                "    \"award\": \"Jantar rom\\u00e2ntico\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box.png?alt=media&token=f79e0f2c-4179-41f7-8b85-1fc56356737e\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/golldChallenger.png?alt=media&token=692ca564-3ac1-459f-82af-f9293647267d\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeGoldChallenger.png?alt=media&token=a15c4c0e-8b04-4979-8aa0-7d4dc44ecf3e\",\n" +
                "    \"type\": \"GOLD_CHALLENGER\",\n" +
                "    \"details\": \"Essa \\u00e9 a carta de Desafio Dourado, normalmente ela cont\\u00e9m premios que podem ser bastante especiais e sempre vir\\u00e1 um premio.\",\n" +
                "    \"value\": 42000,\n" +
                "    \"isComplete\": false,\n" +
                "    \"award\": \"Ingressos para um teatro ou concerto\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/boxGolden.png?alt=media&token=05070a50-6fde-49ee-b7df-42c624c6a3e1\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default.png?alt=media&token=853a1cf6-62bf-4ac8-a0f2-19286af6efdf\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult.png?alt=media&token=49d3b7ff-b38b-4315-8b7e-0af40f6589c9\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 1000,\n" +
                "    \"award\": \"Fazer uma viagem de fim de semana\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box.png?alt=media&token=f79e0f2c-4179-41f7-8b85-1fc56356737e\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/golldChallenger.png?alt=media&token=692ca564-3ac1-459f-82af-f9293647267d\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeGoldChallenger.png?alt=media&token=a15c4c0e-8b04-4979-8aa0-7d4dc44ecf3e\",\n" +
                "    \"type\": \"GOLD_CHALLENGER\",\n" +
                "    \"details\": \"Essa \\u00e9 a carta de Desafio Dourado, normalmente ela cont\\u00e9m premios que podem ser bastante especiais e sempre vir\\u00e1 um premio.\",\n" +
                "    \"value\": 47000,\n" +
                "    \"isComplete\": false,\n" +
                "    \"award\": \"Um kit de queijos e vinhos\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/boxGolden.png?alt=media&token=05070a50-6fde-49ee-b7df-42c624c6a3e1\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/golldChallenger.png?alt=media&token=692ca564-3ac1-459f-82af-f9293647267d\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeGoldChallenger.png?alt=media&token=a15c4c0e-8b04-4979-8aa0-7d4dc44ecf3e\",\n" +
                "    \"type\": \"GOLD_CHALLENGER\",\n" +
                "    \"details\": \"Essa \\u00e9 a carta de Desafio Dourado, normalmente ela cont\\u00e9m premios que podem ser bastante especiais e sempre vir\\u00e1 um premio.\",\n" +
                "    \"value\": 30750,\n" +
                "    \"isComplete\": false,\n" +
                "    \"award\": \"Viagem de fim de semana\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/boxGolden.png?alt=media&token=05070a50-6fde-49ee-b7df-42c624c6a3e1\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default.png?alt=media&token=853a1cf6-62bf-4ac8-a0f2-19286af6efdf\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult.png?alt=media&token=49d3b7ff-b38b-4315-8b7e-0af40f6589c9\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 500,\n" +
                "    \"award\": \"Fazer uma sess\\u00e3o de fotos\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box.png?alt=media&token=f79e0f2c-4179-41f7-8b85-1fc56356737e\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default.png?alt=media&token=853a1cf6-62bf-4ac8-a0f2-19286af6efdf\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult.png?alt=media&token=49d3b7ff-b38b-4315-8b7e-0af40f6589c9\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 1250,\n" +
                "    \"award\": \"Piquenique no parque\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box.png?alt=media&token=f79e0f2c-4179-41f7-8b85-1fc56356737e\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default.png?alt=media&token=853a1cf6-62bf-4ac8-a0f2-19286af6efdf\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult.png?alt=media&token=49d3b7ff-b38b-4315-8b7e-0af40f6589c9\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 500,\n" +
                "    \"award\": \"Ir em uma sorveteria\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box.png?alt=media&token=f79e0f2c-4179-41f7-8b85-1fc56356737e\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/golldChallenger.png?alt=media&token=692ca564-3ac1-459f-82af-f9293647267d\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeGoldChallenger.png?alt=media&token=a15c4c0e-8b04-4979-8aa0-7d4dc44ecf3e\",\n" +
                "    \"type\": \"GOLD_CHALLENGER\",\n" +
                "    \"details\": \"Essa \\u00e9 a carta de Desafio Dourado, normalmente ela cont\\u00e9m premios que podem ser bastante especiais e sempre vir\\u00e1 um premio.\",\n" +
                "    \"value\": 48750,\n" +
                "    \"isComplete\": false,\n" +
                "    \"award\": \"Um livro a sua escolha\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/boxGolden.png?alt=media&token=05070a50-6fde-49ee-b7df-42c624c6a3e1\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default.png?alt=media&token=853a1cf6-62bf-4ac8-a0f2-19286af6efdf\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult.png?alt=media&token=49d3b7ff-b38b-4315-8b7e-0af40f6589c9\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 1000,\n" +
                "    \"award\": \"Fazer uma sess\\u00e3o de fotos\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box.png?alt=media&token=f79e0f2c-4179-41f7-8b85-1fc56356737e\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default.png?alt=media&token=853a1cf6-62bf-4ac8-a0f2-19286af6efdf\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult.png?alt=media&token=49d3b7ff-b38b-4315-8b7e-0af40f6589c9\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 1500,\n" +
                "    \"award\": \"Piquenique no parque\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box.png?alt=media&token=f79e0f2c-4179-41f7-8b85-1fc56356737e\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default.png?alt=media&token=853a1cf6-62bf-4ac8-a0f2-19286af6efdf\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult.png?alt=media&token=49d3b7ff-b38b-4315-8b7e-0af40f6589c9\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 500,\n" +
                "    \"award\": \"Ir a um concerto\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box.png?alt=media&token=f79e0f2c-4179-41f7-8b85-1fc56356737e\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default.png?alt=media&token=853a1cf6-62bf-4ac8-a0f2-19286af6efdf\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult.png?alt=media&token=49d3b7ff-b38b-4315-8b7e-0af40f6589c9\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 750,\n" +
                "    \"award\": \"Piquenique no parque\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box.png?alt=media&token=f79e0f2c-4179-41f7-8b85-1fc56356737e\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default.png?alt=media&token=853a1cf6-62bf-4ac8-a0f2-19286af6efdf\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult.png?alt=media&token=49d3b7ff-b38b-4315-8b7e-0af40f6589c9\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 500,\n" +
                "    \"award\": \"Fazer uma noite de degusta\\u00e7\\u00e3o de queijos\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box.png?alt=media&token=f79e0f2c-4179-41f7-8b85-1fc56356737e\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default.png?alt=media&token=853a1cf6-62bf-4ac8-a0f2-19286af6efdf\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult.png?alt=media&token=49d3b7ff-b38b-4315-8b7e-0af40f6589c9\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 1500,\n" +
                "    \"award\": \"Ir a seu restaurante favorito\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box.png?alt=media&token=f79e0f2c-4179-41f7-8b85-1fc56356737e\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default.png?alt=media&token=853a1cf6-62bf-4ac8-a0f2-19286af6efdf\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult.png?alt=media&token=49d3b7ff-b38b-4315-8b7e-0af40f6589c9\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 750,\n" +
                "    \"award\": \"Cozinhar juntos\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box.png?alt=media&token=f79e0f2c-4179-41f7-8b85-1fc56356737e\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/golldChallenger.png?alt=media&token=692ca564-3ac1-459f-82af-f9293647267d\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeGoldChallenger.png?alt=media&token=a15c4c0e-8b04-4979-8aa0-7d4dc44ecf3e\",\n" +
                "    \"type\": \"GOLD_CHALLENGER\",\n" +
                "    \"details\": \"Essa \\u00e9 a carta de Desafio Dourado, normalmente ela cont\\u00e9m premios que podem ser bastante especiais e sempre vir\\u00e1 um premio.\",\n" +
                "    \"value\": 42750,\n" +
                "    \"isComplete\": false,\n" +
                "    \"award\": \"Um di\\u00e1rio ou caderno de notas bonito\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/boxGolden.png?alt=media&token=05070a50-6fde-49ee-b7df-42c624c6a3e1\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/golldChallenger.png?alt=media&token=692ca564-3ac1-459f-82af-f9293647267d\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeGoldChallenger.png?alt=media&token=a15c4c0e-8b04-4979-8aa0-7d4dc44ecf3e\",\n" +
                "    \"type\": \"GOLD_CHALLENGER\",\n" +
                "    \"details\": \"Essa \\u00e9 a carta de Desafio Dourado, normalmente ela cont\\u00e9m premios que podem ser bastante especiais e sempre vir\\u00e1 um premio.\",\n" +
                "    \"value\": 37750,\n" +
                "    \"isComplete\": false,\n" +
                "    \"award\": \"Viagem de fim de semana\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/boxGolden.png?alt=media&token=05070a50-6fde-49ee-b7df-42c624c6a3e1\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default.png?alt=media&token=853a1cf6-62bf-4ac8-a0f2-19286af6efdf\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult.png?alt=media&token=49d3b7ff-b38b-4315-8b7e-0af40f6589c9\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 500,\n" +
                "    \"award\": \"Passeio Surpresa\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box.png?alt=media&token=f79e0f2c-4179-41f7-8b85-1fc56356737e\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default.png?alt=media&token=853a1cf6-62bf-4ac8-a0f2-19286af6efdf\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult.png?alt=media&token=49d3b7ff-b38b-4315-8b7e-0af40f6589c9\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 1500,\n" +
                "    \"award\": \"Fazer uma viagem de fim de semana\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box.png?alt=media&token=f79e0f2c-4179-41f7-8b85-1fc56356737e\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default.png?alt=media&token=853a1cf6-62bf-4ac8-a0f2-19286af6efdf\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult.png?alt=media&token=49d3b7ff-b38b-4315-8b7e-0af40f6589c9\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 1500,\n" +
                "    \"award\": \"Jogar um jogo de tabuleiro\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box.png?alt=media&token=f79e0f2c-4179-41f7-8b85-1fc56356737e\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default.png?alt=media&token=853a1cf6-62bf-4ac8-a0f2-19286af6efdf\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult.png?alt=media&token=49d3b7ff-b38b-4315-8b7e-0af40f6589c9\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 750,\n" +
                "    \"award\": \"Piquenique no parque\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box.png?alt=media&token=f79e0f2c-4179-41f7-8b85-1fc56356737e\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default.png?alt=media&token=853a1cf6-62bf-4ac8-a0f2-19286af6efdf\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult.png?alt=media&token=49d3b7ff-b38b-4315-8b7e-0af40f6589c9\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 750,\n" +
                "    \"award\": \"Jogar um jogo de tabuleiro\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box.png?alt=media&token=f79e0f2c-4179-41f7-8b85-1fc56356737e\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default.png?alt=media&token=853a1cf6-62bf-4ac8-a0f2-19286af6efdf\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult.png?alt=media&token=49d3b7ff-b38b-4315-8b7e-0af40f6589c9\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 1500,\n" +
                "    \"award\": \"Fazer uma noite de degusta\\u00e7\\u00e3o de vinhos\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box.png?alt=media&token=f79e0f2c-4179-41f7-8b85-1fc56356737e\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default.png?alt=media&token=853a1cf6-62bf-4ac8-a0f2-19286af6efdf\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult.png?alt=media&token=49d3b7ff-b38b-4315-8b7e-0af40f6589c9\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 1500,\n" +
                "    \"award\": \"Piquenique no parque\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box.png?alt=media&token=f79e0f2c-4179-41f7-8b85-1fc56356737e\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default.png?alt=media&token=853a1cf6-62bf-4ac8-a0f2-19286af6efdf\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult.png?alt=media&token=49d3b7ff-b38b-4315-8b7e-0af40f6589c9\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 1500,\n" +
                "    \"award\": \"Cozinhar juntos\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box.png?alt=media&token=f79e0f2c-4179-41f7-8b85-1fc56356737e\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default.png?alt=media&token=853a1cf6-62bf-4ac8-a0f2-19286af6efdf\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult.png?alt=media&token=49d3b7ff-b38b-4315-8b7e-0af40f6589c9\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 1000,\n" +
                "    \"award\": \"Visitar um museu\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box.png?alt=media&token=f79e0f2c-4179-41f7-8b85-1fc56356737e\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/golldChallenger.png?alt=media&token=692ca564-3ac1-459f-82af-f9293647267d\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeGoldChallenger.png?alt=media&token=a15c4c0e-8b04-4979-8aa0-7d4dc44ecf3e\",\n" +
                "    \"type\": \"GOLD_CHALLENGER\",\n" +
                "    \"details\": \"Essa \\u00e9 a carta de Desafio Dourado, normalmente ela cont\\u00e9m premios que podem ser bastante especiais e sempre vir\\u00e1 um premio.\",\n" +
                "    \"value\": 41750,\n" +
                "    \"isComplete\": false,\n" +
                "    \"award\": \"Um livro a sua escolha\",\n" +
                "    \"awardInage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/boxGolden.png?alt=media&token=05070a50-6fde-49ee-b7df-42c624c6a3e1\"\n" +
                "  }\n" +
                "]"
        val listType = object : TypeToken<List<Challenger.Card>>() {}.type

        val cards: List<Challenger.Card> = Gson().fromJson(json, listType)
        viewModelScope.launch {
            setChallengersUseCase(Challenger(cards))
        }
    }
}