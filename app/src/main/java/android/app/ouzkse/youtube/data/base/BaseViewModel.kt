package android.app.ouzkse.youtube.data.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

abstract class BaseViewModel : ViewModel() {

    internal val viewModelJob = Job()
    internal val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    lateinit var serviceFetchingJob: Job

    fun isFetching() = serviceFetchingJob.isActive
}