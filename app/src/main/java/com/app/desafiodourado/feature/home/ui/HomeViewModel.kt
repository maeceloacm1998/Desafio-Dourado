package com.app.desafiodourado.feature.home.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.desafiodourado.core.utils.UiState
import com.app.desafiodourado.feature.home.domain.GetChallengersUseCase
import com.app.desafiodourado.feature.home.domain.GetCoinsUseCase
import com.app.desafiodourado.feature.home.domain.SetChallengersUseCase
import com.app.desafiodourado.feature.details.domain.UpdateChallengersUseCase
import com.app.desafiodourado.feature.home.ui.model.Challenger
import com.app.desafiodourado.feature.home.ui.model.Challenger.Card
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
    private var offset: Int = 0
    private val step: Int = 3

    private val _challengerState = MutableLiveData<UiState<List<Card>>>()
    val challengerState: LiveData<UiState<List<Card>>> = _challengerState

    private var challengerList: MutableList<Card> = mutableListOf()

    init {
        getChallengers()
    }

    fun getChallengers() {
        viewModelScope.launch {
            getChallengersUseCase().onSuccess { document ->
                val challenger = checkNotNull(document.toObject(Challenger::class.java))
                challengerList = challenger.challengers.toMutableList()
                val filtersChallengerNotCompleted = challengerList.filter { !it.complete }
                _challengerState.value = UiState.Success(filtersChallengerNotCompleted)
            }.onFailure {
                _challengerState.value = UiState.Error(it)
            }
        }
    }

    private fun getChallengersOffset(challengerList: List<Card>): List<Card> {
        val filtersChallengerNotCompleted = challengerList.filter { !it.complete }
        val result = filtersChallengerNotCompleted.drop(offset).take(step)
        offset += step

        return result
    }

    fun updateChallengerList(index: Int) {
        when (TopTabIndexType.fromValue(index)) {
            PENDING -> {
                val challengerNotCompleted = challengerList.filter { !it.complete }
                _challengerState.value = UiState.Success(challengerNotCompleted)
            }

            COMPLETED -> {
                val challengerCompleted = challengerList.filter { it.complete }
                _challengerState.value = UiState.Success(challengerCompleted)
            }
        }
    }
    fun getCoins() = getCoinsUseCase()

    fun clearOffset() {
        offset = 0
    }
    fun setChallengers() {
        val json = "[\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default_redux.png?alt=media&token=5e5d6d1f-6bee-4ded-a42b-4b7be8d635b6\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult_redux.png?alt=media&token=0dca7ab3-30f6-4a87-b267-b62cf8ea9e71\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 750,\n" +
                "    \"award\": \"Ir a um concerto\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box_redux.png?alt=media&token=960af6a9-20dc-434f-b3fb-13971d93e095\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default_redux.png?alt=media&token=5e5d6d1f-6bee-4ded-a42b-4b7be8d635b6\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult_redux.png?alt=media&token=0dca7ab3-30f6-4a87-b267-b62cf8ea9e71\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 1000,\n" +
                "    \"award\": \"Passeio Surpresa\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box_redux.png?alt=media&token=960af6a9-20dc-434f-b3fb-13971d93e095\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default_redux.png?alt=media&token=5e5d6d1f-6bee-4ded-a42b-4b7be8d635b6\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult_redux.png?alt=media&token=0dca7ab3-30f6-4a87-b267-b62cf8ea9e71\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 500,\n" +
                "    \"award\": \"Fazer uma noite de jogos\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box_redux.png?alt=media&token=960af6a9-20dc-434f-b3fb-13971d93e095\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default_redux.png?alt=media&token=5e5d6d1f-6bee-4ded-a42b-4b7be8d635b6\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult_redux.png?alt=media&token=0dca7ab3-30f6-4a87-b267-b62cf8ea9e71\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 1500,\n" +
                "    \"award\": \"Fazer uma sess\\u00e3o de fotos\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box_redux.png?alt=media&token=960af6a9-20dc-434f-b3fb-13971d93e095\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default_redux.png?alt=media&token=5e5d6d1f-6bee-4ded-a42b-4b7be8d635b6\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult_redux.png?alt=media&token=0dca7ab3-30f6-4a87-b267-b62cf8ea9e71\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 1500,\n" +
                "    \"award\": \"Jantar rom\\u00e2ntico\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box_redux.png?alt=media&token=960af6a9-20dc-434f-b3fb-13971d93e095\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/golldChallenger_redux.png?alt=media&token=fedd7a23-b447-4a84-809f-b152d952e49d\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeGoldChallenger_redux.png?alt=media&token=00682919-a098-48bd-a3c8-7f9e5907cc72\",\n" +
                "    \"type\": \"GOLD_CHALLENGER\",\n" +
                "    \"details\": \"Essa \\u00e9 a carta de Desafio Dourado, normalmente ela cont\\u00e9m premios que podem ser bastante especiais e sempre vir\\u00e1 um premio.\",\n" +
                "    \"value\": 36500,\n" +
                "    \"isComplete\": false,\n" +
                "    \"award\": \"Ingresso para um show que voc\\u00ea desejar\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/boxGolden_redux.png?alt=media&token=447efaae-b167-472c-8c9e-94597bf3f1cc\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default_redux.png?alt=media&token=5e5d6d1f-6bee-4ded-a42b-4b7be8d635b6\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult_redux.png?alt=media&token=0dca7ab3-30f6-4a87-b267-b62cf8ea9e71\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 500,\n" +
                "    \"award\": \"Fazer uma noite de degusta\\u00e7\\u00e3o de cervejas artesanais\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box_redux.png?alt=media&token=960af6a9-20dc-434f-b3fb-13971d93e095\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default_redux.png?alt=media&token=5e5d6d1f-6bee-4ded-a42b-4b7be8d635b6\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult_redux.png?alt=media&token=0dca7ab3-30f6-4a87-b267-b62cf8ea9e71\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 1500,\n" +
                "    \"award\": \"Um Caf\\u00e9 da manh\\u00e3\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box_redux.png?alt=media&token=960af6a9-20dc-434f-b3fb-13971d93e095\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default_redux.png?alt=media&token=5e5d6d1f-6bee-4ded-a42b-4b7be8d635b6\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult_redux.png?alt=media&token=0dca7ab3-30f6-4a87-b267-b62cf8ea9e71\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 1500,\n" +
                "    \"award\": \"Ir a um evento esportivo\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box_redux.png?alt=media&token=960af6a9-20dc-434f-b3fb-13971d93e095\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default_redux.png?alt=media&token=5e5d6d1f-6bee-4ded-a42b-4b7be8d635b6\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult_redux.png?alt=media&token=0dca7ab3-30f6-4a87-b267-b62cf8ea9e71\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 1250,\n" +
                "    \"award\": \"Fazer uma noite de degusta\\u00e7\\u00e3o de vinhos\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box_redux.png?alt=media&token=960af6a9-20dc-434f-b3fb-13971d93e095\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default_redux.png?alt=media&token=5e5d6d1f-6bee-4ded-a42b-4b7be8d635b6\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult_redux.png?alt=media&token=0dca7ab3-30f6-4a87-b267-b62cf8ea9e71\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 750,\n" +
                "    \"award\": \"Visitar uma feira de artesanato\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box_redux.png?alt=media&token=960af6a9-20dc-434f-b3fb-13971d93e095\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/golldChallenger_redux.png?alt=media&token=fedd7a23-b447-4a84-809f-b152d952e49d\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeGoldChallenger_redux.png?alt=media&token=00682919-a098-48bd-a3c8-7f9e5907cc72\",\n" +
                "    \"type\": \"GOLD_CHALLENGER\",\n" +
                "    \"details\": \"Essa \\u00e9 a carta de Desafio Dourado, normalmente ela cont\\u00e9m premios que podem ser bastante especiais e sempre vir\\u00e1 um premio.\",\n" +
                "    \"value\": 44750,\n" +
                "    \"isComplete\": false,\n" +
                "    \"award\": \"Jantar em um restaurante caro\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/boxGolden_redux.png?alt=media&token=447efaae-b167-472c-8c9e-94597bf3f1cc\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default_redux.png?alt=media&token=5e5d6d1f-6bee-4ded-a42b-4b7be8d635b6\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult_redux.png?alt=media&token=0dca7ab3-30f6-4a87-b267-b62cf8ea9e71\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 750,\n" +
                "    \"award\": \"Um Caf\\u00e9 da manh\\u00e3\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box_redux.png?alt=media&token=960af6a9-20dc-434f-b3fb-13971d93e095\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default_redux.png?alt=media&token=5e5d6d1f-6bee-4ded-a42b-4b7be8d635b6\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult_redux.png?alt=media&token=0dca7ab3-30f6-4a87-b267-b62cf8ea9e71\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 1500,\n" +
                "    \"award\": \"Fazer uma noite de jogos de cartas\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box_redux.png?alt=media&token=960af6a9-20dc-434f-b3fb-13971d93e095\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/golldChallenger_redux.png?alt=media&token=fedd7a23-b447-4a84-809f-b152d952e49d\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeGoldChallenger_redux.png?alt=media&token=00682919-a098-48bd-a3c8-7f9e5907cc72\",\n" +
                "    \"type\": \"GOLD_CHALLENGER\",\n" +
                "    \"details\": \"Essa \\u00e9 a carta de Desafio Dourado, normalmente ela cont\\u00e9m premios que podem ser bastante especiais e sempre vir\\u00e1 um premio.\",\n" +
                "    \"value\": 40000,\n" +
                "    \"isComplete\": false,\n" +
                "    \"award\": \"Ingresso para um show que voc\\u00ea desejar\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/boxGolden_redux.png?alt=media&token=447efaae-b167-472c-8c9e-94597bf3f1cc\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/golldChallenger_redux.png?alt=media&token=fedd7a23-b447-4a84-809f-b152d952e49d\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeGoldChallenger_redux.png?alt=media&token=00682919-a098-48bd-a3c8-7f9e5907cc72\",\n" +
                "    \"type\": \"GOLD_CHALLENGER\",\n" +
                "    \"details\": \"Essa \\u00e9 a carta de Desafio Dourado, normalmente ela cont\\u00e9m premios que podem ser bastante especiais e sempre vir\\u00e1 um premio.\",\n" +
                "    \"value\": 48250,\n" +
                "    \"isComplete\": false,\n" +
                "    \"award\": \"Viagem de fim de semana\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/boxGolden_redux.png?alt=media&token=447efaae-b167-472c-8c9e-94597bf3f1cc\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default_redux.png?alt=media&token=5e5d6d1f-6bee-4ded-a42b-4b7be8d635b6\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult_redux.png?alt=media&token=0dca7ab3-30f6-4a87-b267-b62cf8ea9e71\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 1500,\n" +
                "    \"award\": \"Jantar rom\\u00e2ntico em casa\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box_redux.png?alt=media&token=960af6a9-20dc-434f-b3fb-13971d93e095\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default_redux.png?alt=media&token=5e5d6d1f-6bee-4ded-a42b-4b7be8d635b6\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult_redux.png?alt=media&token=0dca7ab3-30f6-4a87-b267-b62cf8ea9e71\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 1250,\n" +
                "    \"award\": \"Assistir a um filme ou s\\u00e9rie juntos\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box_redux.png?alt=media&token=960af6a9-20dc-434f-b3fb-13971d93e095\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default_redux.png?alt=media&token=5e5d6d1f-6bee-4ded-a42b-4b7be8d635b6\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult_redux.png?alt=media&token=0dca7ab3-30f6-4a87-b267-b62cf8ea9e71\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 500,\n" +
                "    \"award\": \"Ir a um parque tem\\u00e1tico\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box_redux.png?alt=media&token=960af6a9-20dc-434f-b3fb-13971d93e095\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default_redux.png?alt=media&token=5e5d6d1f-6bee-4ded-a42b-4b7be8d635b6\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult_redux.png?alt=media&token=0dca7ab3-30f6-4a87-b267-b62cf8ea9e71\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 750,\n" +
                "    \"award\": \"Ir a um parque tem\\u00e1tico\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box_redux.png?alt=media&token=960af6a9-20dc-434f-b3fb-13971d93e095\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default_redux.png?alt=media&token=5e5d6d1f-6bee-4ded-a42b-4b7be8d635b6\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult_redux.png?alt=media&token=0dca7ab3-30f6-4a87-b267-b62cf8ea9e71\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 1250,\n" +
                "    \"award\": \"Um Caf\\u00e9 da manh\\u00e3\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box_redux.png?alt=media&token=960af6a9-20dc-434f-b3fb-13971d93e095\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default_redux.png?alt=media&token=5e5d6d1f-6bee-4ded-a42b-4b7be8d635b6\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult_redux.png?alt=media&token=0dca7ab3-30f6-4a87-b267-b62cf8ea9e71\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 500,\n" +
                "    \"award\": \"Fazer uma noite de degusta\\u00e7\\u00e3o de cervejas artesanais\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box_redux.png?alt=media&token=960af6a9-20dc-434f-b3fb-13971d93e095\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/golldChallenger_redux.png?alt=media&token=fedd7a23-b447-4a84-809f-b152d952e49d\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeGoldChallenger_redux.png?alt=media&token=00682919-a098-48bd-a3c8-7f9e5907cc72\",\n" +
                "    \"type\": \"GOLD_CHALLENGER\",\n" +
                "    \"details\": \"Essa \\u00e9 a carta de Desafio Dourado, normalmente ela cont\\u00e9m premios que podem ser bastante especiais e sempre vir\\u00e1 um premio.\",\n" +
                "    \"value\": 36750,\n" +
                "    \"isComplete\": false,\n" +
                "    \"award\": \"Rel\\u00f3gio\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/boxGolden_redux.png?alt=media&token=447efaae-b167-472c-8c9e-94597bf3f1cc\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default_redux.png?alt=media&token=5e5d6d1f-6bee-4ded-a42b-4b7be8d635b6\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult_redux.png?alt=media&token=0dca7ab3-30f6-4a87-b267-b62cf8ea9e71\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 1500,\n" +
                "    \"award\": \"Ir a um spa\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box_redux.png?alt=media&token=960af6a9-20dc-434f-b3fb-13971d93e095\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/golldChallenger_redux.png?alt=media&token=fedd7a23-b447-4a84-809f-b152d952e49d\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeGoldChallenger_redux.png?alt=media&token=00682919-a098-48bd-a3c8-7f9e5907cc72\",\n" +
                "    \"type\": \"GOLD_CHALLENGER\",\n" +
                "    \"details\": \"Essa \\u00e9 a carta de Desafio Dourado, normalmente ela cont\\u00e9m premios que podem ser bastante especiais e sempre vir\\u00e1 um premio.\",\n" +
                "    \"value\": 49500,\n" +
                "    \"isComplete\": false,\n" +
                "    \"award\": \"Ingresso para um show que voc\\u00ea desejar\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/boxGolden_redux.png?alt=media&token=447efaae-b167-472c-8c9e-94597bf3f1cc\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default_redux.png?alt=media&token=5e5d6d1f-6bee-4ded-a42b-4b7be8d635b6\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult_redux.png?alt=media&token=0dca7ab3-30f6-4a87-b267-b62cf8ea9e71\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 500,\n" +
                "    \"award\": \"Fazer uma noite de jogos\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box_redux.png?alt=media&token=960af6a9-20dc-434f-b3fb-13971d93e095\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default_redux.png?alt=media&token=5e5d6d1f-6bee-4ded-a42b-4b7be8d635b6\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult_redux.png?alt=media&token=0dca7ab3-30f6-4a87-b267-b62cf8ea9e71\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 1250,\n" +
                "    \"award\": \"Sair para o cinema\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box_redux.png?alt=media&token=960af6a9-20dc-434f-b3fb-13971d93e095\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/golldChallenger_redux.png?alt=media&token=fedd7a23-b447-4a84-809f-b152d952e49d\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeGoldChallenger_redux.png?alt=media&token=00682919-a098-48bd-a3c8-7f9e5907cc72\",\n" +
                "    \"type\": \"GOLD_CHALLENGER\",\n" +
                "    \"details\": \"Essa \\u00e9 a carta de Desafio Dourado, normalmente ela cont\\u00e9m premios que podem ser bastante especiais e sempre vir\\u00e1 um premio.\",\n" +
                "    \"value\": 30000,\n" +
                "    \"isComplete\": false,\n" +
                "    \"award\": \"Um dia no spa\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/boxGolden_redux.png?alt=media&token=447efaae-b167-472c-8c9e-94597bf3f1cc\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default_redux.png?alt=media&token=5e5d6d1f-6bee-4ded-a42b-4b7be8d635b6\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult_redux.png?alt=media&token=0dca7ab3-30f6-4a87-b267-b62cf8ea9e71\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 750,\n" +
                "    \"award\": \"Cozinhar juntos\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box_redux.png?alt=media&token=960af6a9-20dc-434f-b3fb-13971d93e095\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default_redux.png?alt=media&token=5e5d6d1f-6bee-4ded-a42b-4b7be8d635b6\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult_redux.png?alt=media&token=0dca7ab3-30f6-4a87-b267-b62cf8ea9e71\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 500,\n" +
                "    \"award\": \"Fazer uma noite de jogos de cartas\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box_redux.png?alt=media&token=960af6a9-20dc-434f-b3fb-13971d93e095\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default_redux.png?alt=media&token=5e5d6d1f-6bee-4ded-a42b-4b7be8d635b6\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult_redux.png?alt=media&token=0dca7ab3-30f6-4a87-b267-b62cf8ea9e71\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 750,\n" +
                "    \"award\": \"Jogar um jogo de tabuleiro\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box_redux.png?alt=media&token=960af6a9-20dc-434f-b3fb-13971d93e095\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default_redux.png?alt=media&token=5e5d6d1f-6bee-4ded-a42b-4b7be8d635b6\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult_redux.png?alt=media&token=0dca7ab3-30f6-4a87-b267-b62cf8ea9e71\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 1500,\n" +
                "    \"award\": \"Ir em uma sorveteria\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box_redux.png?alt=media&token=960af6a9-20dc-434f-b3fb-13971d93e095\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default_redux.png?alt=media&token=5e5d6d1f-6bee-4ded-a42b-4b7be8d635b6\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult_redux.png?alt=media&token=0dca7ab3-30f6-4a87-b267-b62cf8ea9e71\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 1250,\n" +
                "    \"award\": \"Ir em uma sorveteria\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box_redux.png?alt=media&token=960af6a9-20dc-434f-b3fb-13971d93e095\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default_redux.png?alt=media&token=5e5d6d1f-6bee-4ded-a42b-4b7be8d635b6\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult_redux.png?alt=media&token=0dca7ab3-30f6-4a87-b267-b62cf8ea9e71\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 1500,\n" +
                "    \"award\": \"Cozinhar juntos\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box_redux.png?alt=media&token=960af6a9-20dc-434f-b3fb-13971d93e095\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default_redux.png?alt=media&token=5e5d6d1f-6bee-4ded-a42b-4b7be8d635b6\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult_redux.png?alt=media&token=0dca7ab3-30f6-4a87-b267-b62cf8ea9e71\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 750,\n" +
                "    \"award\": \"Fazer uma noite de degusta\\u00e7\\u00e3o de queijos\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box_redux.png?alt=media&token=960af6a9-20dc-434f-b3fb-13971d93e095\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/golldChallenger_redux.png?alt=media&token=fedd7a23-b447-4a84-809f-b152d952e49d\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeGoldChallenger_redux.png?alt=media&token=00682919-a098-48bd-a3c8-7f9e5907cc72\",\n" +
                "    \"type\": \"GOLD_CHALLENGER\",\n" +
                "    \"details\": \"Essa \\u00e9 a carta de Desafio Dourado, normalmente ela cont\\u00e9m premios que podem ser bastante especiais e sempre vir\\u00e1 um premio.\",\n" +
                "    \"value\": 46500,\n" +
                "    \"isComplete\": false,\n" +
                "    \"award\": \"Ingressos para um teatro ou concerto\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/boxGolden_redux.png?alt=media&token=447efaae-b167-472c-8c9e-94597bf3f1cc\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default_redux.png?alt=media&token=5e5d6d1f-6bee-4ded-a42b-4b7be8d635b6\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult_redux.png?alt=media&token=0dca7ab3-30f6-4a87-b267-b62cf8ea9e71\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 1000,\n" +
                "    \"award\": \"Ir a um parque tem\\u00e1tico\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box_redux.png?alt=media&token=960af6a9-20dc-434f-b3fb-13971d93e095\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default_redux.png?alt=media&token=5e5d6d1f-6bee-4ded-a42b-4b7be8d635b6\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult_redux.png?alt=media&token=0dca7ab3-30f6-4a87-b267-b62cf8ea9e71\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 1250,\n" +
                "    \"award\": \"Fazer uma noite de degusta\\u00e7\\u00e3o de queijos\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box_redux.png?alt=media&token=960af6a9-20dc-434f-b3fb-13971d93e095\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default_redux.png?alt=media&token=5e5d6d1f-6bee-4ded-a42b-4b7be8d635b6\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult_redux.png?alt=media&token=0dca7ab3-30f6-4a87-b267-b62cf8ea9e71\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 500,\n" +
                "    \"award\": \"Fazer uma caminhada na natureza\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box_redux.png?alt=media&token=960af6a9-20dc-434f-b3fb-13971d93e095\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default_redux.png?alt=media&token=5e5d6d1f-6bee-4ded-a42b-4b7be8d635b6\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult_redux.png?alt=media&token=0dca7ab3-30f6-4a87-b267-b62cf8ea9e71\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 500,\n" +
                "    \"award\": \"Jogar um jogo de tabuleiro\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box_redux.png?alt=media&token=960af6a9-20dc-434f-b3fb-13971d93e095\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default_redux.png?alt=media&token=5e5d6d1f-6bee-4ded-a42b-4b7be8d635b6\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult_redux.png?alt=media&token=0dca7ab3-30f6-4a87-b267-b62cf8ea9e71\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 750,\n" +
                "    \"award\": \"Um Caf\\u00e9 da manh\\u00e3\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box_redux.png?alt=media&token=960af6a9-20dc-434f-b3fb-13971d93e095\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default_redux.png?alt=media&token=5e5d6d1f-6bee-4ded-a42b-4b7be8d635b6\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult_redux.png?alt=media&token=0dca7ab3-30f6-4a87-b267-b62cf8ea9e71\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 500,\n" +
                "    \"award\": \"Ir a um evento esportivo\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box_redux.png?alt=media&token=960af6a9-20dc-434f-b3fb-13971d93e095\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default_redux.png?alt=media&token=5e5d6d1f-6bee-4ded-a42b-4b7be8d635b6\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult_redux.png?alt=media&token=0dca7ab3-30f6-4a87-b267-b62cf8ea9e71\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 1500,\n" +
                "    \"award\": \"Fazer uma noite de jogos\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box_redux.png?alt=media&token=960af6a9-20dc-434f-b3fb-13971d93e095\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default_redux.png?alt=media&token=5e5d6d1f-6bee-4ded-a42b-4b7be8d635b6\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult_redux.png?alt=media&token=0dca7ab3-30f6-4a87-b267-b62cf8ea9e71\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 1000,\n" +
                "    \"award\": \"Ter Noite de filmes com pipoca\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box_redux.png?alt=media&token=960af6a9-20dc-434f-b3fb-13971d93e095\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default_redux.png?alt=media&token=5e5d6d1f-6bee-4ded-a42b-4b7be8d635b6\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult_redux.png?alt=media&token=0dca7ab3-30f6-4a87-b267-b62cf8ea9e71\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 750,\n" +
                "    \"award\": \"Ir a um concerto\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box_redux.png?alt=media&token=960af6a9-20dc-434f-b3fb-13971d93e095\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default_redux.png?alt=media&token=5e5d6d1f-6bee-4ded-a42b-4b7be8d635b6\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult_redux.png?alt=media&token=0dca7ab3-30f6-4a87-b267-b62cf8ea9e71\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 750,\n" +
                "    \"award\": \"Um Caf\\u00e9 da manh\\u00e3\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box_redux.png?alt=media&token=960af6a9-20dc-434f-b3fb-13971d93e095\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default_redux.png?alt=media&token=5e5d6d1f-6bee-4ded-a42b-4b7be8d635b6\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult_redux.png?alt=media&token=0dca7ab3-30f6-4a87-b267-b62cf8ea9e71\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 1250,\n" +
                "    \"award\": \"Fazer uma caminhada na natureza\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box_redux.png?alt=media&token=960af6a9-20dc-434f-b3fb-13971d93e095\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/default_redux.png?alt=media&token=5e5d6d1f-6bee-4ded-a42b-4b7be8d635b6\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeDeafult_redux.png?alt=media&token=0dca7ab3-30f6-4a87-b267-b62cf8ea9e71\",\n" +
                "    \"type\": \"NORMAL\",\n" +
                "    \"details\": \"Essa carta \\u00e9 considerada comum, nela tem chance de vir algum presente ou item para sua cole\\u00e7\\u00e3o, contudo tem chances de n\\u00e3o vir nada\",\n" +
                "    \"isComplete\": false,\n" +
                "    \"value\": 750,\n" +
                "    \"award\": \"Jogar um jogo de tabuleiro\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/box_redux.png?alt=media&token=960af6a9-20dc-434f-b3fb-13971d93e095\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/golldChallenger_redux.png?alt=media&token=fedd7a23-b447-4a84-809f-b152d952e49d\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeGoldChallenger_redux.png?alt=media&token=00682919-a098-48bd-a3c8-7f9e5907cc72\",\n" +
                "    \"type\": \"GOLD_CHALLENGER\",\n" +
                "    \"details\": \"Essa \\u00e9 a carta de Desafio Dourado, normalmente ela cont\\u00e9m premios que podem ser bastante especiais e sempre vir\\u00e1 um premio.\",\n" +
                "    \"value\": 42250,\n" +
                "    \"isComplete\": false,\n" +
                "    \"award\": \"Um vinho de presente\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/boxGolden_redux.png?alt=media&token=447efaae-b167-472c-8c9e-94597bf3f1cc\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"image\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/golldChallenger_redux.png?alt=media&token=fedd7a23-b447-4a84-809f-b152d952e49d\",\n" +
                "    \"completeImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/completeGoldChallenger_redux.png?alt=media&token=00682919-a098-48bd-a3c8-7f9e5907cc72\",\n" +
                "    \"type\": \"GOLD_CHALLENGER\",\n" +
                "    \"details\": \"Essa \\u00e9 a carta de Desafio Dourado, normalmente ela cont\\u00e9m premios que podem ser bastante especiais e sempre vir\\u00e1 um premio.\",\n" +
                "    \"value\": 40250,\n" +
                "    \"isComplete\": false,\n" +
                "    \"award\": \"Um conjunto de ch\\u00e1 ou caf\\u00e9 gourmet\",\n" +
                "    \"awardImage\": \"https://firebasestorage.googleapis.com/v0/b/desafio-dourado.appspot.com/o/boxGolden_redux.png?alt=media&token=447efaae-b167-472c-8c9e-94597bf3f1cc\"\n" +
                "  }\n" +
                "]"
        val listType = object : TypeToken<List<Card>>() {}.type

        val cards: List<Card> = Gson().fromJson(json, listType)
        viewModelScope.launch {
            setChallengersUseCase(Challenger(cards))
        }
    }
}