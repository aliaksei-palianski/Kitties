package com.github.rtyvZ.kitties.ui.imageCats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.rtyvZ.kitties.common.models.Cat
import com.github.rtyvZ.kitties.extantions.replaceElement
import com.github.rtyvZ.kitties.network.MyResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RandomCatsViewModel : ViewModel() {

    private val listwithCats = mutableListOf<Cat>()
    private var mutableRandomCats = MutableLiveData<List<Cat>?>()
    private var mutableRandomCatsError = MutableLiveData<Throwable>()
    private var mutableErrorVoteCats = MutableLiveData<Throwable>()

    var getRandomCats: LiveData<List<Cat>?> = mutableRandomCats
        private set
    var getRandomCatsError: LiveData<Throwable> = mutableRandomCatsError
        private set
    var getErrorVoteCat: LiveData<Throwable> = mutableErrorVoteCats

    private val randomCatsRepository = RandomCatsRepository()
    private val randomCatsModel = RandomCatsModel(randomCatsRepository)

    fun clear() {
        listwithCats.clear()
        mutableRandomCats = MutableLiveData()
        getRandomCats = mutableRandomCats
        mutableRandomCatsError = MutableLiveData()
        getRandomCatsError = mutableRandomCatsError
        mutableErrorVoteCats = MutableLiveData()
        getErrorVoteCat = mutableErrorVoteCats
    }

    fun getCats() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    randomCatsModel
                        .execute()
                        ?.collect {
                            listwithCats.addAll(it)
                            mutableRandomCats.postValue(listwithCats)
                        }
                }
            } catch (e: Exception) {
                mutableRandomCatsError.postValue(e)
            }
            /*  when (val getCats = randomCatsRepository.getCats()) {
                  is MyResult.Success<List<Cat>?> -> {
                      getCats.data?.let {
                          listwithCats.addAll(it)
                          mutableRandomCats.postValue(listwithCats)
                      }
                  }

                  is MyResult.Error -> {
                      mutableRandomCatsError.value = getCats.exception
                  }
              }*/
        }
    }

    private fun removeCat(cat: Cat) {
        val listWithCats = mutableListOf<Cat>()
        mutableRandomCats.value?.let {
            listWithCats.addAll(it)
        }
        listWithCats.remove(cat)
        mutableRandomCats.value = listWithCats
    }

    private fun restoreStateCat(cat: Cat) {
        val listCats = mutableListOf<Cat>()
        mutableRandomCats.value?.let {
            listCats.addAll(it)
        }
        listCats.find {
            it.id == cat.id
        }?.choice = -1
        mutableRandomCats.postValue(listCats)
    }

    private fun changeCat(cat: Cat) {
        val listCat = mutableListOf<Cat>()
        mutableRandomCats.value?.let {
            listCat.addAll(it)
        }
        listCat.replaceElement(cat)
        mutableRandomCats.postValue(listCat)
    }

    private fun sendVoteRequest(cat: Cat, choice: StateCatVote) {
        viewModelScope.launch {

            when (val response = randomCatsRepository.voteForCat(cat)) {
                is MyResult.Error -> {
                    response.exception.let {
                        restoreStateCat(cat)
                        mutableErrorVoteCats.postValue(it)
                    }
                }
                is MyResult.Success -> {
                    response.data?.let {
                        cat.apply {
                            voteId = it.id
                        }
                        changeCat(cat)
                    }
                }
            }
        }
    }

    private fun sendDeleteVoteRequest(cat: Cat) {
        viewModelScope.launch {
            when (val response = randomCatsRepository.deleteVote(cat)) {
                is MyResult.Error -> {
                    mutableErrorVoteCats.postValue(response.exception)
                }

                is MyResult.Success -> {
                    cat.choice = -1
                    changeCat(cat)
                }
            }
        }
    }

    fun voteForCat(cat: Cat, choice: StateCatVote) {
        when (choice) {
            StateCatVote.LIKE -> {
                if (cat.choice == LIKE) {
                    cat.choice = WITHOUT_VOTE
                    changeCat(cat)
                    sendDeleteVoteRequest(cat)
                } else {
                    cat.choice = LIKE
                    sendVoteRequest(cat, choice)
                    changeCat(cat)
                }
            }

            StateCatVote.DISLIKE -> {
                if (cat.choice == DISLIKE) {
                    cat.choice = WITHOUT_VOTE
                    changeCat(cat)
                    sendDeleteVoteRequest(cat)
                } else {
                    cat.choice = DISLIKE
                    sendVoteRequest(cat, choice)
                    changeCat(cat)
                }
            }
        }
    }

    fun addToFavorites(position: Int) {
        viewModelScope.launch {
            mutableRandomCats.value?.let {
                removeCat(it[position])
                when (val response = randomCatsRepository.addToFavorite(it[position].id)) {
                    is MyResult.Error -> {
                        restoreStateCat(it[position])
                        mutableErrorVoteCats.postValue(response.exception)
                    }
                }
            }
        }
    }

    companion object {
        const val LIKE = 1
        const val DISLIKE = 0
        const val WITHOUT_VOTE = -1
    }
}
